package com.changgou.search.service;

import java.util.Map;

/**
 * @author Jaime
 * @date 2020/1/5
 * @desc
 */
public interface SearchService {
    /**
     * 按照查询条件进行数据查询
     */
    Map search(Map<String, String> searchMap);
}
