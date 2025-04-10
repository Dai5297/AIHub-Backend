package com.dai.tool;

import com.dai.node.Disease;
import com.dai.node.Drug;
import com.dai.node.Food;
import com.dai.repository.DiseaseRepository;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.model.output.structured.Description;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Description("用于获取医疗相关的内容用于回答用户医疗相关问题")
@Component
public class MedicalTool {

    @Autowired
    private DiseaseRepository diseaseRepository;

    @Tool("根据疾病名获取疾病信息")
    public Disease getDiseaseDetails(@P("疾病名称") String diseaseName) {
        return diseaseRepository.getDiseaseDetails(diseaseName);
    }

    @Tool("根据疾病名获取疾病忌吃食物")
    public Food getAvoidFood(@P("疾病名称") String diseaseName) {
        return diseaseRepository.getAvoidFood(diseaseName);
    }

    @Tool("根据疾病名获取疾病推荐食物")
    public Food getRecommendFood(@P("疾病名称") String diseaseName) {
        return diseaseRepository.getRecommendFood(diseaseName);
    }

    @Tool("根据疾病名获取疾病伴随疾病")
    public List<Disease> getAccompanyDiseases(@P("疾病名称") String diseaseName) {
        return diseaseRepository.getAccompanyDiseases(diseaseName);
    }

    @Tool("根据疾病名获取疾病常用药品")
    public List<Drug> getCommonDrug(@P("疾病名称") String diseaseName) {
        return diseaseRepository.getCommonDrug(diseaseName);
    }

    @Tool("根据疾病名获取疾病好评药品")
    public List<Drug> getRecommendDrug(@P("疾病名称") String diseaseName) {
        return diseaseRepository.getRecommendDrug(diseaseName);
    }
}
