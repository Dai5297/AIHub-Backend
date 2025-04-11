package com.dai.repository;

import com.dai.node.Check;
import com.dai.node.Disease;
import com.dai.node.Drug;
import com.dai.node.Food;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiseaseRepository extends Neo4jRepository<Disease,Long> {

    /**
     * 根据疾病名获取疾病信息
     * @param name
     * @return
     */
    @Query("MATCH (n:Disease) WHERE n.name = $name RETURN n")
    Disease getDiseaseDetails(@Param("name") String name);

    /**
     * 根据疾病名获取疾病忌吃食物
     * @param name
     * @return
     */
    @Query("MATCH (n:Disease)-[r:no_eat]->(m :Food) WHERE n.name = $name RETURN m.name AS name")
    List<Food> getAvoidFood(@Param("name") String name);

    /**
     * 根据疾病名获取疾病推荐食物
     * @param name
     * @return
     */
    @Query("MATCH (n:Disease)-[r:recommand_eat]->(m:Food) WHERE n.name = $name return m.name AS name")
    List<Food> getRecommendFood(@Param("name") String name);

    /**
     * 根据疾病名获取疾病伴随疾病
     * @param name
     * @return
     */
    @Query("MATCH (n:Disease)-[r:acompany_with]->(m:Disease) WHERE n.name = $name RETURN m.name AS name")
    List<Disease> getAccompanyDiseases(@Param("name") String name);


    /**
     * 根据疾病名获取疾病常用药品
     * @param name
     * @name
     */
    @Query("MATCH (n:Disease)-[r:common_drug]->(m:Drug) WHERE n.name = $name RETURN m.name AS name")
    List<Drug> getCommonDrug(@Param("name") String name);

    /**
     * 根据疾病名获取疾病推荐药品
     * @param name
     * @return
     */
    @Query("MATCH (n:Disease)-[r:recommand_drug]->(m:Drug) WHERE n.name = $name RETURN m.name AS name")
    List<Drug> getRecommendDrug(@Param("name") String name);

    /**
     * 根据症状获取疾病
     * @param symptoms
     * @return
     */
    @Query("MATCH (n:Disease)-[r:has_symptom]->(m:Symptom) WHERE m.name IN $symptoms WITH DISTINCT n  RETURN n LIMIT 5")
    List<Disease> getDiseasesBySymptom(@Param("symptoms") List<String> symptoms);

    /**
     * 根据疾病名获取疾病检查
     * @param name
     * @return
     */
    @Query("MATCH (n:Disease)-[r:need_check]->(m:Check) WHERE n.name = $name RETURN m.name AS name")
    List<Check> getCheckByDisease(@Param("name") String name);

    /**
     * 根据疾病名获取疾病推荐食物
     * @param diseaseName
     * @return
     */
    @Query("MATCH (n:Disease)-[r:do_eat]->(m:Food) WHERE n.name = $name return m.name AS name")
    List<Food> getDoEatFoods(String diseaseName);
}
