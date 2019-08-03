package com.lcc.springboot.elasticsearch.service.Impl;

import com.lcc.springboot.elasticsearch.entity.Book;
import com.lcc.springboot.elasticsearch.repository.BookRepository;
import com.lcc.springboot.elasticsearch.service.SpringDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SpringDataServiceImpl implements SpringDataService {

    @Autowired
    private BookRepository bookRepository;


    /**
     * 查询包含title字段的书
     * @param title
     * @return
     */
    @Override
    public List<Book> findByTitle(String title) {
        List<Book> bookList = bookRepository.findByTitle(title);
        return bookList;
    }
}
