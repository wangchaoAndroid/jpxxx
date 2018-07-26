package com.jpp.mall.bean;

import java.util.List;

/**
 * Create by wangchao on 2017/12/18 10:37
 */
public class Course {
    public boolean isExpand;
    public String title;
    public int courseId;
    public List<GroupChildUrlBean> childList;

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getCourseId() {
        return courseId;
    }

    public void setCourseId(int courseId) {
        this.courseId = courseId;
    }

    public List<GroupChildUrlBean> getChildList() {
        return childList;
    }

    public void setChildList(List<GroupChildUrlBean> childList) {
        this.childList = childList;
    }
}
