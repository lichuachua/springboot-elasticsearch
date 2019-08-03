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
        /**
         * 这个方法已经springdata未装，自己编写
         */
        List<Book> bookList = bookRepository.findByTitle(title);
        return bookList;
    }

    /**
     * 根据Id查询
     * @param id
     * @return
     */
    @Override
    public Book queryBookById(String id) {
//        Book book = bookRepository.queryBookById(id);
        Book book = bookRepository.findById(id).get();
        return book;
    }

    /**
     * 添加
     * @param book
     * @return
     */
    @Override
    public String add(Book book){
        /**
         * 这个方法已经被springdata封装，不必自己编写
         */
        bookRepository.save(book);
        return "success";
    }


    /**
     * 删除
     * @param id
     * @return
     */
    @Override
    public String delete(String id) {
        Book book = new Book();
        book.setId(id);
        /**
         * 这个方法已经被springdata封装，不必自己编写
         */
        bookRepository.delete(book);
        return "success";
    }


    /**
     * 更新
     * @param book
     * @return
     */
    @Override
    public String update(Book book) {
//        Book book1 = bookRepository.queryBookById(book.getId());
        Book book1 = bookRepository.findById(book.getId()).get();
        //当传进来的值为空时不变
        if (book.getAuthor()!=null)
        book1.setAuthor(book.getAuthor());
        if (book.getPublish_date()!=null)
        book1.setPublish_date(book.getPublish_date());
        if (book.getTitle()!=null)
        book1.setTitle(book.getTitle());
        if (book.getWord_count()!=null)
        book1.setWord_count(book.getWord_count());
        bookRepository.save(book1);
        return "success";
    }
}
