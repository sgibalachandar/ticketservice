package com.walmart.ticket.dao;

import com.walmart.ticket.dao.mapper.CustomerRowMapper;
import com.walmart.ticket.exception.ErrorCategory;
import com.walmart.ticket.exception.ServiceErrorCode;
import com.walmart.ticket.exception.ServiceRuntimeException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import com.walmart.ticket.model.Customer;

@Repository
public class CustomerDao implements ICustomerDao{
	private static final Log LOG = LogFactory.getLog(CustomerDao.class);
	NamedParameterJdbcTemplate namedParameterJdbcTemplate;
	private static final String REGISTER_CUSTOMER_SQL = "INSERT INTO CUSTOMER  (ID,EMAIL,FIRST_NAME,LAST_NAME) VALUES(NEXT VALUE FOR CUSTOMER_SEQ,:emailId,:firstName,:lastName)";
	private static final String GET_CUSTOMER = "SELECT ID,EMAIL,FIRST_NAME,LAST_NAME FROM  CUSTOMER  WHERE EMAIL = :emailId ";

	@Autowired
	public void setNamedParameterJdbcTemplate(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
		this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
	}
	public int registerCustomer(Customer customer){

		if(getCustomer(customer.getEmailId()) != null){
			throw new ServiceRuntimeException(ErrorCategory.DATA, ServiceErrorCode.CUSTOMER_ALREADY_EXISTS);
		}
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("emailId", customer.getEmailId());
		namedParameters.addValue("firstName", customer.getFirstName());
        namedParameters.addValue("lastName", customer.getLastName());
        int numberOfRowAfftected  = namedParameterJdbcTemplate.update(REGISTER_CUSTOMER_SQL,
                namedParameters);
       return 1;

	}
	public Customer getCustomer(String emailId){

		Customer customer = null;
		MapSqlParameterSource namedParameters = new MapSqlParameterSource();
		namedParameters.addValue("emailId", emailId);
		try {
			customer = namedParameterJdbcTemplate.queryForObject(GET_CUSTOMER, namedParameters, new CustomerRowMapper());
		}catch(EmptyResultDataAccessException e){
			LOG.error("Customer not exist:"+emailId);
		}catch (Exception e){
			LOG.error("Unknown exception occurred while getting customer :"+emailId,e);
			throw new ServiceRuntimeException(ErrorCategory.SYSTEM, ServiceErrorCode.INTERNAL_SERVER_ERROR);
		}
		return customer;

	}

}
