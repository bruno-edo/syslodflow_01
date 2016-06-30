package br.ufsc.inf.syslodflow.service;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.faces.context.FacesContext;

import br.ufsc.inf.syslodflow.util.MessageUtil;
import br.ufsc.inf.syslodflow.util.StringUtils;

public class CommandService {
	
	
	public String createScriptStep02(String ldwProjectName) {
		ldwProjectName = StringUtils.formatName(ldwProjectName);
		
		FacesContext fc = FacesContext.getCurrentInstance();
		String filePath = fc.getExternalContext().getInitParameter("projectsPath").toString();
		filePath = filePath + ldwProjectName;
		
		String ntPath = filePath + "/nt/";
		String ntDataset = filePath + "/nt/dataset.nt";
		String ntProject = filePath + ldwProjectName + ".nt";
		String csvDataset = filePath + "/csv/dataset.csv";
		String mappingSml = filePath + "/mappings/mapping.sml";
		String binPath = filePath + "/bin/";
		String applyingSparqlify = "applyingSparqlify.sh";
		
		/**
		 * Comando que será escrito no disco
		 */
		StringBuilder sb = new StringBuilder();
		
		sb.append("mkdir -p " + ntPath + "\n");
		
		sb.append("if [ -f " + ntDataset + " ]; then rm " + ntDataset + "; fi \n");
		sb.append("if [ -f " + ntProject + " ]; then rm " + ntProject + "; fi \n");
		sb.append("echo generating dataset.nt file by sparqlify \n");
		sb.append("sparqlify-csv -f " + csvDataset +  " -c " + mappingSml + " > " + ntDataset);
	
		
		try {
			File targetFolder = new File(binPath);
			if (!targetFolder.exists()) {
				targetFolder.mkdirs();
			} 
			FileWriter arq = new FileWriter(binPath + applyingSparqlify);
		    PrintWriter gravarArq = new PrintWriter(arq);
		    gravarArq.printf(sb.toString());
		    gravarArq.close();
		    
		} catch (IOException e) {
			e.printStackTrace();
			return MessageUtil.getMessageBundle("crud.file.saveerror");
		}
		
		return binPath+applyingSparqlify ;
	}
	
	public String createScriptStep04(String ldwProjectName) {
		ldwProjectName = StringUtils.formatName(ldwProjectName);
		
		FacesContext fc = FacesContext.getCurrentInstance();
		String filePath = fc.getExternalContext().getInitParameter("projectsPath").toString();
		filePath = filePath + ldwProjectName;
		
		String ntPath = filePath + "/nt/";
		String ntDataset = filePath + "/nt/dataset.nt";
		String ntProjectLinks = filePath + ldwProjectName + "_links.nt";
		String binPath = filePath + "/bin/";
		String scriptsPath = filePath + "/scripts/";
		String interlinkingDbPedia = "interLinkingToDbPedia.sh";
		String linkingMapping = scriptsPath + "linkingMapping.xml";
		
		/**
		 * Comando que será escrito no disco
		 */
		StringBuilder sb = new StringBuilder();
		
		sb.append("mkdir -p " + ntPath + "\n");
		
		sb.append("if [ -f " + ntDataset + " ]; then rm " + ntDataset + "; fi \n");
		sb.append("if [ -f " + ntProjectLinks + " ]; then rm " + ntProjectLinks + "; fi \n");
		sb.append("echo calling LIMES \n");
		sb.append("java -jar tools/LIMES-DIST/LIMES.jar" + linkingMapping);
	
		
		try {
			File targetFolder = new File(binPath);
			if (!targetFolder.exists()) {
				targetFolder.mkdirs();
			} 
			FileWriter arq = new FileWriter(binPath + interlinkingDbPedia);
		    PrintWriter gravarArq = new PrintWriter(arq);
		    gravarArq.printf(sb.toString());
		    gravarArq.close();
		    
		} catch (IOException e) {
			e.printStackTrace();
			return MessageUtil.getMessageBundle("crud.file.saveerror");
		}
		
		return binPath+interlinkingDbPedia;
	}
	
	public String createScriptSavingIntoVirtuoso(String ldwProjectName) {
		
		ldwProjectName = StringUtils.formatName(ldwProjectName);
		
		FacesContext fc = FacesContext.getCurrentInstance();
		String filePath = fc.getExternalContext().getInitParameter("projectsPath").toString();
		filePath = filePath + ldwProjectName;
		
		String ntPath = filePath + "/nt/";
		String ntDataset = filePath + "/nt/dataset.nt";
		String ntProject = filePath + ldwProjectName + ".nt";
		
		return "";
		
	}
	
	
	
	
	
	
	

}
