package com.reader.demo.reader.Controller.Public;

import com.reader.demo.reader.Model.Book;
import com.reader.demo.reader.Model.HttpResponse;
import com.reader.demo.reader.Repository.BookRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@Api("书籍信息查询相关api")
@RequestMapping(value = "/public/books")
public class BookController {
    private final BookRepository bookRepository;

    @Autowired
    public BookController(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }


    @ApiOperation(value = "获取书籍信息", notes = "获取书籍信息", httpMethod = "GET")
    @GetMapping(value = "")
    public HttpResponse<List<Book>> getAll() {
        List<Book> books = bookRepository.findAll();
        HttpResponse<List<Book>> httpResponse = new HttpResponse<>();
        httpResponse.setMessage("success");
        httpResponse.setData(books);
        return httpResponse;
    }

    @ApiOperation(value = "根据书籍名称模糊查询书籍信息", notes = "模糊查询书籍信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "书籍名称", required = true, dataType = "String", paramType = "path"),
    })
    @GetMapping("/title/{title}")
    public HttpResponse<?> findUserLikeUserName(@PathVariable String title) {
        List<Book> books = bookRepository.findLikeBookTitle(title);
        HttpResponse<List<Book>> response = new HttpResponse<>();
        response.setMessage("Success");
        response.setData(books);
        return response;
    }


    @ApiOperation(value = "按书本id查找书籍信息", notes = "按书本id查找书籍信息", httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "书籍Id", required = true, dataType = "Long", paramType = "path"),
    })
    @GetMapping("/{id}")
    public HttpResponse<?> getBookById(@PathVariable Long id) throws Exception {
        HttpResponse<Book> httpResponse = new HttpResponse<>();
        Book book = bookRepository.findBookById(id);
        httpResponse.setMessage("success");
        httpResponse.setData(book);
        return httpResponse;
    }

}
