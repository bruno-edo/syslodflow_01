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
				
		return new LDWProjectDTO(projectName, creatorName, ontProject.getURI());
		
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
		LDWorkflow ldWorkflow = null;
		if(workflow != null) {
			ldWorkflow = ldwWorkflowService.getLDWorkflow(model, model.getIndividual(workflow.getURI()));
		}
		
		
		Person ldwProjectCreator = new Person(creatorName, creator.getURI());
		Homepage ldwProjectHomepage = new Homepage(homepageName, new Location(homepageLocationValue, homepageLocation.getURI()), homepage.getURI());
		Report ldwProjectReport = new Report(reportName, new Location(reportLocationValue, reportLocation.getURI()), report.getURI());
		
		return new LDWProject(ldwProjectName, ldwProjectDescription, ldwProjectGoal, ldwProjectCreator,
				ldwProjectHomepage, ldwProjectReport, ldWorkflow, ontProject.getURI());
		
	}
	
	public OntModel saveLdwProject(OntModel model, LDWProject project) {
		return model;
	}
	
	public OntModel writeLdwProject(OntModel model, LDWProject project) {
		if(project.getUri() == null) {
			//LDWProject Uri
			String uriProject = StringUtils.createUri(project.getName(), project.toString());
			project.setUri(uriProject);
			// Homepage Uri
			String uriHomepage = StringUtils.createUri(project.getName(), project.getHomePage().toString());
			project.getHomePage().setUri(uriHomepage);
			String uriLocationHomepage = StringUtils.createUri(project.getName(), project.getHomePage().getLocation().toString().concat("_").concat(project.getHomePage().toString()));
			project.getHomePage().getLocation().setUri(uriLocationHomepage);
			String nameHomepage = project.getHomePage().toString().concat("_").concat(StringUtils.formatName(project.getName()));
			project.getHomePage().setName(nameHomepage);
			// Report Uri
			String uriReport = StringUtils.createUri(project.getName(), project.getReport().toString());
			project.getReport().setUri(uriReport);
			String nameReport = project.getReport().toString().concat("_").concat(StringUtils.formatName(project.getName()).concat(".html"));
			project.getReport().setName(nameReport);
			String uriLocationReport = StringUtils.createUri(project.getName(), project.getReport().getLocation().toString().concat("_").concat(project.getReport().toString()));
			project.getReport().getLocation().setUri(uriLocationReport);
			project.getReport().getLocation().setValue(ldwpoService.getProjectsPath(StringUtils.formatName(project.getName())));
		
		}
		
		
		if (URIalreadyExists(model, project.getUri()))
			return editLdwProject(model, project);
		else 
			return insertLdwProject(model, project);
	}
	
	private OntModel editLdwProject(OntModel model, LDWProject project) {

		Individual ldwProject = model.getIndividual(project.getUri());
		ldwProject.removeAll(model.getProperty(PropertyURIEnum.NAME.getUri()));
		ldwProject.removeAll(model.getProperty(PropertyURIEnum.GOAL.getUri()));
		ldwProject.removeAll(model.getProperty(PropertyURIEnum.DESCRIPTION.getUri()));
		ldwProject.removeAll(model.getProperty(PropertyURIEnum.CREATOR.getUri()));
		ldwProject.addLiteral(model.getProperty(PropertyURIEnum.NAME.getUri()),project.getName());
		ldwProject.addLiteral(model.getProperty(PropertyURIEnum.GOAL.getUri()),project.getGoal());
		ldwProject.addLiteral(model.getProperty(PropertyURIEnum.DESCRIPTION.getUri()),project.getDescription());
		ldwProject.addProperty(model.getProperty(PropertyURIEnum.CREATOR.getUri()), model.getIndividual(project.getCreator().getUri()));
		model = writeHomepage(model, project.getHomePage());
		ldwProject.removeAll(model.getProperty(PropertyURIEnum.HOMEPAGE.getUri()));
		ldwProject.addProperty(model.getProperty(PropertyURIEnum.HOMEPAGE.getUri()), model.getIndividual(project.getHomePage().getUri()));
		model = writeReport(model, project.getReport());
		ldwProject.removeAll(model.getProperty(PropertyURIEnum.REPORT.getUri()));
		ldwProject.addProperty(model.getProperty(PropertyURIEnum.REPORT.getUri()),model.getIndividual(project.getReport().getUri()));
		return model;
		
	}

	private OntModel insertLdwProject(OntModel model, LDWProject project) {
	
		Individual ldwProject = model.getOntClass(ClassURIEnum.LDWPROJECT.getUri()).createIndividual(project.getUri());
		ldwProject.addLiteral(model.getProperty(PropertyURIEnum.NAME.getUri()),project.getName());
		ldwProject.addLiteral(model.getProperty(PropertyURIEnum.GOAL.getUri()),project.getGoal());
		ldwProject.addLiteral(model.getProperty(PropertyURIEnum.DESCRIPTION.getUri()),project.getDescription());
		ldwProject.addProperty(model.getProperty(PropertyURIEnum.CREATOR.getUri()), model.getIndividual(project.getCreator().getUri()));
		model = writeHomepage(model, project.getHomePage());
		ldwProject.addProperty(model.getProperty(PropertyURIEnum.HOMEPAGE.getUri()), model.getIndividual(project.getHomePage().getUri()));
		model = writeReport(model, project.getReport());
		ldwProject.addProperty(model.getProperty(PropertyURIEnum.REPORT.getUri()),model.getIndividual(project.getReport().getUri()));
		return model;
		
	}
	
	private OntModel writeReport(OntModel model, Report r) {
		
		if (URIalreadyExists(model, r.getUri()))
			return editReport(model, r);
		else 
			return insertReport(model, r);
	}
	
	private OntModel insertReport(OntModel model, Report r) {
		
		Individual report = model.getOntClass(ClassURIEnum.REPORT.getUri()).createIndividual(r.getUri());
		report.addLiteral(model.getProperty(PropertyURIEnum.NAME.getUri()), r.getName());
		Individual location = model.getOntClass(ClassURIEnum.LOCATION.getUri()).createIndividual(r.getLocation().getUri());
		location.addLiteral(model.getProperty(PropertyURIEnum.VALUE.getUri()), r.getLocation().getValue());
		report.addProperty(model.getProperty(PropertyURIEnum.LOCATION.getUri()),location);
		return model;
		
	}

	private OntModel editReport(OntModel model, Report r) {
		
		Individual report = model.getIndividual(r.getUri());
		report.removeAll(model.getProperty(PropertyURIEnum.NAME.getUri()));
		report.addLiteral(model.getProperty(PropertyURIEnum.NAME.getUri()), r.getName());
		Individual location = model.getIndividual(r.getLocation().getUri());
		location.removeAll(model.getProperty(PropertyURIEnum.VALUE.getUri()));
		location.addLiteral(model.getProperty(PropertyURIEnum.VALUE.getUri()), r.getLocation().getValue());
		return model;
	}

	private OntModel writeHomepage(OntModel model, Homepage h) {
		
		if (URIalreadyExists(model, h.getUri()))
			return editHomepage(model, h);
		else 
			return insertHomepage(model, h);
	}
	
	private OntModel insertHomepage(OntModel model, Homepage h) {
		
		Individual homepage = model.getOntClass(ClassURIEnum.HOMEPAGE.getUri()).createIndividual(h.getUri());
		homepage.addLiteral(model.getProperty(PropertyURIEnum.NAME.getUri()), h.getName());
		Individual location = model.getOntClass(ClassURIEnum.LOCATION.getUri()).createIndividual(h.getLocation().getUri());
		location.addLiteral(model.getProperty(PropertyURIEnum.VALUE.getUri()), h.getLocation().getValue());
		homepage.addProperty(model.getProperty(PropertyURIEnum.LOCATION.getUri()),location);
		return model;
	}
	
	private OntModel editHomepage(OntModel model, Homepage h) {
		
		Individual homepage = model.getIndividual(h.getUri());
		homepage.removeAll(model.getProperty(PropertyURIEnum.NAME.getUri()));
		homepage.addLiteral(model.getProperty(PropertyURIEnum.NAME.getUri()), h.getName());
		Individual location = model.getIndividual(h.getLocation().getUri());
		location.removeAll(model.getProperty(PropertyURIEnum.VALUE.getUri()));
		location.addLiteral(model.getProperty(PropertyURIEnum.VALUE.getUri()), h.getLocation().getValue());
		return model;
	}
	

	
	
	
}
