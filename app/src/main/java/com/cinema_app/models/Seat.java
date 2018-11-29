package com.cinema_app.models;

import android.os.Parcel;
import android.os.Parcelable;

public class Seat implements Parcelable {

    public static final Parcelable.Creator CREATOR = new Parcelable.Creator() {
        public Seat createFromParcel(Parcel in) {
            return new Seat(in);
        }

        public Seat[] newArray(int size) {
            return new Seat[size];
        }
    };

    public int id;
    public String status;
    public String id_customer;

    public Seat(int id, String status,String id_customer) {
        this.id = id;
        this.status = status;
        this.id_customer = id_customer;
    }



    public String getId_customer() {
        return id_customer;
    }

    public void setId_customer(String id_customer) {
        this.id_customer = id_customer;
    }

    public Seat(){}

    public int getId() {
        return id;
    }

    public String isStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    // Parcelling part
    public Seat(Parcel in){
        this.id = in.readInt();
        this.status = in.readString();

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.id);
        dest.writeString(this.status);

    }

    @Override
    public String toString() {
        return "Seat{" +
                "id='" + id + '\'' +
                ", status='" + status + '\'' +

                '}';
    }
}
