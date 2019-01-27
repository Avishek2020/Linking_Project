package org.upb.music.artist.similarity.measures;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.aksw.limes.core.measures.measure.string.JaccardMeasure;
import org.apache.commons.text.similarity.JaccardSimilarity;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;


public class SourceClassTargetLiteralMapJaccard {
	private static String endpoint_sc = "http://dbtune.org/musicbrainz/sparql";
	private static String defaultGraphName_sc ="http://dbtune.org";
	//private static String endpoint_sc = "http://dbtune.org/jamendo/sparql";
	private static String endpoint_tc = "http://dbtune.org/magnatune/sparql";
	private static String defaultGraphName_tc ="http://dbtune.org";

	@SuppressWarnings("unused")
	public static void main(String[] args) {

		String fullClassName = SourceClassTargetLiteralMapJaccard.class.getName();
		String sClassName = fullClassName.substring(fullClassName.lastIndexOf('.') + 1);
		File folder = new File("");		
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
			//HybridCache sourceCache = new HybridCache();
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

				String Query_PropObj_sc = "SELECT ?p ?o where { ?instance ?p ?o  FILTER(isliteral(?o))} ";


				GenerateSparqlQuery Qry2 = new GenerateSparqlQuery();

				ResultSet properties_sc = Qry2.GetPSSResource(endpoint_sc,        //  Sparql End point
															  defaultGraphName_sc, //  Graph
															  Query_PropObj_sc,       //  Query
															  "Y",                 //  ParameterQuery
															  "?instance",         //  ParamVariable
															  "N",                 //  DelayPagination
															  musicinstance_sc);   //  QuerySolution

				while (properties_sc.hasNext()) 
				{
					QuerySolution musicproperties_sc = properties_sc.next();
 
			//----------------------Beginning of Target Class -------------------------------------------- 

			GenerateSparqlQuery Qry_tc = new GenerateSparqlQuery();

			ResultSet classes_tc = Qry_tc.GetPSSResource(endpoint_tc,         // Sparql End point
														 defaultGraphName_tc, // Graph
														 Query_Class,         //Query
														 "N",                 //ParameterQuery
														 "",                  //ParamVariable
														 "N",                 //DelayPagination
														 null);               //QuerySolution


        	

			while (classes_tc.hasNext()) 
			{
				//HybridCache targetCache = new HybridCache();
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
					String Query_PropObj_tc = "SELECT ?p ?o where { ?instance ?p ?o.  FILTER(isliteral(?o)) } ";				
					GenerateSparqlQuery Qry2_tc = new GenerateSparqlQuery();					    
					ResultSet properties_tc = Qry2_tc.GetPSSResource(endpoint_tc,         //  Sparql End point
																	 defaultGraphName_tc, //  Graph
																	 Query_PropObj_tc,       //  Query
																	 "Y",                 //  ParameterQuery
																	 "?instance",         //  ParamVariable
																	 "N",                 //  DelayPagination
																	 musicinstance_tc);   //  QuerySolution
					
					while (properties_tc.hasNext()) 
					{
						QuerySolution musicproperties_tc = properties_tc.next();
						//targetCache.addTriple(musicinstance_tc.get("?instance").toString(),musicproperties_tc.get("?p").toString(), musicproperties_tc.get("?o").toString());
						long Start_Time =System.currentTimeMillis();					
						RegexStopperRemoval RSR = new RegexStopperRemoval();
						//RSR.stopperremoval(text)
						//double score = 0;
						JaccardSimilarity jaccardSimilarity = new JaccardSimilarity();
				       // double jcdsimilary1 = jaccardSimilarity.apply("hello", "hello");
						System.out.println("musicproperties_sc.get(\"?o\").toString()-" +musicproperties_sc.get("?o").toString());
						double score = jaccardSimilarity.apply(RSR.stopperremoval(musicproperties_sc.get("?o").toString()), RSR.stopperremoval(musicproperties_tc.get("?o").toString()));
						//double score = JaccardMeasure.getSimilarity("Hello","Hallo");
					    long End_Time = System.currentTimeMillis();
						Long Total_Time = End_Time- Start_Time;
						//System.out.println("Total_Time- "+Total_Time);
						if(score>0.5)
						{
							System.out.println("Similarity for SOURCE CLASS "+musicproperties_sc.get("?class") +" and TARGET CLASS  "+musicproperties_tc.get("?class")+" based on" + " jaccard similarity of literal is SCORE "+score + " TOTAL PROCESSING TIME -"+Total_Time);
						}
					}

				}
		
		
			} // END OF TARGET CLASS
		}
	}

		} // END OF SOURCE CLASS

	}

}
