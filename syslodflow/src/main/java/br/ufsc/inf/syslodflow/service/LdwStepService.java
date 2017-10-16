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
import br.ufsc.inf.syslodflow.enumerator.ClassURIEnum;
import br.ufsc.inf.syslodflow.enumerator.PropertyURIEnum;
import br.ufsc.inf.syslodflow.enumerator.StepOrderEnum;
import br.ufsc.inf.syslodflow.enumerator.ToolSupportedEnum;

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
		int order = getOrder(model, ontLdwStep);
		
		Individual ldwStepTask = model.getIndividual(ontLdwStep.getPropertyResourceValue(model.getProperty(PropertyURIEnum.TASK.getUri())).getURI());
		String ldwStepTaskName = getPropertyStringValue(ldwStepTask, model, PropertyURIEnum.NAME.getUri());
		String ldwStepTaskDescription = getPropertyStringValue(ldwStepTask, model, PropertyURIEnum.DESCRIPTION.getUri());
		Task task = new Task(ldwStepTaskName, ldwStepTaskDescription, ldwStepTask.getURI());
		
		StmtIterator iter = ontLdwStep.listProperties(model.getProperty(PropertyURIEnum.INPUTDATASET.getUri()));
		List<Dataset> inputDatasets = new ArrayList<Dataset>();
		while (iter.hasNext()){
			Individual node = model.getIndividual(iter.nextStatement().getResource().getURI());
			inputDatasets.add(this.getDataset(model, node));		
		}
		
		Individual ldwStepOutputDataset = getSubIndividualByProperty(model, ontLdwStep, PropertyURIEnum.OUTPUTDATASET.getUri());
		Dataset outputDataset = this.getDataset(model, ldwStepOutputDataset);
		
		Individual ldwStepTool = getSubIndividualByProperty(model, ontLdwStep, PropertyURIEnum.TOOL.getUri());
		Tool tool = this.getTool(model, ldwStepTool);
		Individual ldwStepToolConfig = getSubIndividualByProperty(model, ontLdwStep, PropertyURIEnum.TOOLCONFIGURATION.getUri());
		ToolConfiguration toolConfig = this.getToolConfiguration(model, ldwStepToolConfig);

		iter = ontLdwStep.listProperties(model.getProperty(PropertyURIEnum.LDWSTEPEXECUTION.getUri()));
		List<LDWStepExecution> ldwStepExecutions = new ArrayList<LDWStepExecution>();
		while (iter.hasNext()){
			Individual node = model.getIndividual(iter.nextStatement().getResource().getURI());
			ldwStepExecutions.add(ldwStepExecutionService.getLdwStepExecution(model, node));		
		}


		return new LDWStep(ldwStepName, ldwStepDescription, ldwStepCommand, task, inputDatasets, outputDataset, tool, toolConfig, ldwStepExecutions, order, ontLdwStep.getURI());

	}

	private Dataset getDataset(OntModel model, Individual ontDataset) {
		
		if(ontDataset !=  null) {
			String dataSetName = getPropertyStringValue(ontDataset, model, PropertyURIEnum.NAME.getUri());

			Individual ontFormat = model.getIndividual(ontDataset.getPropertyResourceValue(model.getProperty(PropertyURIEnum.FORMAT.getUri())).getURI());
			String formatValue = getPropertyStringValue(ontFormat, model, PropertyURIEnum.VALUE.getUri());
			Format format = new Format(formatValue, ontFormat.getURI());

			Individual ontLicense = model.getIndividual(ontDataset.getPropertyResourceValue(model.getProperty(PropertyURIEnum.LICENSE.getUri())).getURI());
			String licenseName = getPropertyStringValue(ontLicense, model, PropertyURIEnum.NAME.getUri());
			License license = new License(licenseName, ontLicense.getURI());

			Individual ontLocation = model.getIndividual(ontDataset.getPropertyResourceValue(model.getProperty(PropertyURIEnum.LOCATION.getUri())).getURI());
			String locationValue = getPropertyStringValue(ontLocation, model, PropertyURIEnum.VALUE.getUri());
			Location location = new Location(locationValue, ontLocation.getURI());

			return new Dataset(dataSetName, format, license, location, ontDataset.getURI());
		}
		return null;

	}

	private Tool getTool(OntModel model, Individual ontTool) {
		if(ontTool != null) {
			String toolName = getPropertyStringValue(ontTool, model, PropertyURIEnum.NAME.getUri());
			StmtIterator iter = ontTool.listProperties(model.getProperty(PropertyURIEnum.TOOLCONFIGURATION.getUri()));
			List<ToolConfiguration> toolConfigurations = new ArrayList<ToolConfiguration>();
			while (iter.hasNext()){
				Individual node = model.getIndividual(iter.nextStatement().getResource().getURI());
				toolConfigurations.add(this.getToolConfiguration(model, node));		
			}
			
			if(ontTool.hasProperty(model.getProperty(PropertyURIEnum.LOCATION.getUri()))) {
			Individual ontLocation = getSubIndividualByProperty(model, ontTool, PropertyURIEnum.LOCATION.getUri());
			String locationValue = getPropertyStringValue(ontLocation, model, PropertyURIEnum.VALUE.getUri());
			return new Tool(toolName, new Location(locationValue, ontLocation.getURI()), ontTool.getURI());
			}
			else {
				return new Tool(toolName, ontTool.getURI());
			}

		}
		return null;


	}

	private ToolConfiguration getToolConfiguration(OntModel model, Individual individual) {

		if(individual != null)  {
			String toolConfigName = getPropertyStringValue(individual, model, PropertyURIEnum.NAME.getUri());
			Individual toolConfigLocation = model.getIndividual(individual.getPropertyResourceValue(model.getProperty(PropertyURIEnum.LOCATION.getUri())).getURI());
			String toolConfiglocationValue = getPropertyStringValue(toolConfigLocation, model, PropertyURIEnum.VALUE.getUri());

			return new ToolConfiguration(toolConfigName, new Location(toolConfiglocationValue, toolConfigLocation.getURI()), individual.getURI());
		}
		return null;

	}
	
	private int getOrder(OntModel model, Individual ontLdwStep) {

		int order;
		if (!ontLdwStep.hasProperty(model.getProperty(PropertyURIEnum.PREVIOUSSTEP.getUri()))) {
			order = StepOrderEnum.FIRST.getOrder();
		}

		else {

			if (!ontLdwStep.hasProperty(model.getProperty(PropertyURIEnum.NEXTSTEP.getUri()))) {
				order = StepOrderEnum.FIFTH.getOrder();
			}

			else {

				if (!ontLdwStep.getPropertyResourceValue(model.getProperty(PropertyURIEnum.NEXTSTEP.getUri())).hasProperty(model.getProperty(PropertyURIEnum.NEXTSTEP.getUri()))) {
					order = StepOrderEnum.FOURTH.getOrder();

				} else {

					if (!ontLdwStep.getPropertyResourceValue(
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
		
	public List<Tool> getListTools(OntModel model) {
		List<Individual> individuals = listIndividuals(model.getOntClass("http://ldwpo.aksw.org/terms/1.0/Tool"));
		List<Tool> toolList = new ArrayList<Tool>();
		for(Individual i: individuals) {
			Tool tool = getTool(model, i);
			toolList.add(tool);
		}
		return toolList;
	}
	
	public List<Tool> getListToolsStep01(OntModel model) {
		List<Tool> generalTools = getListTools(model);
		List<Tool> supportedTools = new ArrayList<Tool>();
		for(Tool t: generalTools) {
			
			if(t.getName().equalsIgnoreCase(ToolSupportedEnum.MYSQL.getName()) ||
					t.getName().equalsIgnoreCase(ToolSupportedEnum.CSV.getName())){
				supportedTools.add(t);
			}
		}
		return supportedTools;
	}
	
	public List<Tool> getListToolsStep02(OntModel model) {
		List<Tool> generalTools = getListTools(model);
		List<Tool> supportedTools = new ArrayList<Tool>();
		for(Tool t: generalTools) {
			if(t.getName().equalsIgnoreCase(ToolSupportedEnum.SPARQLIFY.getName())){
				supportedTools.add(t);
			}
		}
		return supportedTools;
	}
	
	public List<Tool> getListToolsStep03(OntModel model) {
		List<Tool> generalTools = getListTools(model);
		List<Tool> supportedTools = new ArrayList<Tool>();
		for(Tool t: generalTools) {
			if(t.getName().equalsIgnoreCase(ToolSupportedEnum.VIRTUOSO.getName())){
				supportedTools.add(t);
			}
		}
		return supportedTools;
	}
	
	public List<Tool> getListToolsStep04(OntModel model) {
		List<Tool> generalTools = getListTools(model);
		List<Tool> supportedTools = new ArrayList<Tool>();
		for(Tool t: generalTools) {
			if(t.getName().equalsIgnoreCase(ToolSupportedEnum.LIMES.getName())){
				supportedTools.add(t);
			}
		}
		return supportedTools;
	}
	
	public List<Tool> getListToolsStep05(OntModel model) {
		List<Tool> generalTools = getListTools(model);
		List<Tool> supportedTools = new ArrayList<Tool>();
		for(Tool t: generalTools) {
			if(t.getName().equalsIgnoreCase(ToolSupportedEnum.VIRTUOSO.getName())){
				supportedTools.add(t);
			}
		}
		return supportedTools;
	}

	public OntModel insertLdwStep(OntModel model, LDWStep step) {
		
		Individual ldwstep = model.getOntClass(ClassURIEnum.LDWSTEP.getUri()).createIndividual(step.getUri());
		ldwstep.addLiteral(model.getProperty(PropertyURIEnum.NAME.getUri()), step.getName());
		ldwstep.addLiteral(model.getProperty(PropertyURIEnum.DESCRIPTION.getUri()), step.getDescription());
		ldwstep.addLiteral(model.getProperty(PropertyURIEnum.COMMAND.getUri()), step.getCommand());
		ldwstep.addProperty(model.getProperty(PropertyURIEnum.TOOL.getUri()), model.getIndividual(step.getTool().getUri()));
		ldwstep.addProperty(model.getProperty(PropertyURIEnum.TASK.getUri()), model.getIndividual(step.getTask().getUri()));
		
		if(step.getInputDatasets() != null) {
			for(Dataset ds : step.getInputDatasets()) {
				if(URIalreadyExists(model, ds.getUri())) {
					ldwstep.addProperty(model.getProperty(ClassURIEnum.DATASET.getUri()), model.getIndividual(ds.getUri()));
				} else {
					model = this.insertDataset(model, ds);
					ldwstep.addProperty(model.getProperty(PropertyURIEnum.INPUTDATASET.getUri()), model.getIndividual(ds.getUri()));
				}

			}
		}
		
		if(step.getOutputDataset() != null) {
			model = this.insertDataset(model, step.getOutputDataset());
			ldwstep.addProperty(model.getProperty(PropertyURIEnum.OUTPUTDATASET.getUri()), model.getIndividual(step.getOutputDataset().getUri()));
		}
		
		if(step.getToolConfiguration() != null) {
			model = this.insertToolConfiguration(model, step.getToolConfiguration());
			ldwstep.addProperty(model.getProperty(PropertyURIEnum.TOOLCONFIGURATION.getUri()), model.getIndividual(step.getToolConfiguration().getUri()));
		}

		return model;	
	}

	public void editLdwStep(OntModel model, LDWStep step) {
		
		Individual ldwstep = model.getIndividual(step.getUri());
		ldwstep.removeAll(model.getProperty(PropertyURIEnum.NAME.getUri()));
		ldwstep.addLiteral(model.getProperty(PropertyURIEnum.NAME.getUri()), step.getName());
		ldwstep.removeAll(model.getProperty(PropertyURIEnum.DESCRIPTION.getUri()));
		ldwstep.addLiteral(model.getProperty(PropertyURIEnum.DESCRIPTION.getUri()), step.getDescription());
		ldwstep.removeAll(model.getProperty(PropertyURIEnum.COMMAND.getUri()));
		ldwstep.addLiteral(model.getProperty(PropertyURIEnum.COMMAND.getUri()), step.getCommand());
		ldwstep.removeAll(model.getProperty(PropertyURIEnum.TOOL.getUri()));
		ldwstep.addProperty(model.getProperty(PropertyURIEnum.TOOL.getUri()), model.getIndividual(step.getTool().getUri()));
		
		for(Dataset ds : step.getInputDatasets()){
			model = this.editDataset(model, ds);
		}
		model = this.editDataset(model, step.getOutputDataset());
		
		if(step.getToolConfiguration() != null) {
			model = this.editToolConfiguration(model, step.getToolConfiguration());
		}

	}
	
	private OntModel insertDataset(OntModel model, Dataset d) {
		Individual dataset = model.getOntClass(ClassURIEnum.DATASET.getUri()).createIndividual(d.getUri());
		dataset.addLiteral(model.getProperty(PropertyURIEnum.NAME.getUri()), d.getName());
		dataset.addProperty(model.getProperty(PropertyURIEnum.FORMAT.getUri()), model.getIndividual(d.getFormat().getUri()));
		dataset.addProperty(model.getProperty(PropertyURIEnum.LICENSE.getUri()), model.getIndividual(d.getLicense().getUri()));
		Individual location = model.getOntClass(ClassURIEnum.LOCATION.getUri()).createIndividual(d.getLocation().getUri());
		location.addLiteral(model.getProperty(PropertyURIEnum.VALUE.getUri()), d.getLocation().getValue());
		dataset.addProperty(model.getProperty(PropertyURIEnum.LOCATION.getUri()), location);	
		return model;
	}
	
	private OntModel editDataset(OntModel model, Dataset d) {
		
		Individual dataset = model.getIndividual(d.getUri());
		dataset.removeAll(model.getProperty(PropertyURIEnum.NAME.getUri()));
		dataset.addLiteral(model.getProperty(PropertyURIEnum.NAME.getUri()), d.getName());
		
		dataset.removeAll(model.getProperty(PropertyURIEnum.FORMAT.getUri()));
		dataset.addProperty(model.getProperty(PropertyURIEnum.FORMAT.getUri()), model.getIndividual(d.getFormat().getUri()));
		
		dataset.removeAll(model.getProperty(PropertyURIEnum.LICENSE.getUri()));
		dataset.addProperty(model.getProperty(PropertyURIEnum.LICENSE.getUri()), model.getIndividual(d.getLicense().getUri()));
		
		Individual location = model.getIndividual(d.getLocation().getUri());
		location.removeAll(model.getProperty(PropertyURIEnum.VALUE.getUri()));
		location.addLiteral(model.getProperty(PropertyURIEnum.VALUE.getUri()), d.getLocation().getValue());
		
		return model;
	}
	
	private OntModel insertToolConfiguration(OntModel model, ToolConfiguration t) {
		
		Individual toolConfig = model.getOntClass(ClassURIEnum.TOOLCONFIGURATION.getUri()).createIndividual(t.getUri());
		toolConfig.addLiteral(model.getProperty(PropertyURIEnum.NAME.getUri()), t.getName());
		Individual location = model.getOntClass(ClassURIEnum.LOCATION.getUri()).createIndividual(t.getLocation().getUri());
		location.addLiteral(model.getProperty(PropertyURIEnum.VALUE.getUri()), t.getLocation().getValue());
		toolConfig.addProperty(model.getProperty(PropertyURIEnum.LOCATION.getUri()), location);
		return model;
	}
	
	private OntModel editToolConfiguration(OntModel model, ToolConfiguration t) {
		
		Individual toolConfig = model.getIndividual(t.getUri());
		toolConfig.removeAll(model.getProperty(PropertyURIEnum.NAME.getUri()));
		toolConfig.addLiteral(model.getProperty(PropertyURIEnum.NAME.getUri()), t.getName());
		Individual location = model.getIndividual(t.getLocation().getUri());
		location.removeAll(model.getProperty(PropertyURIEnum.VALUE.getUri()));
		location.addLiteral(model.getProperty(PropertyURIEnum.VALUE.getUri()), t.getLocation().getValue());	
		return model;

	}
	
	public OntModel writeTool(OntModel model, Tool t) {
    	
    	if(URIalreadyExists(model, t.getUri()))
    		return insertTool(model, t);
    	else
    		return editTool(model, t);
    }
    
    
	private OntModel editTool(OntModel model, Tool t) {
		
		Individual tool = model.getIndividual(t.getUri());
		tool.removeAll(model.getProperty(PropertyURIEnum.NAME.getUri()));
		tool.addLiteral(model.getProperty(PropertyURIEnum.NAME.getUri()), t.getName());
		Individual location = model.getOntClass(ClassURIEnum.LOCATION.getUri()).createIndividual(t.getLocation().getUri());
		location.removeAll(model.getProperty(PropertyURIEnum.VALUE.getUri()));
		location.addLiteral(model.getProperty(PropertyURIEnum.VALUE.getUri()), t.getLocation().getValue());
		return model;
		
	}
	
	private OntModel insertTool(OntModel model, Tool t) {
		
		Individual tool = model.getOntClass(ClassURIEnum.TOOL.getUri()).createIndividual(t.getUri());
		tool.addLiteral(model.getProperty(PropertyURIEnum.NAME.getUri()), t.getName());
		Individual location = model.getOntClass(ClassURIEnum.LOCATION.getUri()).createIndividual(t.getLocation().getUri());
		location.addLiteral(model.getProperty(PropertyURIEnum.VALUE.getUri()), t.getLocation().getValue());
		tool.addProperty(model.getProperty(PropertyURIEnum.LOCATION.getUri()), location);
		return model;
	}
	
	public OntModel writeFormat(OntModel model, Format f) {
		
		if(URIalreadyExists(model, f.getUri()))
			return editFormat(model, f);
		else
			return insertFormat(model, f);
	}
	
	private OntModel editFormat(OntModel model, Format f) {
	
		Individual format = model.getIndividual(f.getUri());
		format.removeAll(model.getProperty(PropertyURIEnum.VALUE.getUri()));
		format.addLiteral(model.getProperty(PropertyURIEnum.VALUE.getUri()), f.getValue());
		return model;
	}

	private OntModel insertFormat(OntModel model, Format f) {
		
		Individual format = model.getOntClass(ClassURIEnum.FORMAT.getUri()).createIndividual(f.getUri());
		format.addLiteral(model.getProperty(PropertyURIEnum.VALUE.getUri()), f.getValue());
		return model;
	}

	public OntModel writeLicense(OntModel model, License l) {
		
		if(URIalreadyExists(model, l.getUri()))
			return editLicense(model, l);
		else 
			return insertLicense(model, l);
	}
		
	private OntModel editLicense(OntModel model, License l) {
		Individual license = model.getIndividual(l.getUri());
		license.removeAll(model.getProperty(PropertyURIEnum.NAME.getUri()));
		license.addLiteral(model.getProperty(PropertyURIEnum.NAME.getUri()), l.getName());
		return model;
	}

	private OntModel insertLicense(OntModel model, License l) {
		
		Individual license = model.getOntClass(ClassURIEnum.LICENSE.getUri()).createIndividual(l.getUri());
		license.addLiteral(model.getProperty(PropertyURIEnum.NAME.getUri()), l.getName());
		return model;
	}
	
	
	
}
