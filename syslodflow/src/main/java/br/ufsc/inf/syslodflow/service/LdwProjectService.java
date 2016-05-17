package br.ufsc.inf.syslodflow.service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.ufsc.inf.syslodflow.dto.LDWProjectDTO;
import br.ufsc.inf.syslodflow.entity.Homepage;
import br.ufsc.inf.syslodflow.entity.LDWProject;
import br.ufsc.inf.syslodflow.entity.LDWorkflow;
import br.ufsc.inf.syslodflow.entity.Location;
import br.ufsc.inf.syslodflow.entity.Person;
import br.ufsc.inf.syslodflow.entity.Report;
import br.ufsc.inf.syslodflow.enumerator.ClassURIEnum;
import br.ufsc.inf.syslodflow.enumerator.PropertyURIEnum;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Resource;

public class LdwProjectService extends BaseService {
	
	@Inject
	private LdwpoService ldwpoService;
	@Inject
	private LdWorkflowService ldwWorkflowService;
	
	public LDWProjectDTO getLDWProjectDTO(OntModel model) {
		
		Individual ontProject = listIndividuals(model.getOntClass(ClassURIEnum.LDWPROJECT.getUri())).get(0);
		String projectName = getPropertyStringValue(ontProject, model, PropertyURIEnum.NAME.getUri());
		
		Individual creator = model.getIndividual(ontProject.getPropertyResourceValue(model.getProperty(PropertyURIEnum.CREATOR.getUri())).getURI());
		String creatorName = getPropertyStringValue(creator, model, PropertyURIEnum.NAME.getUri());
				
		return new LDWProjectDTO(projectName, creatorName);
		
	}
	
	/**
	 * Retorna lista de Projetos contidos no filePath do sistema
	 * @return listProjects
	 */
	public List<LDWProjectDTO> getListLdwProjectDTO() {
		List<Path> listFiles = this.ldwpoService.getOntologyFiles();
		List<LDWProjectDTO> listProjects = new ArrayList<LDWProjectDTO>();
		
		for(Path p : listFiles) {
			OntModel model = ldwpoService.doLoadModel(p);
			LDWProjectDTO dto = getLDWProjectDTO(model);
			dto.setPath(p);
			dto.setFileName(p.getFileName().toString());
			listProjects.add(dto);
		}
		return listProjects;
	}

