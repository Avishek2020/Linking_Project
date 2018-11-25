package org.upb.musicclass;
 
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Resource;
import org.aksw.limes.core.measures.measure.string.TrigramMeasure;

import java.io.Serializable;

public class SimpleAllClassTrigram implements Serializable {
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
  String QS ="select ?label ?instance where {\r\n" + 
  		"?instance <http://www.w3.org/2000/01/rdf-schema#label> ?label\r\n" + 
  		"{\r\n" + 
  		"SELECT DISTINCT ?instance\r\n" + 
  		"WHERE { ?instance a ?c }\r\n" + 
  		"ORDER BY ?instance\r\n" + 
  		"}\r\n" + 
  		"}";
  
  String QS2 ="select ?label ?instance where {\r\n" + 
  		"  ?instance <http://www.w3.org/2000/01/rdf-schema#label> ?label {\r\n" + 
  		"select ?instance where \r\n" + 
  		"{ ?instance a ?c\r\n" + 
  		"} order by ?instance\r\n" + 
  		"  }\r\n" + 
  		"}";
  TrigramMeasure tgMeasure = new TrigramMeasure();
  QueryFactory.create(QS);
  QueryFactory.create(QS2);
  QueryExecution qexec = QueryExecutionFactory.sparqlService(endpoint, QS);
  QueryExecution qexec2 = QueryExecutionFactory.sparqlService(endpoint1, QS2);
  ResultSet queryResults = qexec.execSelect();
  //System.out.println(queryResults.hasNext());
  System.out.println("----------------------------------------------- ");
  System.out.println(" *** List of all MusicBrainz classes *** ");
  System.out.println("----------------------------------------------- ");
  //HybridCache cache = new HybridCache(folder);
  while (queryResults.hasNext()) {
	  QuerySolution sol = (QuerySolution) queryResults.next();
	 
	  ResultSet queryResults2 = qexec2.execSelect();
	  while(queryResults2.hasNext())
	  {
		  QuerySolution sol1 = (QuerySolution) queryResults2.next();
		  double sim = tgMeasure.getSimilarity(sol.getLiteral("label").getString(), sol1.getLiteral("label").getString());
		  if(sim>0.2)
		  System.out.println("Similarity for "+sol.get("instance") +" and "+sol1.get("instance")+" based on"
		  		+ "trigram similarity of labels is "+sim);
	  }
	  
   
   }
  }
}