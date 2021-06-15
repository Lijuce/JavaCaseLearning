package com.pojo;

import java.io.Serializable;

public class OrderItem implements Serializable {
    // 订单ID
    private int id;
    // 商品ID
    private int pid;
    // 商品状态标志，-1为未形成订单，仍在购物车中
    private int oid;
    // 用户ID
    private int uid;
    // 商品数量
    private int number;

    //非数据库字段,订单项和产品是多对一关系，即一个产品可以对应多个订单项
    private Product product;  // 购物车需求变量

    public Product getProduct() {
        return product;
    }

    public void setProduct(Product product) {
        this.product = product;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getOid() {
        return oid;
    }

    public void setOid(int oid) {
        this.oid = oid;
    }

    public int getUid() {
        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    @Override
    public String toString() {
        return "OrderItem{" +
                "id=" + id +
                ", pid=" + pid +
                ", oid=" + oid +
                ", uid=" + uid +
                ", number=" + number +
                '}';
    }
}
