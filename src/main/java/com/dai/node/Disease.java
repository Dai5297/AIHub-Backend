package com.dai.node;

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
    private Long id;

    /**
     * 疾病名称
     */
    private String name;

    /**
     * 疾病原因
     */
    private String cause;

    /**
     * 治疗部门
     */
    private List<String> cure_department;

    /**
     * 疾病描述
     */
    private String desc;

    /**
     * 治愈率
     */
    private String cured_prob;

    /**
     * 预防方法
     */
    private String prevent;

    /**
     * 治疗方法
     */
    private List<String> cure_way;

    /**
     * 治疗周期
     */
    private String cure_lastTime;

    /**
     * 易患病人群
     */
    private String easy_get;
}
