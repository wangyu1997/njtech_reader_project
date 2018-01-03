package com.reader.demo.reader.Controller.User;

import com.reader.demo.reader.Dao.AuthService;
import com.reader.demo.reader.Model.Book;
import com.reader.demo.reader.Model.HttpResponse;
import com.reader.demo.reader.Model.SysUser;
import com.reader.demo.reader.Repository.BookRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@Api("用户收藏图书api")
@RequestMapping("/user/books")
public class CollectController {
    @Value("token")
    private String tokenHeader;
    final BookRepository bookRepository;
    final AuthService authService;

    @Autowired
    public CollectController(BookRepository bookRepository, AuthService authService) {
        this.bookRepository = bookRepository;
        this.authService = authService;
    }

    @ApiOperation(value = "添加收藏", notes = "添加书本信息收藏", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bookId", value = "书本Id", required = true, dataType = "Long", paramType = "form")
    })
    @PostMapping("/collect/add")
    public HttpResponse<?> addToCollect(Long bookId, HttpServletRequest httpServletRequest) {
        HttpResponse<SysUser> response = new HttpResponse<>();
        String token = httpServletRequest.getHeader(tokenHeader);
        SysUser user = authService.getCurrentUser(token);
        if (user == null) {
            response.setCode(HttpResponse.ERROR);
            response.setMessage("Login First!");
        } else {
            Long userId = user.getId();
            Book book = bookRepository.findBookById(bookId);
            int cnt = bookRepository.findCollectCountByBookIdAndUserId(userId, bookId);
            if (book == null) {
                response.setCode(HttpResponse.ERROR);
                response.setMessage("The Book is not exist!");
            } else if (cnt > 0) {
                response.setCode(HttpResponse.ERROR);
                response.setMessage("You have already collected this book!");
            } else {
                int result = bookRepository.addCollection(userId, bookId);
                if (result == 1)
                    response.setMessage("success");
                else {
                    response.setCode(HttpResponse.ERROR);
                    response.setMessage("You have already added!");
                }
            }
        }
        return response;
    }

    @ApiOperation(value = "取消收藏", notes = "取消书本信息收藏", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "bookId", value = "书本Id", required = true, dataType = "Long", paramType = "form")
    })
    @PostMapping("/collect/cancel")
    public HttpResponse<?> cancelCollect(Long bookId, HttpServletRequest httpServletRequest) {
        HttpResponse<SysUser> response = new HttpResponse<>();
        String token = httpServletRequest.getHeader(tokenHeader);
        SysUser user = authService.getCurrentUser(token);
        if (user == null) {
            response.setCode(HttpResponse.ERROR);
            response.setMessage("Login First!");
        } else {
            Long userId = user.getId();
            Book book = bookRepository.findBookById(bookId);
            int cnt = bookRepository.findCollectCountByBookIdAndUserId(userId, bookId);
            if (book == null) {
                response.setCode(HttpResponse.ERROR);
                response.setMessage("The Book is not exist!");
            } else if (cnt == 0) {
                response.setCode(HttpResponse.ERROR);
                response.setMessage("You have not collected this book!");
            } else {
                int result = bookRepository.cancelCollection(userId, bookId);
                if (result == 1)
                    response.setMessage("success");
                else {
                    response.setCode(HttpResponse.ERROR);
                    response.setMessage("Cancel failed!");
                }
            }
        }
        return response;
    }


    @ApiOperation(value = "用户查询所有收藏书本信息", notes = "用户查询所有收藏书本信息", httpMethod = "GET")
    @GetMapping("")
    public HttpResponse<?> findAllCollect(HttpServletRequest httpServletRequest) {
        HttpResponse<List<Book>> response = new HttpResponse<>();
        String token = httpServletRequest.getHeader(tokenHeader);
        SysUser user = authService.getCurrentUser(token);
        if (user == null) {
            response.setCode(HttpResponse.ERROR);
            response.setMessage("Login First!");
        } else {
            List<Book> books = bookRepository.findAllCollectById(user.getId());
            response.setMessage("success");
            response.setData(books);
        }
        return response;
    }
}
