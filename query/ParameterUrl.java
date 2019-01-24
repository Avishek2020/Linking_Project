package org.upb.music.artist.similarity;

import org.aksw.jena_sparql_api.core.QueryExecutionFactory;
import org.aksw.jena_sparql_api.delay.core.QueryExecutionFactoryDelay;
import org.aksw.jena_sparql_api.pagination.core.QueryExecutionFactoryPaginated;
import org.aksw.jena_sparql_api.retry.core.QueryExecutionFactoryRetry;
import org.apache.jena.query.ResultSet;
import org.aksw.jena_sparql_api.http.QueryExecutionFactoryHttp;

public class ParameterUrl {
	private String Service;
	private String defaultGraphs;
	int retryCount = 5;
	int retryDelayInMS = 500;
	int requestDelayInMs = 50;
	int pageSize = 900;
	long timeToLive = 24l * 60l * 60l * 1000l;

	public ParameterUrl(String Service,String defaultGraphs) {
		this.Service =Service;
		this.defaultGraphs =defaultGraphs;
	}
	
	public QueryExecutionFactory  GetUrlParameter() {
		org.aksw.jena_sparql_api.core.QueryExecutionFactory qef_sc= new QueryExecutionFactoryHttp(Service, defaultGraphs);
		qef_sc = new QueryExecutionFactoryRetry(qef_sc,retryCount, retryDelayInMS);
		// Add delay in order to be nice to the remote server (delay in milli seconds)
	    qef_sc = new QueryExecutionFactoryDelay(qef_sc, requestDelayInMs);	
		//QueryExecution qexec_sc = QueryExecutionFactory.sparqlService(endpoint_sc, QS);		
		// Add pagination
	    qef_sc = new QueryExecutionFactoryPaginated(qef_sc, pageSize);
	    
	    return qef_sc ;
	}
 
	/*public static void main(String args[]) {
		ParameterUrl u = new ParameterUrl("http://dbtune.org/jamendo/sparql", "http://dbtune.org");
	    System.out.println("url  -"+u.GetUrlParameter());
	}*/
      
}
