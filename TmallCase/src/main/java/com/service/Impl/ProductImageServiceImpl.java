package com.service.Impl;

import com.dao.ProductImageDao;
import com.pojo.Product;
import com.pojo.ProductImage;
import com.service.ProductImageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service("productImageService")
public class ProductImageServiceImpl implements ProductImageService {

    @Autowired
    ProductImageDao productImageDao;

    @Override
    public void setFirstProductImage(Integer pid, Product p) {
        List<ProductImage> firstImage = productImageDao.getFirstImage(pid, productImageDao.type_single);
        if (!firstImage.isEmpty())
            p.setFirstProductImage(firstImage.get(0));
    }

    @Override
    public void getProductImageByImgId(Integer id) {

    }
}
