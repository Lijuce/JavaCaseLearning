package com.pojo;

public class ProductImage {
    private String type;  // 图片类型
    private Product product;
    private int id;  // 图片ID

    public int getId() {
        return id;
    }


    public void setId(int id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

}
