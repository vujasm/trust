/* CVS $Id: $ */
package com.inn.trusthings.model.vocabulary;

/*
 * #%L
 * trusthings-model
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

import com.hp.hpl.jena.rdf.model.*;
 
/**
 * Vocabulary definitions from C:\D-Data\Git\itrust/trusthings-kbase/src/main/resources/ontologies/trustontology.ttl 
 * @author Auto-generated by schemagen on 15 Apr 2014 11:51 
 */
public class Trust {
    /** <p>The RDF model that holds the vocabulary terms</p> */
    private static Model m_model = ModelFactory.createDefaultModel();
    
    /** <p>The namespace of the vocabulary as a string</p> */
    public static final String NS = "http://www.compose-project.eu/ns/web-of-things/trust#";
    
    /** <p>The namespace of the vocabulary as a string</p>
     *  @see #NS */
    public static String getURI() {return NS;}
    
    /** <p>The namespace of the vocabulary as a resource</p> */
    public static final Resource NAMESPACE = m_model.createResource( NS );
    
    /** <p>The ontology's owl:versionInfo as a string</p> */
    public static final String VERSION_INFO = "1.1.0";
    
    public static final Property forContext = m_model.createProperty( "http://www.compose-project.eu/ns/web-of-things/trust#forContext" );
    
    public static final Property hasAttribute = m_model.createProperty( "http://www.compose-project.eu/ns/web-of-things/trust#hasAttribute" );
    
    public static final Property hasCriteria = m_model.createProperty( "http://www.compose-project.eu/ns/web-of-things/trust#hasCriteria" );
    
    /** <p>Importance or weigth.</p> */
    public static final Property hasImportance = m_model.createProperty( "http://www.compose-project.eu/ns/web-of-things/trust#hasImportance" );
    
    public static final Property hasMaxValue = m_model.createProperty( "http://www.compose-project.eu/ns/web-of-things/trust#hasMaxValue" );
    
    public static final Property hasMetric = m_model.createProperty( "http://www.compose-project.eu/ns/web-of-things/trust#hasMetric" );
    
    public static final Property hasMetricValue = m_model.createProperty( "http://www.compose-project.eu/ns/web-of-things/trust#hasMetricValue" );
    
    public static final Property hasMinValue = m_model.createProperty( "http://www.compose-project.eu/ns/web-of-things/trust#hasMinValue" );
    
    /** <p>attaches to any instance</p> */
    public static final Property hasName = m_model.createProperty( "http://www.compose-project.eu/ns/web-of-things/trust#hasName" );
    
    public static final Property hasProfile = m_model.createProperty( "http://www.compose-project.eu/ns/web-of-things/trust#hasProfile" );
    
    public static final Property hasScore = m_model.createProperty( "http://www.compose-project.eu/ns/web-of-things/trust#hasScore" );
    
    public static final Property hasTimestamp = m_model.createProperty( "http://www.compose-project.eu/ns/web-of-things/trust#hasTimestamp" );
    
    public static final Property hasValue = m_model.createProperty( "http://www.compose-project.eu/ns/web-of-things/trust#hasValue" );
    
    public static final Property isTrustProfileOf = m_model.createProperty( "http://www.compose-project.eu/ns/web-of-things/trust#isTrustProfileOf" );
    
    public static final Property next = m_model.createProperty( "http://www.compose-project.eu/ns/web-of-things/trust#next" );
    
    public static final Property rank = m_model.createProperty( "http://www.compose-project.eu/ns/web-of-things/trust#rank" );
    
    public static final Property recommendedCriteria = m_model.createProperty( "http://www.compose-project.eu/ns/web-of-things/trust#recommendedCriteria" );
    
    public static final Property suggestedMatcher = m_model.createProperty( "http://www.compose-project.eu/ns/web-of-things/trust#suggestedMatcher" );
    
    public static final Property trustedParticipant = m_model.createProperty( "http://www.compose-project.eu/ns/web-of-things/trust#trustedParticipant" );
    
