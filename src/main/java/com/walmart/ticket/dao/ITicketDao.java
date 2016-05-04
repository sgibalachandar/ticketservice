package com.walmart.ticket.dao;

import com.walmart.ticket.dao.mapper.ReservationRowMapper;
import com.walmart.ticket.model.Reservation;
import com.walmart.ticket.model.VenueDetail;
import java.util.List;
import java.util.Set;

import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import rx.Observable;

public interface ITicketDao {

	public Observable<Integer> getTotalSeats(int  levelId);
	public Observable<Integer> getReservedSeats(int levelId);
	public List<VenueDetail> getVenueDetail();
	public int holdTicket(Reservation reservation);
	public List<Reservation> getTicketOnHold(String emailId);
	public void reserveTicket(int reservationId);
	public List<Reservation> getAllTicketsOnHold();
	public void deleteExpiredTickets(Set<Integer> reservationIds);
}
