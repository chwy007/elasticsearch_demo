package com.yujizi.es.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

/**
 * @ProjectName: elasticsearch_demo
 * @Package: com.yujizi.es.pojo
 * @ClassName: Item
 * @Author: ychw
 * @Description:
 * @Date: 2020/10/28 21:43
 * @Version: 1.0
 */
@AllArgsConstructor
@NoArgsConstructor
@Data
@Document(indexName = "index01",type = "item",shards = 1,replicas = 1)
public class Item {
    @Id
    @Field(type = FieldType.Long)
    private Long id;
    @Field(type = FieldType.Text,analyzer = "ik_max_word")
    private String title;
    @Field(type = FieldType.Keyword)
    private String category;
    @Field(type = FieldType.Keyword)
    private String brand;
    @Field(type = FieldType.Double)
    private Double price;
    @Field(type = FieldType.Keyword,index = false)
    private String images;
}
