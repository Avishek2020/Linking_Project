package org.apache.jena.example.dbquery;
 
import java.io.File;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Iterator;

import org.apache.jena.graph.Triple;
//import org.apache.jena.ext.com.google.common.io.Files;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdfxml.xmloutput.impl.Abbreviated;



public class RDFMusicArtistClass {
 public static void main(String[] args) {
  final String currentDir = System.getProperty("user.dir");
  //SOURCE DATASET
  ParameterizedSparqlString qs = new ParameterizedSparqlString(
   "DESCRIBE ?x WHERE {?x a <http://purl.org/ontology/mo/MusicArtist>} LIMIT 1000");
  System.out.println(qs);
  QueryExecution SourceURL = QueryExecutionFactory.sparqlService("http://dbtune.org/musicbrainz/sparql", qs.asQuery());
  Iterator<Triple> SourceResults = SourceURL.execDescribeTriples();
  while (SourceResults.hasNext()) {
   System.out.println(SourceResults.next());
  }
  //http://dbtune.org/magnatune/sparql
  //TARGET DATASET
  ParameterizedSparqlString ts = new ParameterizedSparqlString(
		   "DESCRIBE ?y WHERE {?y a <http://purl.org/ontology/mo/MusicArtist>} LIMIT 1000");
		  System.out.println(ts);
  QueryExecution TargetURL = QueryExecutionFactory.sparqlService("http://dbtune.org/magnatune/sparql", ts.asQuery());
  Iterator<Triple> TargetResults = TargetURL.execDescribeTriples();
  while (TargetResults.hasNext()) {
   System.out.println(TargetResults.next());
  }

  try {    
	      Model model = SourceURL.execDescribe();
          StringWriter sw = new StringWriter();
          // System.out.println("model-"+exec.execDescribeTriples());
          model.write(sw, "N-Triples");
          //File
          
          File file = new File("F:\\DataExtraction Project\\Dump\\MusicArtistMBZ.rdf");
          file.createNewFile();
          Files.write(Paths.get("F:\\DataExtraction Project\\Dump\\MusicArtistMBZ.rdf"), sw.toString().getBytes(), StandardOpenOption.APPEND);
          
      }  
  catch (Exception e) {
   // TODO Auto-generated catch block
          e.printStackTrace();
      }
  finally {
	  SourceURL.close();
  }
  try {    
      Model model = TargetURL.execDescribe();
      StringWriter tw = new StringWriter();
      // System.out.println("model-"+exec.execDescribeTriples());
      model.write(tw, "N-Triples");
      //File
      
      File file = new File("F:\\DataExtraction Project\\Dump\\MusicArtistMagnatune.rdf");
      file.createNewFile();
      Files.write(Paths.get("F:\\DataExtraction Project\\Dump\\MusicArtistMagnatune.rdf"), tw.toString().getBytes(), StandardOpenOption.APPEND);
      
  }  
catch (Exception e) {
// TODO Auto-generated catch block
      e.printStackTrace();
  }
finally {
	TargetURL.close();
}
  }
}