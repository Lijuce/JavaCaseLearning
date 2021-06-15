package com.service;

import com.pojo.Category;
import com.pojo.Product;

import java.util.List;

public interface CategoryService {

    // 列出类别
    List<Category> listCategory();

    // 获取商品类别
    Category getCategory(Integer id);

    // 获取每个类别的所有商品
    List<List<Product>> getAllProduct();

    // 根据商品ID获取整个商品信息
    Product getProductInfoById(Integer id);

}
