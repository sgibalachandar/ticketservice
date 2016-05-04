package com.walmart.ticket.services;

import com.walmart.ticket.dao.ITicketDao;
import com.walmart.ticket.model.AvailableStatus;
import com.walmart.ticket.model.Reservation;
import com.walmart.ticket.model.Venue;
import com.walmart.ticket.model.VenueDetail;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import rx.Observable;
import rx.functions.Func0;
import rx.functions.Func2;
import rx.schedulers.Schedulers;

/**
 * Created by bmaria001c on 4/30/16.
 */
@Component
public class TicketService implements  ITicketService {

    @Autowired
    private ITicketDao ticketDao;
    public Observable<List<VenueDetail>> getVenueDetail(){
        return Observable.defer(new Func0<Observable<List<VenueDetail>>>() {
                                    public Observable<List<VenueDetail>> call() {
                                        return Observable.just(ticketDao.getVenueDetail());
                                    }
                                });

    }
    public Observable<AvailableStatus> getAvailableSeats(final int levelId){
       return Observable.zip(ticketDao.getTotalSeats(levelId).subscribeOn(Schedulers.io()), ticketDao.getReservedSeats(levelId).subscribeOn(Schedulers.io()), new Func2<Integer, Integer, AvailableStatus>() {

            public AvailableStatus call(Integer totalSeat,Integer reserverdSeats){
                AvailableStatus availableStatus = new AvailableStatus();
                availableStatus.setSeatsAvailable(totalSeat - reserverdSeats);
                availableStatus.setVenue(Venue.getVenue(levelId).getLevelName());
                return availableStatus;
            }
        });

    }
    public int holdTicket(Reservation reservation){
        return ticketDao.holdTicket(reservation);
    }
    public List<Reservation> getTicketOnHold(String emailId){
        return  ticketDao.getTicketOnHold(emailId);
    }
    public void reserveTicket(int reservationId){
        ticketDao.reserveTicket(reservationId);
    }
    public void findAndHold(String emailId,int numberOfSeats,Venue... venues){
        for(Venue venue : venues) {
            AvailableStatus status  = getAvailableSeats(venue.getLevelId()).toBlocking().single();
            if(status.getSeatsAvailable() >= numberOfSeats){
                Reservation reservation = new Reservation();
                reservation.setEmailId(emailId);
                reservation.setLevelId(venue.getLevelId());
                reservation.setNumberOfSeats(numberOfSeats);
                ticketDao.holdTicket(reservation);
                break;
            }
        }
    }
}
