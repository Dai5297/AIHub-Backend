package com.dai.tool;

import com.dai.entity.Disease;
import com.dai.repository.DiseaseRepository;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.model.output.structured.Description;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Description("用于获取医疗相关的内容用于回答用户医疗相关问题")
@Component
public class MedicalTool {

    @Autowired
    private DiseaseRepository diseaseRepository;

    @Tool("根据疾病名获取疾病的详细信息")
    public Disease getDiseaseDetails(@P("疾病名称") String diseaseName) {
        return diseaseRepository.getDiseaseDetails(diseaseName);
    }
}
