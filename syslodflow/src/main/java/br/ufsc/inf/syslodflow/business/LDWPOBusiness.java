package br.ufsc.inf.syslodflow.business;

import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

@Stateless
public class LDWPOBusiness {
	
	public void init() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
		String arquivo = scontext.getRealPath("/ontology/");
		java.nio.file.Path pathLdwpo = Paths.get(arquivo, "ldwpo.owl");
		
		OntModel modelo = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        modelo.read(pathLdwpo.toUri().toString(), ""); 
        OntClass classe = modelo.getOntClass("http://ldwpo.aksw.org/terms/1.0/LDWProject");

        Individual projeto = listIndividuals(classe).get(0);
        Property myProperty = modelo.getProperty("http://ldwpo.aksw.org/terms/1.0/name");
        System.out.println("name: " + projeto.getPropertyValue(myProperty).asLiteral().getString());
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