	public LDWProject getLDWProject(OntModel model) {
		
		Individual ontProject = listIndividuals(model.getOntClass(ClassURIEnum.LDWPROJECT.getUri())).get(0);
		
		String ldwProjectName = getPropertyStringValue(ontProject, model, PropertyURIEnum.NAME.getUri());
		String ldwProjectDescription = getPropertyStringValue(ontProject, model, PropertyURIEnum.DESCRIPTION.getUri());
		String ldwProjectGoal = getPropertyStringValue(ontProject, model, PropertyURIEnum.GOAL.getUri());
		Individual creator = model.getIndividual(ontProject.getPropertyResourceValue(model.getProperty(PropertyURIEnum.CREATOR.getUri())).getURI());
		String creatorName = getPropertyStringValue(creator, model, PropertyURIEnum.NAME.getUri());
		Individual homepage = model.getIndividual(ontProject.getPropertyResourceValue(model.getProperty(PropertyURIEnum.HOMEPAGE.getUri())).getURI());
		String homepageName = getPropertyStringValue(homepage, model, PropertyURIEnum.NAME.getUri());
		Individual homepageLocation = model.getIndividual(homepage.getPropertyResourceValue(model.getProperty(PropertyURIEnum.LOCATION.getUri())).getURI());
		String homepageLocationValue = getPropertyStringValue(homepageLocation, model, PropertyURIEnum.VALUE.getUri());
		Individual report = model.getIndividual(ontProject.getPropertyResourceValue(model.getProperty(PropertyURIEnum.REPORT.getUri())).getURI());
		String reportName = getPropertyStringValue(report, model, PropertyURIEnum.NAME.getUri());
		Individual reportLocation = model.getIndividual(report.getPropertyResourceValue(model.getProperty(PropertyURIEnum.LOCATION.getUri())).getURI());
		String reportLocationValue = getPropertyStringValue(reportLocation, model, PropertyURIEnum.VALUE.getUri());
		
		Resource workflow = ontProject.getPropertyResourceValue(model.getProperty(PropertyURIEnum.LDWORKFLOW.getUri()));
		
		LDWorkflow ldWorkflow = ldwWorkflowService.getLDWorkflow(model, model.getIndividual(workflow.getURI()));
		
		Person ldwProjectCreator = new Person();
		ldwProjectCreator.setName(creatorName);
		
		Homepage ldwProjectHomepage = new Homepage();
		ldwProjectHomepage.setName(homepageName);
		Location ldwProjectHomepageLocation = new Location();
		ldwProjectHomepageLocation.setValue(homepageLocationValue);
		ldwProjectHomepage.setLocation(ldwProjectHomepageLocation);
		
		Report ldwProjectReport = new Report();
		ldwProjectReport.setName(reportName);
		Location ldwProjectReportLocation = new Location();
		ldwProjectReportLocation.setValue(reportLocationValue);
		ldwProjectReport.setLocation(ldwProjectReportLocation);
		
		
		LDWProject ldwProject = new LDWProject();
		ldwProject.setName(ldwProjectName);
		ldwProject.setDescription(ldwProjectDescription);
		ldwProject.setGoal(ldwProjectGoal);
		ldwProject.setCreator(ldwProjectCreator);
		ldwProject.setHomePage(ldwProjectHomepage);
		ldwProject.setReport(ldwProjectReport);
		ldwProject.setLdWorkFlow(ldWorkflow);
		
		return ldwProject;
		
	}
	
	public OntModel saveLdwProject(OntModel model, LDWProject project) {
		return model;
	}
	
	public void writeLdwProject(OntModel model, LDWProject project) {
		
		if (URIalreadyExists(model, project.getUri()))
			editLdwProject(model, project);
		else 
			insertLdwProject(model, project);
	}
	
