package com.inn.trusthings.op.composition;

import java.net.URI;

import com.inn.trusthings.model.vocabulary.Trust;


public class AggrFunctionGetter {
	
	public synchronized static AggregationFunction getFunction(String type, EnumStructure structure){
		
		if (structure == EnumStructure.PARALLEL){
			return new Min();
		}
		if (structure == EnumStructure.SEQUENTAL){
			if (type.equalsIgnoreCase(Trust.NumberOfCompositions.getURI())
					|| type.equalsIgnoreCase(Trust.NumberOfDevelopers.getURI())
					|| type.equalsIgnoreCase(Trust.NumberOfRequests.getURI())){
				return new Product();
			}
			return new Average();
		}
		
		return new Average();
		
	}

	public static AggregationFunction getFunction(URI uri, EnumStructure structure) {
		return getFunction(uri.toASCIIString(), structure);
	}

}
