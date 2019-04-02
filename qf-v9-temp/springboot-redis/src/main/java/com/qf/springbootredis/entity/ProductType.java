package com.qf.springbootredis.entity;

import java.io.Serializable;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/22
 */
public class ProductType implements Serializable{
    private Long id;
    private Long pid;
    private String name;

    public ProductType(Long id, Long pid, String name) {
        this.id = id;
        this.pid = pid;
        this.name = name;
    }

    @Override
    public String toString() {
        return "ProductType{" +
                "id=" + id +
                ", pid=" + pid +
                ", name='" + name + '\'' +
                '}';
    }

    public ProductType() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getPid() {
        return pid;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
