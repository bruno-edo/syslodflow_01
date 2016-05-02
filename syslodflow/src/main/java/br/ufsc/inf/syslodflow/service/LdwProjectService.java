package br.ufsc.inf.syslodflow.service;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import br.ufsc.inf.syslodflow.dto.LDWProjectDTO;
import br.ufsc.inf.syslodflow.entity.LDWProject;
import br.ufsc.inf.syslodflow.enumerator.ClassURIEnum;
import br.ufsc.inf.syslodflow.enumerator.PropertyURIEnum;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Resource;

public class LdwProjectService extends BaseService {
	
	@Inject
	private LdwpoService ldwpoService;
	
	public LDWProjectDTO getLDWProjectDTO(OntModel model) {
		Individual ontProject = listIndividuals(model.getOntClass(ClassURIEnum.LDWPROJECT.getUri())).get(0);
		String projectName = getIndividualName(ontProject, model);
		
		Resource creator = ontProject.getPropertyResourceValue(model.getProperty(PropertyURIEnum.CREATOR.getUri()));
		String creatorName = getResourceName(creator, model);
				
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

	public LDWProject getLdwProject(OntModel model) {
		
		Individual ontProject = listIndividuals(model.getOntClass(ClassURIEnum.LDWPROJECT.getUri())).get(0);
		
		String ldwProjectName = getIndividualName(ontProject, model);
		
		return new LDWProject();
		
	}
	
	

}
