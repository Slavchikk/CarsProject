package com.example.carsproject;


import android.os.Parcel;
import android.os.Parcelable;

public class Cars implements Parcelable {
    private int ID;
    private int price;
    private String Model;
    private String Brand;
    private String ArticleNumber;

    public Cars(int id_cars, int price, String carsBrand, String carsModel, String articleNumber) {
        this.ID = id_cars;
        this.price = price;
        this.Model = carsModel;
        this.Brand = carsBrand;
        ArticleNumber = articleNumber;
    }

    protected Cars(Parcel in) {
        ID = in.readInt();
        price = in.readInt();
        Model = in.readString();
        Brand = in.readString();
        ArticleNumber = in.readString();
    }

    public static final Creator<Cars> CREATOR = new Creator<Cars>() {
        @Override
        public Cars createFromParcel(Parcel in) {
            return new Cars(in);
        }

        @Override
        public Cars[] newArray(int size) {
            return new Cars[size];
        }
    };


    public void setID(int id_cars) {
        this.ID = id_cars;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setModel(String model) {
        Model = model;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public void setArticleNumber(String articleNumber) {
        ArticleNumber = articleNumber;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(ID);
        parcel.writeInt(price);
        parcel.writeString(Model);
        parcel.writeString(Brand);
        parcel.writeString(ArticleNumber);
    }

    public int getPrice() {
        return price;
    }

    public String getModel() {
        return Model;
    }

    public String getBrand() {
        return Brand;
    }

    public String getArticleNumber() {
        return ArticleNumber;
    }

    public int getID() {
        return ID;
    }
}
