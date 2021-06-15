package com.dao;

import com.pojo.Category;
import com.pojo.Product;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CategoryDao {

    @Select("select * from category")
    List<Category> listCategory();

    @Select("select * from category where id=#{cateId}")
    Category getCategory(Integer cateId);

    @Select("select * from Product where cid=#{cateId} order by id desc")
    List<Product> getProductsByCategory(Integer cateId);

    List<List<Product>> getAllProduct();

}
