package com.inn.trusthings.rest.exception;

/*
 * #%L
 * trusthings-webservice
 * %%
 * Copyright (C) 2014 - 2015 INNOVA S.p.A
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */


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