package br.ufsc.inf.syslodflow.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.ufsc.inf.syslodflow.entity.Condition;
import br.ufsc.inf.syslodflow.entity.LDWProject;
import br.ufsc.inf.syslodflow.entity.LDWStep;
import br.ufsc.inf.syslodflow.entity.LDWorkflow;
import br.ufsc.inf.syslodflow.entity.LDWorkflowExecution;
import br.ufsc.inf.syslodflow.enumerator.ClassURIEnum;
import br.ufsc.inf.syslodflow.enumerator.PropertyURIEnum;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class LdWorkflowService extends BaseService {
	
	@Inject
	private LdwStepService ldwStepService;
	@Inject
	private LdWorkflowExecutionService ldWorkflowExecutionService;
	
	public LDWorkflow getLDWorkflow(OntModel model, Individual ontLdWorkflow) {
		
		String ldwWorkflowName = getPropertyStringValue(ontLdWorkflow, model, PropertyURIEnum.NAME.getUri());
		String ldwWorkflowDescription = getPropertyStringValue(ontLdWorkflow, model, PropertyURIEnum.DESCRIPTION.getUri());
		Individual ldwWorkflowPreCondition = model.getIndividual(ontLdWorkflow.getPropertyResourceValue(model.getProperty(PropertyURIEnum.PRECONDITION.getUri())).getURI());
		String ldwWorkflowPreConditionDescription = getPropertyStringValue(ldwWorkflowPreCondition, model, PropertyURIEnum.DESCRIPTION.getUri());
		Individual ldwWorkflowPostCondition = model.getIndividual(ontLdWorkflow.getPropertyResourceValue(model.getProperty(PropertyURIEnum.POSTCONDITION.getUri())).getURI());
		String ldwWorkflowPostConditionDescription = getPropertyStringValue(ldwWorkflowPostCondition, model, PropertyURIEnum.DESCRIPTION.getUri());
		
		StmtIterator iter = ontLdWorkflow.listProperties(model.getProperty(PropertyURIEnum.LDWSTEP.getUri()));
		List<LDWStep> ldwSteps = new ArrayList<LDWStep>();
		LDWStep firstLdwStep = null;
		while (iter.hasNext()){
			Individual node = model.getIndividual(iter.nextStatement().getResource().getURI());
			if(this.isFirstLdwStep(model, ontLdWorkflow, node)) {
			firstLdwStep = ldwStepService.getLdwStep(model, node);
			}
			ldwSteps.add(ldwStepService.getLdwStep(model, node));		
		}
		
		return new LDWorkflow(ldwWorkflowDescription, ldwWorkflowName, new Condition(ldwWorkflowPreConditionDescription, ldwWorkflowPreCondition.getURI()), 
				new Condition(ldwWorkflowPostConditionDescription, ldwWorkflowPostCondition.getURI()), 
				firstLdwStep, this.listLdWorkflowExecutions(model, ontLdWorkflow) , ldwSteps, ontLdWorkflow.getURI());
	}
	
	public boolean isFirstLdwStep(OntModel model, Individual ontLdWorkflow, Individual ontLdwStep) {
		
		Individual firstLdwStep = model.getIndividual(ontLdWorkflow.getPropertyResourceValue(model.getProperty(PropertyURIEnum.FIRSTLDWSTEP.getUri())).getURI());
		return firstLdwStep.equals(ontLdwStep);
	}
	
	public List<LDWorkflowExecution> listLdWorkflowExecutions(OntModel model, Individual ontLdWorkflow) {
		
		StmtIterator iter = ontLdWorkflow.listProperties(model.getProperty(PropertyURIEnum.LDWORKFLOWEXECUTION.getUri()));
		List<LDWorkflowExecution> ldWorkflowExecutions = new ArrayList<LDWorkflowExecution>();
		while (iter.hasNext()){
			Individual node = model.getIndividual(iter.nextStatement().getResource().getURI());
			ldWorkflowExecutions.add(ldWorkflowExecutionService.getLdwLdWorkflowExecution(model, node));
		}
		
		return ldWorkflowExecutions;
	}
	
	
	public void writeLdwWorkflow(OntModel model, LDWorkflow workflow) {
		
		if (URIalreadyExists(model, workflow.getUri()))
			editLdWorkflow(model, workflow);
		else 
			insertLdWorkflow(model, workflow);
	}
	
	private void editLdWorkflow(OntModel model, LDWorkflow workflow) {
		
		Individual ldworkflow = model.getIndividual(workflow.getUri());
		
		ldworkflow.removeAll(model.getProperty(PropertyURIEnum.NAME.getUri()));
		ldworkflow.addLiteral(model.getProperty(PropertyURIEnum.NAME.getUri()), workflow.getName());
		
		ldworkflow.removeAll(model.getProperty(PropertyURIEnum.DESCRIPTION.getUri()));
		ldworkflow.addLiteral(model.getProperty(PropertyURIEnum.DESCRIPTION.getUri()), workflow.getName());
		
		Individual preCondition = model.getIndividual(ldworkflow.getPropertyResourceValue(model.getProperty(PropertyURIEnum.PRECONDITION.getUri())).getURI());
		preCondition.removeAll(model.getProperty(PropertyURIEnum.DESCRIPTION.getUri()));
		preCondition.addLiteral(model.getProperty(PropertyURIEnum.DESCRIPTION.getUri()), workflow.getPreCondition().getDescription());
		
		Individual postCondition = model.getIndividual(ldworkflow.getPropertyResourceValue(model.getProperty(PropertyURIEnum.POSTCONDITION.getUri())).getURI());
		postCondition.removeAll(model.getProperty(PropertyURIEnum.DESCRIPTION.getUri()));
		postCondition.addLiteral(model.getProperty(PropertyURIEnum.DESCRIPTION.getUri()), workflow.getPreCondition().getDescription());
	}

	private void insertLdWorkflow(OntModel model, LDWorkflow workflow) {
		
		Individual ldworkflow = model.getOntClass(ClassURIEnum.LDWORKFLOW.getUri()).createIndividual(workflow.getUri());
		
		ldworkflow.addLiteral(model.getProperty(PropertyURIEnum.NAME.getUri()), workflow.getName());
		ldworkflow.addLiteral(model.getProperty(PropertyURIEnum.DESCRIPTION.getUri()), workflow.getName());
		
		Individual preCondition = model.getOntClass(ClassURIEnum.CONDITION.getUri()).createIndividual(workflow.getPreCondition().getUri());
		preCondition.addLiteral(model.getProperty(PropertyURIEnum.DESCRIPTION.getUri()), workflow.getPreCondition().getDescription());
		ldworkflow.addProperty(model.getProperty(PropertyURIEnum.PRECONDITION.getUri()), preCondition);
		
		Individual postCondition = model.getOntClass(ClassURIEnum.CONDITION.getUri()).createIndividual(workflow.getPostCondition().getUri());
		preCondition.addLiteral(model.getProperty(PropertyURIEnum.DESCRIPTION.getUri()), workflow.getPostCondition().getDescription());
		ldworkflow.addProperty(model.getProperty(PropertyURIEnum.POSTCONDITION.getUri()), postCondition);
		
		
		
		
		
		
		
	}


	
	

}
