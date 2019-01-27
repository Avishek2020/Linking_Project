package org.upb.music.artist.similarity.measures;

import java.util.Date;

import org.aksw.limes.core.controller.ResultMappings;
import org.aksw.limes.core.io.config.Configuration;
import org.aksw.limes.core.io.mapping.AMapping;
import org.aksw.limes.core.io.serializer.ISerializer;
import org.aksw.limes.core.io.serializer.SerializerFactory;

public class DataToFile 
   {
	    protected String verificationFile;
	    protected String verificationRelation;
	    protected String AcceptanceRelation;
	    protected String AcceptanceFile;
	    protected AMapping verificationMapping;
	    protected AMapping acceptanceMapping;
	    String outputFormat;
		void Save(AMapping acceptanceMapping,AMapping verificationMapping) {
			 Date d1 = new Date();
			outputFormat         = "TAB";
			verificationFile     = "_ver.nt";
			verificationRelation = "owl:sameAs";
			AcceptanceRelation   = "owl:sameAs";
			AcceptanceFile       = "_acc.nt";
			//String outputFormat = config.getOutputFormat();
			ISerializer output = SerializerFactory.createSerializer(outputFormat);
			//output.setPrefixes(config.getPrefixes());
			
			
			
			
			output.writeToFile(verificationMapping,verificationRelation,verificationFile); 
			output.writeToFile(acceptanceMapping, AcceptanceRelation, AcceptanceFile);
		}

   }