    public static final Property trustingParticipant = m_model.createProperty( "http://www.compose-project.eu/ns/web-of-things/trust#trustingParticipant" );
    
    /** <p>An agent is a entity who participates in a trust relationship. An agent can 
     *  be any resource such as software agent, human, software service, etc. An agent 
     *  engages in relationships with other agents to meet certian goals withing some 
     *  context or domain, under belief (trust) that other agent will provide what 
     *  claimed to provide. Therefore, an agent may have trust requerments as a precondition 
     *  for engaging into a relationship with other agent. Each agent has a trust 
     *  profile, which is a set of its characteristics (aspects, attributes) to meet 
     *  trust requrments.</p>
     */
    public static final Resource Agent = m_model.createResource( "http://www.compose-project.eu/ns/web-of-things/trust#Agent" );
    
    public static final Resource Availability = m_model.createResource( "http://www.compose-project.eu/ns/web-of-things/trust#Availability" );
    
    /** <p>A context within a trust relationship is being or to be established. E.g. 
     *  public transport, tourism, retail. For each context, there can be a recommended 
     *  req trust profile(s) provided, in general, or agent can have req trust profile 
     *  defined for the context. The term context defines the nature of the service 
     *  or service functions, and each Context has a name, a type and a functional 
     *  specification, such as �rent a car� or �buy a book� Context Type can be matched 
     *  to a Service Type.</p>
     */
    public static final Resource Context = m_model.createResource( "http://www.compose-project.eu/ns/web-of-things/trust#Context" );
    
    /** <p>Contract Compliance can be a measure of compliance of actual behaviour of 
     *  a service with respect to the service contract. This informatin may be comming 
     *  out from COMPOSE Activity Monitors</p>
     */
    public static final Resource ContractCompliance = m_model.createResource( "http://www.compose-project.eu/ns/web-of-things/trust#ContractCompliance" );
    
    /** <p>Feedback from agents about other agents. E.g. user feedback about some particular 
     *  service. Feedback can be in a form of rating with predefined rating scale 
     *  (e.g. poor, fair, good, very good, excellent)</p>
     */
    public static final Resource Feedback = m_model.createResource( "http://www.compose-project.eu/ns/web-of-things/trust#Feedback" );
    
    /** <p>A matcher represents an matching technique or algorithm or strategy to compute 
     *  the level of trust between two agents. The agents expose their trust profiles 
     *  (capabilities) and impose their trust criteria (requirments), which are then 
     *  used by a matcher to assess whether trustworthy relationship can be established 
     *  or not. I.e., whether trust capabilities meet trust requirments. E.g., concrete 
     *  matchers are Additive Weighting or TOPSIS.</p>
     */
    public static final Resource Matcher = m_model.createResource( "http://www.compose-project.eu/ns/web-of-things/trust#Matcher" );
    
    public static final Resource MeasurableTrustAttribute = m_model.createResource( "http://www.compose-project.eu/ns/web-of-things/trust#MeasurableTrustAttribute" );
    
    /** <p>A metric as a measurement for specific trust attribute</p> */
    public static final Resource Metric = m_model.createResource( "http://www.compose-project.eu/ns/web-of-things/trust#Metric" );
    
    /** <p>A list of values of metric, if applicable.</p> */
    public static final Resource MetricValue = m_model.createResource( "http://www.compose-project.eu/ns/web-of-things/trust#MetricValue" );
    
    /** <p>Popularity of an agent. It may be provided as simple as enumeration of two 
     *  values - popular and no-popular - or as a numerical value e.g. any number 
     *  between 0 and 1. (In COMPOSE, provevance information can be used to calculate 
     *  popularity)</p>
     */
    public static final Resource Popularity = m_model.createResource( "http://www.compose-project.eu/ns/web-of-things/trust#Popularity" );
    
