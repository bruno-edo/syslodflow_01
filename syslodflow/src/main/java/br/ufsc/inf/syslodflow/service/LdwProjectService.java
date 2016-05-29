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
import br.ufsc.inf.syslodflow.enumerator.NSURIEnum;
import br.ufsc.inf.syslodflow.enumerator.PropertyURIEnum;
import br.ufsc.inf.syslodflow.util.StringUtils;

import com.hp.hpl.jena.ontology.Individual;
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
		
		Person ldwProjectCreator = new Person(creatorName, creator.getURI());
		Homepage ldwProjectHomepage = new Homepage(homepageName, new Location(homepageLocationValue, homepageLocation.getURI()), homepage.getURI());
		Report ldwProjectReport = new Report(reportName, new Location(reportLocationValue, reportLocation.getURI()), report.getURI());
		
		return new LDWProject(ldwProjectName, ldwProjectDescription, ldwProjectGoal, ldwProjectCreator,
				ldwProjectHomepage, ldwProjectReport, ldWorkflow, ontProject.getURI());
		
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
		ldwProject.addLiteral(model.getProperty(PropertyURIEnum.NAME.getUri()),project.getName());
		ldwProject.addLiteral(model.getProperty(PropertyURIEnum.GOAL.getUri()),project.getGoal());
		ldwProject.addLiteral(model.getProperty(PropertyURIEnum.DESCRIPTION.getUri()),project.getDescription());
		ldwProject.addProperty(model.getProperty(PropertyURIEnum.CREATOR.getUri()), model.getIndividual(project.getCreator().getUri()));
		writeHomepage(model, project.getHomePage());
		ldwProject.removeAll(model.getProperty(PropertyURIEnum.HOMEPAGE.getUri()));
		ldwProject.addProperty(model.getProperty(PropertyURIEnum.HOMEPAGE.getUri()), model.getIndividual(project.getHomePage().getUri()));
		writeReport(model, project.getReport());
		ldwProject.removeAll(model.getProperty(PropertyURIEnum.REPORT.getUri()));
		ldwProject.addProperty(model.getProperty(PropertyURIEnum.REPORT.getUri()),model.getIndividual(project.getReport().getUri()));
		}

	private void insertLdwProject(OntModel model, LDWProject project) {
	
		Individual ldwProject = model.getOntClass(ClassURIEnum.LDWPROJECT.getUri()).createIndividual(project.getUri());
		ldwProject.addLiteral(model.getProperty(PropertyURIEnum.NAME.getUri()),project.getName());
		ldwProject.addLiteral(model.getProperty(PropertyURIEnum.GOAL.getUri()),project.getGoal());
		ldwProject.addLiteral(model.getProperty(PropertyURIEnum.DESCRIPTION.getUri()),project.getDescription());
		ldwProject.addProperty(model.getProperty(PropertyURIEnum.CREATOR.getUri()), model.getIndividual(project.getCreator().getUri()));
		writeHomepage(model, project.getHomePage());
		ldwProject.addProperty(model.getProperty(PropertyURIEnum.HOMEPAGE.getUri()), model.getIndividual(project.getHomePage().getUri()));
		writeReport(model, project.getReport());
		ldwProject.addProperty(model.getProperty(PropertyURIEnum.REPORT.getUri()),model.getIndividual(project.getReport().getUri()));
		
		
	}
	
	private void writeReport(OntModel model, Report r) {
		
		if (URIalreadyExists(model, r.getUri()))
			editReport(model, r);
		else 
			insertReport(model, r);
	}
	
	private void insertReport(OntModel model, Report r) {
		
		Individual report = model.getOntClass(ClassURIEnum.REPORT.getUri()).createIndividual(r.getUri());
		report.addLiteral(model.getProperty(PropertyURIEnum.NAME.getUri()), r.getName());
		Individual location = model.getOntClass(ClassURIEnum.LOCATION.getUri()).createIndividual(r.getLocation().getUri());
		location.addLiteral(model.getProperty(PropertyURIEnum.VALUE.getUri()), r.getLocation().getValue());
		report.addProperty(model.getProperty(PropertyURIEnum.LOCATION.getUri()),location);
		
	}

	private void editReport(OntModel model, Report r) {
		
		Individual report = model.getIndividual(r.getUri());
		report.removeAll(model.getProperty(PropertyURIEnum.NAME.getUri()));
		report.addLiteral(model.getProperty(PropertyURIEnum.NAME.getUri()), r.getName());
		Individual location = model.getIndividual(r.getLocation().getUri());
		location.removeAll(model.getProperty(PropertyURIEnum.VALUE.getUri()));
		location.addLiteral(model.getProperty(PropertyURIEnum.VALUE.getUri()), r.getLocation().getValue());
	}

	private void writeHomepage(OntModel model, Homepage h) {
		
		if (URIalreadyExists(model, h.getUri()))
			editHomepage(model, h);
		else 
			insertHomepage(model, h);
	}
	
	private void insertHomepage(OntModel model, Homepage h) {
		
		Individual homepage = model.getOntClass(ClassURIEnum.HOMEPAGE.getUri()).createIndividual(h.getUri());
		homepage.addLiteral(model.getProperty(PropertyURIEnum.NAME.getUri()), h.getName());
		Individual location = model.getOntClass(ClassURIEnum.LOCATION.getUri()).createIndividual(h.getLocation().getUri());
		location.addLiteral(model.getProperty(PropertyURIEnum.VALUE.getUri()), h.getLocation().getValue());
		homepage.addProperty(model.getProperty(PropertyURIEnum.LOCATION.getUri()),location);
	}
	
	private void editHomepage(OntModel model, Homepage h) {
		
		Individual homepage = model.getIndividual(h.getUri());
		homepage.removeAll(model.getProperty(PropertyURIEnum.NAME.getUri()));
		homepage.addLiteral(model.getProperty(PropertyURIEnum.NAME.getUri()), h.getName());
		Individual location = model.getIndividual(h.getLocation().getUri());
		location.removeAll(model.getProperty(PropertyURIEnum.VALUE.getUri()));
		location.addLiteral(model.getProperty(PropertyURIEnum.VALUE.getUri()), h.getLocation().getValue());
	}
	
	public String createUri(String name) {
		String tmp = StringUtils.tirarAcentuacao(name);
		tmp = tmp.trim();
		tmp = tmp.replaceAll(" ", "_");
		String firstChar = tmp.substring(0, 1).toLowerCase();
		tmp = firstChar.concat(tmp.substring(1));
		
		return NSURIEnum.NS.getUri().concat(tmp);
		
		
	}
	
	
	
}
