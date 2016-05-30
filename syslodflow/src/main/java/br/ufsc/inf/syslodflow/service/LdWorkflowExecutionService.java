package br.ufsc.inf.syslodflow.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.ufsc.inf.syslodflow.entity.LDWStepExecution;
import br.ufsc.inf.syslodflow.entity.LDWorkflowExecution;
import br.ufsc.inf.syslodflow.enumerator.ClassURIEnum;
import br.ufsc.inf.syslodflow.enumerator.PropertyURIEnum;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class LdWorkflowExecutionService extends BaseService {
	
	@Inject 
	private LdwStepExecutionService ldwStepExecutionService; 
	
	public LDWorkflowExecution getLdwLdWorkflowExecution(OntModel model, Individual ontLdwWorkFlowExecution) {
		
		String description = getPropertyStringValue(ontLdwWorkFlowExecution, model, PropertyURIEnum.DESCRIPTION.getUri());
		String name = getPropertyStringValue(ontLdwWorkFlowExecution, model, PropertyURIEnum.NAME.getUri());
		String startedDate = getPropertyStringValue(ontLdwWorkFlowExecution, model, PropertyURIEnum.STARTEDDATE.getUri());
		String endedDate = getPropertyStringValue(ontLdwWorkFlowExecution, model, PropertyURIEnum.ENDEDDATE.getUri());

		StmtIterator iter = ontLdwWorkFlowExecution.listProperties(model.getProperty(PropertyURIEnum.LDWSTEPEXECUTION.getUri()));
		List<LDWStepExecution> ldwStepExecutions = new ArrayList<LDWStepExecution>();
		LDWStepExecution firstLdwStepExecution = null;
		while (iter.hasNext()){
			Individual node = model.getIndividual(iter.nextStatement().getResource().getURI());
			LDWStepExecution l = ldwStepExecutionService.getLdwStepExecution(model, node);
			ldwStepExecutions.add(l);
			if (l.getUri().equals(ontLdwWorkFlowExecution.getPropertyResourceValue(model.getProperty(PropertyURIEnum.FIRSTLDWSTEPEXECUTION.getUri())).getURI()))
				firstLdwStepExecution = l;
		}
		
		return new LDWorkflowExecution(firstLdwStepExecution, ldwStepExecutions, description, name, startedDate, endedDate, ontLdwWorkFlowExecution.getURI());
	}
	
	
	public OntModel writeLDWorkflowExecution(OntModel model, LDWorkflowExecution l, Individual ldworkflow) {
		if(URIalreadyExists(model, l.getUri())) 
			return editLDWorkflowExecution(model, l);
		else 
			return insertLDWorkflowExecution(model, l, ldworkflow);
	}
	public OntModel insertLDWorkflowExecution(OntModel model, LDWorkflowExecution l, Individual ldworkflow) {
		
		Individual ldworkflowexecution = model.getOntClass(ClassURIEnum.LDWORKFLOWEXECUTION.getUri()).createIndividual(l.getUri());
		ldworkflowexecution.addLiteral(model.getProperty(PropertyURIEnum.NAME.getUri()), l.getName());
		ldworkflowexecution.addLiteral(model.getProperty(PropertyURIEnum.DESCRIPTION.getUri()), l.getDescription());
		List<LDWStepExecution> sortedLDWStepExecutions = sortLDWStepExecutions(l.getLdwStepExecutions());
		for(int i=0; i<l.getLdwStepExecutions().size(); i++) {
			ldwStepExecutionService.insertLdwStepExecution(model, l.getLdwStepExecutions().get(i));
			ldworkflowexecution.addProperty(model.getProperty(PropertyURIEnum.LDWSTEPEXECUTION.getUri()), model.getIndividual(sortedLDWStepExecutions.get(i).getUri()));
		}
		ldworkflowexecution.addProperty(model.getProperty(PropertyURIEnum.FIRSTLDWSTEPEXECUTION.getUri()), model.getIndividual(sortedLDWStepExecutions.get(0).getUri()));
		model.getIndividual(sortedLDWStepExecutions.get(0).getUri()).addProperty(model.getProperty(PropertyURIEnum.NEXTSTEP.getUri()), model.getIndividual(sortedLDWStepExecutions.get(1).getUri()));
		model.getIndividual(sortedLDWStepExecutions.get(1).getUri()).addProperty(model.getProperty(PropertyURIEnum.NEXTSTEP.getUri()), model.getIndividual(sortedLDWStepExecutions.get(2).getUri()));
		model.getIndividual(sortedLDWStepExecutions.get(1).getUri()).addProperty(model.getProperty(PropertyURIEnum.PREVIOUSSTEP.getUri()), model.getIndividual(sortedLDWStepExecutions.get(0).getUri()));
		model.getIndividual(sortedLDWStepExecutions.get(2).getUri()).addProperty(model.getProperty(PropertyURIEnum.NEXTSTEP.getUri()), model.getIndividual(sortedLDWStepExecutions.get(3).getUri()));
		model.getIndividual(sortedLDWStepExecutions.get(2).getUri()).addProperty(model.getProperty(PropertyURIEnum.PREVIOUSSTEP.getUri()), model.getIndividual(sortedLDWStepExecutions.get(2).getUri()));
		model.getIndividual(sortedLDWStepExecutions.get(3).getUri()).addProperty(model.getProperty(PropertyURIEnum.NEXTSTEP.getUri()), model.getIndividual(sortedLDWStepExecutions.get(4).getUri()));
		model.getIndividual(sortedLDWStepExecutions.get(3).getUri()).addProperty(model.getProperty(PropertyURIEnum.PREVIOUSSTEP.getUri()), model.getIndividual(sortedLDWStepExecutions.get(2).getUri()));
		model.getIndividual(sortedLDWStepExecutions.get(4).getUri()).addProperty(model.getProperty(PropertyURIEnum.PREVIOUSSTEP.getUri()), model.getIndividual(sortedLDWStepExecutions.get(3).getUri()));
		ldworkflowexecution.addProperty(model.getProperty(PropertyURIEnum.LDWORKFLOWEXECUTION.getUri()), ldworkflow);
		return model;
	}
	
	public OntModel editLDWorkflowExecution(OntModel model, LDWorkflowExecution l) {
		
		Individual ldworkflowexecution = model.getIndividual(l.getUri());
		ldworkflowexecution.removeAll(model.getProperty(PropertyURIEnum.NAME.getUri()));
		ldworkflowexecution.addLiteral(model.getProperty(PropertyURIEnum.NAME.getUri()), l.getName());
		ldworkflowexecution.removeAll(model.getProperty(PropertyURIEnum.DESCRIPTION.getUri()));
		ldworkflowexecution.addLiteral(model.getProperty(PropertyURIEnum.DESCRIPTION.getUri()), l.getDescription());
		List<LDWStepExecution> sortedLDWStepExecutions = sortLDWStepExecutions(l.getLdwStepExecutions());
		for(int i=0; i<sortedLDWStepExecutions.size(); i++) {
			ldwStepExecutionService.editLdwStepExecution(model, sortedLDWStepExecutions.get(i));
		}
		return model;
	}
		
	private List<LDWStepExecution> sortLDWStepExecutions(List<LDWStepExecution> ldwstepexecutions) {
		List<LDWStepExecution> sortedList = new ArrayList<LDWStepExecution>();
		for(LDWStepExecution s: ldwstepexecutions){
			sortedList.add(s);
		}
		
		for(int i=0; i<ldwstepexecutions.size(); i++) {
			sortedList.set(ldwstepexecutions.get(i).getOrder()-1, ldwstepexecutions.get(i));
		}
		return sortedList;
	}

	
}
