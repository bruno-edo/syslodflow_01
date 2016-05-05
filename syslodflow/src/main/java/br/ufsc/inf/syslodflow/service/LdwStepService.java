package br.ufsc.inf.syslodflow.service;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;

import br.ufsc.inf.syslodflow.entity.LDWStep;
import br.ufsc.inf.syslodflow.enumerator.PropertyURIEnum;

public class LdwStepService extends BaseService {

	public LDWStep getLdwStep(OntModel model, Individual ontLdwStep) {
		
		String ldwStepName = getPropertyStringValue(ontLdwStep, model, PropertyURIEnum.NAME.getUri());
		String ldwStepDescription = getPropertyStringValue(ontLdwStep, model, PropertyURIEnum.DESCRIPTION.getUri());
		String ldwStepCommand = getPropertyStringValue(ontLdwStep, model, PropertyURIEnum.COMMAND.getUri());
		
		Individual ldwStepTask = model.getIndividual(ontLdwStep.getPropertyResourceValue(model.getProperty(PropertyURIEnum.TASK.getUri())).getURI());
		String ldwStepTaskName = getPropertyStringValue(ldwStepTask, model, PropertyURIEnum.NAME.getUri());
		
		
		return null;
		
	}
	
}
