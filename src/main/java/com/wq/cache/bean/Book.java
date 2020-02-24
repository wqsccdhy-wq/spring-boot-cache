package com.wq.cache.bean;

import java.io.Serializable;

/**
 * @author wangqing
 * @Desc
 * @Date: 2020-02-24 14:33
 * @since 2020-2-24 14:33
 */
public class Book implements Serializable {

    private Long id;

    private String name;

    private String auto;

    public Book() {

    }

    public Book(Long id, String name, String auto) {
        this.id = id;
        this.name = name;
        this.auto = auto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAuto() {
        return auto;
    }

    public void setAuto(String auto) {
        this.auto = auto;
    }
}
