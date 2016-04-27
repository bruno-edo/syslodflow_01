package br.ufsc.inf.syslodflow.business;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import br.ufsc.inf.syslodflow.util.MyFileVisitor;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

@Stateless
public class LDWPOBusiness {
	
	@PostConstruct
	public void init() {

	}
	
	public OntModel doNewModel() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
		String filePath = scontext.getRealPath("/ontology/");
		java.nio.file.Path pathLdwpo = Paths.get(filePath, "ldwpo.owl");
		
		OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        model.read(pathLdwpo.toUri().toString(), "");
        return model;
		
	}
	
	public OntModel doLoadModel(Path path) {
//		FacesContext fc = FacesContext.getCurrentInstance();
//		String filePath = fc.getExternalContext().getInitParameter("filePath").toString();
//		java.nio.file.Path pathLdwpo = Paths.get(filePath, nameProject);
		OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        model.read(path.toUri().toString(), "");
        return model;
	}
	
	public List<Path> getOntologyFiles() {
		FacesContext fc = FacesContext.getCurrentInstance();
		String filePath = fc.getExternalContext().getInitParameter("filePath").toString();
		Path source = Paths.get(filePath);
		try {
			MyFileVisitor visitor = new MyFileVisitor();
			Files.walkFileTree(source, visitor);
			return visitor.getListFiles();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	

	public static List<Individual> listIndividuals(OntClass classe) {

		List<Individual> listIndividuals = new ArrayList<Individual>();

		ExtendedIterator<? extends OntResource> instances = classe.listInstances();
		while (instances.hasNext())
		{
			Individual thisInstance = (Individual) instances.next();
			listIndividuals.add(thisInstance);
		}
		return listIndividuals;
	}
     
     
    public static List<Statement> listProperties(Individual individual) {
         
        List<Statement> listProperties = new ArrayList<Statement>();
         
        for (StmtIterator j = individual.listProperties(); j.hasNext(); ) {
            Statement s = j.next();
            listProperties.add(s);
        }
         
        return listProperties;
    }
	

}
