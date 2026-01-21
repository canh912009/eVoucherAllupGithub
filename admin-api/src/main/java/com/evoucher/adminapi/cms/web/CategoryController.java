package com.evoucher.adminapi.cms.web;

import com.evoucher.adminapi.cms.service.CategoryService;
import com.evoucher.adminapi.cms.service.models.CategoryDTO;
import com.evoucher.adminapi.cms.service.models.FilterSearchCms;
import com.evoucher.adminapi.cms.service.models.request.CategoryRequest;
import com.evoucher.adminapi.common.message.BaseResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/category")
public class CategoryController {

    private final CategoryService categoryService;

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse> findById(@PathVariable String id) {
        CategoryDTO supplierDTO = categoryService.findDtoById(id);
        return new ResponseEntity<>(new BaseResponse(supplierDTO), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<BaseResponse> createCategory(@Valid @RequestBody CategoryRequest categoryRequest) {
        CategoryDTO result = categoryService.createCategory(categoryRequest);
        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse> updateCategory(@PathVariable("id") String id, @Valid @RequestBody CategoryRequest categoryRequest) {
        CategoryDTO result = categoryService.updateCategory(id, categoryRequest);
        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse> deleteCategory(@PathVariable String id) {
        String result = categoryService.delete(id);
        return new ResponseEntity<>(new BaseResponse(result), HttpStatus.OK);
    }

    @GetMapping(value = {"/search/{page}/{pageSize}", "/search"})
    public ResponseEntity<BaseResponse> searchCategory(FilterSearchCms filterSearchCms,
                                                       @PathVariable(name = "page", required = false) Integer page,
                                                       @PathVariable(name = "pageSize", required = false) Integer pageSize) {
        Page<CategoryDTO> result = categoryService.searchCategory(filterSearchCms);
        BaseResponse res = new BaseResponse(result.getContent());
        res.setTotalCount(result.getTotalElements());
        return new ResponseEntity<>(res, HttpStatus.OK);
    }
}
