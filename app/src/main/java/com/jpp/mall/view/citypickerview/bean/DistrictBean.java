package com.jpp.mall.view.citypickerview.bean;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @2Do:
 * @Author M2
 * @Version v ${VERSION}
 * @Date 2017/7/7 0007.
 */
public class DistrictBean implements Parcelable {

    private String regionId; /*110101*/
    
    private String name; /*东城区*/

    @Override
    public String toString() {
        return name;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.regionId);
        dest.writeString(this.name);
    }

    public DistrictBean() {
    }

    protected DistrictBean(Parcel in) {
        this.regionId = in.readString();
        this.name = in.readString();
    }

    public static final Creator<DistrictBean> CREATOR = new Creator<DistrictBean>() {
        @Override
        public DistrictBean createFromParcel(Parcel source) {
            return new DistrictBean(source);
        }

        @Override
        public DistrictBean[] newArray(int size) {
            return new DistrictBean[size];
        }
    };
}
