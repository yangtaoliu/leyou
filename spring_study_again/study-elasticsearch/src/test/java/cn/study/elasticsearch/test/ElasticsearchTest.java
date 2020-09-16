package cn.study.elasticsearch.test;

import cn.study.elasticsearch.dao.ItemRepository;
import cn.study.elasticsearch.pojo.Item;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.aggregations.Aggregation;
import org.elasticsearch.search.aggregations.AggregationBuilders;
import org.elasticsearch.search.aggregations.bucket.terms.StringTerms;
import org.elasticsearch.search.aggregations.metrics.avg.InternalAvg;
import org.elasticsearch.search.sort.SortBuilders;
import org.elasticsearch.search.sort.SortOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.query.FetchSourceFilter;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class ElasticsearchTest {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    private ItemRepository itemRepository;

    @Test
    public void testIndex() {
        this.elasticsearchTemplate.createIndex(Item.class);
        this.elasticsearchTemplate.putMapping(Item.class);
    }

    @Test
    public void testCreate() {
        //id相同将会覆盖，即更新
        /*Item item = new Item(1L, "小米手机7", " 手机",
                "小米", 3499.00, "http://image.leyou.com/13123.jpg");
        this.itemRepository.save(item);*/

        //this.itemRepository.delete(item);   //删除

        List<Item> list = new ArrayList<>();
        list.add(new Item(2L, "坚果手机R1", " 手机", "锤子", 3699.00, "http://image.leyou.com/123.jpg"));
        list.add(new Item(3L, "华为META10", " 手机", "华为", 4499.00, "http://image.leyou.com/3.jpg"));
        // 接收对象集合，实现批量新增
        itemRepository.saveAll(list);


    }

    @Test
    public void testFind() {
/*        Optional<Item> o = this.itemRepository.findById(1L);
        Item item = o.get();
        System.out.println(item);*/

        Iterable<Item> items = this.itemRepository.findAll(Sort.by("price").descending());
/*        items.forEach(item -> {
            System.out.println(item);
        });*/
        items.forEach(System.out::println);     // jdk1.8新语法
    }

    @Test
    public void testFindByTitle(){
        List<Item> items = this.itemRepository.findByTitle("手机");
        items.forEach(System.out::println);
    }

    @Test
    public void testFindByPriceBetween(){
        List<Item> items = this.itemRepository.findByPriceBetween(3699d, 4499d);
        items.forEach(System.out::println);
    }

    @Test
    public void indexList() {
        List<Item> list = new ArrayList<>();
        list.add(new Item(1L, "小米手机7", "手机", "小米", 3299.00, "http://image.leyou.com/13123.jpg"));
        list.add(new Item(2L, "坚果手机R1", "手机", "锤子", 3699.00, "http://image.leyou.com/13123.jpg"));
        list.add(new Item(3L, "华为META10", "手机", "华为", 4499.00, "http://image.leyou.com/13123.jpg"));
        list.add(new Item(4L, "小米Mix2S", "手机", "小米", 4299.00, "http://image.leyou.com/13123.jpg"));
        list.add(new Item(5L, "荣耀V10", "手机", "华为", 2799.00, "http://image.leyou.com/13123.jpg"));
        // 接收对象集合，实现批量新增
        itemRepository.saveAll(list);
    }

    @Test
    public void testSearch(){
        //通过查询构建器工具构建查询条件
        MatchQueryBuilder matchQueryBuilder = QueryBuilders.matchQuery("title", "手机");
        //执行查询，获取结果集
        Iterable<Item> items = this.itemRepository.search(matchQueryBuilder);
        items.forEach(System.out::println);
    }

    //原生查询
    @Test
    public void testNative(){
        //构建自定义查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //添加基本查询条件
        queryBuilder.withQuery(QueryBuilders.matchQuery("title", "手机"));
        //执行查询，获取分页结果集，部分也的话默认一页
        Page<Item> itemPage = this.itemRepository.search(queryBuilder.build());
        System.out.println(itemPage.getTotalPages());
        System.out.println(itemPage.getTotalElements());
        itemPage.forEach(System.out::println);

        List<Item> items = itemPage.getContent();
        items.forEach(System.out::println);

    }

    @Test
    public void testPage(){
        //构建自定义查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //添加基本查询条件
        queryBuilder.withQuery(QueryBuilders.matchQuery("category", "手机"));
        //添加分页条件，页码从0开始
        //queryBuilder.withPageable(PageRequest.of(1, 2));
        //添加排序
        queryBuilder.withSort(SortBuilders.fieldSort("price").order(SortOrder.DESC));
        //执行查询，获取分页结果集，部分也的话默认一页
        Page<Item> itemPage = this.itemRepository.search(queryBuilder.build());

        System.out.println(itemPage.getTotalPages());
        System.out.println(itemPage.getTotalElements());

        List<Item> items = itemPage.getContent();
        items.forEach(System.out::println);

    }

    @Test
    public void testAggs(){
        //构建自定义查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //添加聚合
        queryBuilder.addAggregation(AggregationBuilders.terms("brandAgg").field("brand"));
        //添加结果集过滤，不包括任何字段
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{}, null));
        AggregatedPage itemPage = (AggregatedPage<Item>)this.itemRepository.search(queryBuilder.build());
        //解析聚合结果集,根据聚合的类型以及字段类型要进行强转   brand是字符串类型的，聚合类型为磁条聚合，通过聚合名称获取聚合对象
        StringTerms stringTerms = (StringTerms)itemPage.getAggregation("brandAgg");
        List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
        buckets.forEach(bucket -> {
            System.out.println(bucket.getKeyAsString());
            System.out.println(bucket.getDocCount());
        });

    }

    @Test
    public void testSubAggs(){
        //构建自定义查询构建器
        NativeSearchQueryBuilder queryBuilder = new NativeSearchQueryBuilder();
        //添加聚合
        queryBuilder.addAggregation(AggregationBuilders.terms("brandAgg").field("brand")
                .subAggregation(AggregationBuilders.avg("price_avg").field("price")));
        //添加结果集过滤，不包括任何字段
        queryBuilder.withSourceFilter(new FetchSourceFilter(new String[]{}, null));
        AggregatedPage itemPage = (AggregatedPage<Item>)this.itemRepository.search(queryBuilder.build());
        //解析聚合结果集,根据聚合的类型以及字段类型要进行强转   brand是字符串类型的，聚合类型为磁条聚合，通过聚合名称获取聚合对象
        StringTerms stringTerms = (StringTerms)itemPage.getAggregation("brandAgg");
        List<StringTerms.Bucket> buckets = stringTerms.getBuckets();
        buckets.forEach(bucket -> {
            System.out.println(bucket.getKeyAsString());
            System.out.println(bucket.getDocCount());
            //获取子聚合的map集合，key为聚合名称，value为对应子聚合对象
            Map<String, Aggregation> stringAggregationMap = bucket.getAggregations().asMap();
            InternalAvg priceAvg = (InternalAvg)stringAggregationMap.get("price_avg");
            System.out.println(priceAvg.getValue());
        });

    }
}
