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

    /**
     * 添加类型
     * @param book
     * @return
     */
    String add(Book book);

    /**
     * 删除
     * @param id
     * @return
     */
    String delete(String id);

    /**
     * 根据Id查询
     * @param id
     * @return
     */
    Book queryBookById(String id);

    /**
     * 更新
     * @param book
     * @return
     */
    String update(Book book);

}
