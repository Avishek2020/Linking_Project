package org.upb.music.artist.similarity;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.aksw.jena_sparql_api.delay.core.QueryExecutionFactoryDelay;
import org.aksw.jena_sparql_api.http.QueryExecutionFactoryHttp;
import org.aksw.jena_sparql_api.pagination.core.QueryExecutionFactoryPaginated;
import org.aksw.jena_sparql_api.retry.core.QueryExecutionFactoryRetry;
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
import org.aksw.limes.core.measures.mapper.MappingOperations;
import org.aksw.limes.core.ml.algorithm.ACoreMLAlgorithm;
import org.aksw.limes.core.ml.algorithm.LearningParameter;
import org.aksw.limes.core.ml.algorithm.MLAlgorithmFactory;
import org.aksw.limes.core.ml.algorithm.MLImplementationType;
import org.aksw.limes.core.ml.algorithm.MLResults;
import org.aksw.limes.core.ml.algorithm.UnsupervisedMLAlgorithm;
import org.aksw.limes.core.ml.algorithm.WombatSimple;
import org.aksw.limes.core.ml.algorithm.matching.stablematching.HospitalResidents;
import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.rdf.model.Resource;
import org.apache.jena.rdf.model.ResourceFactory;

public class SourceClassTargetMappingMusic {
	//private static String endpoint_sc = "http://dbtune.org/musicbrainz/sparql";
	private static String defaultGraphName_sc ="http://dbtune.org";
	private static String endpoint_sc = "http://dbtune.org/jamendo/sparql";
	private static String endpoint_tc = "http://dbtune.org/magnatune/sparql";
	private static String defaultGraphName_tc ="http://dbtune.org";

