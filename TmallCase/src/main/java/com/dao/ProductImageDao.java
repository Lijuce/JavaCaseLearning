package com.dao;

import com.pojo.Product;
import com.pojo.ProductImage;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import java.util.List;

//@Repository
public interface ProductImageDao {

    public static final String type_single = "type_single";
    public static final String type_detail = "type_detail";

    List<ProductImage> getFirstImage(@Param("id") Integer id, @Param("type") String type);

}
