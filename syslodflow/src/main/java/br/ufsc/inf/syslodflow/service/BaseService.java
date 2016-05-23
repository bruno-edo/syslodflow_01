package br.ufsc.inf.syslodflow.service;

import java.util.ArrayList;
import java.util.List;

import br.ufsc.inf.syslodflow.entity.Tool;
import br.ufsc.inf.syslodflow.enumerator.ClassURIEnum;
import br.ufsc.inf.syslodflow.enumerator.NSURIEnum;
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
    
    public static String createURI(String value) {
		
    	value = NSURIEnum.NS.getUri()+value.replaceAll(" ", "_").toLowerCase();
		
    	return value;
    	
    }
    
    public static boolean URIalreadyExists(OntModel model, String URI) {
    	
    	if(model.getIndividual(URI)!=null)
    		return true;
    	else
    		return false;
    }
    
    
	private void insertTool(OntModel model, Tool t) {
		
		Individual tool = model.getOntClass(ClassURIEnum.TOOL.getUri()).createIndividual(t.getUri());
		tool.addLiteral(model.getProperty(PropertyURIEnum.NAME.getUri()), t.getName());
		Individual location = model.getOntClass(ClassURIEnum.LOCATION.getUri()).createIndividual(t.getLocation().getUri());
		location.addLiteral(model.getProperty(PropertyURIEnum.VALUE.getUri()), t.getLocation().getValue());
		tool.addProperty(model.getProperty(PropertyURIEnum.LOCATION.getUri()), location);
		
	}
    

}
