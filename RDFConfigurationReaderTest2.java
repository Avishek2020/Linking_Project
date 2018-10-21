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
import org.aksw.limes.core.io.config.Configuration;
import org.aksw.limes.core.io.config.KBInfo;
import org.aksw.limes.core.io.config.reader.rdf.RDFConfigurationReader;
import org.aksw.limes.core.io.serializer.ISerializer;
import org.aksw.limes.core.io.serializer.SerializerFactory;
import org.aksw.limes.core.ml.algorithm.LearningParameter;
import org.junit.Before;
import org.junit.Test;


/**
 * @author Mohamed Sherif (sherif@informatik.uni-leipzig.de)
 * @version Jan 15, 2016
 */
public class RDFConfigurationReaderTest2 {
    
    private static final String SYSTEM_DIR = System.getProperty("user.dir");
    Map<String, String> prefixes;
    LinkedHashMap<String, Map<String, String>> functions;
    KBInfo sourceInfo, targetInfo;
    Configuration testConf;

    @Before
    public void init() {
        prefixes = new HashMap<>();
       /* prefixes.put("geos", "http://www.opengis.net/ont/geosparql#");
        prefixes.put("lgdo", "http://linkedgeodata.org/ontology/");
        prefixes.put("geom", "http://geovocab.org/geometry#");
        prefixes.put("rdfs", "http://www.w3.org/2000/01/rdf-schema#");
        prefixes.put("limes", "http://limes.sf.net/ontology/");*/
        prefixes.put("mbz", "http://dbtune.org/musicbrainz/snorql/");
        prefixes.put("vocab", "http://dbtune.org/musicbrainz/resource/vocab/");
        
        
        functions = new LinkedHashMap<>();
        Map<String, String> f = new LinkedHashMap<>();
        //f.put("polygon", null);
       // functions.put("geom:geometry/geos:asWKT", f);
        
        sourceInfo = new KBInfo(
                "artistdata",                                                  //String id
                "http://dbtune.org/musicbrainz/snorql/",                                //String endpoint
                null,                                                             //String graph
                "?x",                                                             //String var
                new ArrayList<String>(), //List<String> properties
                new ArrayList<String>(),                                          //List<String> optionalProperties
                new ArrayList<String>(Arrays.asList("?x a vocab:l_artist_artist")),       //ArrayList<String> restrictions
                functions,                                                        //LinkedHashMap<String, Map<String, String>> functions
                prefixes,                                                         //Map<String, String> prefixes
                1000,                                                             //int pageSize
                "sparql",                                                         //String type
                -1,                                                               //int minOffset
                -1                                                                //int maxoffset
        );

        targetInfo = new KBInfo(
                "artistdata2",                                                  //String id
                "http://dbtune.org/musicbrainz/snorql/",                                //String endpoint
                null,                                                             //String graph
                "?y",                                                             //String var
                new ArrayList<String>(), //List<String> properties
                new ArrayList<String>(),                                          //List<String> optionalProperties
                new ArrayList<String>(Arrays.asList("?y a vocab:l_artist_artist")),       //ArrayList<String> restrictions
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
        //testConf.setAcceptanceRelation("lgdo:near");       
        //testConf.setVerificationRelation("lgdo:near");
        testConf.setAcceptanceFile("sameartist.nt");
        testConf.setVerificationThreshold(0.5);
        testConf.setVerificationFile("sameartistverify.nt");
        testConf.setOutputFormat("TAB");
        testConf.setAcceptanceThreshold(0.9); 

    }
    //Added to write in file
     private static void writeResults(ResultMappings mappings, Configuration config) {
        String outputFormat = config.getOutputFormat();
        ISerializer output = SerializerFactory.createSerializer(outputFormat);
        output.setPrefixes(config.getPrefixes());
        output.writeToFile(mappings.getVerificationMapping(), config.getVerificationRelation(),
                config.getVerificationFile());
        output.writeToFile(mappings.getAcceptanceMapping(), config.getAcceptanceRelation(), config.getAcceptanceFile());
    System.out.println("My name is AVishek"+outputFormat);
     }

    @Test
    public void testRDFReaderForMLAgorithm() {    	
    
    	ResultMappings mapping =Controller.getMapping(testConf);
    	System.out.println("My name is AVishek"+mapping);
    	//writeResults(mapping, testConf);
		//ResultMappings mappings1 = Controller.getMapping(testConf);
    	//writeResults(Controller.getMapping(testConf), testConf);
       // List<LearningParameter> mlParameters = new ArrayList<>();
       /* LearningParameter lp = new LearningParameter();
        lp.setName("max execution time in minutes");
        lp.setValue(60);
        mlParameters.add(lp);

        testConf.setMlAlgorithmName("wombat simple");
        testConf.setMlAlgorithmParameters(mlParameters);
*/
//        String file = System.getProperty("user.dir") + "/resources/lgd-lgd-ml.ttl";

/*        String file = Thread.currentThread().getContextClassLoader().getResource("lgd-lgd-ml.ttl").getPath();
        RDFConfigurationReader c = new RDFConfigurationReader(file);
        Configuration fileConf = c.read();
        assertTrue(testConf.equals(fileConf));*/
    }

/*    @Test
    public void testRDFReaderForMetric() {
        testConf.setMetricExpression("cosine(x.rdfs:label, y.rdfs:label)");
        //testConf.setAcceptanceRelation("lgdo:near");       
        //testConf.setVerificationRelation("lgdo:near");
        testConf.setAcceptanceThreshold(0.9); 
        testConf.setAcceptanceFile("lgd_relaybox_verynear1.nt");
        testConf.setVerificationThreshold(0.5);
        testConf.setVerificationFile("lgd_relaybox_near1.nt");
        testConf.setOutputFormat("TAB");

//        String file = System.getProperty("user.dir") + "/resources/lgd-lgd.ttl";
        String file = Thread.currentThread().getContextClassLoader().getResource("lgd-lgd.ttl").getPath();

        RDFConfigurationReader c = new RDFConfigurationReader(file);
        Configuration fileConf = c.read();
        assertTrue(testConf.equals(fileConf));
    }*/
    
   /* @Test
    public void testRDFReaderForOptionalProperties() {
        testConf.setMetricExpression("x.rdfs:label, y.rdfs:label");
        
        sourceInfo.setOptionalProperties(Arrays.asList("rdfs:label"));
        targetInfo.setOptionalProperties(Arrays.asList("rdfs:label"));

        String file = SYSTEM_DIR + "/resources/lgd-lgd-optional-properties.ttl";
        RDFConfigurationReader c = new RDFConfigurationReader(file);
        Configuration fileConf = c.read();       
        
        assertTrue(testConf.equals(fileConf));
    }*/
}
