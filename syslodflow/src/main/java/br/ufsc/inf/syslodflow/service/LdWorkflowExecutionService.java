package br.ufsc.inf.syslodflow.service;

import br.ufsc.inf.syslodflow.entity.LDWorkflowExecution;
import br.ufsc.inf.syslodflow.entity.Location;
import br.ufsc.inf.syslodflow.entity.Report;
import br.ufsc.inf.syslodflow.enumerator.PropertyURIEnum;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;

public class LdWorkflowExecutionService extends BaseService {
	
	public LDWorkflowExecution getLdwLdWorkflowExecution(OntModel model, Individual ontLdwWorkFlowExecution) {
		String description = getPropertyStringValue(ontLdwWorkFlowExecution, model, PropertyURIEnum.DESCRIPTION.getUri());
		String name = getPropertyStringValue(ontLdwWorkFlowExecution, model, PropertyURIEnum.NAME.getUri());
		
		Individual report = model.getIndividual(ontLdwWorkFlowExecution.getPropertyResourceValue
				(model.getProperty(PropertyURIEnum.REPORT.getUri())).getURI());
		String reportName = getPropertyStringValue(report, model, PropertyURIEnum.NAME.getUri());
		Individual reportLocation = model.getIndividual(report.getPropertyResourceValue(model.getProperty(PropertyURIEnum.LOCATION.getUri())).getURI());
		String reportLocationValue = getPropertyStringValue(reportLocation, model, PropertyURIEnum.VALUE.getUri());

		Report ldWorflowExecutionReport = new Report();
		ldWorflowExecutionReport.setName(reportName);
		Location ldWorflowExecutionReportLocation = new Location();
		ldWorflowExecutionReportLocation.setValue(reportLocationValue);
		ldWorflowExecutionReport.setLocation(ldWorflowExecutionReportLocation);

		LDWorkflowExecution ldwWorflowExecution = new LDWorkflowExecution();
		ldwWorflowExecution.setName(name);
		ldwWorflowExecution.setDescription(description);
		ldwWorflowExecution.setReport(ldWorflowExecutionReport);
		return ldwWorflowExecution;
	}

	
	

}
