package br.ufsc.inf.syslodflow.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.ufsc.inf.syslodflow.entity.LDWStepExecution;
import br.ufsc.inf.syslodflow.entity.LDWorkflowExecution;
import br.ufsc.inf.syslodflow.entity.Location;
import br.ufsc.inf.syslodflow.entity.Report;
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
		
		Individual report = model.getIndividual(ontLdwWorkFlowExecution.getPropertyResourceValue
				(model.getProperty(PropertyURIEnum.REPORT.getUri())).getURI());
		String reportName = getPropertyStringValue(report, model, PropertyURIEnum.NAME.getUri());
		Individual reportLocation = model.getIndividual(report.getPropertyResourceValue(model.getProperty(PropertyURIEnum.LOCATION.getUri())).getURI());
		String reportLocationValue = getPropertyStringValue(reportLocation, model, PropertyURIEnum.VALUE.getUri());
		String startedDate = getPropertyStringValue(ontLdwWorkFlowExecution, model, PropertyURIEnum.STARTEDDATE.getUri());
		String endedDate = getPropertyStringValue(ontLdwWorkFlowExecution, model, PropertyURIEnum.ENDEDDATE.getUri());

		Report ldWorflowExecutionReport = new Report();
		ldWorflowExecutionReport.setName(reportName);
		Location ldWorflowExecutionReportLocation = new Location();
		ldWorflowExecutionReportLocation.setValue(reportLocationValue);
		ldWorflowExecutionReport.setLocation(ldWorflowExecutionReportLocation);
		
		StmtIterator iter = ontLdwWorkFlowExecution.listProperties(model.getProperty(PropertyURIEnum.LDWSTEPEXECUTION.getUri()));
		List<LDWStepExecution> ldwStepExecutions = new ArrayList<LDWStepExecution>();
		while (iter.hasNext()){
			Individual node = model.getIndividual(iter.nextStatement().getResource().getURI());
			ldwStepExecutions.add(ldwStepExecutionService.getLdwStepExecution(model, node));		
		}
		
		
		LDWorkflowExecution ldwWorflowExecution = new LDWorkflowExecution();
		ldwWorflowExecution.setName(name);
		ldwWorflowExecution.setDescription(description);
		ldwWorflowExecution.setReport(ldWorflowExecutionReport);
		ldwWorflowExecution.setStartedDate(startedDate);
		ldwWorflowExecution.setEndedDate(endedDate);
		ldwWorflowExecution.setLdwStepExecutions(ldwStepExecutions);
		
		return ldwWorflowExecution;
	}

	
	

}
