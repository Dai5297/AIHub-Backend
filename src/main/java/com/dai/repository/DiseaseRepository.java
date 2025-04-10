package com.dai.repository;

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
     * @param diseaseName
     * @return
     */
    @Query("MATCH (n:Disease) WHERE n.name = $name RETURN n")
    Disease getDiseaseDetails(@Param("name") String diseaseName);

    /**
     * 根据疾病名获取疾病忌吃食物
     * @param diseaseName
     * @return
     */
    @Query("MATCH (n:Disease)-[r:no_eat]->(m :Food) WHERE n.name = $name RETURN m")
    Food getAvoidFood(@Param("name") String diseaseName);

    /**
     * 根据疾病名获取疾病推荐食物
     * @param diseaseName
     * @return
     */
    @Query("MATCH (n:Disease)-[r:do_eat]->(m:Food) where n.name = $name return m;")
    Food getRecommendFood(@Param("name") String diseaseName);

    /**
     * 根据疾病名获取疾病伴随疾病
     * @param diseaseName
     * @return
     */
    @Query("MATCH (n:Disease)-[r:acompany_with]->(m:Disease) where n.name = $name RETURN m")
    List<Disease> getAccompanyDiseases(@Param("name") String diseaseName);


    /**
     * 根据疾病名获取疾病常用药品
     * @param diseaseName
     * @return
     */
    @Query("MATCH (n:Disease)-[r:common_drug]->(m:Drug) where n.name = $name RETURN m")
    List<Drug> getCommonDrug(@Param("name") String diseaseName);

    /**
     * 根据疾病名获取疾病推荐药品
     * @param diseaseName
     * @return
     */
    @Query("MATCH (n:Disease)-[r:recommand_drug]->(m:Drug) where n.name = $name RETURN m")
    List<Drug> getRecommendDrug(String diseaseName);
}
