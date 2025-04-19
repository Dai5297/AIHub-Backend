package com.dai.mapper;

import com.dai.entity.File;
import com.dai.entity.History;
import com.dai.entity.Title;
import com.dai.vo.HistoryVo;
import com.dai.vo.TitleVo;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface PdfMapper {
    void saveFile(File file);

    List<TitleVo> getUserTitles(Long UserId);

    void saveHistory(History user);

    List<HistoryVo> getHistoryDetails(Long memoryId);

    void saveTitle(Title title);

    void deleteHistory(Long memoryId);

    void deleteTitle(Long memoryId);

    void updateTitle(Title newTitle);

    String getFileName(Long id);

    String getUrl(Long id);
}
