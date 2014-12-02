package com.inn.trusthings.db;

/*
 * #%L
 * trusthings-kbase
 * %%
 * Copyright (C) 2014 INNOVA S.p.A
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


import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.hp.hpl.jena.datatypes.RDFDatatype;
import com.hp.hpl.jena.datatypes.xsd.XSDDatatype;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.ResourceFactory;
import com.hp.hpl.jena.vocabulary.RDF;
import com.inn.trusthings.IBridge;
import com.inn.trusthings.d2r.Bridge;
import com.inn.trusthings.model.vocabulary.ModelEnum;
import com.inn.trusthings.model.vocabulary.NSPrefixes;
import com.inn.trusthings.model.vocabulary.Trust;
import com.inn.trusthings.model.vocabulary.UsdlSec;
import com.mysql.jdbc.PreparedStatement;

/**
 * Obtainer of trust profile from sql database and turn it into rdf
 * @author marko
 *
 */
public class BridgeDB implements IBridge{
	
	
	private static final Logger log = LoggerFactory.getLogger(BridgeDB.class);
	
	private String defaultDBHost = "d2rq.147.83.30.133.xip.io";
	private String defaultDBPort = "";
	private  String jdbc_url = "jdbc:mysql://localhost/composetrust?user=root&password=";
	
	private Connection conn = null;
	
	java.sql.PreparedStatement ps = null;
	
	
	private String sqlString = 
			" select a.ID hasID "+
			" ,  a.ALTID hasCOMPOSEUID "+
			" , a.NAME hasName "+
//			" , a.NUMBEROFDEVELOPERS hasNumberOfDevelopers "+
//			" , a.NUMBEROFFOLLOWERS hasNumberOfFollowers "+
//			" , a.NUMBEROFMASHUPS  hasNumberOfMashups "+
			" , tp.id hasProfile "+
			" , attr.id hasAttribute "+
			" , attrtype.NAME hasType "+
			" , attr.VALUE hasValue "+
			" ,sec.id hasSecurityDescription "+
			" , secgoal.Name hasSecurityGoal "+
			" , secmech.Name hasSecurityMechanism "+
			" , sectech.Name isRealizedByTechnology "+
			" ,cert.id hasCertificateDetail "+
			" , cert.COUNTRY hasCountry "+
			" , auth.name hasCertificateAuthority "+
			" from agent a  "+
			" 	join profileorrequest tp on a.id = tp.AGENTID "+
			"   join attribute attr ON tp.ID = attr.porid "+
			"  join attributetype attrtype on  attr.TYPEID = attrtype.id "+
			" left join servicesecurity sec on  attr.SECURITYDESCRID = sec.id "+
			" 	left join securityconcept secgoal on sec.goal = secgoal.id "+
			" 	left join securityconcept secmech on sec.mechanism  = secmech.id "+
			" 	left join securityconcept sectech on sec.technology = sectech.id "+
			" 	left join certificatedetail cert on attr.CERTIFICATEDETAIL_ID = cert.id "+
			" 	left join authority auth on cert.AUTHORITY_ID = auth.id "+
			" where a.altid = ? ";

	
	
	public BridgeDB(){
		String host  = System.getProperty("iserve.filter.trust.dbhost", this.defaultDBHost);
		String port = System.getProperty("iserve.filter.trust.dbport", this.defaultDBPort);
		log.info(jdbc_url);
	}

	
	@Override
	public Model obtainTrustProfile(String serviceId)   {
		ResultSet rs;
		try {
			rs = executeSelect(serviceId);
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}
		Model m = toJenaModel(rs);
		return m;
	}


