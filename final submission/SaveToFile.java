package org.upb.music.artist.similarity.measures;

import org.aksw.limes.core.controller.ResultMappings;
import org.aksw.limes.core.io.config.Configuration;
import org.aksw.limes.core.io.serializer.ISerializer;
import org.aksw.limes.core.io.serializer.SerializerFactory;

public class SaveToFile {
	 void Save(ResultMappings mappings, Configuration config) {
	  String outputFormat = config.getOutputFormat();
	  ISerializer output = SerializerFactory.createSerializer(outputFormat);
	  output.setPrefixes(config.getPrefixes());
	  output.writeToFile(mappings.getVerificationMapping(), config.getVerificationRelation(),config.getVerificationFile());
	  output.writeToFile(mappings.getAcceptanceMapping(), config.getAcceptanceRelation(), config.getAcceptanceFile());
	 }
	
}