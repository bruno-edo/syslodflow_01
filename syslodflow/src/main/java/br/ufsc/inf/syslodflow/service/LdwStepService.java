package br.ufsc.inf.syslodflow.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.primefaces.model.UploadedFile;

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
		Task task = new Task(ldwStepTaskName, ldwStepTaskDescription);
		
		//InputDataSet
		Individual ldwStepInputDataset = getSubIndividualByProperty(model, ontLdwStep, PropertyURIEnum.INPUTDATASET.getUri());
		Dataset inputDataset = this.getDataset(model, ldwStepInputDataset);
		
		//OutuputDataSet
		Individual ldwStepOutputDataset = getSubIndividualByProperty(model, ontLdwStep, PropertyURIEnum.OUTPUTDATASET.getUri());
		Dataset outputDataset = this.getDataset(model, ldwStepOutputDataset);
		
		// Tool
		Individual ldwStepTool = getSubIndividualByProperty(model, ontLdwStep, PropertyURIEnum.TOOL.getUri());
		Tool tool = this.getTool(model, ldwStepTool);
		
		Individual ldwStepToolConfig = getSubIndividualByProperty(model, ontLdwStep, PropertyURIEnum.TOOLCONFIGURATION.getUri());
		ToolConfiguration toolConfig = this.getToolConfiguration(model, ldwStepToolConfig);

		StmtIterator iter = ontLdwStep.listProperties(model.getProperty(PropertyURIEnum.LDWSTEPEXECUTION.getUri()));
		List<LDWStepExecution> ldwStepExecutions = new ArrayList<LDWStepExecution>();
		while (iter.hasNext()){
			Individual node = model.getIndividual(iter.nextStatement().getResource().getURI());
			ldwStepExecutions.add(ldwStepExecutionService.getLdwStepExecution(model, node));		
		}


		return new LDWStep(ldwStepName, ldwStepDescription, ldwStepCommand, task, inputDataset, outputDataset, tool, toolConfig, ldwStepExecutions, order);

	}

	private Dataset getDataset(OntModel model, Individual ontDataset) {
		
		if(ontDataset !=  null) {
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
		return null;

	}

	private Tool getTool(OntModel model, Individual ontTool) {
		if(ontTool != null) {
			String toolName = getPropertyStringValue(ontTool, model, PropertyURIEnum.NAME.getUri());
			Individual ontLocation = getSubIndividualByProperty(model, ontTool, PropertyURIEnum.LOCATION.getUri());
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

		if(individual != null)  {
			String toolConfigName = getPropertyStringValue(individual, model, PropertyURIEnum.NAME.getUri());
			Individual toolConfigLocation = model.getIndividual(individual.getPropertyResourceValue(model.getProperty(PropertyURIEnum.LOCATION.getUri())).getURI());
			String toolConfiglocationValue = getPropertyStringValue(toolConfigLocation, model, PropertyURIEnum.VALUE.getUri());

			return new ToolConfiguration(toolConfigName, new Location(toolConfiglocationValue));
		}
		return null;

	}
	
	private int getOrder(OntModel model, Individual ontLdwStep) {

		int order;
		if (!ontLdwStep.hasProperty(model
				.getProperty(PropertyURIEnum.PREVIOUSSTEP.getUri()))) {
			order = StepOrderEnum.FIRST.getOrder();
		}

		else {

			if (!ontLdwStep.hasProperty(model
					.getProperty(PropertyURIEnum.NEXTSTEP.getUri()))) {
				order = StepOrderEnum.FIFTH.getOrder();
			}

			else {

				if (!ontLdwStep.getPropertyResourceValue(
						model.getProperty(PropertyURIEnum.NEXTSTEP.getUri()))
						.hasProperty(
								model.getProperty(PropertyURIEnum.NEXTSTEP
										.getUri()))) {
					order = StepOrderEnum.FOURTH.getOrder();

				} else {

					if (ontLdwStep.getPropertyResourceValue(
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
	
	
	public String saveFile(UploadedFile file, String projectName) {
		FacesContext fc = FacesContext.getCurrentInstance();
		String filePath = fc.getExternalContext().getInitParameter("filePath").toString();
		filePath = filePath + "\\" + projectName;
		String fileName = file.getFileName();
		String extensao = "";
		
		try {
			File targetFolder = new File(filePath);
			if (!targetFolder.exists()) {
				targetFolder.mkdirs();
			} 
			InputStream inputStream = file.getInputstream();
			OutputStream out = new FileOutputStream(new File(targetFolder, fileName));
			int read = 0;
			byte[] bytes = new byte[1024];
			while ((read = inputStream.read(bytes)) != -1) {
				out.write(bytes, 0, read);
			}
			inputStream.close();
			out.flush();
			out.close();
			return fileName + "." + extensao;
		} catch (IOException e) {
			e.printStackTrace();
			return "Erro ao salvar";
		}
	}
	


}
