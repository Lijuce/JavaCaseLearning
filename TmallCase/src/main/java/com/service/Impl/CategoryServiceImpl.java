package com.service.Impl;

import com.dao.CategoryDao;
import com.dao.ProductDao;
import com.pojo.Category;
import com.pojo.Product;
import com.service.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("categoryService")
public class CategoryServiceImpl implements CategoryService {

    @Autowired
    CategoryDao categoryDao;

    @Autowired
    ProductDao productDao;

    @Override
    public List<Category> listCategory() {
        List<Category> categories = categoryDao.listCategory();
        for (Category c: categories){  // 将具体商品填充至每个类别中
            List<Product> productsByCategory = categoryDao.getProductsByCategory(c.getId());
            c.setProducts(productsByCategory);
        }
        return categories;
    }

    @Override
    public Category getCategory(Integer id) {
        Category category = categoryDao.getCategory(id);
//        int eachCateId = category.getId();
//        List<Product> productsByCategory = categoryDao.getProductsByCategory(eachCateId);
//        category.setProducts(productsByCategory);
        return category;
    }

    @Override
    public List<List<Product>> getAllProduct() {
        return null;
    }

    @Override
    public Product getProductInfoById(Integer id) {
        Product productById = productDao.getProductById(id);
        return productById;
    }
}
