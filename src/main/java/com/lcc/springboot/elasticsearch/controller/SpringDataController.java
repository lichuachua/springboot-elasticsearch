package com.lcc.springboot.elasticsearch.controller;

import com.lcc.springboot.elasticsearch.entity.Book;
import com.lcc.springboot.elasticsearch.service.SpringDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/springBoot")
public class SpringDataController {

    @Autowired
    private SpringDataService springDataService;

    /**
     * 查询包含title字段的书
     * @param title
     * @return
     */
    @PostMapping("/book/novel/findByTitle")
    public List<Book> findByTitle(@RequestParam(name = "title",required = false) String title) {
        List<Book> bookList = springDataService.findByTitle(title);
        return bookList;
    }


}
