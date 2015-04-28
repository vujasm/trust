package com.inn.trusthings.op.composition;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.inn.trusthings.model.graph.Vertex;
import com.inn.trusthings.model.pojo.TrustAttribute;

public abstract class AggregationFunction {
	
	Double returnValueWhenNoPresent= 1D;
	
	public abstract Double compute(List<Vertex> vertices, TrustAttribute attribute);
	
	public Double findAttributeNormalizedValue(Vertex vertex, TrustAttribute attribute){
		TrustAttribute trustAttribute = findAttribute(vertex, attribute);
		if (trustAttribute!=null){
				return vertex.getScores().get(trustAttribute);
		}
		return returnValueWhenNoPresent;
	}
	
	public TrustAttribute findAttribute(Vertex vertex, TrustAttribute attribute){
		URI typeRequired = attribute.getTypesAll().get(0).getUri();
		Map<TrustAttribute, Double> map = vertex.getScores();
		Set<TrustAttribute> set = map.keySet();
		for (TrustAttribute trustAttribute : set) {
			URI type = trustAttribute.getTypesAll().get(0).getUri();
			if (type.compareTo(typeRequired)==0){
				return trustAttribute;
			}
		}
		return null;
	}

}
