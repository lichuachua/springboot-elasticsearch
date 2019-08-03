package com.lcc.springboot.elasticsearch.service;

import com.lcc.springboot.elasticsearch.entity.Book;

import java.util.List;

public interface SpringDataService {

    /**
     * 查询包含title字段的书
     * @param title
     * @return
     */
    List<Book> findByTitle(String title);

}
