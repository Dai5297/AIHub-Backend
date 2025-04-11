package com.dai.tool;

import cn.hutool.core.util.ObjectUtil;
import com.dai.entity.ToolResult;
import com.dai.node.*;
import com.dai.repository.DiseaseRepository;
import dev.langchain4j.agent.tool.P;
import dev.langchain4j.agent.tool.Tool;
import dev.langchain4j.model.output.structured.Description;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Description("用于获取医疗相关的内容用于回答用户医疗相关问题")
@Component
@RequiredArgsConstructor
public class MedicalTool {

    private final DiseaseRepository diseaseRepository;

    @Tool("根据疾病名获取疾病信息")
    public ToolResult<Disease> getDiseaseDetails(@P("疾病名称") String diseaseName) {
        Disease diseaseDetails = diseaseRepository.getDiseaseDetails(diseaseName);
        if (ObjectUtil.isNull(diseaseDetails) || ObjectUtil.isEmpty(diseaseDetails)) {
            ToolResult.error("未查询到相关信息");
        }
        return ToolResult.success(diseaseDetails);
    }

    @Tool("根据疾病名获取疾病忌吃食物")
    public ToolResult<List<Food>> getAvoidFood(@P("疾病名称") String diseaseName) {
        List<Food> avoidFood = diseaseRepository.getAvoidFood(diseaseName);
        if (ObjectUtil.isNull(avoidFood) || ObjectUtil.isEmpty(avoidFood)) {
            ToolResult.error("未查询到相关信息");
        }
        return ToolResult.success(avoidFood);
    }

    @Tool("根据疾病名获取疾病推荐食物")
    public ToolResult<List<Food>> getRecommendFood(@P("疾病名称") String diseaseName) {
        List<Food> recommendFood = diseaseRepository.getRecommendFood(diseaseName);
        if (ObjectUtil.isNull(recommendFood) || ObjectUtil.isEmpty(recommendFood)) {
            ToolResult.error("未查询到相关信息");
        }
        return ToolResult.success(recommendFood);
    }

    @Tool("根据疾病名获取疾病伴随疾病")
    public ToolResult<List<Disease>> getAccompanyDiseases(@P("疾病名称") String diseaseName) {
        List<Disease> accompanyDiseases = diseaseRepository.getAccompanyDiseases(diseaseName);
        if (ObjectUtil.isNull(accompanyDiseases) || ObjectUtil.isEmpty(accompanyDiseases)) {
            ToolResult.error("未查询到相关信息");
        }
        return ToolResult.success(accompanyDiseases);
    }

    @Tool("根据疾病名获取疾病常用药品")
    public ToolResult<List<Drug>> getCommonDrug(@P("疾病名称") String diseaseName) {
        List<Drug> commonDrug = diseaseRepository.getCommonDrug(diseaseName);
        if (ObjectUtil.isNull(commonDrug) || ObjectUtil.isEmpty(commonDrug)) {
            ToolResult.error("未查询到相关信息");
        }
        return ToolResult.success(commonDrug);
    }

    @Tool("根据疾病名获取疾病好评药品")
    public ToolResult<List<Drug>> getRecommendDrug(@P("疾病名称") String diseaseName) {
        List<Drug> recommendDrug = diseaseRepository.getRecommendDrug(diseaseName);
        if (ObjectUtil.isNull(recommendDrug) || ObjectUtil.isEmpty(recommendDrug)) {
            ToolResult.error("未查询到相关信息");
        }
        return ToolResult.success(recommendDrug);
    }

    @Tool("根据症状推测疾病")
    public ToolResult<List<Disease>> getDiseasesBySymptom(@P("症状") List<String> Symptom) {
        if (Symptom.size() < 2){
            return ToolResult.error("提供的症状信息不足");
        }
        List<Disease> diseasesBySymptom = diseaseRepository.getDiseasesBySymptom(Symptom);
        if (ObjectUtil.isNull(diseasesBySymptom) || ObjectUtil.isEmpty(diseasesBySymptom)) {
            return ToolResult.error("未查询到相关信息");
        }
        return ToolResult.success(diseasesBySymptom);
    }

    @Tool("获取疾病的伴随症状")
    public ToolResult<List<Symptom>> getSymptomByDisease(@P("疾病名称") String diseaseName) {
        List<Symptom> symptomByDisease = diseaseRepository.getSymptomByDisease(diseaseName);
        if (ObjectUtil.isNull(symptomByDisease) || ObjectUtil.isEmpty(symptomByDisease)) {
            return ToolResult.error("未查询到相关信息");
        }
        return ToolResult.success(symptomByDisease);
    }

    @Tool("根据疾病推荐检查项目")
    public ToolResult<List<Check>> getCheckByDisease(@P("疾病名称") String diseaseName) {
        List<Check> checks = diseaseRepository.getCheckByDisease(diseaseName);
        if (ObjectUtil.isNull(checks) || ObjectUtil.isEmpty(checks)) {
            ToolResult.error("未查询到相关信息");
        }
        return ToolResult.success(checks);
    }

    @Tool("判断得某个疾病时能不能吃某个食物")
    public ToolResult<Boolean> canEat(@P("疾病名称") String diseaseName, @P("食物名称") String foodName) {
        List<Food> foods = diseaseRepository.getDoEatFoods(diseaseName);
        for (Food food : foods) {
            if (foodName.equals(food.getName()))
                return ToolResult.success(true);
        }
        return ToolResult.success(false);
    }
}
