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
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.NodeIterator;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class LdwProjectService extends BaseService {
	
	@Inject
	private LdwpoService ldwpoService;
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
	
		
		StmtIterator iter = ontProject.listProperties(model.getProperty(PropertyURIEnum.LDWORKFLOW.getUri()));
		List<LDWorkflow> ldWorkflows = new ArrayList<LDWorkflow>();
		while (iter.hasNext()){
			Individual node = model.getIndividual(iter.nextStatement().getResource().getURI());
			ldWorkflows.add(ldwWorkflowService.getLDWorkflow(model, node));
		}
		
		
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
		ldwProject.setLdWorkFlows(ldWorkflows);
		
		return ldwProject;
		
	}
	
	public OntModel saveLdwProject(OntModel model, LDWProject project) {
		return model;
	}
	

}
