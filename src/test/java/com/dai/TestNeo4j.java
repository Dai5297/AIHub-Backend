package com.dai;

import com.dai.repository.DiseaseRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class TestNeo4j {

    @Autowired
    private DiseaseRepository diseaseRepository;

    @Test
    public void testNeo4j() {
        System.out.println(diseaseRepository.getAvoidFood("肺炎"));
        System.out.println(diseaseRepository.getDiseaseDetails("肺炎"));
        System.out.println(diseaseRepository.getCommonDrug("肺炎"));
    }
}
