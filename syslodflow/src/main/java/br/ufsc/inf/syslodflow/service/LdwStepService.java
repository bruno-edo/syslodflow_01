package br.ufsc.inf.syslodflow.service;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.ufsc.inf.syslodflow.entity.Dataset;
import br.ufsc.inf.syslodflow.entity.Format;
import br.ufsc.inf.syslodflow.entity.LDWStep;
import br.ufsc.inf.syslodflow.entity.LDWStepExecution;
import br.ufsc.inf.syslodflow.entity.License;
import br.ufsc.inf.syslodflow.entity.Location;
import br.ufsc.inf.syslodflow.entity.Task;
import br.ufsc.inf.syslodflow.entity.Tool;
import br.ufsc.inf.syslodflow.entity.ToolConfiguration;
import br.ufsc.inf.syslodflow.enumerator.PropertyURIEnum;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class LdwStepService extends BaseService {

	@Inject 
	LdwStepExecutionService ldwStepExecutionService;

	public LDWStep getLdwStep(OntModel model, Individual ontLdwStep) {

		String ldwStepName = getPropertyStringValue(ontLdwStep, model, PropertyURIEnum.NAME.getUri());
		String ldwStepDescription = getPropertyStringValue(ontLdwStep, model, PropertyURIEnum.DESCRIPTION.getUri());
		String ldwStepCommand = getPropertyStringValue(ontLdwStep, model, PropertyURIEnum.COMMAND.getUri());
		Individual ldwStepTask = model.getIndividual(ontLdwStep.getPropertyResourceValue(model.getProperty(PropertyURIEnum.TASK.getUri())).getURI());
		String ldwStepTaskName = getPropertyStringValue(ldwStepTask, model, PropertyURIEnum.NAME.getUri());
		String ldwStepTaskDescription = getPropertyStringValue(ldwStepTask, model, PropertyURIEnum.DESCRIPTION.getUri());
		Task task = new Task(ldwStepTaskName, ldwStepTaskDescription);
		Individual ldwStepInputDataset = model.getIndividual(ontLdwStep.getPropertyResourceValue(model.getProperty(PropertyURIEnum.INPUTDATASET.getUri())).getURI());
		Dataset inputDataset = this.getDataset(model, ldwStepInputDataset);
		Individual ldwStepOutputDataset = model.getIndividual(ontLdwStep.getPropertyResourceValue(model.getProperty(PropertyURIEnum.OUTPUTDATASET.getUri())).getURI());
		Dataset outputDataset = this.getDataset(model, ldwStepOutputDataset);
		
		// LDWStep
		Individual ldwStepTool = getSubIndividualByProperty(model, ontLdwStep, PropertyURIEnum.TOOL.getUri());
		Tool tool = this.getTool(model, ldwStepTool);
		Individual ldwStepToolConfig = model.getIndividual(ontLdwStep.getPropertyResourceValue(model.getProperty(PropertyURIEnum.TOOLCONFIGURATION.getUri())).getURI());
		ToolConfiguration toolConfig = this.getToolConfiguration(model, ldwStepToolConfig);

		StmtIterator iter = ontLdwStep.listProperties(model.getProperty(PropertyURIEnum.LDWSTEPEXECUTION.getUri()));
		List<LDWStepExecution> ldwStepExecutions = new ArrayList<LDWStepExecution>();
		while (iter.hasNext()){
			Individual node = model.getIndividual(iter.nextStatement().getResource().getURI());
			ldwStepExecutions.add(ldwStepExecutionService.getLdwStepExecution(model, node));		
		}


		return new LDWStep(ldwStepName, ldwStepDescription, ldwStepCommand, task, inputDataset, outputDataset, tool, toolConfig, ldwStepExecutions);

	}

	private Dataset getDataset(OntModel model, Individual ontDataset) {

		String dataSetName = getPropertyStringValue(ontDataset, model, PropertyURIEnum.NAME.getUri());

		Individual ontFormat = model.getIndividual(ontDataset.getPropertyResourceValue(model.getProperty(PropertyURIEnum.FORMAT.getUri())).getURI());
		String formatValue = getPropertyStringValue(ontFormat, model, PropertyURIEnum.VALUE.getUri());
		Format format = new Format(formatValue);

		Individual ontLicense = model.getIndividual(ontDataset.getPropertyResourceValue(model.getProperty(PropertyURIEnum.LICENSE.getUri())).getURI());
		String licenseName = getPropertyStringValue(ontLicense, model, PropertyURIEnum.NAME.getUri());
		License license = new License(licenseName);

		Individual ontLocation = model.getIndividual(ontDataset.getPropertyResourceValue(model.getProperty(PropertyURIEnum.LOCATION.getUri())).getURI());
		String locationValue = getPropertyStringValue(ontLocation, model, PropertyURIEnum.VALUE.getUri());
		Location location = new Location(locationValue);

		return new Dataset(dataSetName, format, license, location);

	}

	private Tool getTool(OntModel model, Individual ontTool) {
		if(ontTool != null) {
			String toolName = getPropertyStringValue(ontTool, model, PropertyURIEnum.NAME.getUri());
			Individual ontLocation = model.getIndividual(ontTool.getPropertyResourceValue(model.getProperty(PropertyURIEnum.LOCATION.getUri())).getURI());
			String locationValue = getPropertyStringValue(ontLocation, model, PropertyURIEnum.VALUE.getUri());

			StmtIterator iter = ontTool.listProperties(model.getProperty(PropertyURIEnum.TOOLCONFIGURATION.getUri()));
			List<ToolConfiguration> toolConfigurations = new ArrayList<ToolConfiguration>();
			while (iter.hasNext()){
				Individual node = model.getIndividual(iter.nextStatement().getResource().getURI());
				toolConfigurations.add(this.getToolConfiguration(model, node));		
			}

			return new Tool(toolName, new Location(locationValue), toolConfigurations);
		}
		return null;


	}

	private ToolConfiguration getToolConfiguration(OntModel model, Individual individual) {

		String toolConfigName = getPropertyStringValue(individual, model, PropertyURIEnum.NAME.getUri());
		Individual toolConfigLocation = model.getIndividual(individual.getPropertyResourceValue(model.getProperty(PropertyURIEnum.LOCATION.getUri())).getURI());
		String toolConfiglocationValue = getPropertyStringValue(toolConfigLocation, model, PropertyURIEnum.VALUE.getUri());

		return new ToolConfiguration(toolConfigName, new Location(toolConfiglocationValue));



	}



}
