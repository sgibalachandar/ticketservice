package com.walmart.ticket.dao.mapper;

import com.walmart.ticket.model.Reservation;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by bmaria001c on 5/1/16.
 */
public class ReservationRowMapper implements RowMapper<Reservation> {
    //RESERVATION_ID,CUSTOMER_ID,VENUE_LEVEL_ID,NUMBER_OF_SEATS,HOLD_FLAG,TIME_RESERVED
    public Reservation mapRow(ResultSet rs, int rowNum) throws SQLException {
        Reservation reservation = new Reservation();
        reservation.setReservationId(rs.getInt("RESERVATION_ID"));
        reservation.setNumberOfSeats(rs.getInt("NUMBER_OF_SEATS"));
        reservation.setLevelId(rs.getInt("VENUE_LEVEL_ID"));
        reservation.setCusomerId(rs.getInt("CUSTOMER_ID"));
        reservation.setTimeReserved(rs.getTimestamp("TIME_RESERVED"));
        return reservation;
    }
}
