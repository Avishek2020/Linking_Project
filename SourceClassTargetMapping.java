package org.upb.musicclass;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.aksw.limes.core.controller.Controller;
import org.aksw.limes.core.controller.MLPipeline;
import org.aksw.limes.core.controller.ResultMappings;
import org.aksw.limes.core.evaluation.evaluator.EvaluatorFactory;
import org.aksw.limes.core.evaluation.evaluator.EvaluatorType;
import org.aksw.limes.core.evaluation.qualititativeMeasures.PseudoFMeasure;
import org.aksw.limes.core.exceptions.UnsupportedMLImplementationException;
import org.aksw.limes.core.io.cache.ACache;
import org.aksw.limes.core.io.cache.HybridCache;
import org.aksw.limes.core.io.config.Configuration;
import org.aksw.limes.core.io.mapping.AMapping;
import org.aksw.limes.core.io.serializer.ISerializer;
import org.aksw.limes.core.io.serializer.SerializerFactory;
import org.aksw.limes.core.ml.algorithm.ACoreMLAlgorithm;
import org.aksw.limes.core.ml.algorithm.LearningParameter;
import org.aksw.limes.core.ml.algorithm.MLAlgorithmFactory;
import org.aksw.limes.core.ml.algorithm.MLImplementationType;
import org.aksw.limes.core.ml.algorithm.MLResults;
import org.aksw.limes.core.ml.algorithm.UnsupervisedMLAlgorithm;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

public class SourceClassTargetMapping {
	private static String endpoint_sc = "http://dbtune.org/musicbrainz/sparql";//http://localhost:8890/spaqql
	private static String endpoint_tc = "http://dbtune.org/magnatune/sparql";

