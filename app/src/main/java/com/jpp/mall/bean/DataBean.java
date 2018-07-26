package com.jpp.mall.bean;

import java.util.List;

/**
 * Created by Administrator on 2018/5/10 0010.
 */

public class DataBean {

    public static final int PARENT_ITEM = 0;//父布局
    public static final int CHILD_ITEM = 1;//子布局

    private int type;// 显示类型
    private boolean isExpand;// 是否展开
    private DataBean childBean;

    private String ID;
    private String parentLeftTxt;
    private String parentRightTxt;
    private String childLeftTxt;
    private String childRightTxt;

    private List<DetailModel.AttrName> mAttrNames;

    private List<DetailModel.Part> mParts;

    public List<DetailModel.Part> getParts() {
        return mParts;
    }

    public void setParts(List<DetailModel.Part> parts) {
        mParts = parts;
    }

    public List<DetailModel.AttrName> getAttrNames() {
        return mAttrNames;
    }

    public void setAttrNames(List<DetailModel.AttrName> attrNames) {
        mAttrNames = attrNames;
    }

    public String getParentLeftTxt() {
        return parentLeftTxt;
    }

    public void setParentLeftTxt(String parentLeftTxt) {
        this.parentLeftTxt = parentLeftTxt;
    }

    public String getChildRightTxt() {
        return childRightTxt;
    }

    public void setChildRightTxt(String childRightTxt) {
        this.childRightTxt = childRightTxt;
    }

    public String getChildLeftTxt() {
        return childLeftTxt;
    }

    public void setChildLeftTxt(String childLeftTxt) {
        this.childLeftTxt = childLeftTxt;
    }

    public String getParentRightTxt() {
        return parentRightTxt;
    }

    public void setParentRightTxt(String parentRightTxt) {
        this.parentRightTxt = parentRightTxt;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isExpand() {
        return isExpand;
    }

    public void setExpand(boolean expand) {
        isExpand = expand;
    }

    public DataBean getChildBean() {
        return childBean;
    }

    public void setChildBean(DataBean childBean) {
        this.childBean = childBean;
    }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }
}
