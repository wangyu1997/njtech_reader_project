package com.reader.demo.reader.Controller.Public;

import com.reader.demo.reader.Model.Category;
import com.reader.demo.reader.Model.HttpResponse;
import com.reader.demo.reader.Repository.CategoryRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import java.util.List;


@RestController
@Api("标签查询相关api")
@RequestMapping(value = "/public/categories")
public class CategoryController {
    private final CategoryRepository categoryRepository;

    @Autowired
    public CategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @ApiOperation(value = "获取书籍标签", notes = "获取书籍标签",httpMethod = "GET")
    @GetMapping(value = "")
    public HttpResponse<List<Category>> getAll() {
        List<Category> categories = categoryRepository.findAll();
        HttpResponse<List<Category>> httpResponse = new HttpResponse<>();
        httpResponse.setMessage("success");
        httpResponse.setData(categories);
        return httpResponse;
    }


    @ApiOperation(value = "按分类标签名字获取标签信息", notes = "获取标签信息",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "标签名", required = true, dataType = "String",paramType = "path")
    })
    @PermitAll
    @GetMapping(value = "/name/{name}")
    public HttpResponse<Category> getOne(@PathVariable("name") String name) {
        Category category = categoryRepository.findCategoriesByName(name);
        HttpResponse<Category> httpResponse = new HttpResponse<>();
        httpResponse.setMessage("success");
        httpResponse.setData(category);
        return httpResponse;
    }

    @ApiOperation(value = "按书籍标签id获取标签信息", notes = "获取标签信息",httpMethod = "GET")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "id", value = "标签id", required = true, dataType = "Long",paramType = "path")
    })
    @PermitAll
    @GetMapping(value = "/{id}")
    public HttpResponse<Category> getById(@PathVariable("id") Long id) {
        Category category = categoryRepository.findCategoriesById(id);
        HttpResponse<Category> httpResponse = new HttpResponse<>();
        httpResponse.setMessage("success");
        httpResponse.setData(category);
        return httpResponse;
    }
}
