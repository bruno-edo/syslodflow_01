package br.ufsc.inf.syslodflow.service;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;

import br.ufsc.inf.syslodflow.dto.LDWStepExecutionDTO;
import br.ufsc.inf.syslodflow.entity.LDWStepExecution;
import br.ufsc.inf.syslodflow.entity.Message;
import br.ufsc.inf.syslodflow.entity.Person;
import br.ufsc.inf.syslodflow.entity.Status;
import br.ufsc.inf.syslodflow.enumerator.PropertyURIEnum;

public class LdwStepExecutionService extends BaseService {
	
	public LDWStepExecution getLdwStepExecution(OntModel model, Individual ontLdwStepExec) {
		
		String ldwStepExecutionName = getPropertyStringValue(ontLdwStepExec, model, PropertyURIEnum.NAME.getUri());
		String ldwStepExecutionDescription = getPropertyStringValue(ontLdwStepExec, model, PropertyURIEnum.DESCRIPTION.getUri());
		
		Individual ldwStepExecutionStatus = model.getIndividual(ontLdwStepExec.getPropertyResourceValue(model.getProperty(PropertyURIEnum.STATUS.getUri())).getURI());
		String ldwStepExecutionStatusValue = getPropertyStringValue(ldwStepExecutionStatus, model, PropertyURIEnum.VALUE.getUri());
		Status status = new Status(ldwStepExecutionStatusValue);
		
		Individual ldwStepExecutionMessage = model.getIndividual(ontLdwStepExec.getPropertyResourceValue(model.getProperty(PropertyURIEnum.MESSAGE.getUri())).getURI());
		String ldwStepExecutionStatusMessageValue = getPropertyStringValue(ldwStepExecutionMessage, model, PropertyURIEnum.VALUE.getUri());
		Message msg = new Message(ldwStepExecutionStatusMessageValue);
		
		Individual ldwStepExecutionContributor = model.getIndividual(ontLdwStepExec.getPropertyResourceValue(model.getProperty(PropertyURIEnum.CONTRIBUTOR.getUri())).getURI());
		String ldwStepExecutionContributorName = getPropertyStringValue(ldwStepExecutionContributor, model, PropertyURIEnum.NAME.getUri());
		Person contributor = new Person(ldwStepExecutionContributorName);
		
		String ldwStepExecutionStartedDate = getPropertyStringValue(ontLdwStepExec, model, PropertyURIEnum.STARTEDDATE.getUri());
		String ldwStepExecutionEndedDate = getPropertyStringValue(ontLdwStepExec, model, PropertyURIEnum.ENDEDDATE.getUri());
		
		LDWStepExecutionDTO nextStep = this.getNextStepExecution(model, ontLdwStepExec);
		LDWStepExecutionDTO previousStep = this.getPreviousStepExecution(model, ontLdwStepExec);
		
		return new LDWStepExecution(ldwStepExecutionName, ldwStepExecutionDescription, status, msg, contributor, ldwStepExecutionStartedDate, ldwStepExecutionEndedDate,
				nextStep, previousStep);
	}
	
	private LDWStepExecutionDTO getNextStepExecution(OntModel model, Individual stepExecution) {
		
		if(stepExecution.hasProperty(model.getProperty(PropertyURIEnum.NEXTSTEP.getUri()))) {
			Individual nextStep = model.getIndividual(stepExecution.getPropertyResourceValue(model.getProperty(PropertyURIEnum.NEXTSTEP.getUri())).getURI());
			String nextStepName = getPropertyStringValue(nextStep, model, PropertyURIEnum.NAME.getUri());
			String nextStepURI = nextStep.getURI();
			return new LDWStepExecutionDTO(nextStepName, nextStepURI);
		}
		else { return null; }
	}
	
	private LDWStepExecutionDTO getPreviousStepExecution(OntModel model, Individual stepExecution) {
		
		if(stepExecution.hasProperty(model.getProperty(PropertyURIEnum.PREVIOUSSTEP.getUri()))) {
			Individual previousStep = model.getIndividual(stepExecution.getPropertyResourceValue(model.getProperty(PropertyURIEnum.NEXTSTEP.getUri())).getURI());
			String previousStepName = getPropertyStringValue(previousStep, model, PropertyURIEnum.NAME.getUri());
			String previousStepURI = previousStep.getURI();
			return new LDWStepExecutionDTO(previousStepName, previousStepURI);
		}
		else { return null; }
	}


}
