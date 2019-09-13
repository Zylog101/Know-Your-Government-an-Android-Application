package com.example.assingment.knowyourgovernment;

import android.os.Parcel;
import android.os.Parcelable;

public class Official implements Parcelable{
    private String name;
    private String address;
    private String partyName;
    private String phone;
    private String websiteUrl;
    private String photoUrl;
    private String officeTitle;
    private String email;
    private String googleChannel;
    private String youtubeChannel;
    private String twitterChannel;
    private String facebookChannel;

    public Official(String name, String address, String partyName, String phone, String websiteUrl,
                    String photoUrl, String officeTitle, String email, String googleChannel,
                    String youtubeChannel, String twitterChannel, String facebookChannel) {
        this.name = name;
        this.address = address;
        this.partyName = partyName;
        this.phone = phone;
        this.websiteUrl = websiteUrl;
        this.photoUrl = photoUrl;
        this.officeTitle = officeTitle;
        this.email = email;
        this.googleChannel = googleChannel;
        this.youtubeChannel = youtubeChannel;
        this.twitterChannel = twitterChannel;
        this.facebookChannel = facebookChannel;
    }

    protected Official(Parcel in) {
        name = in.readString();
        address = in.readString();
        partyName = in.readString();
        phone = in.readString();
        websiteUrl = in.readString();
        photoUrl = in.readString();
        officeTitle = in.readString();
        email = in.readString();
        googleChannel = in.readString();
        youtubeChannel = in.readString();
        twitterChannel = in.readString();
        facebookChannel = in.readString();
    }

    public static final Creator<Official> CREATOR = new Creator<Official>() {
        @Override
        public Official createFromParcel(Parcel in) {
            return new Official(in);
        }

        @Override
        public Official[] newArray(int size) {
            return new Official[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getPartyName() {
        return partyName;
    }

    public String getPhone() {
        return phone;
    }

    public String getWebsiteUrl() {
        return websiteUrl;
    }

    public String getPhotoUrl() {
        return photoUrl;
    }

    public String getOfficeTitle() {
        return officeTitle;
    }

    public String getEmail() {
        return email;
    }

    public String getGoogleChannel() {
        return googleChannel;
    }

    public String getYoutubeChannel() {
        return youtubeChannel;
    }

    public String getTwitterChannel() {
        return twitterChannel;
    }

    public String getFacebookChannel() {
        return facebookChannel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(name);
        parcel.writeString(address);
        parcel.writeString(partyName);
        parcel.writeString(phone);
        parcel.writeString(websiteUrl);
        parcel.writeString(photoUrl);
        parcel.writeString(officeTitle);
        parcel.writeString(email);
        parcel.writeString(googleChannel);
        parcel.writeString(youtubeChannel);
        parcel.writeString(twitterChannel);
        parcel.writeString(facebookChannel);

    }
}
