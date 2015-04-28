package com.inn.trusthings.op.composition;

import java.util.List;

import com.inn.trusthings.model.graph.Vertex;
import com.inn.trusthings.model.pojo.TrustAttribute;

public class Min extends AggregationFunction {
	
	
	@Override
	public Double compute(List<Vertex> vertices, TrustAttribute attribute) {
		Double result = null ;
		for (Vertex vertex : vertices) {
			Double val = findAttributeNormalizedValue(vertex, attribute);
			if (result==null || val.compareTo(result)==-1){
				result = val;
			}
		}
		return result;
	}

}
