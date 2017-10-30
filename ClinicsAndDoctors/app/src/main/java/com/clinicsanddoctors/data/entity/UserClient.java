package com.clinicsanddoctors.data.entity;

import android.os.Parcel;
import android.os.Parcelable;

import com.clinicsanddoctors.data.remote.respons.RegisterResponse;
import com.clinicsanddoctors.data.remote.respons.UserPostResponse;

public class UserClient implements Parcelable {

    protected String accessToken;
    protected int id;
    protected String facebookId;
    protected String fullName;
    protected String email;
    protected String url;
    protected String password;
    protected String mobile;

    protected String userRole;
    protected String city;
    protected String state;
    protected String country;
    protected String address;
    protected double latitude;
    protected double longitude;
    protected String categoryId;
    protected int jobsCount;
    protected int planId;

    public UserClient() {

    }

    public UserClient(RegisterResponse registerResponse) {
        id = registerResponse.getUserId();
        accessToken = registerResponse.getAccessToken();
        facebookId = registerResponse.getFb_id();
        fullName = registerResponse.getFullname();
        email = registerResponse.getEmail();
        url = registerResponse.getProfilePicture();
        mobile = registerResponse.getMobileNumber();
        userRole = registerResponse.getUser_role();
        city = registerResponse.getCity();
        state = registerResponse.getState();
        country = registerResponse.getCountry();
        address = registerResponse.getAddress();
        latitude = registerResponse.getLatitude();
        longitude = registerResponse.getLongitude();
        categoryId = registerResponse.getCategory_id();
        jobsCount = registerResponse.getJobs_count();
        planId = registerResponse.getPlan_id();
    }

    public UserClient(UserPostResponse userPostResponse) {
        if (userPostResponse != null) {
            id = Integer.parseInt(userPostResponse.getId());
            email = userPostResponse.getEmail();
            fullName = userPostResponse.getName();
            url = userPostResponse.getPicture();
            mobile = userPostResponse.getPhoneNumber();
        }
    }

    protected UserClient(Parcel in) {
        accessToken = in.readString();
        id = in.readInt();
        facebookId = in.readString();
        fullName = in.readString();
        email = in.readString();
        url = in.readString();
        password = in.readString();
        mobile = in.readString();
        userRole = in.readString();
        city = in.readString();
        state = in.readString();
        country = in.readString();
        address = in.readString();
        latitude = in.readDouble();
        longitude = in.readDouble();
        categoryId = in.readString();
        jobsCount = in.readInt();
        planId = in.readInt();
    }

    public static final Creator<UserClient> CREATOR = new Creator<UserClient>() {
        @Override
        public UserClient createFromParcel(Parcel in) {
            return new UserClient(in);
        }

        @Override
        public UserClient[] newArray(int size) {
            return new UserClient[size];
        }
    };

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFacebookId() {
        return facebookId;
    }

    public void setFacebookId(String facebookId) {
        this.facebookId = facebookId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCompleteName() {
        return fullName;
    }

    public boolean isFacebookUer() {
        if (facebookId != null && !facebookId.isEmpty()) return true;
        return false;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getUserRole() {
        return userRole;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getAddress() {
        return address;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public String getCategoryId() {
        return categoryId;
    }

    public int getJobsCount() {
        return jobsCount;
    }

    public int getPlanId() {
        return planId;
    }

    public void setUserRole(String userRole) {
        this.userRole = userRole;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setCategoryId(String categoryId) {
        this.categoryId = categoryId;
    }

    public void setJobsCount(int jobsCount) {
        this.jobsCount = jobsCount;
    }

    public void setPlanId(int planId) {
        this.planId = planId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(accessToken);
        dest.writeInt(id);
        dest.writeString(facebookId);
        dest.writeString(fullName);
        dest.writeString(email);
        dest.writeString(url);
        dest.writeString(password);
        dest.writeString(mobile);
        dest.writeString(userRole);
        dest.writeString(city);
        dest.writeString(state);
        dest.writeString(country);
        dest.writeString(address);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(categoryId);
        dest.writeInt(jobsCount);
        dest.writeInt(planId);
    }
}
