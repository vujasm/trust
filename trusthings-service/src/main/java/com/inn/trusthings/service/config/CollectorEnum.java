package com.inn.trusthings.service.config;

import com.inn.trusthings.service.collectors.ActivityCollector;
import com.inn.trusthings.service.collectors.Collector;
import com.inn.trusthings.service.collectors.FeedbackCollector;
import com.inn.trusthings.service.collectors.QoSCollector;
import com.inn.trusthings.service.collectors.ReputationCollector;

public enum CollectorEnum {
	

	Reputation( new ReputationCollector("http://localhost//")),
	Activity( new ActivityCollector("http://localhost//")),
	Feedback( new FeedbackCollector("http://localhost//")),
	QoS( new QoSCollector("http://localhost//"));
	
	private  final Collector collector;
	
	CollectorEnum(Collector collector) {
		this.collector = collector;
	}
	
	public Collector getCollector() {
		return collector;
	}

}
