package com.yujizi.es.repository;

import com.yujizi.es.pojo.Item;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.List;

/**
 * @ProjectName: elasticsearch_demo
 * @Package: com.yujizi.es.repository
 * @ClassName: ItemRepository
 * @Author: ychw
 * @Description:
 * @Date: 2020/10/28 22:34
 * @Version: 1.0
 */
public interface ItemRepository extends ElasticsearchRepository<Item,Long> {
    List<Item> findByPriceBetween(Double begin,Double end);
}
