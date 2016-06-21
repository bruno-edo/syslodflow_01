package br.ufsc.inf.syslodflow.service;

import javax.faces.context.FacesContext;

public class CommandService {
	
	
	public String createScriptStep02(String ldwProjectName) {
		FacesContext fc = FacesContext.getCurrentInstance();
		String filePath = fc.getExternalContext().getInitParameter("projectsPath").toString();
		filePath = filePath + "/" + ldwProjectName ;
		
		String ntPath = filePath + "/nt/";
		String ntDataset = filePath + "/nt/dataset.nt";
		String ntProject = filePath + "/" + ldwProjectName + ".nt";
		String csvDataset = filePath + "/csv/dataset.csv";
		String mappingSml = filePath + "/mappings/mapping.sml";
		
		/**
		 * Comando que será escrito no disco
		 */
		StringBuilder sb = new StringBuilder();
		
		sb.append("mkdir -p " + ntPath);
		
		sb.append("if [ -f " + ntDataset + " ]; then rm " + ntDataset + "; fi ");
		sb.append("if [ -f " + ntProject + " ]; then rm " + ntProject + "; fi ");
		
		sb.append("sparqlify-csv -f " + csvDataset +  "-c" + mappingSml + " > " + ntDataset);
	
		
		
		
		return "";
	}
	
	
	

}
