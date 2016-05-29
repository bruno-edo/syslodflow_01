package br.ufsc.inf.syslodflow.service;

import java.util.ArrayList;
import java.util.List;

import br.ufsc.inf.syslodflow.entity.Person;
import br.ufsc.inf.syslodflow.enumerator.ClassURIEnum;
import br.ufsc.inf.syslodflow.enumerator.PropertyURIEnum;

import com.hp.hpl.jena.ontology.Individual;
import com.hp.hpl.jena.ontology.OntModel;

public class PersonService extends BaseService {
	
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
