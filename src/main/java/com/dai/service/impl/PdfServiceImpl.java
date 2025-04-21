package com.dai.service.impl;

import cn.hutool.json.JSONUtil;
import com.dai.config.PdfAssistantConfig.PDFAssistant;
import com.dai.config.PersistentChatMemoryStore;
import com.dai.config.TitleAssistantConfig.TitleAssistant;
import com.dai.dto.ChatDto;
import com.dai.dto.FileDto;
import com.dai.entity.File;
import com.dai.entity.History;
import com.dai.entity.Title;
import com.dai.mapper.PdfMapper;
import com.dai.service.PdfService;
import com.dai.utils.AliOSSUtils;
import com.dai.utils.UserThreadLocal;
import com.dai.vo.PDFHistoryVo;
import com.dai.vo.TitleVo;
import com.dai.vo.UserVo;
import dev.langchain4j.data.document.Document;
import dev.langchain4j.data.document.loader.UrlDocumentLoader;
import dev.langchain4j.data.document.parser.apache.pdfbox.ApachePdfBoxDocumentParser;
import dev.langchain4j.data.document.splitter.DocumentByCharacterSplitter;
import dev.langchain4j.data.document.splitter.DocumentByRegexSplitter;
import dev.langchain4j.model.embedding.EmbeddingModel;
import dev.langchain4j.store.embedding.EmbeddingStoreIngestor;
import dev.langchain4j.store.embedding.pgvector.PgVectorEmbeddingStore;
import io.micrometer.common.util.StringUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import reactor.core.publisher.Flux;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class PdfServiceImpl implements PdfService {

    private final PdfMapper pdfMapper;

    private final AliOSSUtils ossUtils;

    private final PgVectorEmbeddingStore embeddingStore;

    private final EmbeddingModel embeddingModel;

    private final PDFAssistant assistant;

    private final TitleAssistant titleAssistant;

    private final PersistentChatMemoryStore chatMemoryStore;

    @Override
    public void uploadFile(FileDto fileDto) {
        MultipartFile file = fileDto.getFile();
        String fileName = fileDto.getFileName();
        String url;
        try {
            url = ossUtils.upload(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String subject = UserThreadLocal.getSubject();
        UserVo userVo = JSONUtil.toBean(subject, UserVo.class);

        History systemHistory = History.builder()
                .memoryId(String.valueOf(fileDto.getMemoryId()))
                .role("ai")
                .content("您好, 我是你的AI智能助手，有什么我能帮助你的吗？")
                .build();
        pdfMapper.saveHistory(systemHistory);

        File buildFile = File.builder()
                .userId(userVo.getId())
                .fileName(fileName)
                .url(url)
                .memoryId(fileDto.getMemoryId())
                .build();
        pdfMapper.saveFile(buildFile);

        ingestDocument(url);
    }

    @Override
    public Flux<String> chatAI(ChatDto chatDto) {
        if (chatDto == null || StringUtils.isBlank(chatDto.getMessage())) {
            throw new IllegalArgumentException("Invalid chat request");
        }

        // 异步保存用户消息
        CompletableFuture.runAsync(() ->
                pdfMapper.saveHistory(History.builder()
                        .memoryId(chatDto.getMemoryId())
                        .role("user")
                        .content(chatDto.getMessage())
                        .build())
        );
        return Flux.create(sink -> {
            StringBuilder responseBuilder = new StringBuilder("<p>");

            assistant.chat(chatDto.getMemoryId(), chatDto.getMessage())
                    .onPartialResponse(partial -> {
                        responseBuilder.append(partial);
                        sink.next(partial);
                    })
                    .onCompleteResponse(complete -> {
                        pdfMapper.saveHistory(History.builder()
                                .memoryId(chatDto.getMemoryId())
                                .role("ai")
                                .content(responseBuilder + "</p>")
                                .build());
                        sink.complete();
                    })
                    .onError(sink::error)
                    .start();
        });
    }

    @Override
    public String generateTitle(ChatDto chatDto) {
        String title = titleAssistant.chat(chatDto.getMessage());
        String jsonStr = UserThreadLocal.getSubject();
        UserVo userVo = JSONUtil.toBean(jsonStr, UserVo.class);
        Long id = userVo.getId();
        Title newTitle = Title.builder()
                .memoryId(chatDto.getMemoryId())
                .userId(id)
                .title(title)
                .build();
        pdfMapper.updateTitle(newTitle);
        return title;
    }

    @Override
    public List<TitleVo> getHistories() {
        String jsonStr = UserThreadLocal.getSubject();
        UserVo userVo = JSONUtil.toBean(jsonStr, UserVo.class);
        Long id = userVo.getId();
        return pdfMapper.getUserTitles(id);
    }

    @Override
    public PDFHistoryVo getHistoryById(Long id) {
        embeddingStore.removeAll();

        String url = pdfMapper.getUrl(id);

        if (!(url == null) && !url.isEmpty())
            ingestDocument(url);
        return PDFHistoryVo.builder()
                .historyVos(pdfMapper.getHistoryDetails(id))
                .fileName(pdfMapper.getFileName(id))
                .build();
    }

    @Override
    public void newChat(Long memoryId) {
        embeddingStore.removeAll();
    }

    @Override
    public void deleteHistory(Long memoryId) {
        pdfMapper.deleteHistory(memoryId);
        pdfMapper.deleteTitle(memoryId);
        chatMemoryStore.deleteMessages(memoryId);
    }

    private void ingestDocument(String url) {
        if (Objects.equals(url, "NULL"))
            return;
        Document document = UrlDocumentLoader.load(url, new ApachePdfBoxDocumentParser());
        EmbeddingStoreIngestor embeddingStoreIngestor = EmbeddingStoreIngestor.builder().embeddingStore(embeddingStore)
                .embeddingModel(embeddingModel)
                .documentSplitter(new DocumentByRegexSplitter(
                        "\\n\\d+\\.",
                        "\n",
                        80,
                        20,
                        new DocumentByCharacterSplitter(100, 20)
                )).build();
        embeddingStoreIngestor.ingest(document);
    }
}