	@SuppressWarnings("unused")
	public static void main(String[] args) throws UnsupportedMLImplementationException {

		String fullClassName = SourceClassTargetMappingMusic.class.getName();
		String sClassName = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
		File folder = new File("");
		HybridCache cache = new HybridCache(folder);
		AMapping results = null;
		AMapping newresults = null;
		// RDFNode uri;
		String uri = null;
		String propertyLabel = null;
		String value = null;
	   final int MAX_ITERATIONS_NUMBER = 10;
		Configuration conf = null;

	    ArrayList<HybridCache> sourcelist = new ArrayList<HybridCache>();
		ArrayList<HybridCache> targetlist = new ArrayList<HybridCache>();
	    DataToFile DataFile = new DataToFile();

		// Below Query Populates all Music Artist Classes      
		// Parameter for Url With Delay Retry & Pagination
		ParameterUrl p_url = new ParameterUrl(endpoint_sc, defaultGraphName_sc);
		org.aksw.jena_sparql_api.core.QueryExecutionFactory qef_sc;
		qef_sc=p_url.GetUrlParameter();

		// Query to get all classes ....		
		String QS = "SELECT DISTINCT ?class WHERE {[] a ?class } ORDER BY ?class ";

		QueryExecution qexec_sc = qef_sc.createQueryExecution(QS);
		ResultSet classes_sc = qexec_sc.execSelect();

		while (classes_sc.hasNext()) 
		{
			HybridCache sourceCache = new HybridCache();
			
			QuerySolution musicclass_sc = classes_sc.next();
			ParameterizedSparqlString pss_sc = new ParameterizedSparqlString("select ?s where {?s a ?class }");
			pss_sc.setParam("?class", musicclass_sc.get("?class"));
			// Parameter for Url With Delay Retry & Pagination
			ParameterUrl p1_url = new ParameterUrl(endpoint_sc, defaultGraphName_sc);
			org.aksw.jena_sparql_api.core.QueryExecutionFactory qef1_sc;
			qef1_sc=p1_url.GetUrlParameter();
			QueryExecution qexec1_sc = qef1_sc.createQueryExecution(pss_sc.toString());	
			
			ResultSet instances_sc = qexec1_sc.execSelect();
			// System.out.println("Instances of class "+musicclass_sc.get("?class"));
			while (instances_sc.hasNext()) 
			{
				QuerySolution musicinstance_sc = instances_sc.next();
				ParameterizedSparqlString pss1_sc = new ParameterizedSparqlString("SELECT ?p ?o where { ?s ?p ?o } ");
				pss1_sc.setParam("?s", musicinstance_sc.get("?s"));						

				// Parameter for Url With Delay Retry & Pagination
				ParameterUrl p2_url = new ParameterUrl(endpoint_sc, defaultGraphName_sc);
				org.aksw.jena_sparql_api.core.QueryExecutionFactory qef2_sc;
				qef2_sc=p2_url.GetUrlParameter();
				////QueryExecution qexec2_sc = QueryExecutionFactory.sparqlService(endpoint_sc, pss1_sc.toString());
				QueryExecution qexec2_sc = qef2_sc.createQueryExecution(pss1_sc.toString());	

				ResultSet properties_sc = qexec2_sc.execSelect();
				// System.out.println("Properties of class instance"+musicinstance_sc.get("?s"));

				while (properties_sc.hasNext()) 
				{
					QuerySolution musicproperties_sc = properties_sc.next();

					sourceCache.addTriple(musicinstance_sc.get("?s").toString(),musicproperties_sc.get("?p").toString(), musicproperties_sc.get("?o").toString());
					//System.out.println("<--SOURCACHE-->"+sourceCache);
					System.out.println("Started....source");
				}
			}

			//----------------------Begining of Target -------------------------------------------- 
			// Parameter for Url With Delay Retry & Pagination
			ParameterUrl p_url_tc = new ParameterUrl(endpoint_tc, defaultGraphName_tc);
			org.aksw.jena_sparql_api.core.QueryExecutionFactory qef_tc;
			qef_tc=p_url_tc.GetUrlParameter();


			QueryExecution qexec_tc = qef_tc.createQueryExecution(QS);

			ResultSet classes_tc = qexec_tc.execSelect();
			double CurrNofMap =0;
			while (classes_tc.hasNext()) 
			{
				HybridCache targetCache = new HybridCache();
				QuerySolution musicclass_tc = classes_tc.next();
				ParameterizedSparqlString pss_tc = new ParameterizedSparqlString("select ?s where {?s a ?class } ");

				pss_tc.setParam("?class", musicclass_tc.get("?class"));

				// Parameter for Url With Delay Retry & Pagination
				ParameterUrl p1_url_tc = new ParameterUrl(endpoint_tc, defaultGraphName_tc);
				org.aksw.jena_sparql_api.core.QueryExecutionFactory qef1_tc;
				qef1_tc=p1_url_tc.GetUrlParameter();

				QueryExecution qexec1_tc = qef1_tc.createQueryExecution(pss_tc.toString());	


				ResultSet instances_tc = qexec1_tc.execSelect();
				//System.out.println("Instances of Target class "+musicclass_tc.get("?class"));

				while (instances_tc.hasNext()) 
				{
					QuerySolution musicinstance_tc = instances_tc.next();
					ParameterizedSparqlString pss1_tc = new ParameterizedSparqlString("SELECT ?p ?o where { ?s ?p ?o }");
					pss1_tc.setParam("?s", musicinstance_tc.get("?s"));

					ParameterUrl p2_url_tc = new ParameterUrl(endpoint_tc, defaultGraphName_tc);
					org.aksw.jena_sparql_api.core.QueryExecutionFactory qef2_tc;
					qef2_tc=p1_url_tc.GetUrlParameter();

					//QueryExecution qexec2_tc = QueryExecutionFactory.sparqlService(endpoint_tc, pss1_tc.toString());
					QueryExecution qexec2_tc = qef2_tc.createQueryExecution(pss1_tc.toString());			


					ResultSet properties_tc = qexec2_tc.execSelect();
					// System.out.println("Properties of class instance "+musicinstance.get("?s"));
					while (properties_tc.hasNext()) 
					{
						QuerySolution musicproperties_tc = properties_tc.next();
						targetCache.addTriple(musicinstance_tc.get("?s").toString(),musicproperties_tc.get("?p").toString(), musicproperties_tc.get("?o").toString());
						//System.out.println("targetCache "+targetCache);
						System.out.println("Started....target");
					}

				}

				// Testing of Wombat //
                long start_time =System.currentTimeMillis();
				MlalgorithmWombat wm = new MlalgorithmWombat(sourceCache, targetCache);
				results = wm.GetMlalgorithmWombat();
				long end_time =System.currentTimeMillis();
				AMapping acceptanceMapping = results.getSubMap(0.9);
				
			    AMapping verificationMapping = MappingOperations.difference(results, acceptanceMapping);
			   //////// System.out.println("Mapping size: " + acceptanceMapping.size() + " (accepted) + " + verificationMapping.size()+ " (need verification) = " + results.size() + " (total)");
			     
			    HospitalResidents hs = new HospitalResidents();
			    AMapping stable = hs.getMatching(results);
				System.out.println("STABLE"+stable);

				if(CurrNofMap==0) {
					CurrNofMap = results.getNumberofMappings();
					newresults = results;
				}

				if(CurrNofMap < results.getNumberofMappings())
				{
					CurrNofMap = results.getNumberofMappings();
					newresults = results;
				}
				////////System.out.println("Clazz Src : "+musicclass_sc.get("?class").toString()+" Target Class"+musicclass_tc.get("?class") +" No of Mapping"+CurrNofMap);
				if (CurrNofMap > 10) 

				{
					System.out.println("<Real Linking> \n" + newresults);
					 
				    DataFile.Save(acceptanceMapping,verificationMapping);
				}

			} // END OF TARGET CLASS

		} // END OF SOURCE CLASS

	}

}
