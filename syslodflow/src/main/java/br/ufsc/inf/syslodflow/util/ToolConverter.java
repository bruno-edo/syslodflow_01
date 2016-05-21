package br.ufsc.inf.syslodflow.util;

import javax.faces.component.UIComponent;
import javax.faces.context.FacesContext;
import javax.faces.convert.Converter;
import javax.faces.convert.FacesConverter;

import br.ufsc.inf.syslodflow.entity.Tool;

@FacesConverter(forClass = Tool.class)
public class ToolConverter implements Converter {

		
		@Override
		public Object getAsObject(FacesContext context, UIComponent component,String value) {
			Tool tool  = null;
			if (!value.equals(null) && !value.isEmpty() && !value.equals("null")) {
	            return (Tool) component.getAttributes().get(value);
	        }
	        return tool;
		}

		@Override
		public String getAsString(FacesContext context, UIComponent component,	Object value) {
			if (value instanceof Tool) {
				Tool entity = (Tool) value;
	            if (entity != null && entity instanceof Tool && entity.getName() != null) {
	            	component.getAttributes().put(entity.getName().toString(), entity);
	                return entity.getName().toString();
	            }
	        }
	        return "";
		}


}
