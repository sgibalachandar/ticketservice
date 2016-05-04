package com.walmart.ticket.dao;

import com.google.common.collect.Lists;
import com.walmart.ticket.exception.ServiceRuntimeException;
import com.walmart.ticket.model.Customer;
import com.walmart.ticket.model.Reservation;
import com.walmart.ticket.model.Venue;
import com.walmart.ticket.model.VenueDetail;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.isA;
import static org.mockito.Mockito.when;
import static org.junit.Assert.assertEquals;
import rx.Observable;
import java.util.List;

/**
 * Created by bmaria001c on 5/3/16.
 */
@RunWith(MockitoJUnitRunner.class)
public class TestTicketDao {
    @Mock
    private NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    @Mock
    private ICustomerDao customerDao;
    private TicketDao ticketDao = new TicketDao();
    @Mock
    private Reservation reservation;

    @Before
    public void setup() throws Exception{
        ticketDao.setCustomerDao(this.customerDao);
        ticketDao.setLimitPerCustomer(10);
        ticketDao.setNamedParameterJdbcTemplate(this.namedParameterJdbcTemplate);
    }
    @Test
    public void testGetTotalSeatsFromDb(){

        VenueDetail venueDetail  = new VenueDetail();
        venueDetail.setNumberOfRow(2);
        venueDetail.setSeatsInRow(5);
        when(namedParameterJdbcTemplate.queryForObject(isA(String.class), isA(MapSqlParameterSource.class), isA(BeanPropertyRowMapper.class)))
                .thenReturn(venueDetail);
        Observable<Integer> seats  =  ticketDao.getTotalSeats(1);
        assertEquals(Integer.valueOf(10), seats.toBlocking().single());
    }
    @Test
    public void testGetReservedSeats(){
        when(namedParameterJdbcTemplate.queryForInt(isA(String.class), isA(MapSqlParameterSource.class)))
                .thenReturn(2);
        Observable<Integer> reserved = ticketDao.getReservedSeats(1);
        assertEquals(Integer.valueOf(2),reserved.toBlocking().single());
    }
    @Test(expected = ServiceRuntimeException.class)
    public void testHoldTicketWithoutRegisteringCustomer(){
        when(customerDao.getCustomer(isA(String.class))).thenReturn(null);

        ticketDao.holdTicket(reservation);

    }
    @Test(expected = ServiceRuntimeException.class)
    public void testCustomerThresholdLimit(){
        Reservation reservationForHold = new Reservation();
        reservationForHold.setNumberOfSeats(10);
        List<Reservation> reserve  = Lists.newArrayList();
        reserve.add(reservationForHold);
        Customer customer = Mockito.mock(Customer.class);
        when(customerDao.getCustomer(anyString())).thenReturn(customer);
        when(ticketDao.getTicketOnHold(isA(String.class))).thenReturn(reserve);

        ticketDao.holdTicket(reservationForHold);
    }
}
