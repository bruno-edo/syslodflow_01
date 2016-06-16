package br.ufsc.inf.syslodflow.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.ufsc.inf.syslodflow.entity.LDWStepExecution;
import br.ufsc.inf.syslodflow.entity.LDWorkflow;
import br.ufsc.inf.syslodflow.entity.LDWorkflowExecution;
import br.ufsc.inf.syslodflow.enumerator.ClassURIEnum;
import br.ufsc.inf.syslodflow.enumerator.PropertyURIEnum;
import br.ufsc.inf.syslodflow.util.StringUtils;

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
		
		ldwStepExecutions = sortLDWStepExecutions(ldwStepExecutions);
		
		return new LDWorkflowExecution(firstLdwStepExecution, ldwStepExecutions, description, name, startedDate, endedDate, ontLdwWorkFlowExecution.getURI());
	}
	
	public LDWorkflowExecution doNewWorkflowExecution() {
		LDWorkflowExecution workFlowExecution = new LDWorkflowExecution();
		List<LDWStepExecution> stepsExecutions = new ArrayList<LDWStepExecution>();
		workFlowExecution.setLdwStepExecutions(stepsExecutions);
		return workFlowExecution;
	}
	
	public OntModel writeLDWorkflowExecution(OntModel model, LDWorkflowExecution workFlowExec, LDWorkflow workflow) {
		
		if(workFlowExec.getUri() == null) {
			/**
			 * Novo LDWorkflowExecution
			 * - Setar URI
			 * - Setar URI's LDWStepExecution
			 * - Atrelar aos LDWStep os LDWStepExecution
			 */
			
			// Create Uris
			String uriWorkflowExec = StringUtils.createUri(workFlowExec.getName(), workFlowExec.toString());
			workFlowExec.setUri(uriWorkflowExec);
			
			//Step 01
			String uriStepExec01 =  StringUtils.createUri(workFlowExec.getName(), workFlowExec.getLdwStepExecutions().get(0).toString().concat("01"));
			workFlowExec.getLdwStepExecutions().get(0).setUri(uriStepExec01);
			
			//Step 02
			String uriStepExec02 =  StringUtils.createUri(workFlowExec.getName(), workFlowExec.getLdwStepExecutions().get(0).toString().concat("02"));
			workFlowExec.getLdwStepExecutions().get(1).setUri(uriStepExec01);		
			
			//Step 03
			String uriStepExec03 =  StringUtils.createUri(workFlowExec.getName(), workFlowExec.getLdwStepExecutions().get(0).toString().concat("03"));
			workFlowExec.getLdwStepExecutions().get(2).setUri(uriStepExec01);
			
			//Step 04
			String uriStepExec04 =  StringUtils.createUri(workFlowExec.getName(), workFlowExec.getLdwStepExecutions().get(0).toString().concat("04"));
			workFlowExec.getLdwStepExecutions().get(3).setUri(uriStepExec01);
			
			//Step 05
			String uriStepExec05 =  StringUtils.createUri(workFlowExec.getName(), workFlowExec.getLdwStepExecutions().get(0).toString().concat("05"));
			workFlowExec.getLdwStepExecutions().get(4).setUri(uriStepExec01);

		}
		
		if(URIalreadyExists(model, workFlowExec.getUri())) 
			return editLDWorkflowExecution(model, workFlowExec);
		else 
			return insertLDWorkflowExecution(model, workFlowExec, workflow);
	}
	
	
	public OntModel insertLDWorkflowExecution(OntModel model, LDWorkflowExecution l, LDWorkflow w) {
		
		Individual ldworkflow = model.getIndividual(w.getUri());
		Individual ldworkflowexecution = model.getOntClass(ClassURIEnum.LDWORKFLOWEXECUTION.getUri()).createIndividual(l.getUri());
		ldworkflowexecution.addLiteral(model.getProperty(PropertyURIEnum.NAME.getUri()), l.getName());
		ldworkflowexecution.addLiteral(model.getProperty(PropertyURIEnum.DESCRIPTION.getUri()), l.getDescription());
		List<LDWStepExecution> sortedLDWStepExecutions = sortLDWStepExecutions(l.getLdwStepExecutions());
		for(int i=0; i<l.getLdwStepExecutions().size(); i++) {
			ldwStepExecutionService.insertLdwStepExecution(model, l.getLdwStepExecutions().get(i));
			//Adiciona StepExecution como propriedade LDWorkflowExecution
			ldworkflowexecution.addProperty(model.getProperty(PropertyURIEnum.LDWSTEPEXECUTION.getUri()), model.getIndividual(sortedLDWStepExecutions.get(i).getUri()));
			//Adiciona StepExecution como propriedade do Step
			Individual step = model.getIndividual(w.getLdwSteps().get(i).getUri());
			step.addProperty(model.getProperty(PropertyURIEnum.LDWSTEPEXECUTION.getUri()), model.getIndividual(sortedLDWStepExecutions.get(i).getUri()));
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
		ldworkflow.addProperty(model.getProperty(PropertyURIEnum.LDWORKFLOWEXECUTION.getUri()), ldworkflowexecution);
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
		
	public LDWorkflowExecution doNewWorkflowExecutionService(LDWorkflow workflow) {
		LDWorkflowExecution workflowExecution =  new LDWorkflowExecution();
		
		//Step Execution 01
		LDWStepExecution stepExecution01 = new LDWStepExecution();
		
		//Step Execution 02
		LDWStepExecution stepExecution02 = new LDWStepExecution();
		
		//Step Execution 03
		LDWStepExecution stepExecution03 = new LDWStepExecution();
		
		//Step Execution 04
		LDWStepExecution stepExecution04 = new LDWStepExecution();
		
		//Step Execution 05
		LDWStepExecution stepExecution05 = new LDWStepExecution();
		
		//Steps
		List<LDWStepExecution> stepsExecution = new ArrayList<LDWStepExecution>();
		stepsExecution.add(stepExecution01);
		stepsExecution.add(stepExecution02);
		stepsExecution.add(stepExecution03);
		stepsExecution.add(stepExecution04);
		stepsExecution.add(stepExecution05);
		
		workflowExecution.setLdwStepExecutions(stepsExecution);
		return workflowExecution;
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
