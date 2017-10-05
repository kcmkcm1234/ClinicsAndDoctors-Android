package com.clinicsanddoctors.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.clinicsanddoctors.data.remote.respons.CategoryResponse;


/**
 * Created by Daro on 08/08/2017.
 */

public class Category implements Parcelable {

    private String id;
    private String name;
    private String color;

    public Category() {

    }

    public Category(CategoryResponse categoryResponse) {
        id = categoryResponse.getId();
        name = categoryResponse.getName();
        color = categoryResponse.getColor();
    }

    protected Category(Parcel in) {
        id = in.readString();
        name = in.readString();
        color = in.readString();
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public String getId() {
        return id;
    }

    public Category setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public Category setName(String name) {
        this.name = name;
        return this;
    }

    public String getColor() {
        return color;
    }

    public Category setColor(String color) {
        this.color = color;
        return this;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(id);
        parcel.writeString(name);
        parcel.writeString(color);
    }
}
