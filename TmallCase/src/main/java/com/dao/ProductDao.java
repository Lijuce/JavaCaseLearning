package com.dao;

import com.pojo.Product;
import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDao {

//    @Select("select * from (select * from Product where id = 319) as temp, productimage p where temp.id=p.pid;")
    @Select("select * from Product where id = #{id}")
    Product getProductById(Integer id);
}
