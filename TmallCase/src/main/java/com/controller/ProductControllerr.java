package com.controller;

import com.pojo.Category;
import com.pojo.Product;
import com.service.CategoryService;
import com.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ProductControllerr {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private ProductImageService productImageService;

    @RequestMapping("/foreproduct")
    public String listProduct(@RequestParam("pid") Integer id, Model model){
        Product product = categoryService.getProductInfoById(id);// 获取指定商品ID对应的完整商品信息
        Category categoryInfo = categoryService.getCategory(product.getCid());  // 获取商品所对应类别
        productImageService.setFirstProductImage(product.getId(), product);

        product.setCategory(categoryInfo);

        System.out.println(categoryInfo);
        model.addAttribute("p", product);

        return "product";
    }
}
