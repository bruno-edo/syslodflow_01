package br.ufsc.inf.syslodflow.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.ufsc.inf.syslodflow.entity.LDWStep;
import br.ufsc.inf.syslodflow.entity.LDWorkflow;
import br.ufsc.inf.syslodflow.entity.LDWorkflowExecution;
import br.ufsc.inf.syslodflow.enumerator.ClassURIEnum;
import br.ufsc.inf.syslodflow.enumerator.PropertyURIEnum;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class LdWorkflowService extends BaseService {
	
	@Inject
	private LdwStepService ldwStepService;
	private LdwWorkflowExecutionService ldWorkflowExecutionService;
	
	public LDWorkflow getLDWorkflow(OntModel model, Individual ontLdWorkflow) {
		
		String ldwWorkflowName = getPropertyStringValue(ontLdWorkflow, model, PropertyURIEnum.NAME.getUri());
		String ldwWorkflowDescription = getPropertyStringValue(ontLdWorkflow, model, PropertyURIEnum.DESCRIPTION.getUri());

		Individual ldwWorkflowPreCondition = model.getIndividual(ontLdWorkflow.getPropertyResourceValue(model.getProperty(PropertyURIEnum.PRECONDITION.getUri())).getURI());
		String ldwWorkflowPreConditionDescription = getPropertyStringValue(ldwWorkflowPreCondition, model, PropertyURIEnum.DESCRIPTION.getUri());
		
		Individual ldwWorkflowPostCondition = model.getIndividual(ontLdWorkflow.getPropertyResourceValue(model.getProperty(PropertyURIEnum.POSTCONDITION.getUri())).getURI());
		String ldwWorkflowPostConditionDescription = getPropertyStringValue(ldwWorkflowPostCondition, model, PropertyURIEnum.DESCRIPTION.getUri());
		
		
//		iter = ontLdWorkflow.listProperties(model.getProperty(PropertyURIEnum.LDWORKFLOWEXECUTION.getUri()));
//		List<LDWorkflowExecution> ldWorkflowExecutions = new ArrayList<LDWorkflowExecution>();
//		while (iter.hasNext()){
//			Individual node = (Individual) iter.nextStatement();
//			ldWorkflowExecutions.add(ldWorkflowExecutionService.getLdWorkflowExecution(model, node));
//		}
		
		return null;
		
	}
	
	
	public boolean isFirstLdwStep(OntModel model, Individual ontLdWorkflow, Individual ontLdwStep) {
		
		Individual firstLdwStep = (Individual) ontLdWorkflow.getPropertyValue(model.getProperty(PropertyURIEnum.FIRSTLDWSTEP.getUri()));
		firstLdwStep.equals(ontLdwStep);
		
		
		return true;
	}
	
//	public List<LDWStep> listSteps(OntModel model, Individual ontLdWorkflow) {
//	
//		StmtIterator iter = ontLdWorkflow.listProperties(model.getProperty(PropertyURIEnum.LDWSTEP.getUri()));
//		List<LDWStep> ldwSteps = new ArrayList<LDWStep>();
//		while (iter.hasNext()){
//			Individual node = model.getIndividual(iter.nextStatement().getResource().getURI());
//			ldwSteps.add(ldwStepService.getLdwStep(model, node));
//			if(this.isFirstLdwStep(model, ontLdWorkflow, node) {
//				
//			}
//		}
//			
//	}
	
	

}
