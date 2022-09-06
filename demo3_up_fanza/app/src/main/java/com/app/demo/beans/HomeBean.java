package com.app.demo.beans;

import org.litepal.crud.DataSupport;

import java.io.Serializable;

/**
 * @description : 新闻新闻表
 */


public class   HomeBean extends DataSupport  implements Serializable {

    private int id;
    private String pic;
    private String name;
    private String url;
    public int stype;


    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
