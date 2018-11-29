package com.cinema_app.models;

import java.util.List;

public class SeatList {

   public String id_movie;

   public List<Seat> seatList;

    public String getId_movie() {
        return id_movie;
    }

    public List<Seat> getSeatList() {
        return seatList;
    }

    public void setId_movie(String id_movie) {
        this.id_movie = id_movie;
    }

    public void setSeatList(List<Seat> seatList) {
        this.seatList = seatList;
    }
}
