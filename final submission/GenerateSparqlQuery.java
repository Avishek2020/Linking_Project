package org.upb.music.artist.similarity.measures;

import org.apache.jena.query.ParameterizedSparqlString;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;

public class GenerateSparqlQuery {
	ResultSet Resultset_val;

	public ResultSet GetPSSResource(String endpoint_sc,
			String defaultGraphName_sc,
			String query, 
			String ParameterQuery,
			String ParamVariable,
			String DelayPagination,
			QuerySolution musicinstance_sc
			) {
		/*
		 * DelayPagination = "N" some end point give Server Error When we use Delay and Pagination
		 */
		if ((DelayPagination == "N") &&  (ParameterQuery =="N")) 
		{
			QueryExecution qexec_sc = QueryExecutionFactory.sparqlService(endpoint_sc, query);
			Resultset_val = qexec_sc.execSelect();
		}
		else if((DelayPagination == "Y")  &&  ParameterQuery =="N")
		{
			ParameterUrl p_url = new ParameterUrl(endpoint_sc,defaultGraphName_sc);
			org.aksw.jena_sparql_api.core.QueryExecutionFactory qef_sc;
			qef_sc=p_url.GetUrlParameter();
			QueryExecution qexec_sc = qef_sc.createQueryExecution(query);
			Resultset_val = qexec_sc.execSelect();
		}
		else if((DelayPagination == "N") &&(ParameterQuery =="Y")) {
			QuerySolution musicinstance_sc1 = musicinstance_sc;
			ParameterizedSparqlString pss_sc1 = new ParameterizedSparqlString(query);
			//pss_sc1.setParam("?c", musicinstance_sc1.get("?c"));
			pss_sc1.setParam(ParamVariable, musicinstance_sc1.get(ParamVariable));
			QueryExecution qexec_sc = QueryExecutionFactory.sparqlService(endpoint_sc, pss_sc1.toString());
			Resultset_val = qexec_sc.execSelect();
		}
		else if((DelayPagination == "Y") &&(ParameterQuery =="Y")) 
		{
			QuerySolution musicinstance_sc2 = musicinstance_sc;
			ParameterizedSparqlString pss_sc2 = new ParameterizedSparqlString(query);
			//pss_sc.setParam("?s", musicinstance_sc.get("?s"));
			pss_sc2.setParam(ParamVariable, musicinstance_sc2.get(ParamVariable));
			// Parameter for Url With Delay Retry & Pagination
			ParameterUrl p2_url2 = new ParameterUrl(endpoint_sc, defaultGraphName_sc);
			org.aksw.jena_sparql_api.core.QueryExecutionFactory qef2_sc2;
			qef2_sc2=p2_url2.GetUrlParameter();
			////QueryExecution qexec2_sc = QueryExecutionFactory.sparqlService(endpoint_sc, pss1_sc.toString());
			QueryExecution qexec_sc2 = qef2_sc2.createQueryExecution(pss_sc2.toString());	
			Resultset_val = qexec_sc2.execSelect();
		}

		return Resultset_val;

	}
	/*public static void main(String args[]) {
		GenerateSparqlQuery u = new GenerateSparqlQuery();
		ResultSet rs = u.GetPSSResource("http://dbtune.org/jamendo/sparql", 
				                         "http://dbtune.org", 
				                         "SELECT DISTINCT ?class WHERE {[] a ?class } ORDER BY ?class ", 
				                         "N", 
				                         "N"
				                          );
	    System.out.println("ResultSet  -"+rs);

	}*/
}
