package com.qf.entity;

import java.io.Serializable;

/**
 * @author HuangGuiZhao
 * @Date 2019/3/20
 */
public class Student implements Serializable{
    private String name;
    private Integer money;

    public Student(String name, Integer money) {
        this.name = name;
        this.money = money;
    }

    @Override
    public String toString() {
        return "Student{" +
                "name='" + name + '\'' +
                ", money=" + money +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getMoney() {
        return money;
    }

    public void setMoney(Integer money) {
        this.money = money;
    }
}