	@SuppressWarnings("unused")
	public static void main(String[] args) throws UnsupportedMLImplementationException {

		String fullClassName = SourceClassTargetMapping4.class.getName();
		String sClassName = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
		File folder = new File("");
		HybridCache cache = new HybridCache(folder);
		AMapping results = null;
		// RDFNode uri;
		String uri = null;
		String propertyLabel = null;
		String value = null;
		List<LearningParameter> MlAlgorithmParameters = new ArrayList<>();
		LearningParameter params = new LearningParameter();
		final int MAX_ITERATIONS_NUMBER = 10;
		Configuration conf = null;
		params.setName("max execution time in minutes");
		params.setValue(60);
		MlAlgorithmParameters.add(params);
		
		ArrayList<HybridCache> sourceList = new ArrayList<HybridCache>();
		ArrayList<HybridCache> targetList = new ArrayList<HybridCache>();
		

		SaveToFile SaveFile = new SaveToFile();

		// Below Query Populates all Music Artist Classes

		String QS = "SELECT DISTINCT ?class\r\n" + "WHERE { [] a ?class }\r\n" + "ORDER BY ?class\r\n ";

		QueryExecution qexec_sc = QueryExecutionFactory.sparqlService(endpoint_sc, QS);

		// QueryExecution qexec_tc =
		// QueryExecutionFactory.sparqlService(endpoint_tc,QS);

		ResultSet classes_sc = qexec_sc.execSelect();

		while (classes_sc.hasNext()) 
		{
			HybridCache sourceCache = new HybridCache();
			QuerySolution musicclass_sc = classes_sc.next();

			ParameterizedSparqlString pss_sc = new ParameterizedSparqlString("select ?s where {?s a ?class } limit 1");

			pss_sc.setParam("?class", musicclass_sc.get("?class"));
			QueryExecution qexec1_sc = QueryExecutionFactory.sparqlService(endpoint_sc, pss_sc.toString());

			ResultSet instances_sc = qexec1_sc.execSelect();
			// System.out.println("Instances of class "+musicclass_sc.get("?class"));
			//System.out.println("Lists of CLASS NAME --> " + musicclass_sc.get("?class"));
			while (instances_sc.hasNext()) {
				QuerySolution musicinstance_sc = instances_sc.next();
				ParameterizedSparqlString pss1_sc = new ParameterizedSparqlString("SELECT ?p ?o where { ?s ?p ?o }");
				pss1_sc.setParam("?s", musicinstance_sc.get("?s"));
				QueryExecution qexec2_sc = QueryExecutionFactory.sparqlService(endpoint_sc, pss1_sc.toString());
				ResultSet properties_sc = qexec2_sc.execSelect();
				// System.out.println("Properties of class instance
				// "+musicinstance_sc.get("?s"));
				//System.out.println("Class instance--> " + musicinstance_sc.get("?s"));
				while (properties_sc.hasNext()) {
					QuerySolution musicproperties_sc = properties_sc.next();

					sourceCache.addTriple(musicinstance_sc.get("?s").toString(),musicproperties_sc.get("?p").toString(), musicproperties_sc.get("?o").toString());
					//System.out.println("<--SOURCACHE-->"+sourceCache);
				}
			}
			sourceList.add(sourceCache);
			System.out.println("<--SOURCELIST-->\n"+sourceList);
		}
			//----------------------Begining of Target -------------------------------------------- 
			QueryExecution qexec_tc = QueryExecutionFactory.sparqlService(endpoint_tc, QS);
			ResultSet classes_tc = qexec_tc.execSelect();

			while (classes_tc.hasNext()) 
			
			{
				HybridCache targetCache = new HybridCache();
				QuerySolution musicclass_tc = classes_tc.next();
				ParameterizedSparqlString pss_tc = new ParameterizedSparqlString("select ?s where {?s a ?class } ");

				pss_tc.setParam("?class", musicclass_tc.get("?class"));
				QueryExecution qexec1_tc = QueryExecutionFactory.sparqlService(endpoint_tc, pss_tc.toString());

				ResultSet instances_tc = qexec1_tc.execSelect();
				// System.out.println("Instances of class "+musicclass.get("?class"));

				while (instances_tc.hasNext()) {
					QuerySolution musicinstance_tc = instances_tc.next();
					ParameterizedSparqlString pss1_tc = new ParameterizedSparqlString(
							"SELECT ?p ?o where { ?s ?p ?o }");
					pss1_tc.setParam("?s", musicinstance_tc.get("?s"));

					QueryExecution qexec2_tc = QueryExecutionFactory.sparqlService(endpoint_tc, pss1_tc.toString());

					ResultSet properties_tc = qexec2_tc.execSelect();
					// System.out.println("Properties of class instance "+musicinstance.get("?s"));
					while (properties_tc.hasNext()) {
						QuerySolution musicproperties_tc = properties_tc.next();
						targetCache.addTriple(musicinstance_tc.get("?s").toString(),
								musicproperties_tc.get("?p").toString(), musicproperties_tc.get("?o").toString());
						// System.out.println("sourceCache "+sourceCache);
					}

				}
				targetList.add(targetCache);
				System.out.println("<--TARGETLIST-->\n"+targetList);
			} // --------------------------------------eof target
			  
				// Testing of Wombat //
			 for (int i = 0; i < sourceList.size(); i++) 
			 { 		      
		          System.out.println("<sourceList>->"+sourceList.get(i)); 	
		        for (int j = 0; j < targetList.size(); j++) 
		        { 		      
				  //System.out.println(targetList.get(j)); 		
		        	MLResults mlm;
					UnsupervisedMLAlgorithm mlu = new UnsupervisedMLAlgorithm(MLAlgorithmFactory.getAlgorithmType("wombat simple"));
					mlu.init(MlAlgorithmParameters, sourceList.get(i) , targetList.get(j));
					PseudoFMeasure pfm = null;
					EvaluatorType pfmType = null;
					if(pfmType != null)
					{
						pfm = (PseudoFMeasure) EvaluatorFactory.create(pfmType);
					}
					mlm = mlu.learn(pfm);
					System.out.println("Learned: " + mlm.getLinkSpecification().getFullExpression() + " with threshold: " + mlm.getLinkSpecification().getThreshold());
					results = mlu.predict(sourceList.get(i), targetList.get(j), mlm); 

					double sim = results.getNumberofMappings();
					if (sim > 100) 
					{
						System.out.println("<Real Linking> \n" + results);
						// SaveFile.Save(mapping, conf);
					}
				
			    } 
  
		     }   		

			
		} 
	
}
