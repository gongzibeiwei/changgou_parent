package com.changgou.search.controller;

import com.changgou.search.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.Set;

/**
 * @author Jaime
 * @date 2020/1/5
 * @desc
 */
@RestController
@RequestMapping("/search")
public class SearchController {
    @Autowired
    private SearchService searchService;

    /**
     * 条件查询
     * @param searchMap
     * @return
     */
    @GetMapping
    public Map search(@RequestParam Map<String, String> searchMap) {
        //特殊符号处理
        this.handleSearchMap(searchMap);
        Map searchResult = searchService.search(searchMap);
        return searchResult;
    }

    /**
     * 对请求路径中特殊符号进行处理
     * @param searchMap
     */
    private void handleSearchMap(Map<String, String> searchMap) {
        Set<Map.Entry<String, String>> entries = searchMap.entrySet();
        for (Map.Entry<String, String> entry : entries) {
            if (entry.getKey().startsWith("spec_")) {
                searchMap.put(entry.getKey(), entry.getValue().replace("+", "%2B"));
            }
        }
    }
}
