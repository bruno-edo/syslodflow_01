package br.ufsc.inf.syslodflow.service;

import java.util.ArrayList;
import java.util.List;

import br.ufsc.inf.syslodflow.entity.Person;
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
    
    public List<Person> listPersons(OntModel model) {
    	
     List<Individual> individuals = listIndividuals(model.getOntClass(ClassURIEnum.PERSON.getUri()));
     List<Person> persons = new ArrayList<Person>();
     for(int i=0; i<individuals.size(); i++) {
    	 persons.add(getPerson(model, individuals.get(i)));
     }
		return persons;
    	
    }
    
    private Person getPerson(OntModel model, Individual individual) {
		
    	String name = getPropertyStringValue(individual, model, PropertyURIEnum.NAME.getUri());
		return new Person(name, individual.getURI());
	}

	public void writePerson(OntModel model, Person p) {
    	
    	if(URIalreadyExists(model, p.getUri()))
    		insertPerson(model, p);
    	else
    		editPerson(model, p);
    }
    
    private void insertPerson(OntModel model, Person p) {
    	
    	Individual person = model.getOntClass(ClassURIEnum.PERSON.getUri()).createIndividual(p.getUri());
    	person.addLiteral(model.getProperty(PropertyURIEnum.NAME.getUri()), p.getName());
    }
    
    private void editPerson(OntModel model, Person p) {
    	
    	Individual person = model.getIndividual(p.getUri());
    	person.removeAll(model.getProperty(PropertyURIEnum.NAME.getUri()));
    	person.addLiteral(model.getProperty(PropertyURIEnum.NAME.getUri()), p.getName());
    }    
}
