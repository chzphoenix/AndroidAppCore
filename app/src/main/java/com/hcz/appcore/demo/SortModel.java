package com.hcz.appcore.demo;

import com.hcz.core.utils.SortString;

/**
 * Created by hcui on 4/7/17.
 */

public class SortModel {
    private int id;

    @SortString
    private String name;

    public SortModel(int id, String name){
        this.id = id;
        this.name = name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @SortString
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Model{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
