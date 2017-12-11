package com.famous.restaurant;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by Jin on 2017-12-09.
 */

public class RestaurantVO implements Parcelable {
    private String name;
    private String category;
    private double latitude;
    private double longitude;
    private String phone;
    private String address;
    private String businessHours;
    private String menuList;
    private String imageURL;

    public RestaurantVO(Parcel in) {
        name=in.readString();
        category=in.readString();
        latitude=in.readDouble();
        longitude=in.readDouble();
        phone=in.readString();
        address=in.readString();
        businessHours=in.readString();
        menuList=in.readString();
        imageURL=in.readString();
    }

    public RestaurantVO() {}

    public static final Creator<RestaurantVO> CREATOR = new Creator<RestaurantVO>() {
        @Override
        public RestaurantVO createFromParcel(Parcel in) {
            return new RestaurantVO(in);
        }

        @Override
        public RestaurantVO[] newArray(int size) {
            return new RestaurantVO[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBusinessHours() {
        return businessHours;
    }

    public void setBusinessHours(String businessHours) {
        this.businessHours = businessHours;
    }

    public String getMenuList() {
        return menuList;
    }

    public void setMenuList(String menuList) {
        this.menuList = menuList;
    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(category);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(phone);
        dest.writeString(address);
        dest.writeString(businessHours);
        dest.writeString(menuList);
        dest.writeString(imageURL);
    }
}
