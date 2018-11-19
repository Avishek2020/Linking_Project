package org.upb.musicclass;
 
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
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
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
import java.io.Serializable;

public class ListAllClassMusic implements Serializable {
	private static final long serialVersionUID = -2268344215686055231L;
	private static String endpoint = "http://dbtune.org/musicbrainz/sparql";
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
  String QS = "SELECT DISTINCT ?class WHERE { [] a ?class } ORDER BY ?class";
  QueryFactory.create(QS);
  QueryExecution qexec = QueryExecutionFactory.sparqlService(endpoint, QS);
  ResultSet queryResults = qexec.execSelect();
  //System.out.println(queryResults.hasNext());
  System.out.println("----------------------------------------------- ");
  System.out.println(" *** List of all MusicBrainz classes *** ");
  System.out.println("----------------------------------------------- ");
  //HybridCache cache = new HybridCache(folder);
  while (queryResults.hasNext()) {
	  QuerySolution sol = (QuerySolution) queryResults.next();
	   RDFNode node = sol.get("class");	
	  System.out.println(sol.get("class"));
     
     FileOutputStream fos = null;
     ObjectOutputStream out = null;
     // Write to cache 
     try {
		 fos = new FileOutputStream(filename);
    	 out = new ObjectOutputStream(fos);		
		// out.writeObject(sol.get("class"));
		// out.close();
	} catch (FileNotFoundException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	} catch (IOException e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
   
  }
  }
}