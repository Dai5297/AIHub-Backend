package com.dai.repository;

import com.dai.entity.Disease;
import org.apache.ibatis.annotations.Param;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.neo4j.repository.query.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DiseaseRepository extends Neo4jRepository<Disease,Long> {

    @Query("MATCH (n:Disease) WHERE n.cause CONTAINS($desc) RETURN n")
    Disease getDiseaseByDesc(@Param("desc") String desc);

    @Query("MATCH (n:Disease) WHERE n.name = $name RETURN n")
    Disease getDiseaseDetails(@Param("name") String name);
}
