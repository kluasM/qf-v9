package com.qf.freemarker.entity;

import java.util.Date;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/15
 */
public class Product {
    private Integer id;
    private String name;
    //
    private Date birthday;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }
}
