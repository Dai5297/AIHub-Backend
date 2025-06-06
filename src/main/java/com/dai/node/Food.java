package com.dai.node;

import lombok.Data;
import org.springframework.data.neo4j.core.schema.Id;
import org.springframework.data.neo4j.core.schema.Node;
import org.springframework.data.neo4j.core.schema.Property;

@Data
@Node(labels = "Food")
public class Food {

    @Id
    private Long id;

    @Property
    private String name;
}
