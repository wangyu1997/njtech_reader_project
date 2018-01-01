package com.reader.demo.reader.Controller.Admin;

import com.reader.demo.reader.Model.Category;
import com.reader.demo.reader.Model.HttpResponse;
import com.reader.demo.reader.Repository.CategoryRepository;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@Api("管理员标签管理相关api")
@RequestMapping("/admin/categories")
public class AdminCategoryController {
    private final CategoryRepository categoryRepository;

    @Autowired
    public AdminCategoryController(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }


    @ApiOperation(value = "增加书籍标签", notes = "根据名字增加书籍标签",httpMethod = "POST")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "标签名", required = true, dataType = "String",paramType = "form")
    })
    @PreAuthorize("hasAuthority('ROLE_ADMIN')")
    @PostMapping(value = "")
    public HttpResponse<String> addCategory(String name) throws Exception {
        Category category = categoryRepository.findCategoriesByName(name);
        if (category != null)
            throw new Exception("The category " + category.getName() + " is already exist");
        category = new Category();
        category.setName(name);
        HttpResponse<String> httpResponse = new HttpResponse<>();
        if (categoryRepository.save(category) != null) {
            httpResponse.setMessage("success");
        } else {
            httpResponse.setMessage("failed");
            httpResponse.setCode(HttpResponse.ERROR);
        }
        httpResponse.setData("");
        return httpResponse;
    }
}
