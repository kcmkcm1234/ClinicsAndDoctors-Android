package com.clinicsanddoctors.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.clinicsanddoctors.data.remote.respons.ClinicAndDoctorResponse;

/**
 * Created by Daro on 27/07/2017.
 */

public class ClinicAndDoctor implements Parcelable {

    private String id;
    private String name;
    private Category category;
    private String rating;
    private String picture;
    private String email;
    private String phoneNumber;
    private String description;
    private String city;
    private String state;
    private String country;
    private String address;
    private double latitude;
    private double longitude;

    public ClinicAndDoctor() {

    }


    public ClinicAndDoctor(ClinicAndDoctorResponse clinicAndDoctorResponse) {
        id = clinicAndDoctorResponse.getId();
        name = clinicAndDoctorResponse.getName();
        if (clinicAndDoctorResponse.getCategoryResponse() != null)
            category = new Category(clinicAndDoctorResponse.getCategoryResponse());
        rating = clinicAndDoctorResponse.getRating();
        picture = clinicAndDoctorResponse.getPicture();
        email = clinicAndDoctorResponse.getEmail();
        phoneNumber = clinicAndDoctorResponse.getPhoneNumber();
        description = clinicAndDoctorResponse.getDescription();
        city = clinicAndDoctorResponse.getCity();
        state = clinicAndDoctorResponse.getState();
        country = clinicAndDoctorResponse.getCountry();
        address = clinicAndDoctorResponse.getAddress();
        latitude = Double.parseDouble(clinicAndDoctorResponse.getLatitude());
        longitude = Double.parseDouble(clinicAndDoctorResponse.getLongitude());
    }


    protected ClinicAndDoctor(Parcel in) {
        id = in.readString();
        name = in.readString();
        category = in.readParcelable(Category.class.getClassLoader());
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeParcelable(category, flags);
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
    }
}
