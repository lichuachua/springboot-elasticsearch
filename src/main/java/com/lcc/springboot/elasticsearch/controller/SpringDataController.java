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

    /**
     * 根据Id查询
     * @param id
     * @return
     */
    @GetMapping("/book/novel/queryBookById")
    public Book queryBookById(String id) {
        Book book = springDataService.queryBookById(id);
        return book;
    }

    /**
     * 添加类型
     * @param book
     * @return
     */
    @PostMapping("/add/book/novel")
    public String add(Book book) {
        String info = springDataService.add(book);
        return info;
    }


    /**
     * 删除
     * @param id
     * @return
     */
    @DeleteMapping("/delete/book/novel")
    public String delete(String id) {
        String info = springDataService.delete(id);
        return info;
    }


    /**
     * 更新（本质上就是添加）
     * @param book
     * @return
     */
    @PutMapping("/update/book/novel")
    public String update(Book book){
        String info = springDataService.update(book);
        return info;
    }




}
