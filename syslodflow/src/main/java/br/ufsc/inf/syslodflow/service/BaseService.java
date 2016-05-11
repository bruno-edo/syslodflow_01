package br.ufsc.inf.syslodflow.service;

import java.util.ArrayList;
import java.util.List;

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
	
    public static String getPropertyStringValue(Individual individual, OntModel model, String uriProperty)
    {
    	if(individual != null) {
        	Property property = model.getProperty(uriProperty);
    		String value = individual.getPropertyValue(property).asLiteral().getString();
    		return value;
    	}
    	return "";

    }
    
    public static Individual getSubIndividualByProperty(OntModel model, Individual individual, String uriProperty) {
    	if(individual != null) {
        	Resource resource = individual.getPropertyResourceValue(model.getProperty(uriProperty));
        	if(resource != null) {
        		Individual subIndividual = model.getIndividual((resource).getURI());
        		return subIndividual;
        	}
    	}
    	return null;

    	
    }

}
