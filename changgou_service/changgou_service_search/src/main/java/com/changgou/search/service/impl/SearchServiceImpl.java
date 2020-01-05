package com.changgou.search.service.impl;

import com.alibaba.fastjson.JSON;
import com.changgou.search.pojo.SkuInfo;
import com.changgou.search.service.SearchService;
import org.apache.commons.lang.StringUtils;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.Operator;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Jaime
 * @date 2020/1/5
 * @desc
 */

@Service
public class SearchServiceImpl implements SearchService {
    @Autowired
    private ElasticsearchTemplate elasticsearchTemplate;

    /**
     * 按照查询条件进行数据查询
     *
     * @param searchMap
     */
    @Override
    public Map search(Map<String, String> searchMap) {
        Map<String, Object> resultMap = new HashMap<>();

        //构建查询
        if (searchMap != null) {
            //构建查询条件封装对象
            NativeSearchQueryBuilder nativeSearchQueryBuilder = new NativeSearchQueryBuilder();
            BoolQueryBuilder boolQuery = QueryBuilders.boolQuery();

            //按照关键字查询
            if (StringUtils.isNotEmpty(searchMap.get("keywords"))) {
                boolQuery.must(QueryBuilders.matchQuery("name", searchMap.get("keywords")).operator(Operator.AND));
            }
            nativeSearchQueryBuilder.withQuery(boolQuery);
            //开启查询
            /**
             * 第一个参数: 条件构建对象
             * 第二个参数: 查询操作实体类
             * 第三个参数: 查询结果操作对象
             */
            //封装查询结果
            AggregatedPage<SkuInfo> resultInfo = elasticsearchTemplate.queryForPage(nativeSearchQueryBuilder.build(), SkuInfo.class, new SearchResultMapper() {
                @Override
                public <T> AggregatedPage<T> mapResults(SearchResponse searchResponse, Class<T> aClass, Pageable pageable) {
                    //查询结果操作
                    List<T> list = new ArrayList<>();

                    //获取查询命中结果数据
                    SearchHits hits = searchResponse.getHits();
                    if (hits != null) {
                        //有查询结果
                        for (SearchHit hit : hits) {
                            //SearchHit转换为skuinfo
                            SkuInfo skuInfo = JSON.parseObject(hit.getSourceAsString(), SkuInfo.class);
                            list.add((T) skuInfo);
                        }
                    }
                    return new AggregatedPageImpl<T>(list, pageable, hits.getTotalHits(), searchResponse.getAggregations());
                }
            });
            //封装最终的返回结果
            //总记录数
            resultMap.put("total", resultInfo.getTotalElements());
            //总页数
            resultMap.put("totalPages", resultInfo.getTotalPages());
            //数据集合
            resultMap.put("rows", resultInfo.getContent());
            return resultMap;
        }
        return null;
    }
}
