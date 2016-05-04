package com.walmart.ticket.model;

/**
 * Created by bmaria001c on 4/30/16.
 */
public class AvailableStatus {
    private String venue;
    private int seatsAvailable;

    public int getSeatsAvailable() {
        return seatsAvailable;
    }

    public void setSeatsAvailable(int seatsAvailable) {
        this.seatsAvailable = seatsAvailable;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

}
