package com.jpp.mall.bean;

/**
 * @created date : 2017/12/7
 * @author: jannonx
 * @org: cop
 * @:describe: 描述信息
 */

public class GroupChildUrlBean {
    private int id;
    private String title;
    private String url;
    private int status;
    private String createTime;
    private int sort;


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getCreateTime() {
        return createTime;
    }

    public void setCreateTime(String createTime) {
        this.createTime = createTime;
    }

    public int getSort() {
        return sort;
    }

    public void setSort(int sort) {
        this.sort = sort;
    }

}
