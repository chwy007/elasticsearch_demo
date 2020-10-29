package com.yujizi.es.demo;

import com.yujizi.es.pojo.Item;
import com.yujizi.es.repository.ItemRepository;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.Aggregations;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SourceFilter;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;

/**
 * @ProjectName: elasticsearch_demo
 * @Package: com.yujizi.test
 * @ClassName: com.yujizi.es.demo.EsTest
 * @Author: ychw
 * @Description:
 * @Date: 2020/10/28 21:46
 * @Version: 1.0
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class EsTest {
    @Autowired
    private ElasticsearchTemplate esTemplate;

    @Autowired
    private ItemRepository repository;

    @Test
    public void create() {
        //创建索引库
        esTemplate.createIndex(Item.class);
        //映射关系
        esTemplate.putMapping(Item.class);
    }

    @Test
    public void find(){
        Iterable<Item> all = repository.findAll();
        for(Item item:all){
            System.out.println(item);
        }
    }

    @Test
    public void insert(){
        List<Item> list=new ArrayList<>();

        list.add(new Item(1l,"坚果手机","手机","锤子",3699.00,"http://contentcms-bj.cdn.bcebos.com/cmspic/841e59974d034c33688290c7091556ff.jpeg?x-bce-process=image/crop,x_0,y_0,w_900,h_489"));
        list.add(new Item(2l,"华为手机","手机","华为",5699.00,"http://contentcms-bj.cdn.bcebos.com/cmspic/841e59974d034c33688290c7091556ff.jpeg?x-bce-process=image/crop,x_0,y_0,w_900,h_589"));
        list.add(new Item(3l,"iphone手机","手机","Apple",8699.00,"http://contentcms-bj.cdn.bcebos.com/cmspic/841e59974d034c33688290c7091556ff.jpeg?x-bce-process=image/crop,x_0,y_0,w_900,h_689"));
        list.add(new Item(4l,"荣耀手机","手机","华为荣耀",1699.00,"http://contentcms-bj.cdn.bcebos.com/cmspic/841e59974d034c33688290c7091556ff.jpeg?x-bce-process=image/crop,x_0,y_0,w_900,h_5189"));
        list.add(new Item(5l,"小米手机","手机","小米",699.00,"http://contentcms-bj.cdn.bcebos.com/cmspic/841e59974d034c33688290c7091556ff.jpeg?x-bce-process=image/crop,x_0,y_0,w_900,h_6829"));
        repository.saveAll(list);
    }

    @Test
    public void complexFind(){
        List<Item> list = repository.findByPriceBetween(2000d, 9000d);
        for(Item item:list){
            System.out.println("item="+item);
        }
    }

    @Test
    public  void query(){
        /**
         * TODO
         * 原生查询
         * @ClassName EsTest
         * @author ychw
         * @date 2020/10/29 12:36
         */
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();//nativesearch原生查询
        //添加查询条件
        queryBuilder.withQuery(QueryBuilders.matchQuery("title", "华为手机"));
        //结果过滤
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{"id","title","price"},null));
        //排序
        queryBuilder.withSort(SortBuilders.fieldSort("price").order(SortOrder.DESC));
        //分页
        queryBuilder.withPageable(PageRequest.of(0,2));

        Page<Item> result = repository.search(queryBuilder.build());

        long totalElements = result.getTotalElements();
        System.out.println(totalElements);
        int totalPages = result.getTotalPages();
        System.out.println(totalPages);
        List<Item> list = result.getContent();
        for (Item item : list) {
            System.out.println(item);
        }



    }

    @Test
    public void aggre(){
        /**
         * TODO
         * 聚合查询
         * @ClassName EsTest
         * @author ychw
         * @date 2020/10/29 12:41
         */
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //聚合
        queryBuilder.addAggregation(AggregationBuilders.terms("popularBrand").field("brand"));


        AggregatedPage<Item> result = esTemplate.queryForPage(queryBuilder.build(), Item.class);
        //解析聚合
        Aggregations aggregations = result.getAggregations();
        //根据名称查聚合
        StringTerms terms = aggregations.get("popularBrand");
        //获取桶
        List<StringTerms.Bucket> buckets = terms.getBuckets();
        for (StringTerms.Bucket bucket : buckets) {
            System.out.println("bucket.getKeyAsString() = " + bucket.getKeyAsString());
            System.out.println("bucket.getDocCount() = " + bucket.getDocCount());
        }



    }



}
