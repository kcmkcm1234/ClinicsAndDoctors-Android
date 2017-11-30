package com.clinicsanddoctors.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.clinicsanddoctors.data.remote.respons.CategoryResponse;
import com.clinicsanddoctors.data.remote.respons.ClinicAndDoctorResponse;
import com.clinicsanddoctors.data.remote.respons.DoctorResponse;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Daro on 27/07/2017.
 */

public class ClinicAndDoctor implements Parcelable {

    private String id;
    private String name;
    private Category category;
    private List<Category> categoryList;
    private String rating;
    private String picture;
    private String email;
    private String phoneNumber;
    private String description;
    private String city;
    private String state;
    private String country;
    private String address;
    private String type;
    private double latitude;
    private double longitude;
    private boolean isFavorite;
    private boolean isRated;

    public ClinicAndDoctor() {

    }


    public ClinicAndDoctor(ClinicAndDoctorResponse clinicAndDoctorResponse, Category category) {
        id = clinicAndDoctorResponse.getId();
        name = clinicAndDoctorResponse.getName();
        if (category != null)
            this.category = category;

        if (clinicAndDoctorResponse.getCategoryResponseList() != null
                && !clinicAndDoctorResponse.getCategoryResponseList().isEmpty()) {
            categoryList = new ArrayList<>();
            for (CategoryResponse categoryResponse : clinicAndDoctorResponse.getCategoryResponseList())
                categoryList.add(new Category(categoryResponse));
            if (this.category == null)
                this.category = categoryList.get(0);
        }

        rating = clinicAndDoctorResponse.getRating();
        picture = clinicAndDoctorResponse.getPicture();
        email = clinicAndDoctorResponse.getEmail();
        phoneNumber = clinicAndDoctorResponse.getPhoneNumber();
        description = clinicAndDoctorResponse.getDescription();
        city = clinicAndDoctorResponse.getCity();
        state = clinicAndDoctorResponse.getState();
        country = clinicAndDoctorResponse.getCountry();
        address = clinicAndDoctorResponse.getAddress();
        if (clinicAndDoctorResponse.getLatitude() != null && !clinicAndDoctorResponse.getLatitude().isEmpty())
            latitude = Double.parseDouble(clinicAndDoctorResponse.getLatitude());
        if (clinicAndDoctorResponse.getLongitude() != null && !clinicAndDoctorResponse.getLongitude().isEmpty())
            longitude = Double.parseDouble(clinicAndDoctorResponse.getLongitude());
        isFavorite = clinicAndDoctorResponse.isFavorite();
        isRated = clinicAndDoctorResponse.isRated();
    }

    public ClinicAndDoctor(DoctorResponse doctorResponse, Category category) {
        id = doctorResponse.getId();
        name = doctorResponse.getName();
        type = doctorResponse.getType();
        if (category != null)
            this.category = category;
        else
            this.category = new Category(doctorResponse.getCategoryResponse());

        rating = doctorResponse.getRating();
        picture = doctorResponse.getPicture();
        email = doctorResponse.getEmail();
        phoneNumber = doctorResponse.getPhoneNumber();
        description = doctorResponse.getDescription();
        city = doctorResponse.getCity();
        state = doctorResponse.getState();
        country = doctorResponse.getCountry();
        address = doctorResponse.getAddress();
        if (doctorResponse.getLatitude() != null && !doctorResponse.getLatitude().isEmpty())
            latitude = Double.parseDouble(doctorResponse.getLatitude());
        if (doctorResponse.getLongitude() != null && !doctorResponse.getLongitude().isEmpty())
            longitude = Double.parseDouble(doctorResponse.getLongitude());
        isFavorite = doctorResponse.isFavorite();
        isRated = doctorResponse.isRated();
    }

