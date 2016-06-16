package br.ufsc.inf.syslodflow.service;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;

import br.ufsc.inf.syslodflow.entity.LDWStepExecution;
import br.ufsc.inf.syslodflow.entity.Message;
import br.ufsc.inf.syslodflow.entity.Person;
import br.ufsc.inf.syslodflow.entity.Status;
import br.ufsc.inf.syslodflow.enumerator.ClassURIEnum;
import br.ufsc.inf.syslodflow.enumerator.PropertyURIEnum;
import br.ufsc.inf.syslodflow.enumerator.StepOrderEnum;

public class LdwStepExecutionService extends BaseService {
	
	public LDWStepExecution getLdwStepExecution(OntModel model, Individual ontLdwStepExec) {
		
		String ldwStepExecutionName = getPropertyStringValue(ontLdwStepExec, model, PropertyURIEnum.NAME.getUri());
		String ldwStepExecutionDescription = getPropertyStringValue(ontLdwStepExec, model, PropertyURIEnum.DESCRIPTION.getUri());
		
		int order = getOrder(model, ontLdwStepExec);
		
		Individual ldwStepExecutionStatus = model.getIndividual(ontLdwStepExec.getPropertyResourceValue(model.getProperty(PropertyURIEnum.STATUS.getUri())).getURI());
		String ldwStepExecutionStatusValue = getPropertyStringValue(ldwStepExecutionStatus, model, PropertyURIEnum.VALUE.getUri());
		Status status = new Status(ldwStepExecutionStatusValue, ldwStepExecutionStatus.getURI());
		
		Individual ldwStepExecutionMessage = model.getIndividual(ontLdwStepExec.getPropertyResourceValue(model.getProperty(PropertyURIEnum.MESSAGE.getUri())).getURI());
		String ldwStepExecutionStatusMessageValue = getPropertyStringValue(ldwStepExecutionMessage, model, PropertyURIEnum.VALUE.getUri());
		Message msg = new Message(ldwStepExecutionStatusMessageValue, ldwStepExecutionMessage.getURI());
		
		Individual ldwStepExecutionContributor = model.getIndividual(ontLdwStepExec.getPropertyResourceValue(model.getProperty(PropertyURIEnum.CONTRIBUTOR.getUri())).getURI());
		String ldwStepExecutionContributorName = getPropertyStringValue(ldwStepExecutionContributor, model, PropertyURIEnum.NAME.getUri());
		Person contributor = new Person(ldwStepExecutionContributorName, ldwStepExecutionContributor.getURI());
		
		String ldwStepExecutionStartedDate = getPropertyStringValue(ontLdwStepExec, model, PropertyURIEnum.STARTEDDATE.getUri());
		String ldwStepExecutionEndedDate = getPropertyStringValue(ontLdwStepExec, model, PropertyURIEnum.ENDEDDATE.getUri());
		
		return new LDWStepExecution(ldwStepExecutionName, ldwStepExecutionDescription, status, msg, contributor, ldwStepExecutionStartedDate, ldwStepExecutionEndedDate, order, ontLdwStepExec.getURI());
	}
	
	public OntModel writeLdwStepExecution(OntModel model, LDWStepExecution l) {
		if(URIalreadyExists(model, l.getUri())) 
			return editLdwStepExecution(model, l);
		else
			return insertLdwStepExecution(model, l);
	}
	
	public OntModel insertLdwStepExecution(OntModel model, LDWStepExecution l) {
		
		Individual ldwstepexecution = model.getOntClass(ClassURIEnum.LDWSTEPEXECUTION.getUri()).createIndividual(l.getUri());
		ldwstepexecution.addLiteral(model.getProperty(PropertyURIEnum.NAME.getUri()), l.getName());
		ldwstepexecution.addLiteral(model.getProperty(PropertyURIEnum.DESCRIPTION.getUri()), l.getDescription());
		ldwstepexecution.addProperty(model.getProperty(PropertyURIEnum.CONTRIBUTOR.getUri()), model.getIndividual(l.getContributor().getUri()));
		
		Individual message = model.getOntClass(PropertyURIEnum.MESSAGE.getUri()).createIndividual(l.getMessage().getUri());
		message.addLiteral(model.getProperty(PropertyURIEnum.VALUE.getUri()), l.getMessage().getValue());
		ldwstepexecution.addProperty(model.getProperty(PropertyURIEnum.MESSAGE.getUri()), message);
		return model;
	}
	
	private int getOrder(OntModel model, Individual ontLdwStepExecution) {

		int order;
		if (!ontLdwStepExecution.hasProperty(model.getProperty(PropertyURIEnum.PREVIOUSSTEP.getUri()))) {
			order = StepOrderEnum.FIRST.getOrder();
		}

		else {

			if (!ontLdwStepExecution.hasProperty(model.getProperty(PropertyURIEnum.NEXTSTEP.getUri()))) {
				order = StepOrderEnum.FIFTH.getOrder();
			}

			else {

				if (!ontLdwStepExecution.getPropertyResourceValue(model.getProperty(PropertyURIEnum.NEXTSTEP.getUri())).hasProperty(model.getProperty(PropertyURIEnum.NEXTSTEP.getUri()))) {
					order = StepOrderEnum.FOURTH.getOrder();

				} else {

					if (!ontLdwStepExecution.getPropertyResourceValue(
							model.getProperty(PropertyURIEnum.PREVIOUSSTEP
									.getUri())).hasProperty(
							model.getProperty(PropertyURIEnum.PREVIOUSSTEP
									.getUri()))) {
						order = StepOrderEnum.SECOND.getOrder();

					} else {
						order = StepOrderEnum.THIRD.getOrder();
					}
				}
			}

		}
		
		return order;
	}

	public OntModel editLdwStepExecution(OntModel model, LDWStepExecution l) {
		
		Individual ldwstepexecution = model.getIndividual(l.getUri());
		ldwstepexecution.removeAll(model.getProperty(PropertyURIEnum.NAME.getUri()));
		ldwstepexecution.addLiteral(model.getProperty(PropertyURIEnum.NAME.getUri()), l.getName());
		ldwstepexecution.removeAll(model.getProperty(PropertyURIEnum.DESCRIPTION.getUri()));
		ldwstepexecution.addLiteral(model.getProperty(PropertyURIEnum.DESCRIPTION.getUri()), l.getDescription());
		ldwstepexecution.removeAll(model.getProperty(PropertyURIEnum.CONTRIBUTOR.getUri()));
		ldwstepexecution.addProperty(model.getProperty(PropertyURIEnum.CONTRIBUTOR.getUri()), model.getIndividual(l.getContributor().getUri()));
		Individual message = model.getIndividual(l.getMessage().getUri());
		ldwstepexecution.removeAll(model.getProperty(PropertyURIEnum.MESSAGE.getUri()));
		ldwstepexecution.addProperty(model.getProperty(PropertyURIEnum.MESSAGE.getUri()), message);
		return model;
		
	}


}
