package br.ufsc.inf.syslodflow.service;

import br.ufsc.inf.syslodflow.dto.LDWProjectDTO;
import br.ufsc.inf.syslodflow.enumerator.ClassURIEnum;
import br.ufsc.inf.syslodflow.enumerator.PropertyURIEnum;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.rdf.model.Resource;

public class LdwProjectService extends BaseService {
	
	public LDWProjectDTO getLDWProjectDTO(OntModel model) {
		Individual project = listIndividuals(model.getOntClass(ClassURIEnum.LDWPROJECT.getUri())).get(0);
		String projectName = project.getPropertyValue(model.getProperty(PropertyURIEnum.NAME.getUri())).asLiteral().getString();
		
		Resource creator = project.getPropertyResourceValue(model.getProperty(PropertyURIEnum.CREATOR.getUri()));
		String creatorName = creator.getRequiredProperty(model.getProperty(PropertyURIEnum.NAME.getUri())).getString();
		
		return new LDWProjectDTO(projectName, creatorName);
		
	}

}
