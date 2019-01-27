package org.upb.music.artist.similarity.measures;
import org.aksw.limes.core.controller.Controller;
import org.aksw.limes.core.controller.ResultMappings;
import org.aksw.limes.core.io.config.Configuration;
import org.aksw.limes.core.io.config.KBInfo;
import org.aksw.limes.core.io.serializer.ISerializer;
import org.aksw.limes.core.io.serializer.SerializerFactory;
import org.aksw.limes.core.ml.algorithm.LearningParameter;
import org.aksw.limes.core.ml.algorithm.MLImplementationType;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

import java.util.ArrayList;
import java.util.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.upb.musicclass.SaveToFile;

public class SimpleLimesUnsupervisedLearning {

	private static final String SYSTEM_DIR = System.getProperty("user.dir");

	public static void main(String[] args) {

		String fullClassName = SimpleLimesUnsupervisedLearning.class.getName();
		String sClassName = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
		Configuration conf;
		// private static String endpoint = "http://dbtune.org/musicbrainz/sparql";

		Map < String, String > prefixes;
		LinkedHashMap < String, Map < String, String >> functions;
		KBInfo sourceInfo;
		KBInfo targetInfo;
		SaveToFile write = new SaveToFile();
		prefixes = new HashMap < > ();
		prefixes.put("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
		prefixes.put("owl", "http://www.w3.org/2002/07/owl#");
		prefixes.put("dc", "http://purl.org/dc/elements/1.1/");
		prefixes.put("foaf", "http://xmlns.com/foaf/0.1/");
		prefixes.put("vocab", "http://dbtune.org/musicbrainz/resource/vocab/");
		prefixes.put("mo", "http://purl.org/ontology/mo/"); 
		prefixes.put("db", "http://data.linkedmdb.org/resource/");
		prefixes.put("dbo", "http://dbpedia.org/ontology/");
		prefixes.put("ns2", "http://purl.org/ontology/mo/");
		prefixes.put("bio", "http://purl.org/vocab/bio/0.1/");
		prefixes.put("geo", "http://www.geonames.org/ontology#");
		prefixes.put("owl", "http://www.w3.org/2002/07/owl#");
		prefixes.put("lingvoj", "http://www.lingvoj.org/ontology#");

		functions = new LinkedHashMap < > ();
		ArrayList<String> prop_List = new ArrayList<>();
		ArrayList<String> prop_List2 = new ArrayList<>();
		ArrayList<String> prop_ListX ;
		//String prop_ListX;
		prop_ListX =new ArrayList<String>(Arrays.asList("<http://purl.org/ontology/mo/musicbrainz>","<http://www.w3.org/2002/07/owl#sameAs>","<http://www.w3.org/2000/01/rdf-schema#label>","<http://dbtune.org/musicbrainz/resource/vocab/artist_quality>","<http://dbtune.org/musicbrainz/resource/vocab/sortname>","<http://xmlns.com/foaf/0.1/name>"));	  

 
		/*String QSrc = "PREFIX rdfs:<http://www.w3.org/2000/01/rdf-schema#>"
				+ "SELECT DISTINCT ?p where \r\n" + 
				"{\r\n" + 
				"?s ?p ?o {select ?s where {?s a <http://purl.org/ontology/mo/MusicArtist> }}\r\n" + 
				"filter(langMatches(lang(?o),\"EN\"))\r\n" + 
				"} order by ?s";

		String QSrc22 = "PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#>\r\n" + 
				"select  distinct ?p where {\r\n" + 
				" {?s ?p ?o\r\n" + 
				" filter( !strstarts( str(?p), str(rdf:) ) )} \r\n" + 
				"} LIMIT 10 ";*/
		String QSrc = "\r\n" + 
				"SELECT DISTINCT ?p where {?s ?p ?o .\r\n" + 
				"                         ?s a <http://purl.org/ontology/mo/MusicArtist> ."
				+ "                        } order by ?p";

		Query qsrc = QueryFactory.create(QSrc);
		QueryExecution qexec = QueryExecutionFactory.sparqlService("http://dbtune.org/musicbrainz/sparql", qsrc);
		ResultSet queryResults = qexec.execSelect();
		while (queryResults.hasNext()) {
			QuerySolution sol = (QuerySolution) queryResults.next();
			prop_List.add("<"+sol.get("?p").toString()+">");
			//System.out.println(sol.getResource("?p"));

			// System.out.println(sol.get("?p"));
			// Collections.addAll(prop_List,"\"" + sol.get("p").toString() + "\""); 
		}
		//prop_listx=prop_listx.add("http://www.w3.org/2000/01/rdf-schema#label");

		System.out.println("aviSRC"+prop_List);
		sourceInfo = new KBInfo(
				"MUSICBRAINZ",
				"http://dbtune.org/musicbrainz/sparql", 
				null,
				"?x", 
				new ArrayList < String >(prop_ListX),
				// new ArrayList<String>(Arrays.asList("rdfs:label")),								   
				//new ArrayList<String>(Arrays.asList("<http://dbtune.org/musicbrainz/resource/vocab/artist_type>","<http://www.w3.org/2000/01/rdf-schema#label>")),
				new ArrayList < String > (),
				new ArrayList < String > (Arrays.asList("?x a mo:MusicArtist")),								   
				functions, 
				prefixes, 
				1000, 
				"sparql", 
				-1,
				-1 
				);

		/*String QTrg = "SELECT DISTINCT ?p where \r\n" + 
				"{\r\n" + 
				"?s ?p ?o {select ?s where {?s a <http://purl.org/ontology/mo/MusicArtist> } Limit 5 offset 0}\r\n" + 
				"} order by ?s";*/

		String QTrg = "\r\n" + 
				"SELECT DISTINCT ?p where {?s ?p ?o .\r\n" + 
				"                         ?s a <http://purl.org/ontology/mo/MusicArtist> ."
				+ "                        } order by ?p";

		Query qtrg = QueryFactory.create(QTrg);
		QueryExecution qexec2 = QueryExecutionFactory.sparqlService("http://dbtune.org/magnatune/sparql", qtrg);
		ResultSet queryResults2 = qexec2.execSelect();
		while (queryResults2.hasNext()) {
			QuerySolution sol2 = (QuerySolution) queryResults2.next();
			prop_List2.add("<"+sol2.get("p").toString()+">");
			// Collections.addAll(prop_List2,sol2.get("p").toString());
			// Collections.addAll(prop_List,"\"" + sol2.get("p").toString() + "\""); 
		}

		targetInfo = new KBInfo(
				"MAGNATUNE", 
				"http://dbtune.org/magnatune/sparql", 
				null, 
				"?y", 
				new ArrayList < String >(prop_List2),
				// new ArrayList<String>(Arrays.asList("rdfs:label")),

				// new ArrayList<String>(Arrays.asList("<http://www.w3.org/1999/02/22-rdf-syntax-ns#type>")),
				new ArrayList < String > (),
				new ArrayList < String > (Arrays.asList("?y a ns2:MusicArtist")), 
				functions, 
				prefixes, 
				1000, 
				"sparql",
				-1, 
				-1         
				);



		conf = new Configuration(); //Class configuration object
		conf.setPrefixes(prefixes);
		conf.setSourceInfo(sourceInfo);
		conf.setTargetInfo(targetInfo);
		conf.setAcceptanceRelation("owl:sameAs");
		conf.setVerificationRelation("owl:sameAs");
		conf.setAcceptanceFile(sClassName+".nt");
		conf.setVerificationThreshold(0.5);
		conf.setVerificationFile(sClassName+"Verify.nt");
		conf.setOutputFormat("TAB");
		conf.setAcceptanceThreshold(0.6);

		// MACHINE LEARNING PART

		conf.setMlAlgorithmName("WOMBAT SIMPLE");		  
		List<LearningParameter> mlParameters = new ArrayList<>();		  
		LearningParameter params = new LearningParameter();
		LearningParameter params2 = new LearningParameter();
		params.setName("max execution time in minutes");
		params.setValue(60);	
		// params2.setName("property learning rate");
		// params2.setValue(1);  
		mlParameters.add(params);	
		// mlParameters.add(params2);	
		// System.out.println("Mlp"+mlParameters);
		conf.setMlAlgorithmParameters(mlParameters);

		conf.setMlImplementationType(MLImplementationType.UNSUPERVISED);

		// MAPPING 
		ResultMappings mapping = Controller.getMapping(conf); 

		//WRITE ON FILE
		write.Save(mapping, conf);		  
	}

}