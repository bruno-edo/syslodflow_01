package br.ufsc.inf.syslodflow.service;

import java.util.ArrayList;
import java.util.List;

import br.ufsc.inf.syslodflow.enumerator.PropertyURIEnum;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntClass;
import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntResource;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;
import com.hp.hpl.jena.util.iterator.ExtendedIterator;

public class BaseService {
	
	
	
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
	
    public static String getIndividualName(Individual individual, OntModel model) {
    	
    	Property nameProperty = model.getProperty(PropertyURIEnum.NAME.getUri());
		String name = individual.getPropertyValue(nameProperty).asLiteral().getString();
		return name;
    }
    
    public static String getResourceName(Resource resource, OntModel model) {
    	
    	Property nameProperty = model.getProperty(PropertyURIEnum.NAME.getUri());
		String name = resource.getRequiredProperty(nameProperty).getString();
		return name;
    }

}
