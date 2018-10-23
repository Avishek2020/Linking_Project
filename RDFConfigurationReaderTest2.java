package org.aksw.limes.core.io.config.reader.xml;

import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.aksw.limes.core.controller.Controller;
import org.aksw.limes.core.controller.ResultMappings;
import org.aksw.limes.core.io.cache.ACache;
import org.aksw.limes.core.io.cache.HybridCache;
import org.aksw.limes.core.io.config.Configuration;
import org.aksw.limes.core.io.config.KBInfo;
import org.aksw.limes.core.io.config.reader.rdf.RDFConfigurationReader;
import org.aksw.limes.core.io.query.IQueryModule;
import org.aksw.limes.core.io.query.ResilientSparqlQueryModule;
import org.aksw.limes.core.io.serializer.ISerializer;
import org.aksw.limes.core.io.serializer.SerializerFactory;
import org.aksw.limes.core.ml.algorithm.LearningParameter;
import org.junit.Before;
import org.junit.Test;


public class RDFConfigurationReader {
    
    private static final String SYSTEM_DIR = System.getProperty("user.dir");
    Map<String, String> prefixes;
    LinkedHashMap<String, Map<String, String>> functions;
    KBInfo sourceInfo, targetInfo;
    Configuration testConf;

    @Before
    public void init() {
        prefixes = new HashMap<>();
        prefixes.put("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
        prefixes.put("owl","http://www.w3.org/2002/07/owl#");
        prefixes.put("dc", "http://purl.org/dc/elements/1.1/");
        prefixes.put("vocab", "http://dbtune.org/musicbrainz/resource/vocab/");
        
        
        functions = new LinkedHashMap<>();
        Map<String, String> f = new LinkedHashMap<>();
        //f.put("polygon", null);
       // functions.put("geom:geometry/geos:asWKT", f);
        
        sourceInfo = new KBInfo(
                "artistdata",                                                     //String id
                "http://dbtune.org/musicbrainz/sparql",                           //String endpoint
                null,                                                             //String graph
                "?x",                                                             //String var
                new ArrayList<String>(Arrays.asList("rdfs:label")), //List<String> properties
                new ArrayList<String>(),                                          //List<String> optionalProperties
                new ArrayList<String>(Arrays.asList("?x a vocab:l_artist_artist")),//ArrayList<String> restrictions
                functions,                                                        //LinkedHashMap<String, Map<String, String>> functions
                prefixes,                                                         //Map<String, String> prefixes
                10,                                                               //int pageSize
                "sparql",                                                         //String type
                -1,                                                               //int minOffset
                -1                                                                //int maxoffset
        );

        targetInfo = new KBInfo(
                "artistdata2",                                                    //String id
                "http://dbtune.org/musicbrainz/sparql",                           //String endpoint
                null,                                                             //String graph
                "?y",                                                             //String var
                new ArrayList<String>(Arrays.asList("rdfs:label")),               //List<String> properties
                new ArrayList<String>(),                                          //List<String> optionalProperties
                new ArrayList<String>(Arrays.asList("?y a vocab:l_artist_artist")), //ArrayList<String> restrictions
                functions,                                                        //LinkedHashMap<String, Map<String, String>> functions
                prefixes,                                                         //Map<String, String> prefixes
                10,                                                               //int pageSize
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
        testConf.setAcceptanceFile("sameartist.nt");
        testConf.setVerificationThreshold(0.5);
        testConf.setVerificationFile("sameartistverify.nt");
        testConf.setOutputFormat("TAB");
        testConf.setAcceptanceThreshold(0.9); 
        //testConf.setMetricExpression("Cosine(x.rdfs:label,y.dc:title)");
        testConf.setMetricExpression("trigrams(x.rdfs:label,y.rdfs:label)");
      }
    //Added to write in file
     private static void writeResults(ResultMappings mappings, Configuration config) {
        String outputFormat = config.getOutputFormat();
        ISerializer output = SerializerFactory.createSerializer(outputFormat);
        output.setPrefixes(config.getPrefixes());
        output.writeToFile(mappings.getVerificationMapping(), config.getVerificationRelation(),
                config.getVerificationFile());
        output.writeToFile(mappings.getAcceptanceMapping(), config.getAcceptanceRelation(), config.getAcceptanceFile());
     }
    
    @Test    
    public void testRDFReaderForMLAgorithm() {    	
    	ResultMappings mapping =Controller.getMapping(testConf);
    	writeResults(mapping, testConf);
        assertTrue(true);
    }  
  
}
