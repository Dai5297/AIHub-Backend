package com.dai;

import com.dai.entity.Disease;
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
        Disease diseaseByDesc = diseaseRepository.getDiseaseByDesc("肺泡腔内压力升高");
        System.out.println(diseaseByDesc);
//        List<Disease> all = diseaseRepository.findByName();
//        System.out.println(all);
    }
}
