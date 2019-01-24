package org.upb.music.artist.similarity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import org.apache.jena.graph.Triple;
//import org.apache.jena.ext.com.google.common.io.Files;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
//import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
//import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdfxml.xmloutput.impl.Abbreviated;
import org.aksw.limes.core.controller.Controller;
import org.aksw.limes.core.controller.ResultMappings;
import org.aksw.limes.core.io.cache.HybridCache;
import org.aksw.limes.core.io.config.Configuration;
import org.aksw.limes.core.io.config.reader.AConfigurationReader;
import org.aksw.limes.core.io.config.reader.rdf.RDFConfigurationReader;
import org.aksw.limes.core.io.query.IQueryModule;
import org.aksw.limes.core.io.query.QueryModuleFactory;
import org.aksw.limes.core.measures.measure.string.JaccardMeasure;
import org.aksw.limes.core.measures.measure.string.TrigramMeasure;
import org.aksw.limes.core.util.Clock;

import java.io.Serializable;

public class SourceTargetLiteralMappingJaccardSimilarity implements Serializable {
	private static final long serialVersionUID = -2268344215686055231L;
	private static String endpoint_sc = "http://dbtune.org/musicbrainz/sparql";
	//private static String endpoint_sc = "http://dbtune.org/jamendo/sparql";
	private static String endpoint_tc = "http://dbtune.org/magnatune/sparql";
	private static String graph = "http://dbtune.org/musicbrainz/";
	String fileNameOrUri;
	static int hash;
	int maxoffset=-1;
	static int minoffset=-1;
	final static int prime = 31;
	static int result = 1;
	static String filename = "C:\\LIMES\\limes-core\\cache\\NewCache\\"+prime * result + minoffset+".ser";

	public static void main(String[] args) {
		final String currentDir = System.getProperty("user.dir");

		List<Resource> results = new ArrayList<Resource>();
		//String QS = "SELECT DISTINCT ?class WHERE { [] a ?class } ORDER BY ?class";
		String QS1 ="SELECT ?s ?p ?o where \r\n" + 
				"{\r\n" + 
				"?s ?p ?o {select ?s where {?s a ?class {SELECT DISTINCT ?class WHERE { [] a ?class } ORDER BY ?class} }} \r\n" + 
				"FILTER(isliteral(?o)) \r\n" + 
				"filter(langMatches(lang(?o),\"EN\"))\r\n" + 
				"}  Limit 10 ";

		String QS2 ="SELECT ?s ?p ?o where \r\n" + 
				"{\r\n" + 
				"?s ?p ?o {select ?s where {?s a ?class {SELECT DISTINCT ?class WHERE { [] a ?class } ORDER BY ?class} }} \r\n" + 
				"FILTER(isliteral(?o)) \r\n" + 
				"filter(langMatches(lang(?o),\"EN\"))\r\n" + 
				"}  Limit 10  ";
		JaccardMeasure JaccardMeasure = new JaccardMeasure();
		Query QS11 = QueryFactory.create(QS1);  
		Query QS22 = QueryFactory.create(QS2);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(endpoint_sc, QS11);
		QueryExecution qexec2 = QueryExecutionFactory.sparqlService(endpoint_tc, QS22);
		ResultSet queryResults = qexec.execSelect();
		//System.out.println(queryResults.hasNext());
		System.out.println("----------------------------------------------- ");
		System.out.println(" *** Jaccard Similarity for Literals *** ");
		System.out.println("----------------------------------------------- ");
		while (queryResults.hasNext()) {
			QuerySolution sol = (QuerySolution) queryResults.next();

			ResultSet queryResults2 = qexec2.execSelect();
			while(queryResults2.hasNext())
			{
				QuerySolution sol1 = (QuerySolution) queryResults2.next();
				long Start_Time =System.currentTimeMillis();
				//System.out.println("Start Time- "+Start_Time);
				//double score1 = JaccardMeasure.getSimilarity("l_track_track #26296", "l_track_track #26296");
				//System.out.println("score1 --"+score1);
				RegexStopperRemoval RSR = new RegexStopperRemoval();
				//RSR.stopperremoval(text)
				double score = JaccardMeasure.getSimilarity(RSR.stopperremoval(sol.get("?o").toString()),RSR.stopperremoval(sol1.get("?o").toString()));
				//double Score = tgMeasure.getSimilarity(sol.getLiteral("label").getString(), sol1.getLiteral("label").getString());
				long End_Time = System.currentTimeMillis();
				Long Total_Time = End_Time- Start_Time;
				//System.out.println("Total_Time- "+Total_Time);
				if(score>0.5)
					System.out.println("Similarity for SOURCE CLASS "+sol.get("?s") +" and TARGET CLASS  "+sol1.get("?s")+" based on"
							+ " jaccard similarity of literal is SCORE "+score + " TOTAL PROCESSING TIME -"+Total_Time);

			}


		}
	}
}