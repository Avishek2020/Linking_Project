package org.upb.musicclass;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import org.aksw.limes.core.controller.Controller;
import org.aksw.limes.core.controller.ResultMappings;
import org.aksw.limes.core.io.config.Configuration;
import org.aksw.limes.core.io.config.KBInfo;
import org.aksw.limes.core.io.serializer.ISerializer;
import org.aksw.limes.core.io.serializer.SerializerFactory;

public class ClassArtist {
    
   // private static final String SYSTEM_DIR = System.getProperty("user.dir");

     public static void main(String[] args) {
    	Configuration testConf;
    	Map<String, String> prefixes;
        LinkedHashMap<String, Map<String, String>> functions;
        KBInfo sourceInfo;
   	    KBInfo targetInfo;
   	    
        prefixes = new HashMap<>();
        prefixes.put("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
        prefixes.put("owl","http://www.w3.org/2002/07/owl#");
        prefixes.put("dc", "http://purl.org/dc/elements/1.1/");
        prefixes.put("foaf", "http://xmlns.com/foaf/0.1/");
        prefixes.put("vocab", "http://dbtune.org/musicbrainz/resource/vocab/");
        prefixes.put("mo", "http://purl.org/ontology/mo/");//MusicArtist
        prefixes.put("db", "http://data.linkedmdb.org/resource/");
        prefixes.put("dbo", "http://dbpedia.org/ontology/");
        prefixes.put("ns2","http://purl.org/ontology/mo/");
        
        
        functions = new LinkedHashMap<>();
     
           sourceInfo = new KBInfo(
                "artistdata",                                                           //String id
                "http://dbtune.org/musicbrainz/sparql",                                //String endpoint
                null,                                                             //String graph
                "?x",                                                             //String var
                //new ArrayList<String>(Arrays.asList("rdfs:label")), //List<String> properties
                new ArrayList<String>(Arrays.asList("rdfs:label")), //List<String> properties
                new ArrayList<String>(),                                          //List<String> optionalProperties
                new ArrayList<String>(Arrays.asList("?x a mo:MusicArtist")),       //ArrayList<String> restrictions
                functions,                                                        //LinkedHashMap<String, Map<String, String>> functions
                prefixes,                                                         //Map<String, String> prefixes
                1000,                                                             //int pageSize
                "sparql",                                                         //String type
                -1,                                                               //int minOffset
                -1                                                                //int maxoffset
        );

        targetInfo = new KBInfo(
                "artistactordata2",                                                  //String id
                "http://dbtune.org/magnatune/sparql",                                //String endpoint
                null,                                                             //String graph
                "?y",                                                             //String var
              //  new ArrayList<String>(Arrays.asList("rdfs:label")),               //List<String> properties
              new ArrayList<String>(Arrays.asList("rdfs:label")),
                new ArrayList<String>(),                                          //List<String> optionalProperties
                new ArrayList<String>(Arrays.asList("?y a ns2:MusicArtist")),       //ArrayList<String> restrictions
                functions,                                                        //LinkedHashMap<String, Map<String, String>> functions
                prefixes,                                                         //Map<String, String> prefixes
                1000,                                                             //int pageSize
                "sparql",                                                         //String type
                -1,                                                               //int minOffset
                -1                                                                //int maxoffset                
        );
        
        testConf = new Configuration();//Class configuration object
        testConf.setPrefixes(prefixes);
        testConf.setSourceInfo(sourceInfo);
        testConf.setTargetInfo(targetInfo);
        testConf.setAcceptanceRelation("owl:sameAs");       
        testConf.setVerificationRelation("owl:sameAs");
        testConf.setAcceptanceFile("SameMusicArtist.nt");
        testConf.setVerificationThreshold(0.5);
        testConf.setVerificationFile("SameMusicverify.nt");
        testConf.setOutputFormat("TAB");
        testConf.setAcceptanceThreshold(0.6); 
        //testConf.setMetricExpression("OR(Cosine(x.rdfs:label,y.rdfs:label)|0.6,Cosine(x.foaf:name,y.foaf:name)|0.6)");
        testConf.setMetricExpression("trigrams(x.rdfs:label,y.rdfs:label)");
        //System.out.println("1.1Avi >>testConf"+testConf);
		//return testConf;
        ResultMappings mapping =Controller.getMapping(testConf);
    	//System.out.println("AVMishr--"+mapping);
    	writeResults(mapping, testConf);
    
    }
//}
 /*   public static void main(String[] args) {
    	//Configuration c = RDFAllClassMusic();
    	ResultMappings mapping =Controller.getMapping(testConf);
    	//System.out.println("AVMishr--"+mapping);
    	writeResults(mapping, testConf);
        }*/
    //Added to write in file
    
     private static void writeResults(ResultMappings mappings, Configuration config) {
        String outputFormat = config.getOutputFormat();
        ISerializer output = SerializerFactory.createSerializer(outputFormat);
        output.setPrefixes(config.getPrefixes());
        output.writeToFile(mappings.getVerificationMapping(), config.getVerificationRelation(),
                config.getVerificationFile());
        output.writeToFile(mappings.getAcceptanceMapping(), config.getAcceptanceRelation(), config.getAcceptanceFile());
      }
 
  
}
