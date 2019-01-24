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
import org.aksw.limes.core.datastrutures.GoldStandard;
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
import org.aksw.limes.core.evaluation.qualititativeMeasures.Precision;
import org.aksw.limes.core.evaluation.qualititativeMeasures.PseudoFMeasure;
import org.aksw.limes.core.evaluation.qualititativeMeasures.Recall;

/*
 * 
 * This Class doesn't contain Delay pagination in URL.
 * Reason - while testing some End point doesn't allow Delay ,Pagination Setting in URL.
 */
public class SourceClassTargetMappingMusicNoPagn {
	private static String endpoint_sc = "http://dbtune.org/musicbrainz/sparql";
	private static String defaultGraphName_sc ="http://dbtune.org";
	//private static String endpoint_sc = "http://dbtune.org/jamendo/sparql";
	private static String endpoint_tc = "http://dbtune.org/magnatune/sparql";
	private static String defaultGraphName_tc ="http://dbtune.org";

	@SuppressWarnings("unused")
	public static void main(String[] args) throws UnsupportedMLImplementationException {

		String fullClassName = SourceClassTargetMappingMusicNoPagn.class.getName();
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

		String Query_Class = "SELECT DISTINCT ?class WHERE {[] a ?class } ORDER BY ?class ";
		
		GenerateSparqlQuery Qry = new GenerateSparqlQuery();

		ResultSet classes_sc = Qry.GetPSSResource( endpoint_sc,         // Sparql End point
												   defaultGraphName_sc, // Graph
												   Query_Class,         //Query
												   "N",                 //ParameterQuery
												   "",                  //ParamVariable
												   "N",                 //DelayPagination
												   null);               //QuerySolution


		while (classes_sc.hasNext()) 
		{
			HybridCache sourceCache = new HybridCache();
			QuerySolution musicclass_sc = classes_sc.next();
			// System.out.println("Instances of class "+musicclass_sc.get("?class"));

			String Query_Instance = "Select ?instance where {?instance a ?class } Limit 1 ";

			GenerateSparqlQuery Qry1 = new GenerateSparqlQuery();

			ResultSet instances_sc = Qry1.GetPSSResource(endpoint_sc,          // Sparql End point
														defaultGraphName_sc,   // Graph
														Query_Instance,        // Query
														"Y",                   // ParameterQuery
														"?class",              // ParamVariable
														"N",                   // DelayPagination
														musicclass_sc);        // QuerySolution

			while (instances_sc.hasNext()) 
			{
				QuerySolution musicinstance_sc = instances_sc.next();

				String Query_PropObj = "SELECT ?p ?o where { ?instance ?p ?o } limit 1 ";


				GenerateSparqlQuery Qry2 = new GenerateSparqlQuery();

				ResultSet properties_sc = Qry2.GetPSSResource(endpoint_sc,        //  Sparql End point
															  defaultGraphName_sc, //  Graph
															  Query_PropObj,       //  Query
															  "Y",                 //  ParameterQuery
															  "?instance",         //  ParamVariable
															  "N",                 //  DelayPagination
															  musicinstance_sc);   //  QuerySolution

				while (properties_sc.hasNext()) 
				{
					QuerySolution musicproperties_sc = properties_sc.next();

					sourceCache.addTriple(musicinstance_sc.get("?instance").toString(),musicproperties_sc.get("?p").toString(), musicproperties_sc.get("?o").toString());
					//System.out.println("<--SOURCACHE-->"+sourceCache);
					//System.out.println("Started....source");
				}
			}

			//----------------------Beginning of Target Class -------------------------------------------- 

			GenerateSparqlQuery Qry_tc = new GenerateSparqlQuery();

			ResultSet classes_tc = Qry_tc.GetPSSResource(endpoint_tc,         // Sparql End point
														 defaultGraphName_tc, // Graph
														 Query_Class,         //Query
														 "N",                 //ParameterQuery
														 "",                  //ParamVariable
														 "N",                 //DelayPagination
														 null);               //QuerySolution


        	double CurrNofMap =0;

			while (classes_tc.hasNext()) 
			{
				HybridCache targetCache = new HybridCache();
				QuerySolution musicclass_tc = classes_tc.next();				

				String Query_Instance_tc = "Select ?instance where {?instance a ?class } ";

				GenerateSparqlQuery Qry1_tc = new GenerateSparqlQuery();

				ResultSet instances_tc = Qry1_tc.GetPSSResource(endpoint_tc,           // Sparql End point
																defaultGraphName_tc,   // Graph
																Query_Instance_tc,     // Query
																"Y",                   // ParameterQuery
																"?class",              // ParamVariable
																"N",                   // DelayPagination
																musicclass_tc);        // QuerySolution

		
				while (instances_tc.hasNext()) 
				{
					QuerySolution musicinstance_tc = instances_tc.next();					
					String Query_PropObj = "SELECT ?p ?o where { ?instance ?p ?o } ";				
					GenerateSparqlQuery Qry2_tc = new GenerateSparqlQuery();					    
					ResultSet properties_tc = Qry2_tc.GetPSSResource(endpoint_tc,         //  Sparql End point
																	 defaultGraphName_tc, //  Graph
																	 Query_PropObj,       //  Query
																	 "Y",                 //  ParameterQuery
																	 "?instance",         //  ParamVariable
																	 "N",                 //  DelayPagination
																	 musicinstance_tc);   //  QuerySolution
					
					while (properties_tc.hasNext()) 
					{
						QuerySolution musicproperties_tc = properties_tc.next();
						targetCache.addTriple(musicinstance_tc.get("?instance").toString(),musicproperties_tc.get("?p").toString(), musicproperties_tc.get("?o").toString());
						//System.out.println("targetCache "+targetCache);
						//System.out.println("Started....target");
					}

				}

				// Testing of Wombat //
				long Start_Time =System.currentTimeMillis();
				System.out.println("Start Time- "+Start_Time);
				MlalgorithmWombat wm = new MlalgorithmWombat(sourceCache, targetCache);
				results = wm.GetMlalgorithmWombat();
				long End_Time = System.currentTimeMillis();
				long Total_Time = End_Time- Start_Time;
				System.out.println("Total execution time for Class ~ "+musicclass_sc.get("?class").toString() + "in WOMBAT USUPERVISED LEARNING- "+Total_Time);
				AMapping acceptanceMapping = results.getSubMap(0.9);

				AMapping verificationMapping = MappingOperations.difference(results, acceptanceMapping);
				System.out.println("Mapping size: " + acceptanceMapping.size() + " (accepted) + " + verificationMapping.size()+ " (need verification) = " + results.size() + " (total)");
				
				org.aksw.limes.core.evaluation.qualititativeMeasures.PseudoFMeasure pseudoFMeasure = null ;
				
				double pfm = pseudoFMeasure.calculate(results, new GoldStandard(null, sourceCache, targetCache));
				double recall =pseudoFMeasure.recall(results, new GoldStandard(null, sourceCache, targetCache));
				double precsion = pseudoFMeasure.precision(results, new GoldStandard(null, sourceCache, targetCache));
				/*HospitalResidents hs = new HospitalResidents();
				AMapping stable = hs.getMatching(results);
				System.out.println("STABLE"+stable);*/

				if(CurrNofMap==0) {
					CurrNofMap = results.getNumberofMappings();
					newresults = results;
				}

				if(CurrNofMap < results.getNumberofMappings())
				{
					CurrNofMap = results.getNumberofMappings();
					newresults = results;
				}
				System.out.println("Clazz Src : "+musicclass_sc.get("?class").toString()+" Target Class"+musicclass_tc.get("?class") +" No of Mapping"+CurrNofMap);
				String Message;
				Message ="Similarity for Source Class "+musicclass_sc.get("?class").toString()+" and Target Class " +musicclass_tc.get("?class") + "No of Mapping"+results.getNumberofMappings();
				//if (CurrNofMap > 10) 
				if (CurrNofMap >= 0.4) 

				{
					System.out.println("<Real Linking> \n" + newresults);

					//DataFile.Save(acceptanceMapping,verificationMapping);
					DataFile.SaveBestMatchClass(newresults);
					WriteFileData wf = new WriteFileData();
					wf.SaveData(Message);				
					
				}
				//System.out.println(Message);
				//DataFile.Save(acceptanceMapping,verificationMapping);
				//DataFile.SaveBestMatchClass(newresults);
			} // END OF TARGET CLASS

		} // END OF SOURCE CLASS

	}

}