	private void editLdwProject(OntModel model, LDWProject project) {
		
	Individual ldwProject = model.getIndividual(project.getUri());
	ldwProject.removeAll(model.getProperty(PropertyURIEnum.NAME.getUri()));
	ldwProject.removeAll(model.getProperty(PropertyURIEnum.GOAL.getUri()));
	ldwProject.removeAll(model.getProperty(PropertyURIEnum.DESCRIPTION.getUri()));
	
	ldwProject.addLiteral(model.getProperty(PropertyURIEnum.NAME.getUri()), project.getName());
	ldwProject.addLiteral(model.getProperty(PropertyURIEnum.GOAL.getUri()), project.getGoal());
	ldwProject.addLiteral(model.getProperty(PropertyURIEnum.DESCRIPTION.getUri()), project.getDescription());
	
	// Verifica existência de Criador
	if(URIalreadyExists(model, project.getCreator().getUri())) {
		Individual ldwProjectCreator = model.getIndividual(ldwProject.getPropertyResourceValue(model.getProperty(PropertyURIEnum.CREATOR.getUri())).getURI());
		ldwProject.removeAll(model.getProperty(PropertyURIEnum.CREATOR.getUri()));
		ldwProject.addProperty(model.getProperty(PropertyURIEnum.CREATOR.getUri()), ldwProjectCreator);
	}
	else {
		Individual ldwProjectCreator = model.getOntClass(ClassURIEnum.PERSON.getUri()).createIndividual(project.getCreator().getUri());
		ldwProjectCreator.addLiteral(model.getProperty(PropertyURIEnum.NAME.getUri()), project.getCreator().getName());
		ldwProject.addProperty(model.getProperty(PropertyURIEnum.CREATOR.getUri()), ldwProjectCreator);
	}
	
	// Verifica existência de Homepage
	if(URIalreadyExists(model, project.getHomePage().getUri())) {
		Individual ldwProjectHomepage = model.getIndividual(ldwProject.getPropertyResourceValue(model.getProperty(PropertyURIEnum.HOMEPAGE.getUri())).getURI());
		Individual ldwProjectHomepageLocation = model.getIndividual(ldwProjectHomepage.getPropertyResourceValue(model.getProperty(PropertyURIEnum.LOCATION.getUri())).getURI());
		ldwProjectHomepageLocation.removeAll(model.getProperty(PropertyURIEnum.VALUE.getUri()));
		ldwProjectHomepageLocation.addLiteral(model.getProperty(PropertyURIEnum.VALUE.getUri()), project.getHomePage().getLocation().getValue());
	}
	else {
		Individual ldwProjectHomepage = model.getOntClass(ClassURIEnum.HOMEPAGE.getUri()).createIndividual(project.getHomePage().getUri());
		ldwProjectHomepage.addLiteral(model.getProperty(PropertyURIEnum.NAME.getUri()), project.getHomePage().getName());
		Individual ldwProjectHomepageLocation = model.getOntClass(ClassURIEnum.LOCATION.getUri()).createIndividual(project.getHomePage().getLocation().getUri());
		ldwProjectHomepageLocation.addLiteral(model.getProperty(PropertyURIEnum.VALUE.getUri()), project.getHomePage().getLocation().getValue());
		ldwProjectHomepage.addProperty(model.getProperty(PropertyURIEnum.LOCATION.getUri()), ldwProjectHomepageLocation);
		ldwProject.addProperty(model.getProperty(PropertyURIEnum.HOMEPAGE.getUri()), ldwProjectHomepage);
	}
	
	// Verifica existência de Report
	
	if(URIalreadyExists(model, project.getReport().getUri())) {
		Individual ldwProjectReport = model.getIndividual(ldwProject.getPropertyResourceValue(model.getProperty(PropertyURIEnum.REPORT.getUri())).getURI());
		ldwProjectReport.removeAll(model.getProperty(PropertyURIEnum.NAME.getUri()));
		Individual ldwProjectReportLocation = model.getIndividual(ldwProjectReport.getPropertyResourceValue(model.getProperty(PropertyURIEnum.LOCATION.getUri())).getURI());
		ldwProjectReportLocation.removeAll(model.getProperty(PropertyURIEnum.VALUE.getUri()));
		ldwProject.addLiteral(model.getProperty(PropertyURIEnum.NAME.getUri()), project.getReport().getName());
		ldwProjectReportLocation.addLiteral(model.getProperty(PropertyURIEnum.VALUE.getUri()), project.getReport().getLocation().getValue());
		
	}
	else
	{
		Individual ldwProjectReport = model.getOntClass(ClassURIEnum.REPORT.getUri()).createIndividual(project.getReport().getUri());
		ldwProjectReport.addLiteral(model.getProperty(PropertyURIEnum.NAME.getUri()), project.getReport().getName());
		Individual ldwProjectReportLocation = model.getOntClass(ClassURIEnum.LOCATION.getUri()).createIndividual(project.getReport().getLocation().getUri());
		ldwProjectReportLocation.addLiteral(model.getProperty(PropertyURIEnum.VALUE.getUri()), project.getReport().getLocation().getValue());
		ldwProjectReport.addProperty(model.getProperty(PropertyURIEnum.LOCATION.getUri()), ldwProjectReportLocation);
		ldwProject.addProperty(model.getProperty(PropertyURIEnum.REPORT.getUri()), ldwProjectReport);
		
	}
	
	
	
	
	
	
	
	
	
	
	
	//recuperar objeto
	// deletar valores de propriedades ja contidas
	// inserir novos valores de propriedades
	
		
	}

	private void insertLdwProject(OntModel model, LDWProject project) {
		
	}
	
	
}
