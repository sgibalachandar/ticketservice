package com.walmart.ticket.dao;

import com.walmart.ticket.model.Customer;

public interface ICustomerDao {

	public int registerCustomer(Customer customer);
	public Customer getCustomer(String emailId);
}
