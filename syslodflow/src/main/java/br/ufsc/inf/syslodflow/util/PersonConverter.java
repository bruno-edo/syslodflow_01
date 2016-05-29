package br.ufsc.inf.syslodflow.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.ufsc.inf.syslodflow.entity.Person;
import br.ufsc.inf.syslodflow.entity.Tool;

@FacesConverter(forClass = Person.class)
public class PersonConverter implements Converter {

		
		@Override
		public Object getAsObject(FacesContext context, UIComponent component,String value) {
			Person person  = null;
			if (!value.equals(null) && !value.isEmpty() && !value.equals("null")) {
	            return (Person) component.getAttributes().get(value);
	        }
	        return person;
		}

		@Override
		public String getAsString(FacesContext context, UIComponent component,	Object value) {
			if (value instanceof Person) {
				Person entity = (Person) value;
	            if (entity != null && entity instanceof Person && entity.getName() != null) {
	            	component.getAttributes().put(entity.getName().toString(), entity);
	                return entity.getName().toString();
	            }
	        }
	        return "";
		}

}
