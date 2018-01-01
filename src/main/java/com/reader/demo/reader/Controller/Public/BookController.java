package com.reader.demo.reader.Controller.Public;

import com.reader.demo.reader.Model.Book;
import com.reader.demo.reader.Model.HttpResponse;
import com.reader.demo.reader.Repository.BookRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


@RestController
@Api("书籍信息查询相关api")
@RequestMapping(value = "/public")
public class BookController {
    private final BookRepository bookRepository;

    @Autowired
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    @ApiOperation(value = "获取书籍信息", notes = "获取书籍信息")
    @GetMapping(value = "/books")
    public HttpResponse<List<Book>> getAll() {
        List<Book> books = bookRepository.findAll();
        HttpResponse<List<Book>> httpResponse = new HttpResponse<>();
        httpResponse.setMessage("success");
        httpResponse.setData(books);
        return httpResponse;
    }

    @ApiOperation(value = "按书籍名字获取书籍信息", notes = "获取书籍信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "书籍名称", required = true, dataType = "String")
    })
    @GetMapping(value = "/book/name/{title}")
    public HttpResponse<Book> getByTitle(@PathVariable("title") String title) {
        Book book = bookRepository.findBookByTitle(title);
        HttpResponse<Book> httpResponse = new HttpResponse<>();
        httpResponse.setMessage("success");
        httpResponse.setData(book);
        return httpResponse;
    }

    @ApiOperation(value = "按书籍id获取书籍信息", notes = "获取书籍信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "书籍id", required = true, dataType = "Long")
    })
    @GetMapping(value = "/book/{id}")
    public HttpResponse<Book> getById(@PathVariable("id") Long id) {
        Book book = bookRepository.findBookById(id);
        HttpResponse<Book> httpResponse = new HttpResponse<>();
        httpResponse.setMessage("success");
        httpResponse.setData(book);
        return httpResponse;
    }
}
