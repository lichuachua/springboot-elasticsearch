package com.lcc.springboot.elasticsearch.repository;

import com.lcc.springboot.elasticsearch.entity.Book;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends ElasticsearchRepository<Book, String> {

    /**
     * 查询包含title字段的书
     * @param title
     * @return
     */
    List<Book> findByTitle(String title);

}
