package com.reader.demo.reader.Controller.Admin;

import com.reader.demo.reader.Model.Book;
import com.reader.demo.reader.Model.Category;
import com.reader.demo.reader.Model.HttpResponse;
import com.reader.demo.reader.Repository.BookRepository;
import com.reader.demo.reader.Repository.CategoryRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;


@RestController
@Api("管理员书籍管理相关api")
@RequestMapping("/admin/books")
public class AdminBookController {
    private final CategoryRepository categoryRepository;
    private final BookRepository bookRepository;

    @Autowired
    public AdminBookController(CategoryRepository categoryRepository, BookRepository bookRepository) {
        this.categoryRepository = categoryRepository;
        this.bookRepository = bookRepository;
    }

    @ApiOperation(value = "增加书籍", notes = "根据书籍信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "title", value = "书籍名称", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "imgSrc", value = "书籍图片url地址", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "description", value = "书籍简介", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "contentUrl", value = "书籍文本txt url地址", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "category_id", value = "书籍类别id 用英文符号;隔开 如：1;2;4;5", required = true, dataType = "String", paramType = "form")
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping(value = "/")
    public HttpResponse<String> addBook(String title, String imgSrc, String description, String contentUrl, String category_id) throws Exception {
        Book book = bookRepository.findBookByTitle(title);
        if (book != null)
            throw new Exception("The Book " + book.getTitle() + " is already exist");
        List<Category> categories = getCategoriesByString(category_id);
        book = new Book();
        book.setTitle(title);
        book.setImgSrc(imgSrc);
        book.setDescription(description);
        book.setContentUrl(contentUrl);
        book.setCategories(categories);
        HttpResponse<String> httpResponse = new HttpResponse<>();
        if (bookRepository.save(book) != null) {
            httpResponse.setMessage("success");
        } else {
            httpResponse.setMessage("failed");
            httpResponse.setCode(HttpResponse.ERROR);
        }
        httpResponse.setData("");
        return httpResponse;
    }

    @ApiOperation(value = "修改书籍", notes = "修改书籍信息", httpMethod = "PUT")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "书籍Id", required = true, dataType = "Long", paramType = "path"),
            @ApiImplicitParam(name = "title", value = "书籍名称", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "imgSrc", value = "书籍图片url地址", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "description", value = "书籍简介", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "contentUrl", value = "书籍文本txt url地址", required = true, dataType = "String", paramType = "form"),
            @ApiImplicitParam(name = "category_id", value = "书籍类别id 用英文符号;隔开 如：1;2;4;5", required = true, dataType = "String", paramType = "form")
    })
    @PutMapping("/{id}")
    public HttpResponse<?> updateBook(@PathVariable Long id, String title, String imgSrc, String description, String contentUrl, String category_id) throws Exception {
        Book book = bookRepository.findBookById(id);
        if (book == null)
            throw new Exception("The Book which id is " + id + " is not exist");
        List<Category> categories = getCategoriesByString(category_id);
        book.setTitle(title);
        book.setImgSrc(imgSrc);
        book.setDescription(description);
        book.setContentUrl(contentUrl);
        book.setCategories(categories);
        HttpResponse<Book> httpResponse = new HttpResponse<>();
        if (bookRepository.save(book) != null) {
            httpResponse.setMessage("success");
            httpResponse.setData(book);
        } else httpResponse.setMessage("failed");
        return httpResponse;
    }

    @ApiOperation(value = "删除书籍", notes = "删除书籍信息", httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "书籍Id", required = true, dataType = "Long", paramType = "path"),
    })
    @PostMapping("/delete/{id}")
    public HttpResponse<?> deleteBook(@PathVariable Long id) throws Exception {
        HttpResponse<String> httpResponse = new HttpResponse<>();
        if (bookRepository.deleteBookById(id) == 1)
            httpResponse.setMessage("success");
        else {
            httpResponse.setMessage("failed");
            httpResponse.setCode(HttpResponse.ERROR);
        }
        httpResponse.setData("");
        return httpResponse;
    }

    private List<Category> getCategoriesByString(String category_id) throws Exception {
        String[] c_ids = category_id.split(";");
        List<Category> categories = new ArrayList<>();
        Category category;
        for (String id : c_ids) {
            if (id.trim().isEmpty())
                continue;
            Long categoryId = Long.valueOf(id);
            category = categoryRepository.findCategoriesById(categoryId);
            if (category == null)
                throw new Exception("There are no such category which id is " + id);
            categories.add(category);
        }
        return categories;
    }
}
