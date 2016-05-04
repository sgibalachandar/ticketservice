package com.walmart.ticket;

import com.walmart.ticket.dao.ITicketDao;
import com.walmart.ticket.model.Reservation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.joda.time.DateTime;
import org.joda.time.Seconds;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by bmaria001c on 5/1/16.
 */
@Component
public class Scheduler {
    private static final Log LOG = LogFactory.getLog(Scheduler.class);
    @Autowired
    private ITicketDao ticketDao;
    @Value("${ticket.expiration.period.secs}")
    private int ticketExpirationPeriodInSeconds;
    @Scheduled(fixedDelay = 10000)
    @Transactional(isolation = Isolation.SERIALIZABLE)
    //Scheduled to run every 10 secs
    public void testScheduler(){
        List<Reservation> ticketsOnHold  = ticketDao.getAllTicketsOnHold();
        Set<Integer> reservationsToDelete = new HashSet<Integer>();
        for(Reservation reservation  : ticketsOnHold){
            reservation.getTimeReserved();
            DateTime timerReserved = new DateTime(reservation.getTimeReserved());
            DateTime now =  DateTime.now();
            if(Seconds.secondsBetween(timerReserved,now).getSeconds() > ticketExpirationPeriodInSeconds){
                reservationsToDelete.add(reservation.getReservationId());
            }
        }
        if(reservationsToDelete.size() > 0){
            LOG.info("Deleting expired hold : "+reservationsToDelete);
            ticketDao.deleteExpiredTickets(reservationsToDelete);
        }
    }
}
