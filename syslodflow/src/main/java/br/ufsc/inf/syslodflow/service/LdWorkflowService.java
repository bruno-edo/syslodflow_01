	package br.ufsc.inf.syslodflow.service;

import java.util.ArrayList;
import java.util.List;

import javax.faces.context.FacesContext;
import javax.inject.Inject;

import br.ufsc.inf.syslodflow.dto.LDWProjectDTO;
import br.ufsc.inf.syslodflow.entity.Condition;
import br.ufsc.inf.syslodflow.entity.Dataset;
import br.ufsc.inf.syslodflow.entity.Format;
import br.ufsc.inf.syslodflow.entity.LDWStep;
import br.ufsc.inf.syslodflow.entity.LDWorkflow;
import br.ufsc.inf.syslodflow.entity.LDWorkflowExecution;
import br.ufsc.inf.syslodflow.entity.License;
import br.ufsc.inf.syslodflow.entity.Location;
import br.ufsc.inf.syslodflow.entity.Task;
import br.ufsc.inf.syslodflow.enumerator.ClassURIEnum;
import br.ufsc.inf.syslodflow.enumerator.IndividualEnum;
import br.ufsc.inf.syslodflow.enumerator.PropertyURIEnum;
import br.ufsc.inf.syslodflow.util.StringUtils;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class LdWorkflowService extends BaseService {
	
	public static final String FILE_NOT_FOUND = "Arquivo não encontrado";
	public static final String FILE_INVALID_FORMAT = "Formato inválido";
	
	
	@Inject
	private LdwStepService ldwStepService;
	@Inject
	private LdWorkflowExecutionService ldWorkflowExecutionService;
	@Inject 
	private LdwpoService ldwpoService;
	
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
		//List Order
		ldwSteps = sortLDWSteps(ldwSteps);
		
		return new LDWorkflow(ldwWorkflowDescription, ldwWorkflowName, new Condition(ldwWorkflowPreConditionDescription, ldwWorkflowPreCondition.getURI()), 
				new Condition(ldwWorkflowPostConditionDescription, ldwWorkflowPostCondition.getURI()), 
				firstLdwStep, this.listLdWorkflowExecutions(model, ontLdWorkflow) , ldwSteps, ontLdWorkflow.getURI());
	}
	
	public boolean isFirstLdwStep(OntModel model, Individual ontLdWorkflow, Individual ontLdwStep) {
		
		Individual firstLdwStep = model.getIndividual(ontLdWorkflow.getPropertyResourceValue(model.getProperty(PropertyURIEnum.FIRSTLDWSTEP.getUri())).getURI());
		return firstLdwStep.equals(ontLdwStep);
	}
	
	public LDWorkflow doNewWorkflow(OntModel model) {
		// Step 01
		LDWStep step01 = new LDWStep();
		step01.setTask(new Task(IndividualEnum.TASK_EXTRACT_DATA_LEGACYSYS.getUri()));
		
		// Step 02
		LDWStep step02 = new LDWStep();
		step02.setTask(new Task(IndividualEnum.TASK_CONVERTING_DATA.getUri()));
		
		// Step 03
		LDWStep step03 = new LDWStep();
		step03.setTask(new Task(IndividualEnum.TASK_STORAGE_GRAPH.getUri()));
		
		// Step 04
		LDWStep step04 = new LDWStep();
		step04.setTask(new Task(IndividualEnum.TASK_INTERLINKING.getUri()));
		
		// Step 05
		LDWStep step05 = new LDWStep();
		step05.setTask(new Task(IndividualEnum.TASK_STORAGE_GRAPH.getUri()));
		
		// Steps
		List<LDWStep> steps = new ArrayList<LDWStep>();
		steps.add(step01);
		steps.add(step02);
		steps.add(step03);
		steps.add(step04);
		steps.add(step05);
		
		LDWorkflow ldWorkflow = new LDWorkflow();
		ldWorkflow.setLdwSteps(steps);
		
		return ldWorkflow;
		
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
	
	
	public OntModel writeLdwWorkflow(OntModel model, LDWorkflow workflow, LDWProjectDTO ldwProjectDTO) {
		
		Individual ldwprojectIndividual = model.getIndividual(ldwProjectDTO.getUri());
		
				
		if(workflow.getUri() == null) {
			// Create Uris
			String uriWorkflow = StringUtils.createUri(ldwProjectDTO.getName(), workflow.toString());
			workflow.setUri(uriWorkflow);
			
			// Pre condition
			String uriPreCondition = StringUtils.createUri(ldwProjectDTO.getName(), workflow.getPreCondition().toString().concat("_preCondition_").concat(workflow.toString()));
			workflow.getPreCondition().setUri(uriPreCondition);
			
			// Post condition
			String uriPostCondition = StringUtils.createUri(ldwProjectDTO.getName(), workflow.getPostCondition().toString().concat("_postCondition_").concat(workflow.toString()));
			workflow.getPostCondition().setUri(uriPostCondition);
			Location locationDataset = getUriLocationDataset(ldwProjectDTO.getName());
			
			// Step 01
			String uriStep01 =  StringUtils.createUri(ldwProjectDTO.getName(), workflow.getLdwSteps().get(0).toString().concat("01"));
			workflow.getLdwSteps().get(0).setUri(uriStep01);
			
			Dataset datasetCsv = new Dataset();
			String uriDatasetCsv = StringUtils.createUri(ldwProjectDTO.getName(), datasetCsv.toString().concat("_").concat("csv"));
			datasetCsv = new Dataset("dataset.csv", new Format(IndividualEnum.FORMAT_CSV.getUri()), new License(IndividualEnum.NO_LICENSE.getUri()), locationDataset, uriDatasetCsv);
			workflow.getLdwSteps().get(0).setOutputDataset(datasetCsv);
			workflow.setFirstLdwStep(workflow.getLdwSteps().get(0));
			workflow.getLdwSteps().get(0).setCommand("");
			
			//Step 02
			String uriStep02 = StringUtils.createUri(ldwProjectDTO.getName(), workflow.getLdwSteps().get(1).toString().concat("02"));
			workflow.getLdwSteps().get(1).setUri(uriStep02);
			
			Dataset datasetNt = new Dataset();
			String uriDatasetNt = StringUtils.createUri(ldwProjectDTO.getName(), datasetNt.toString().concat("_").concat("nt"));
			datasetNt = new Dataset("dataset.nt", new Format(IndividualEnum.FORMAT_NT.getUri()), new License(IndividualEnum.NO_LICENSE.getUri()), locationDataset, uriDatasetNt);
			workflow.getLdwSteps().get(1).setOutputDataset(datasetNt);
			List<Dataset> inputDatasets02 = new ArrayList<>();
			inputDatasets02.add(datasetCsv);
			workflow.getLdwSteps().get(1).setCommand("");
			
			//Step 03
			String uriStep03 = StringUtils.createUri(ldwProjectDTO.getName(), workflow.getLdwSteps().get(1).toString().concat("03"));
			workflow.getLdwSteps().get(2).setUri(uriStep03);
			
			Dataset inputDataset03 = new Dataset();
			String uriDatasetForSavingVirtuoso = StringUtils.createUri(ldwProjectDTO.getName(), inputDataset03.toString().concat("_forSavingVirtuoso_").concat("nt"));
			inputDataset03 = new Dataset("dataset_forsaving_virtuoso.nt", new Format(IndividualEnum.FORMAT_NT.getUri()), new License(IndividualEnum.NO_LICENSE.getUri()), locationDataset, uriDatasetForSavingVirtuoso);
			List<Dataset> inputDatasets03 = new ArrayList<>();
			workflow.getLdwSteps().get(2).setInputDataset(inputDatasets03);
			
			Dataset outputDataset03 = new Dataset();
			Location locationGraph = getLocationEndpoint(ldwProjectDTO.getName());
			String uriDatasetGraph = StringUtils.createUri(ldwProjectDTO.getName(), outputDataset03.toString().concat("_").concat("graph"));
			outputDataset03 = new Dataset(ldwProjectDTO.getName().concat(" Graph"), new Format(IndividualEnum.FORMAT_RDF_XML.getUri()), new License(IndividualEnum.CC0_LICENSE.getUri()), locationGraph, uriDatasetGraph);
			workflow.getLdwSteps().get(2).setOutputDataset(outputDataset03);
			workflow.getLdwSteps().get(2).setCommand("");
			
	
			//Step 04
			String uriStep04 = StringUtils.createUri(ldwProjectDTO.getName(), workflow.getLdwSteps().get(1).toString().concat("04"));
			workflow.getLdwSteps().get(3).setUri(uriStep04);
			
			Dataset dbPediaDataset = new Dataset(IndividualEnum.DBPEDIA_GRAPH.getUri());
			List<Dataset> inputDatasets04 = new ArrayList<Dataset>();
			inputDatasets04.add(outputDataset03);
			inputDatasets04.add(dbPediaDataset);
			workflow.getLdwSteps().get(3).setInputDataset(inputDatasets04);
			workflow.getLdwSteps().get(3).setOutputDataset(inputDataset03);
			workflow.getLdwSteps().get(3).setCommand("");
			
			
			//Step 05
			String uriStep05 = StringUtils.createUri(ldwProjectDTO.getName(), workflow.getLdwSteps().get(1).toString().concat("05"));
			workflow.getLdwSteps().get(4).setUri(uriStep05);
			List<Dataset> inputDatasets05 = new ArrayList<Dataset>();
			inputDatasets05.add(inputDataset03);
			workflow.getLdwSteps().get(4).setInputDataset(inputDatasets05);
			workflow.getLdwSteps().get(4).setOutputDataset(outputDataset03);
			workflow.getLdwSteps().get(4).setCommand("");

		}

		if(workflow.getUri() != null) {
			if (URIalreadyExists(model, workflow.getUri()))
				return editLdWorkflow(model, workflow);
			else 
				return insertLdWorkflow(model, workflow, ldwprojectIndividual);
		} else {
			return insertLdWorkflow(model, workflow, ldwprojectIndividual);
		}
		
	}
	
	
	
	private OntModel editLdWorkflow(OntModel model, LDWorkflow workflow) {
		
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
		
		List<LDWStep> sortedLDWSteps = sortLDWSteps(workflow.getLdwSteps());
		for(int i=0; i<sortedLDWSteps.size(); i++) {
			ldwStepService.editLdwStep(model, sortedLDWSteps.get(i));
		}
		
		return model;
	}

	private OntModel insertLdWorkflow(OntModel model, LDWorkflow workflow, Individual ldwproject) {
		
		Individual ldworkflow = model.getOntClass(ClassURIEnum.LDWORKFLOW.getUri()).createIndividual(workflow.getUri());
		
		ldworkflow.addLiteral(model.getProperty(PropertyURIEnum.NAME.getUri()), workflow.getName());
		ldworkflow.addLiteral(model.getProperty(PropertyURIEnum.DESCRIPTION.getUri()), workflow.getDescription());
		
		Individual preCondition = model.getOntClass(ClassURIEnum.CONDITION.getUri()).createIndividual(workflow.getPreCondition().getUri());
		preCondition.addLiteral(model.getProperty(PropertyURIEnum.DESCRIPTION.getUri()), workflow.getPreCondition().getDescription());
		ldworkflow.addProperty(model.getProperty(PropertyURIEnum.PRECONDITION.getUri()), preCondition);
		
		Individual postCondition = model.getOntClass(ClassURIEnum.CONDITION.getUri()).createIndividual(workflow.getPostCondition().getUri());
		postCondition.addLiteral(model.getProperty(PropertyURIEnum.DESCRIPTION.getUri()), workflow.getPostCondition().getDescription());
		ldworkflow.addProperty(model.getProperty(PropertyURIEnum.POSTCONDITION.getUri()), postCondition);
		
		List<LDWStep> sortedLDWSteps = workflow.getLdwSteps();
		for(int i=0; i<workflow.getLdwSteps().size(); i++) {
			ldwStepService.insertLdwStep(model, sortedLDWSteps.get(i));
			ldworkflow.addProperty(model.getProperty(PropertyURIEnum.LDWSTEP.getUri()), model.getIndividual(sortedLDWSteps.get(i).getUri()));
		}
		
		ldworkflow.addProperty(model.getProperty(PropertyURIEnum.FIRSTLDWSTEP.getUri()), model.getIndividual(sortedLDWSteps.get(0).getUri()));
		model.getIndividual(sortedLDWSteps.get(0).getUri()).addProperty(model.getProperty(PropertyURIEnum.NEXTSTEP.getUri()), model.getIndividual(sortedLDWSteps.get(1).getUri()));
		model.getIndividual(sortedLDWSteps.get(1).getUri()).addProperty(model.getProperty(PropertyURIEnum.NEXTSTEP.getUri()), model.getIndividual(sortedLDWSteps.get(2).getUri()));
		model.getIndividual(sortedLDWSteps.get(1).getUri()).addProperty(model.getProperty(PropertyURIEnum.PREVIOUSSTEP.getUri()), model.getIndividual(sortedLDWSteps.get(0).getUri()));
		model.getIndividual(sortedLDWSteps.get(2).getUri()).addProperty(model.getProperty(PropertyURIEnum.NEXTSTEP.getUri()), model.getIndividual(sortedLDWSteps.get(3).getUri()));
		model.getIndividual(sortedLDWSteps.get(2).getUri()).addProperty(model.getProperty(PropertyURIEnum.PREVIOUSSTEP.getUri()), model.getIndividual(sortedLDWSteps.get(2).getUri()));
		model.getIndividual(sortedLDWSteps.get(3).getUri()).addProperty(model.getProperty(PropertyURIEnum.NEXTSTEP.getUri()), model.getIndividual(sortedLDWSteps.get(4).getUri()));
		model.getIndividual(sortedLDWSteps.get(3).getUri()).addProperty(model.getProperty(PropertyURIEnum.PREVIOUSSTEP.getUri()), model.getIndividual(sortedLDWSteps.get(2).getUri()));
		model.getIndividual(sortedLDWSteps.get(4).getUri()).addProperty(model.getProperty(PropertyURIEnum.PREVIOUSSTEP.getUri()), model.getIndividual(sortedLDWSteps.get(3).getUri()));
		
		ldwproject.addProperty(model.getProperty(PropertyURIEnum.LDWORKFLOW.getUri()), ldworkflow);
		return model;
	}
	
	private List<LDWStep> sortLDWSteps(List<LDWStep> ldwsteps) {
		List<LDWStep> sortedList = new ArrayList<LDWStep>();
		for(LDWStep s: ldwsteps){
			sortedList.add(s);
		}
		
		for(int i=0; i<ldwsteps.size(); i++) {
			sortedList.set(ldwsteps.get(i).getOrder()-1, ldwsteps.get(i));
		}
		return sortedList;
	}

	public Location getUriLocationDataset(String ldwProjectName) {
		Location location = new Location();
		Dataset dstemp = new Dataset();
		String uriLocation = StringUtils.createUri(ldwProjectName, location.toString().concat("_").concat(dstemp.toString()));
		location.setUri(uriLocation);
		location.setValue(ldwpoService.getProjectsPath(StringUtils.formatName(ldwProjectName)));
		return location;
	}
	
	public Location getLocationEndpoint(String ldwProjectName) {
		FacesContext fc = FacesContext.getCurrentInstance();
		String value = fc.getExternalContext().getInitParameter("endpoint").toString();
		Location location = new Location();
		location.setValue(value);
		String uri = StringUtils.createUri(ldwProjectName, location.toString().concat("_").concat("graph"));
		return new Location(value, uri);
	}
	
	

}
