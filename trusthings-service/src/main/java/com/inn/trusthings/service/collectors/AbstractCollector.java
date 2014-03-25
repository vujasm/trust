package com.inn.trusthings.service.collectors;

public abstract class AbstractCollector implements Collector{
	
	String sourceUri = null;
	
	public void setSourceUri(String sourceUri) {
		this.sourceUri = sourceUri;
	}
	
	public String getSourceUri() {
		return sourceUri;
	}
	
	
	public AbstractCollector(String sourceUri) {
		this.sourceUri = sourceUri;
	}
}
