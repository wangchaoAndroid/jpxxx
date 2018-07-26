package com.jpp.mall.bean;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/5/6 0006.
 */

public class HomeModel implements Serializable {
   public String linkURL;

   public int  linkType;

   public String pic;

   public String title;

   @Override
   public String toString() {
      return "HomeModel{" +
              "linkURL='" + linkURL + '\'' +
              ", linkType='" + linkType + '\'' +
              ", pic='" + pic + '\'' +
              ", title='" + title + '\'' +
              '}';
   }
}
