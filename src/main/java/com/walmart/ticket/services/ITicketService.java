package com.walmart.ticket.services;

import com.walmart.ticket.model.AvailableStatus;
import com.walmart.ticket.model.Reservation;
import com.walmart.ticket.model.Venue;
import com.walmart.ticket.model.VenueDetail;
import rx.Observable;

import java.util.List;

/**
 * Created by bmaria001c on 4/30/16.
 */
public interface ITicketService {
    /**
     *
     * @return List of VenueDetail
     */
    public Observable<List<VenueDetail>> getVenueDetail();
    public Observable<AvailableStatus> getAvailableSeats(int levelId);
    public void findAndHold(String emailId,int numberOfSeats,Venue... venues);
    public int holdTicket(Reservation reservation);
    public List<Reservation> getTicketOnHold(String emailId);
    public void reserveTicket(int reservationId);
}
