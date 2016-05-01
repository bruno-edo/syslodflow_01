package br.ufsc.inf.syslodflow.service;

import br.ufsc.inf.syslodflow.dto.LDWProjectDTO;
import br.ufsc.inf.syslodflow.entity.LDWProject;
import br.ufsc.inf.syslodflow.enumerator.ClassURIEnum;
import br.ufsc.inf.syslodflow.enumerator.PropertyURIEnum;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Resource;

public class LdwProjectService extends BaseService {
	
	public LDWProjectDTO getLDWProjectDTO(OntModel model) {
		Individual ontProject = listIndividuals(model.getOntClass(ClassURIEnum.LDWPROJECT.getUri())).get(0);
		String projectName = getIndividualName(ontProject, model);
		
		Resource creator = ontProject.getPropertyResourceValue(model.getProperty(PropertyURIEnum.CREATOR.getUri()));
		String creatorName = getResourceName(creator, model);
				
		return new LDWProjectDTO(projectName, creatorName);
		
	}

	public LDWProject getLdwProject(OntModel model) {
		
		Individual ontProject = listIndividuals(model.getOntClass(ClassURIEnum.LDWPROJECT.getUri())).get(0);
		
		String ldwProjectName = getIndividualName(ontProject, model);
		
		return new LDWProject();
		
	}

}
