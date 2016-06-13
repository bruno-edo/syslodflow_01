package br.ufsc.inf.syslodflow.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.ufsc.inf.syslodflow.dto.LDWProjectDTO;

@FacesConverter(forClass = LDWProjectDTO.class)
public class LDWProjectDTOConverter implements Converter {

		
		@Override
		public Object getAsObject(FacesContext context, UIComponent component,String value) {
			LDWProjectDTO project  = null;
			if (!value.equals(null) && !value.isEmpty() && !value.equals("null")) {
	            return (LDWProjectDTO) component.getAttributes().get(value);
	        }
	        return project;
		}

		@Override
		public String getAsString(FacesContext context, UIComponent component,	Object value) {
			if (value instanceof LDWProjectDTO) {
				LDWProjectDTO entity = (LDWProjectDTO) value;
	            if (entity != null && entity instanceof LDWProjectDTO && entity.getUri() != null) {
	            	component.getAttributes().put(entity.getUri().toString(), entity);
	                return entity.getUri().toString();
	            }
	        }
	        return "";
		}


}
