package org.upb.music.artist.similarity.measures;

import java.util.ArrayList;
import java.util.List;

import org.aksw.limes.core.evaluation.evaluator.EvaluatorFactory;
import org.aksw.limes.core.evaluation.evaluator.EvaluatorType;
import org.aksw.limes.core.evaluation.qualititativeMeasures.PseudoFMeasure;
import org.aksw.limes.core.exceptions.UnsupportedMLImplementationException;
import org.aksw.limes.core.io.cache.ACache;
import org.aksw.limes.core.io.config.Configuration;
import org.aksw.limes.core.io.mapping.AMapping;
import org.aksw.limes.core.ml.algorithm.LearningParameter;
import org.aksw.limes.core.ml.algorithm.MLAlgorithmFactory;
import org.aksw.limes.core.ml.algorithm.MLResults;
import org.aksw.limes.core.ml.algorithm.UnsupervisedMLAlgorithm;

public class MlalgorithmWombat {
	ACache sourceCache;
	ACache targetCache;
	AMapping results = null;
	
	List<LearningParameter> MlAlgorithmParameters = new ArrayList<>();
	LearningParameter params = new LearningParameter();
	//List<LearningParameter> lp = new ArrayList<>();
   
	public MlalgorithmWombat(ACache sourceCache,ACache targetCache) 
	{
		//this.lp =lp;
		this.sourceCache = sourceCache;
		this.targetCache = targetCache;
	}
	
	public AMapping GetMlalgorithmWombat() throws UnsupportedMLImplementationException 
	{
		params.setName("max execution time in minutes");
		params.setValue(60);
		params.setName("minimum property coverage");
		params.setValue(0.4);
		MlAlgorithmParameters.add(params);
		
		MLResults mlm;
		UnsupervisedMLAlgorithm mlu = new UnsupervisedMLAlgorithm(MLAlgorithmFactory.getAlgorithmType("wombat simple"));
		mlu.init(MlAlgorithmParameters, sourceCache, targetCache);
		PseudoFMeasure pfm = null;
		EvaluatorType pfmType = null;
		if(pfmType != null)
			 {
					pfm = (PseudoFMeasure) EvaluatorFactory.create(pfmType);
			 }
		mlm = mlu.learn(pfm);
		System.out.println("Learned: " + mlm.getLinkSpecification().getFullExpression() + " with threshold: " + mlm.getLinkSpecification().getThreshold());
		results = mlu.predict(sourceCache, targetCache, mlm); 
		return results;
	}

}
