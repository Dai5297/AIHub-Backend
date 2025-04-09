package com.dai.entity;

import dev.langchain4j.agent.tool.P;
import lombok.Data;
import org.springframework.data.neo4j.core.schema.GeneratedValue;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

import java.io.Serializable;
import java.util.List;

@Data
@Node(labels = "Disease")
public class Disease implements Serializable {

    /**
     * 疾病id
     */
    @Id
    @GeneratedValue
    private Long id;

    /**
     * 疾病名称
     */
    @Property
    private String name;

    /**
     * 疾病原因
     */
    @Property
    private String cause;

    /**
     * 治疗部门
     */
    @Property
    private List<String> cure_department;

    /**
     * 疾病描述
     */
    @Property
    private String desc;

    /**
     * 治愈率
     */
    @Property
    private String cured_prob;

    /**
     * 预防方法
     */
    @Property
    private String prevent;

    /**
     * 治疗方法
     */
    @Property
    private List<String> cure_way;

    /**
     * 治疗周期
     */
    @Property
    private String cure_lastTime;

    /**
     * 易患病人群
     */
    @Property
    private String easy_get;
}
