package com.walmart.ticket.model;

import java.sql.Timestamp;

/**
 * Created by bmaria001c on 4/30/16.
 */
public class Reservation {
    private int reservationId;
    private String emailId;
    private int levelId;
    private int numberOfSeats;
    private java.sql.Timestamp timeReserved;
    private String holdFlag;

    public int getCusomerId() {
        return cusomerId;
    }

    public void setCusomerId(int cusomerId) {
        this.cusomerId = cusomerId;
    }

    private int cusomerId;
    public Timestamp getTimeReserved() {
        return timeReserved;
    }

    public String getHoldFlag() {
        return holdFlag;
    }

    public void setHoldFlag(String holdFlag) {
        this.holdFlag = holdFlag;
    }

    public void setTimeReserved(Timestamp timeReserved) {
        this.timeReserved = timeReserved;
    }

    public int getNumberOfSeats() {
        return numberOfSeats;
    }

    public void setNumberOfSeats(int numberOfSeats) {
        this.numberOfSeats = numberOfSeats;
    }

    public int getLevelId() {
        return levelId;
    }

    public void setLevelId(int levelId) {
        this.levelId = levelId;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public int getReservationId() {
        return reservationId;
    }

    public void setReservationId(int reservationId) {
        this.reservationId = reservationId;
    }

}
