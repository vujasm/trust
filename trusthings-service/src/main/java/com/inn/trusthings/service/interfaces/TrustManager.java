package com.inn.trusthings.service.interfaces;

import com.inn.trusthings.model.pojo.TrustCriteria;
import com.inn.trusthings.service.config.GlobalTrustCriteria;

public interface TrustManager {
	
	
	/**
	 *  Set global trust request. If not set, the trust manager will be using the default the default one {@link GlobalTrustCriteria}
	 * @param criteria Trust criteria as POJO
	 */
	public void setGlobalTrustCriteria(TrustCriteria criteria);
	
	/**
	 * Set global trust request. If not set, the trust manager will be using the default the default one {@link GlobalTrustCriteria}
	 * @param critaeriaAsJson  Trust criteria as Json string
	 */
	public void setGlobalTrustCriteria(String critaeriaAsJson) ; 
	
	
	public TrustCriteria getGlobalTrustCriteria();

}
