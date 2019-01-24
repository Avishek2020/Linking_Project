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
import org.aksw.limes.core.measures.measure.string.TrigramMeasure;
import org.aksw.limes.core.util.Clock;

import java.io.Serializable;

public class SimpleArtistClassTrigram implements Serializable {
	private static final long serialVersionUID = -2268344215686055231L;
	private static String endpoint = "http://dbtune.org/musicbrainz/sparql";
	private static String endpoint1 = "http://dbtune.org/magnatune/sparql";
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
		String QS1 ="select ?label ?instance where {\r\n" + 
				"?instance <http://www.w3.org/2000/01/rdf-schema#label> ?label\r\n" + 
				"{\r\n" + 
				"SELECT DISTINCT ?instance\r\n" + 
				"WHERE { ?instance a <http://purl.org/ontology/mo/MusicArtist> }\r\n" + 
				"ORDER BY ?instance\r\n" + 
				"}\r\n" + 
				"}";

		String QS2 ="select ?label ?instance where {\r\n" + 
				"  ?instance <http://www.w3.org/2000/01/rdf-schema#label> ?label {\r\n" + 
				"select ?instance where \r\n" + 
				"{ ?instance a <http://purl.org/ontology/mo/MusicArtist>\r\n" + 
				"} order by ?instance\r\n" + 
				"  }\r\n" + 
				"}";
		TrigramMeasure tgMeasure = new TrigramMeasure();
		Query QS11 = QueryFactory.create(QS1);  
		Query QS22 = QueryFactory.create(QS2);
		QueryExecution qexec = QueryExecutionFactory.sparqlService(endpoint, QS11);
		QueryExecution qexec2 = QueryExecutionFactory.sparqlService(endpoint1, QS22);
		ResultSet queryResults = qexec.execSelect();
		//System.out.println(queryResults.hasNext());
		System.out.println("----------------------------------------------- ");
		System.out.println(" *** List of all MusicArtist classes *** ");
		System.out.println("----------------------------------------------- ");
		while (queryResults.hasNext()) {
			QuerySolution sol = (QuerySolution) queryResults.next();

			ResultSet queryResults2 = qexec2.execSelect();
			while(queryResults2.hasNext())
			{
				QuerySolution sol1 = (QuerySolution) queryResults2.next();
				long Start_Time =System.currentTimeMillis();
				System.out.println("Start Time- "+Start_Time);
				double sim = tgMeasure.getSimilarity(sol.getLiteral("label").getString(), sol1.getLiteral("label").getString());
				long End_Time = System.currentTimeMillis()- Start_Time;
				System.out.println("End Time- "+End_Time);
				if(sim>0.5)
					System.out.println("Similarity for "+sol.get("instance") +" and "+sol1.get("instance")+" based on"
							+ "trigram similarity of labels is "+sim);

			}


		}
	}
}