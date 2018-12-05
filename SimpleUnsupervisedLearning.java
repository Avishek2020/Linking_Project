package org.upb.musicclass;
import org.aksw.limes.core.controller.Controller;
import org.aksw.limes.core.controller.ResultMappings;
import org.aksw.limes.core.io.config.Configuration;
import org.aksw.limes.core.io.config.KBInfo;
import org.aksw.limes.core.io.serializer.ISerializer;
import org.aksw.limes.core.io.serializer.SerializerFactory;
import org.aksw.limes.core.ml.algorithm.LearningParameter;
import org.aksw.limes.core.ml.algorithm.MLImplementationType;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.upb.musicclass.SaveToFile;

public class SimpleUnsupervisedLearning {

 private static final String SYSTEM_DIR = System.getProperty("user.dir");

 public static void main(String[] args) {
	 
	      String fullClassName = SimpleUnsupervisedLearning.class.getName();
	      String sClassName = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
		  Configuration conf;
		  
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
		
		  sourceInfo = new KBInfo(
								   "MUSICBRAINZ",
								   "http://dbtune.org/musicbrainz/sparql", 
								   null,
								   "?x", 
								   new ArrayList < String > (Arrays.asList("rdfs:label","foaf:name")),
								   new ArrayList < String > (),
								   new ArrayList < String > (Arrays.asList("?x")),								   
								   functions, 
								   prefixes, 
								   1000, 
								   "sparql", 
								   -1,
								   -1 
		                         );
		
		  targetInfo = new KBInfo(
								   "MAGNATUNE", 
								   "http://dbtune.org/magnatune/sparql", 
								   null, 
								   "?y", 
								   new ArrayList < String > (Arrays.asList("rdfs:label","foaf:name")),
								   new ArrayList < String > (),
								   new ArrayList < String > (Arrays.asList("?y")), 
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