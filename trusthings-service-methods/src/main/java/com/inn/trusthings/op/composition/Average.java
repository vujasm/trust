package com.inn.trusthings.op.composition;

import java.util.List;

import com.inn.trusthings.model.graph.Vertex;
import com.inn.trusthings.model.pojo.TrustAttribute;

public class Average extends AggregationFunction {

	
	@Override
	public Double compute(List<Vertex> vertices, TrustAttribute attribute) {
		Double result = 0D;
		for (Vertex vertex : vertices) {
			Double val = findAttributeNormalizedValue(vertex, attribute);
			result = result + val;
		}
		return (result / vertices.size());
	}
}
