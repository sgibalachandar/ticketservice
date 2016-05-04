package com.walmart.ticket.dao;

import com.google.common.base.Function;
import com.walmart.ticket.dao.mapper.ReservationRowMapper;
import com.walmart.ticket.exception.ErrorCategory;
import com.walmart.ticket.exception.ServiceErrorCode;
import com.walmart.ticket.exception.ServiceRuntimeException;
import com.walmart.ticket.model.Customer;
import com.walmart.ticket.model.Reservation;
import com.walmart.ticket.model.VenueDetail;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import rx.Observable;
import rx.functions.Func0;

/**
 * Created by bmaria001c on 4/30/16.
 */
@Repository

public class TicketDao implements ITicketDao {

    private static final Log LOG = LogFactory.getLog(ITicketDao.class);
    private static final String GET_VENUE_DETAILS = "SELECT LEVEL_ID,LEVEL_NAME,PRICE,NUMBER_OF_ROW,SEATS_IN_ROW FROM VENUE";
    private static final String GET_TOTAL_SEATS_FOR_LEVEL = " SELECT LEVEL_ID,LEVEL_NAME,PRICE,NUMBER_OF_ROW,SEATS_IN_ROW FROM VENUE WHERE LEVEL_ID = :levelId ";
    private static final String GET_RESERVED_SEATS = "SELECT SUM(NUMBER_OF_SEATS) FROM RESERVATION WHERE VENUE_LEVEL_ID = :levelId ";
    private static final String HOLD_TICKET = " INSERT INTO RESERVATION (RESERVATION_ID,CUSTOMER_ID,VENUE_LEVEL_ID,NUMBER_OF_SEATS,HOLD_FLAG,TIME_RESERVED) " +
                                                "VALUES (NEXT VALUE FOR RESERVATION_SEQ,:customerId,:levelId,:numberOfSeats,'Y',CURRENT_TIMESTAMP)";

    private static final String GET_TICKET_ONHOLD = " SELECT RESERVATION_ID,CUSTOMER_ID,VENUE_LEVEL_ID,NUMBER_OF_SEATS,HOLD_FLAG,TIME_RESERVED FROM RESERVATION " +
                                                    " WHERE CUSTOMER_ID = :customerId AND HOLD_FLAG='Y'";
    private static final String RESERVE_TICKET = " UPDATE  RESERVATION SET HOLD_FLAG='N' WHERE RESERVATION_ID = :reservationId";
    private static final String TICKETS_ON_HOLD = " SELECT RESERVATION_ID,CUSTOMER_ID,VENUE_LEVEL_ID,NUMBER_OF_SEATS,HOLD_FLAG,TIME_RESERVED FROM RESERVATION WHERE HOLD_FLAG='Y'";
    private static final String DELETE_TICKETS_ON_HOLD = " DELETE FROM RESERVATION WHERE RESERVATION_ID IN (:ids) ";


    NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    ICustomerDao customerDao;
    @Value("${allowed.seats.percustomer}")
    private int limitPerCustomer;

