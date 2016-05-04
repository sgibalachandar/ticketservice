package com.walmart.ticket.resources;


import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;

import com.walmart.ticket.dao.ICustomerDao;
import com.walmart.ticket.model.Customer;

@Path("/register/customers")
@Produces({ MediaType.APPLICATION_JSON + ";charset=utf-8" })
@Component
public class CustomerResource {

	@Autowired
	private ICustomerDao customerDao;

	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@Consumes(MediaType.APPLICATION_JSON)
	@Path("/customer")
	public Response registerCutomer(Customer customer){

		int customerId  = customerDao.registerCustomer(customer);
		return Response.status(Status.CREATED).build();

	}

}