    /** <p>Any attribute about quality of service.</p> */
    public static final Resource QoSAttribute = m_model.createResource( "http://www.compose-project.eu/ns/web-of-things/trust#QoSAttribute" );
    
    /** <p>Reputation of an agent. It may be provided as simple as enumeration of predefined 
     *  reputation values (unknown, bad, good, very good, excellent) or as a numerical 
     *  value e.g. any number between 0 and 1.</p>
     */
    public static final Resource Reputation = m_model.createResource( "http://www.compose-project.eu/ns/web-of-things/trust#Reputation" );
    
    public static final Resource ResponseTime = m_model.createResource( "http://www.compose-project.eu/ns/web-of-things/trust#ResponseTime" );
    
    /** <p>Any attribute about security aspects. E.g. authentication, authorization, 
     *  identification, etc.</p>
     */
    public static final Resource SecurityAttribute = m_model.createResource( "http://www.compose-project.eu/ns/web-of-things/trust#SecurityAttribute" );
    
    public static final Resource SecurityGuarantee = m_model.createResource( "http://www.compose-project.eu/ns/web-of-things/trust#SecurityGuarantee" );
    
    public static final Resource SecurityRequirment = m_model.createResource( "http://www.compose-project.eu/ns/web-of-things/trust#SecurityRequirment" );
    
    /** <p>Any parameter (feature, property, characteristics) that is relevant to a perception 
     *  of trust. E..g. security-related features (authorization mechanism, authentication 
     *  mechanism, data encryption), quality of service apects (reponse, data quality), 
     *  reputation, etc.</p>
     */
    public static final Resource TrustAttribute = m_model.createResource( "http://www.compose-project.eu/ns/web-of-things/trust#TrustAttribute" );
    
    /** <p>Trust criteria is a set of requested trust attributes, with their values and 
     *  preference. In other words, it is a triple &lt;trust attribute/indicator, 
     *  value, relevance&gt;. For example, &lt;'reputation', 'good', '1'&gt;, &lt;'popularity', 
     *  '0.5', '1'&gt;}</p>
     */
    public static final Resource TrustCriteria = m_model.createResource( "http://www.compose-project.eu/ns/web-of-things/trust#TrustCriteria" );
    
    /** <p>Trust prifile is a set of trust-related attributes of an agent, e.g. security 
     *  attributes, reputation, QoS attributes, etc.</p>
     */
    public static final Resource TrustProfile = m_model.createResource( "http://www.compose-project.eu/ns/web-of-things/trust#TrustProfile" );
    
    /** <p>TrustRelationship represents trust relationship as a 6-tuple &lt;Trusted Agent, 
     *  Trusting Agent, Context, TrustCriteria, Score, Timestamp&gt;. Each trust relationship 
     *  is established between two agents who wants to engange in some form of communication 
     *  or cooperation within a certian context (e.g. buying or seeling goods, geting 
     *  some information, etc). An trusting agent trusts that trusted agent has certian 
     *  characteristics (or features), which are defined as a trust criteria by the 
     *  trusting agent. TrustRelationship has a level of trust, which can be numerical 
     *  value within a range e.g. [0..1].</p>
     */
    public static final Resource TrustRelationship = m_model.createResource( "http://www.compose-project.eu/ns/web-of-things/trust#TrustRelationship" );
    
    public static final Resource UnmeasurableTrustAttribute = m_model.createResource( "http://www.compose-project.eu/ns/web-of-things/trust#UnmeasurableTrustAttribute" );
    
    public static final Resource ReputationScale = m_model.createResource( "http://www.compose-project.eu/ns/web-of-things/trust#ReputationScale" );
    
    public static final Resource high = m_model.createResource( "http://www.compose-project.eu/ns/web-of-things/trust#high" );
    
    public static final Resource low = m_model.createResource( "http://www.compose-project.eu/ns/web-of-things/trust#low" );
    
    public static final Resource medium = m_model.createResource( "http://www.compose-project.eu/ns/web-of-things/trust#medium" );
    
}
