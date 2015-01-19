package com.inn.trusthings.rest.exception;

import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class TrustRestException extends WebApplicationException {
	 
	 
	  /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	  * Create a HTTP 400 (BAD_REQUEST) exception.
	  * @param message the String that is the entity of the 400 response.
	  */
	  public TrustRestException(String message) {
	    super(Response.status(Response.Status.BAD_REQUEST).
	    entity(message).type("text/plain").build());
	  }
	  
	  public TrustRestException(Throwable t) {
		    super(Response.status(Response.Status.BAD_REQUEST).
		    entity(t.getMessage()).type("text/plain").build());
		  }
	}