    public void setLimitPerCustomer(int limitPerCustomer){
        this.limitPerCustomer =  limitPerCustomer;
    }
    @Autowired
    public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
    }
    @Autowired
    public void setCustomerDao(ICustomerDao customerDao){
        this.customerDao = customerDao;
    }
    private int getTotalSeatsFromDb(int levelId){
        int totalSeats  = 0;
        try {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("levelId", levelId);
            VenueDetail venueDetail = (VenueDetail) namedParameterJdbcTemplate.queryForObject(GET_TOTAL_SEATS_FOR_LEVEL, namedParameters, new BeanPropertyRowMapper(VenueDetail.class));
            if (venueDetail != null) {
                totalSeats = venueDetail.getSeatsInRow() * venueDetail.getNumberOfRow();
            }
        }catch(EmptyResultDataAccessException e){
            LOG.error("Invalid levelId passed in : "+levelId,e);
            throw new ServiceRuntimeException(ErrorCategory.DATA, ServiceErrorCode.LEVEL_DOES_NOT_EXIST);
        }catch(Exception e){
            LOG.error("Unknow error occurred while finding level:"+levelId);
            throw new ServiceRuntimeException(ErrorCategory.SYSTEM,ServiceErrorCode.INTERNAL_SERVER_ERROR);
        }
        return totalSeats;
    }
    public Observable<Integer> getTotalSeats(final int levelId){
        return Observable.defer(new Func0<Observable<Integer>>() {
            public Observable<Integer> call() {
               return Observable.just(getTotalSeatsFromDb(levelId));
            }
        });
    }
    private int getReservedSeatsFromDb(int levelId){

        int seatsReserved  = 0;
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("levelId", levelId);
        seatsReserved  = namedParameterJdbcTemplate.queryForInt(GET_RESERVED_SEATS, namedParameters);
        return seatsReserved;
    }
    public Observable<Integer> getReservedSeats(final int levelId){

         return Observable.defer(new Func0<Observable<Integer>>() {
            public Observable<Integer> call() {
                return Observable.just(getReservedSeatsFromDb(levelId));
            }
        });
    }
    public List<VenueDetail> getVenueDetail(){
       List<VenueDetail> venueDetails = namedParameterJdbcTemplate.query(GET_VENUE_DETAILS, BeanPropertyRowMapper.newInstance(VenueDetail.class));
       return venueDetails;
    }

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public int holdTicket(Reservation reservation){
        Customer customer  = customerDao.getCustomer(reservation.getEmailId());
        int status = 0;
        if(customer == null){
            throw new ServiceRuntimeException(ErrorCategory.DATA,ServiceErrorCode.CUSTOMER_NOT_REGISTERED);
        }
        if(OF_RESERVED_TICKET_BY_CUSTOMER.apply(getTicketOnHold(reservation.getEmailId())) >= limitPerCustomer){
            throw new ServiceRuntimeException(ErrorCategory.DATA,ServiceErrorCode.CUSTOMER_REACHED_LIMIT_TO_HOLD_SEATS);
        }
        if((getTotalSeatsFromDb(reservation.getLevelId()) - getReservedSeatsFromDb(reservation.getLevelId())) < reservation.getNumberOfSeats()){
            throw new ServiceRuntimeException(ErrorCategory.DATA,ServiceErrorCode.SEATS_UNAVAILABLE);
        }
        try {
            MapSqlParameterSource namedParameters = new MapSqlParameterSource();
            namedParameters.addValue("customerId", customer.getId());
            namedParameters.addValue("levelId", reservation.getLevelId());
            namedParameters.addValue("numberOfSeats", reservation.getNumberOfSeats());
            status = namedParameterJdbcTemplate.update(HOLD_TICKET, namedParameters);
        }catch (Exception e){
            LOG.error("Unable to hold seats -"+reservation.getEmailId()+","+reservation.getLevelId()+","+reservation.getNumberOfSeats(),e);
            throw new ServiceRuntimeException(ErrorCategory.SYSTEM,ServiceErrorCode.INTERNAL_SERVER_ERROR,e);

        }
        return status;

    }

    public List<Reservation> getTicketOnHold(String emailId){

        Customer customer  = customerDao.getCustomer(emailId);
        if(customer == null){
            throw new ServiceRuntimeException(ErrorCategory.DATA,ServiceErrorCode.CUSTOMER_NOT_REGISTERED);
        }
        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("customerId",customer.getId());
        List<Reservation> reservation = namedParameterJdbcTemplate.query(GET_TICKET_ONHOLD, namedParameters, new ReservationRowMapper());
        return  reservation;
    }
    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void reserveTicket(int reservationId){

        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("reservationId",reservationId);
        try {
            int count  = namedParameterJdbcTemplate.update(RESERVE_TICKET, namedParameters);
            if(count == 0){
                throw new ServiceRuntimeException(ErrorCategory.DATA,ServiceErrorCode.HOLD_EXPIRED);
            }
        }catch(Exception e){
            LOG.error("Unable to reserver the seats-"+reservationId,e);
            throw new ServiceRuntimeException(ErrorCategory.SYSTEM,ServiceErrorCode.UNABLE_TO_RESERVE_SEATS);
        }
    }

    public List<Reservation> getAllTicketsOnHold(){

        List<Reservation> ticketsOnHold = namedParameterJdbcTemplate.query(TICKETS_ON_HOLD, new ReservationRowMapper());
        return ticketsOnHold;
    }
    public void deleteExpiredTickets(Set<Integer> reservationIds){

        MapSqlParameterSource namedParameters = new MapSqlParameterSource();
        namedParameters.addValue("ids",reservationIds);
        namedParameterJdbcTemplate.update(DELETE_TICKETS_ON_HOLD,namedParameters);
    }
    private static final Function<List<Reservation>,Integer> OF_RESERVED_TICKET_BY_CUSTOMER = new Function<List<Reservation>, Integer>() {
        public Integer apply(List<Reservation> reservations) {
            int totalNumberOfSeats = 0;
            for(Reservation reservation : reservations){
                totalNumberOfSeats+= reservation.getNumberOfSeats();
            }
            return Integer.valueOf(totalNumberOfSeats);
        }
    };
}