    public ClinicAndDoctor(ClinicAndDoctorResponse clinicAndDoctorResponse) {
        id = clinicAndDoctorResponse.getId();
        name = clinicAndDoctorResponse.getName();

        if (clinicAndDoctorResponse.getCategoryResponseList() != null
                && !clinicAndDoctorResponse.getCategoryResponseList().isEmpty()) {
            categoryList = new ArrayList<>();
            for (CategoryResponse categoryResponse : clinicAndDoctorResponse.getCategoryResponseList())
                categoryList.add(new Category(categoryResponse));
            category = categoryList.get(0);
        }

        rating = clinicAndDoctorResponse.getRating();
        picture = clinicAndDoctorResponse.getPicture();
        email = clinicAndDoctorResponse.getEmail();
        phoneNumber = clinicAndDoctorResponse.getPhoneNumber();
        description = clinicAndDoctorResponse.getDescription();
        city = clinicAndDoctorResponse.getCity();
        state = clinicAndDoctorResponse.getState();
        country = clinicAndDoctorResponse.getCountry();
        address = clinicAndDoctorResponse.getAddress();
        if (clinicAndDoctorResponse.getLatitude() != null && !clinicAndDoctorResponse.getLatitude().isEmpty())
            latitude = Double.parseDouble(clinicAndDoctorResponse.getLatitude());
        if (clinicAndDoctorResponse.getLongitude() != null && !clinicAndDoctorResponse.getLongitude().isEmpty())
            longitude = Double.parseDouble(clinicAndDoctorResponse.getLongitude());
        isFavorite = clinicAndDoctorResponse.isFavorite();
        isRated = clinicAndDoctorResponse.isRated();
    }

    protected ClinicAndDoctor(Parcel in) {
        id = in.readString();
        name = in.readString();
        category = in.readParcelable(Category.class.getClassLoader());
        categoryList = in.createTypedArrayList(Category.CREATOR);
        rating = in.readString();
        picture = in.readString();
        email = in.readString();
        phoneNumber = in.readString();
        description = in.readString();
        city = in.readString();
        state = in.readString();
        country = in.readString();
        address = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        isFavorite = in.readByte() != 0;
        isRated = in.readByte() != 0;
        type = in.readString();
    }

    public static final Creator<ClinicAndDoctor> CREATOR = new Creator<ClinicAndDoctor>() {
        @Override
        public ClinicAndDoctor createFromParcel(Parcel in) {
            return new ClinicAndDoctor(in);
        }

        @Override
        public ClinicAndDoctor[] newArray(int size) {
            return new ClinicAndDoctor[size];
        }
    };

    public String getId() {
        return id;
    }

    public ClinicAndDoctor setId(String id) {
        this.id = id;
        return this;
    }

    public String getName() {
        return name;
    }

    public ClinicAndDoctor setName(String name) {
        this.name = name;
        return this;
    }

    public Category getCategory() {
        return category;
    }

    public ClinicAndDoctor setCategory(Category category) {
        this.category = category;
        return this;
    }

    public String getRating() {
        if (rating == null || rating.isEmpty())
            rating = "0";
        return rating;
    }

    public ClinicAndDoctor setRating(String rating) {
        this.rating = rating;
        return this;
    }

    public String getPicture() {
        return picture;
    }

    public ClinicAndDoctor setPicture(String picture) {
        this.picture = picture;
        return this;
    }

    public String getEmail() {
        return email;
    }

    public ClinicAndDoctor setEmail(String email) {
        this.email = email;
        return this;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public ClinicAndDoctor setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public ClinicAndDoctor setDescription(String description) {
        this.description = description;
        return this;
    }

    public String getCity() {
        return city;
    }

    public ClinicAndDoctor setCity(String city) {
        this.city = city;
        return this;
    }

    public String getState() {
        return state;
    }

    public ClinicAndDoctor setState(String state) {
        this.state = state;
        return this;
    }

    public String getCountry() {
        return country;
    }

    public ClinicAndDoctor setCountry(String country) {
        this.country = country;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public ClinicAndDoctor setAddress(String address) {
        this.address = address;
        return this;
    }

    public double getLatitude() {
        return latitude;
    }

    public ClinicAndDoctor setLatitude(double latitude) {
        this.latitude = latitude;
        return this;
    }

    public double getLongitude() {
        return longitude;
    }

    public ClinicAndDoctor setLongitude(double longitude) {
        this.longitude = longitude;
        return this;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public ClinicAndDoctor setFavorite(boolean favorite) {
        isFavorite = favorite;
        return this;
    }

    public List<Category> getCategoryList() {
        return categoryList;
    }

    public boolean isRated() {
        return isRated;
    }

    public void setRated(boolean rated) {
        isRated = rated;
    }

    public String getType() {
        return type;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeParcelable(category, flags);
        dest.writeTypedList(categoryList);
        dest.writeString(rating);
        dest.writeString(picture);
        dest.writeString(email);
        dest.writeString(phoneNumber);
        dest.writeString(description);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(country);
        dest.writeString(address);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
        dest.writeByte((byte) (isRated ? 1 : 0));
        dest.writeString(type);
    }
}
