package br.ufsc.inf.syslodflow.service;

import javax.faces.context.FacesContext;

public class CommandService {
	
	
	public String createCommandStep02(String ldwProjectName) {
		FacesContext fc = FacesContext.getCurrentInstance();
		String filePath = fc.getExternalContext().getInitParameter("projectsPath").toString();
		filePath = filePath + "/" + ldwProjectName ;
		
		/**
		 * Comando que será escrito no disco
		 */
		StringBuilder sb = new StringBuilder();
		sb.append("sparqlify-csv -f ");
		sb.append(filePath + "/csv/dataset.csv " +  "-c" + filePath + "/mappings/mapping.sml");
		sb.append(" > " + filePath + "/nt/dataset.nt");
		
		return "";
		
	}
	

}
