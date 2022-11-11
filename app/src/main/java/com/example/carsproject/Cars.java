package com.example.carsproject;

import android.os.Parcel;
import android.os.Parcelable;

public class Cars implements Parcelable {

    public Integer id_cars;
    public String carsBrand;
    public String carsModel;
    public int price;
    public String image;

    public Cars(Integer id_cars, String carsBrand, String carsModel, int price, String image) {
        this.id_cars = id_cars;
        this.carsBrand = carsBrand;
        this.carsModel = carsModel;
        this.price = price;
        this.image = image;
    }

    protected Cars(Parcel in) {
        id_cars = in.readInt();
        carsBrand = in.readString();
        carsModel = in.readString();
        price = in.readInt();
        image = in.readString();
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






    public void setId_cars(Integer id_cars) {
        this.id_cars = id_cars;
    }

    public void setCarsBrand(String carsBrand) {
        this.carsBrand = carsBrand;
    }

    public void setCarsModel(String carsModel) {
        this.carsModel = carsModel;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setImage(String image) {
        this.image = image;
    }


    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id_cars);
        dest.writeString(carsBrand);
        dest.writeString(carsModel);
        dest.writeInt(price);
        dest.writeString(image);
    }

    public int getId_cars() {
        return id_cars;
    }

    public String getCarsBrand() {
        return carsBrand;
    }

    public String getCarsModel() {
        return carsModel;
    }

    public int getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }



}

