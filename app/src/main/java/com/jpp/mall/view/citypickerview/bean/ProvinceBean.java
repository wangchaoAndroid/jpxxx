package com.jpp.mall.view.citypickerview.bean;

import java.io.Serializable;
import java.util.List;

/**
 * @2Do:
 * @Author M2
 * @Version v ${VERSION}
 * @Date 2017/7/7 0007.
 */
public class ProvinceBean implements Serializable {

  private String regionId; /*110101*/

  private String name; /*东城区*/


  private List<CityBean> cities;



  @Override
  public String toString() {
    return  name ;
  }

  public String getId() {
    return regionId == null ? "" : regionId;
  }

  public void setId(String id) {
    this.regionId = id;
  }

  public String getName() {
    return name == null ? "" : name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public List<CityBean> getCityList() {
    return cities;
  }

  public void setCityList(List<CityBean> cityList) {
    this.cities = cityList;
  }

  public ProvinceBean() {
  }




}
