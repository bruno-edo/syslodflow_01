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
		
		Individual report = getSubIndividualByProperty(model, ontLdwWorkFlowExecution, PropertyURIEnum.REPORT.getUri());
		String reportName = getPropertyStringValue(report, model, PropertyURIEnum.NAME.getUri());
		Individual reportLocation = getSubIndividualByProperty(model, report, PropertyURIEnum.LOCATION.getUri());
		String reportLocationValue = getPropertyStringValue(reportLocation, model, PropertyURIEnum.VALUE.getUri());
		String startedDate = getPropertyStringValue(ontLdwWorkFlowExecution, model, PropertyURIEnum.STARTEDDATE.getUri());
		String endedDate = getPropertyStringValue(ontLdwWorkFlowExecution, model, PropertyURIEnum.ENDEDDATE.getUri());

		Report ldWorflowExecutionReport = new Report(reportName, new Location(reportLocationValue, reportLocation.getURI()), report.getURI());
		
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
		
		return new LDWorkflowExecution(firstLdwStepExecution, ldwStepExecutions, ldWorflowExecutionReport, description, name, startedDate, endedDate, ontLdwWorkFlowExecution.getURI());
		
	}

	
	

}
