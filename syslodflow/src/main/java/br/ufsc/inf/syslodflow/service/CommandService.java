package br.ufsc.inf.syslodflow.service;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import br.ufsc.inf.syslodflow.util.MessageUtil;
import br.ufsc.inf.syslodflow.util.StringUtils;

public class CommandService {
	
	
	public String createScriptStep02(String ldwProjectName) {
		ldwProjectName = StringUtils.formatName(ldwProjectName);
		
		FacesContext fc = FacesContext.getCurrentInstance();
		String filePath = fc.getExternalContext().getInitParameter("projectsPath").toString();
		filePath = filePath + ldwProjectName;
		
		String ntPath = filePath + "/nt";
		String ntDataset = ntPath + "/dataset.nt";
		String ntProject = ntPath + "/" + ldwProjectName + ".nt";
		String csvDataset = filePath + "/csv/dataset.csv";
		String mappingSml = filePath + "/tool_configs/mapping.sml";
		String binPath = filePath + "/bin";
		String applyingSparqlify = "/applyingSparqlify.sh";
		
		/**
		 * Comando que será escrito no disco
		 */
		StringBuilder sb = new StringBuilder();
		
		sb.append("mkdir -p " + ntPath + "/\n");
		
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
		
		String ntPath = filePath + "/nt";
		String ntDataset = ntPath + "/dataset.nt";
		String ntProjectLinks = ntPath + "/" + ldwProjectName + "_links.nt";
		String binPath = filePath + "/bin";
		String interlinkingDbPedia = "/interLinkingToDbPedia.sh";
		String linkingMapping = filePath + "/tool_configs/linkingMapping.xml";
		
		/**
		 * Comando que será escrito no disco
		 */
		StringBuilder sb = new StringBuilder();
		
		sb.append("mkdir -p " + ntPath + "/\n");
		
		sb.append("if [ -f " + ntDataset + " ]; then rm " + ntDataset + "; fi \n");
		sb.append("if [ -f " + ntProjectLinks + " ]; then rm " + ntProjectLinks + "; fi \n");
		sb.append("echo calling LIMES \n");
		sb.append("java -jar tools/LIMES-DIST/LIMES.jar " + linkingMapping);
	
		
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
		
	public String createScriptSavingIntoVirtuoso(String ldwProjectName, String graphName) throws IOException {
		
		ldwProjectName = StringUtils.formatName(ldwProjectName);
		
		FacesContext fc = FacesContext.getCurrentInstance();
		ServletContext scontext = (ServletContext) fc.getExternalContext().getContext();
		String filePath = fc.getExternalContext().getInitParameter("projectsPath").toString();
		filePath = filePath + ldwProjectName;
		
		String ntPath = filePath + "/nt/";
		String ntProject = filePath + "/" + ldwProjectName + ".nt";
		String binPath = filePath + "/bin/";
		String savingVirtuoso = "savingIntoVirtuoso.sh";
		
		String fileReader = scontext.getRealPath("/WEB-INF/classes/" + savingVirtuoso);
	    String fileWriter = binPath + savingVirtuoso;
	    
	    getClass().getResource(savingVirtuoso);
	    BufferedWriter writer = new BufferedWriter(new FileWriter(fileWriter));
	    BufferedReader reader = new BufferedReader(new FileReader(fileReader));

	    String linha;
	    while ((linha = reader.readLine()) != null) {
	        if (linha.contains("$ntPath")) {
	            linha = linha.replace("$ntPath", ntPath + "*.nt");
	        }
	        if(linha.contains("$ntGraph")) {
	        	linha = linha.replace("$ntGraph",  ntProject);
	        }
	        writer.write(linha + "\n");
	    }
	    writer.close();        
	    reader.close();
	    
	    return fileWriter + " " + ntProject + " " + graphName;
	    
	}

}
