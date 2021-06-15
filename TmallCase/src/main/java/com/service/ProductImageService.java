package com.service;

import com.pojo.Product;

public interface ProductImageService {


    void setFirstProductImage(Integer id, Product p);

    void getProductImageByImgId(Integer id);
}