	private Model toJenaModel(ResultSet rs) {
		Model model = ModelFactory.createDefaultModel();
		model.setNsPrefixes(NSPrefixes.map);
		
		if (rs!= null){
			try {
				Resource profile = null;
				while (rs.next()){
					Integer agentId = rs.getInt(1);
					URL agentComposeUID = rs.getURL(2);
					String agentName = rs.getString(3);
					Integer profileId = rs.getInt(4);
					if (profile == null){
						profile = addAgentAndProfile(model, agentId, agentName, agentComposeUID, profileId);
					}
					Integer attributeId = rs.getInt(5);
					String attributeType = rs.getString(6);
					Object value = rs.getObject(7);
					Resource attribute = addAttributeToProfile(profile, model,attributeId, attributeType, value);
					if (Trust.SecurityGuarantee.toString().contains(attributeType)){
							Integer securityDescrId = rs.getInt(8);
							String goal = rs.getString(9);
							String mechanism = rs.getString(10);
							String technology = rs.getString(11);
							addSecurityDescription(attribute, model, securityDescrId, goal, mechanism, technology);
							//do security
						}
						if (Trust.CertificateAuthorityAttribute.toString().contains(attributeType)){
							Integer caId = rs.getInt(12);
							String country = rs.getString(13);
							String authority = rs.getString(14);
							addCADescription(attribute, model, caId, country, authority);
						}
				}
				rs.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
		return model;
	}


	private void addCADescription(Resource attribute, Model model, Integer caId, String country, String authority) {
		Resource descr = createJenaResource(NSPrefixes.map.get("db"),"certificatedetail",caId);
		model.add(attribute, Trust.hasCertificateDetail,descr);
		model.add(descr, RDF.type,ResourceFactory.createResource(NSPrefixes.map.get("compose-sec")+"CertificateDetail"));
		if (country !=null ){
			model.add(descr,	ModelFactory.createDefaultModel().
					createProperty(ModelEnum.SecurityOntology.getURI()+"#hasCountry")
			, ResourceFactory.createResource(NSPrefixes.map.get("compose-sec")+country) );
		}
		if (authority!=null){

			model.add(descr,	ModelFactory.createDefaultModel().
					createProperty(ModelEnum.SecurityOntology.getURI()+"#hasCertificateAuthority")
			, ResourceFactory.createResource(NSPrefixes.map.get("compose-sec")+authority) );
		}
		
	}


	private void addSecurityDescription(Resource attribute, Model model, Integer securityDescrId, String goal, String mechanism, String technology) {
		Resource secDescr = createJenaResource(NSPrefixes.map.get("db"),"servicesecurity",securityDescrId);
		model.add(attribute, ModelFactory.createDefaultModel().createProperty(Trust.NS+"hasSecurityDescription"),secDescr);
		
		if (goal !=null ){
			model.add(secDescr, UsdlSec.hasSecurityGoal, ResourceFactory.createResource(UsdlSec.NS+goal) );
		}
		if (mechanism!=null){
			model.add(secDescr, UsdlSec.isImplementedBy, ResourceFactory.createResource(UsdlSec.NS+mechanism) );
		}
		if (technology!=null){
			model.add(secDescr, UsdlSec.isRealizedByTechnology, ResourceFactory.createResource(NSPrefixes.map.get("compose-sec")+technology));
		}
		
	}


	private Resource addAttributeToProfile(Resource profile, Model model, Integer attributeId, String attributeType, Object value) {
		Resource attribute = createJenaResource(NSPrefixes.map.get("db"),"attribute",attributeId);
		model.add(attribute, RDF.type, ResourceFactory.createResource(Trust.NS+attributeType));
		if (value!=null){
			model.add(attribute, Trust.hasValue, value.toString(), XSDDatatype.XSDdecimal);
		}
		model.add(profile, Trust.hasAttribute, attribute);
		return attribute;
	}


	private Resource addAgentAndProfile(Model model, Integer agentId, String agentName, URL agentComposeUID, Integer profileId) {
		Resource agent = createJenaResource(NSPrefixes.map.get("db"),"agent",agentId);
		Resource profile = createJenaResource(NSPrefixes.map.get("db"),"profile",profileId);
		model.add(agent, RDF.type, Trust.Agent);
		model.add(agent, Trust.hasName, agentName);
		model.add(agent,(ModelFactory.createDefaultModel().createProperty(Trust.NS+"composeUID")), ResourceFactory.createResource(agentComposeUID.toString()));
		model.add(agent, Trust.hasProfile, profile);
		model.add(profile, RDF.type, Trust.TrustProfile);
		return profile;
	}
	
	private Resource createJenaResource(String namespace, String type, Integer id){
		String uri = namespace+""+type+"/"+id;
		return ResourceFactory.createResource(uri);
	}


	private ResultSet executeSelect(String serviceId) throws Exception{
		if (ps == null) ps = getConnection().prepareStatement(sqlString);
		ps.setString(1, serviceId);
		return ps.executeQuery();
	}
	
	
	private Connection  getConnection() throws Exception{
		if (conn == null || conn.isClosed()){
			 Class.forName("com.mysql.jdbc.Driver").newInstance();
			 conn = DriverManager.getConnection(jdbc_url);
		}
		return conn;
	}
	
	@Override
	public void stop() {
		try {
			if (ps != null)
				ps.close();
			getConnection().close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) {
		IBridge b = new BridgeDB();
		Model m = b.obtainTrustProfile("http://iserve.kmi.open.ac.uk/iserve/id/services/610b64a2-6cc0-4b5c-9d6e-a619bdf0c18f/twitter");
		RDFDataMgr.write(System.out, m, Lang.TURTLE) ;
	}

}
