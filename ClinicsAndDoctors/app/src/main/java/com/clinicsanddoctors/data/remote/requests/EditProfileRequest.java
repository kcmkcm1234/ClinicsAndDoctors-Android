package com.clinicsanddoctors.data.remote.requests;

/**
 * Created by Daro on 16/08/2017.
 */

public class EditProfileRequest {
    private String service_provider_id;
    private String password;

    private String picture;
    private String user_id;
    private String name;
    private String surname;
    private String phone_number;
    private String email;

    private String description;
    private String address;
    private String city;
    private String state;
    private String zipcode;
    private String country;
    private String category_id;

    public EditProfileRequest setService_provider_id(String service_provider_id) {
        this.service_provider_id = service_provider_id;
        return this;
    }

    public EditProfileRequest setUser_id(String user_id) {
        this.user_id = user_id;
        return this;
    }

    public EditProfileRequest setFirst_name(String first_name) {
        this.name = first_name;
        return this;
    }

    public EditProfileRequest setLast_name(String last_name) {
        this.surname = last_name;
        return this;
    }

    public EditProfileRequest setProfile_picture(String profile_picture) {
        this.picture = profile_picture;
        return this;
    }

    public EditProfileRequest setPassword(String password) {
        this.password = password;
        return this;
    }

    public EditProfileRequest setMobile(String mobile) {
        this.phone_number = mobile;
        return this;
    }

    public EditProfileRequest setEmail(String email) {
        this.email = email;
        return this;
    }

    public EditProfileRequest setDescription(String description) {
        this.description = description;
        return this;
    }

    public EditProfileRequest setAddress(String address) {
        this.address = address;
        return this;
    }

    public EditProfileRequest setCity(String city) {
        this.city = city;
        return this;
    }

    public EditProfileRequest setState(String state) {
        this.state = state;
        return this;
    }

    public EditProfileRequest setZipcode(String zipcode) {
        this.zipcode = zipcode;
        return this;
    }

    public EditProfileRequest setCountry(String country) {
        this.country = country;
        return this;
    }

    public EditProfileRequest setCategory_id(String category_id) {
        this.category_id = category_id;
        return this;
    }

    public String getPicture() {
        return picture;
    }
}
