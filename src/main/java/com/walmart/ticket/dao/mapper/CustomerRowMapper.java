package com.walmart.ticket.dao.mapper;

import com.walmart.ticket.model.Customer;
import com.walmart.ticket.model.Reservation;
import org.springframework.jdbc.core.RowMapper;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by bmaria001c on 5/2/16.
 */
public class CustomerRowMapper implements RowMapper<Customer> {
        //ID,EMAIL,FIRST_NAME,LAST_NAME
        public Customer mapRow(ResultSet rs, int rowNum) throws SQLException {
            Customer customer = new Customer();
            customer.setId(rs.getInt("ID"));
            customer.setEmailId(rs.getString("EMAIL"));
            customer.setFirstName(rs.getString("FIRST_NAME"));
            customer.setLastName(rs.getString("LAST_NAME"));
            return customer;
    }


}
