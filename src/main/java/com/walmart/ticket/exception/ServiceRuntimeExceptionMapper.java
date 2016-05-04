package com.walmart.ticket.exception;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

@Provider
public class ServiceRuntimeExceptionMapper implements ExceptionMapper<ServiceRuntimeException> {


	private static final Log LOG = LogFactory.getLog(ServiceRuntimeExceptionMapper.class);
	public Response toResponse(ServiceRuntimeException exception) {
		  return  Response.status(exception.getErrorCode().getHttpstatus())
	                        .type(MediaType.APPLICATION_JSON + "; charset=UTF-8")
	                        .entity(exception.getErrorCode().getMessage()).build();
	}

}