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
		String mappingSml = filePath + "/scripts/mapping.sml";
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
		String interlinkingDbPedia = "interLinkingToDbPedia.sh";
		String linkingMapping = filePath + "linkingMapping.xml";
		
		/**
		 * Comando que será escrito no disco
		 */
		StringBuilder sb = new StringBuilder();
		
		sb.append("mkdir -p " + ntPath + "\n");
		
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
	
	public String createScriptSavingIntoVirtuoso(String ldwProjectName) {
		
		ldwProjectName = StringUtils.formatName(ldwProjectName);
		
		FacesContext fc = FacesContext.getCurrentInstance();
		String filePath = fc.getExternalContext().getInitParameter("projectsPath").toString();
		filePath = filePath + ldwProjectName;
		
		String ntPath = filePath + "/nt/";
		String ntDataset = filePath + "/nt/dataset.nt";
		String ntProject = filePath + ldwProjectName + ".nt";
		String binPath = filePath + "/bin/";
		String savingVirtuoso = "savingIntoVirtuoso.sh";
		
		String lineBreak = "\n";
		
		/**
		 * Comando que será escrito no arquivo
		 * 
		 * 	#!/bin/bash
		 *	# Script for loading an rdf file into a virtuoso store using
		 *	# virtuoso's isql
		 *	# Usage: sourceFile graphName port userName passWord
		 *	# e.g. <cmd> myfile.n3.bzip2 http://mygraph.org 1115 dba dba
		 */
		
		StringBuilder sb = new StringBuilder();
		
		sb.append("virt_isql=\"isql-vt\"" + lineBreak);

		sb.append("unzip_source=$1" + lineBreak);
		sb.append("virt_graphName=$2" + lineBreak);
		sb.append("virt_port=1111" + lineBreak);
		sb.append("virt_userName=dba" + lineBreak);
		sb.append("virt_passWord=dba" + lineBreak);
				
//		sb.append("echo \"concatenating the nt files\"" + lineBreak);
//		sb.append("for file in real/Graphs/QualisBrasil/nt/*.nt; " + lineBreak);
//		sb.append("do" + lineBreak);
//		sb.append("FILESIZE=$(stat -c%s $file)" + lineBreak);
//		sb.append(" if [ $FILESIZE -eq 0 ]; " + lineBreak);
//		sb.append("then" + lineBreak);
//		sb.append(" echo \"$file is empty\" >>  errors.txt" + lineBreak);
//		sb.append("$ERROR=1" + lineBreak);
//		sb.append("  else" + lineBreak);
//		sb.append("grep \"^<http\" $file;" + lineBreak);
//		sb.append("fi;" + lineBreak);
//		sb.append("done 2>/dev/null >> real/Graphs/QualisBrasil/QualisBrasil.nt" + lineBreak);
//
//		sb.append("echo \"SPARQL CLEAR GRAPH <$2>;\" | isql-vt -S \"$virt_port\" -U \"$virt_userName\" -P \"$virt_passWord\"" + lineBreak);
//		
		/**
		 * 	# Phase 2: Convert to n-triple
		 *	# FIXME Skip this step if the source file is already in n-triples format	
		 */
		
//		sb.append("rapper_source=$unzip_target" + lineBreak);
//		sb.append("rapper_extension=${rapper_source##*.}" + lineBreak);
//		sb.append("rapper_target=\"${rapper_source%.*}.nt\"" + lineBreak);
//
//		sb.append("if [ $rapper_extension != \"nt\" ]; then" + lineBreak);
//		sb.append("rapper_target=`mktemp`" + lineBreak);
//		sb.append("rapper_target=\"$rapper_target.nt\"" + lineBreak);
//		sb.append("echo \"Converting to n-triples. File is $rapper_target\"" + lineBreak);
//		sb.append("rapper $rapper_source -i guess -o ntriples >> $rapper_target" + lineBreak);
//		sb.append("fi");
//		
//		sb.append("split_size=$(stat -c%s \"$rapper_target\")" + lineBreak);
//
//		sb.append("echo \"Size = $split_size\"" + lineBreak);
//
//		sb.append("if [ $split_size -gt 5000000 ]; then" + lineBreak);
//		sb.append("echo \"File is large.\"" + lineBreak);
//		
//		
//		/**
//		 *  # Phase 3: Split
//		 */
//		
//		
//		sb.append("split_source=$rapper_target" + lineBreak);
//		sb.append("split_dir=`mktemp -d`" + lineBreak);
//		sb.append("echo \"Performing split on file $split_source\"" + lineBreak);
//		sb.append("split -a 10 -l 50000 $split_source \"${split_dir}/file\"" + lineBreak);
//		
//		/**
//		 * # Phase 4: Load
//		 */
//		
//		sb.append("echo \"creating load statement\"" + lineBreak);
//		sb.append(" for file in `ls $split_dir`" + lineBreak);
//		sb.append(" do" + lineBreak);
//		sb.append("   load_target=\"$split_dir/$file\"");
//		sb.append("   load_query=\"EXEC=TTLP_MT(file_to_string_output('$load_target'),'', '$virt_graphName', 255);\"" + lineBreak);
//		sb.append("   $virt_isql \"$virt_port\" \"$virt_userName\" \"$virt_passWord\" \"$load_query\"" + lineBreak);
//		
//		sb.append("done;" + lineBreak);
//		sb.append("rm -rf /tmp/*" + lineBreak);
//		sb.append("echo \"done\"" + lineBreak);
//		sb.append("else" + lineBreak);
//		sb.append("echo \"File is small. Loading directly.\"" + lineBreak);
//		sb.append("load_source=$rapper_target" + lineBreak);
//		sb.append("load_target=`mktemp`" + lineBreak);
//		
//		/**
//		 *   # NOTE By default virtuoso restricts access 
//  		 *	 # to files to only explicitely allowed directories. 
//  		 *	 # By default /tmp is allowed, therefore we copy the file there.
//		 */
//
//		sb.append("cp $load_source $load_target" + lineBreak);
//		sb.append("load_query=\"EXEC=TTLP_MT(file_to_string_output('$load_target'), '', '$virt_graphName', 255)\"" + lineBreak);
//		sb.append("echo \"$virt_isql $virt_port $virt_userName $virt_passWord $load_query\"" + lineBreak);
//		sb.append("$virt_isql \"$virt_port\" \"$virt_userName\" \"$virt_passWord\" \"$load_query\"" + lineBreak);
//		sb.append("fi");
		
		try {
			File targetFolder = new File(binPath);
			if (!targetFolder.exists()) {
				targetFolder.mkdirs();
			} 
			FileWriter arq = new FileWriter(binPath + savingVirtuoso);
		    PrintWriter gravarArq = new PrintWriter(arq);
		    gravarArq.printf(sb.toString());
		    gravarArq.close();
		    
		} catch (IOException e) {
			e.printStackTrace();
			return MessageUtil.getMessageBundle("crud.file.saveerror");
		}
		
		
		
		return "";
		
	}
	
	
	
	
	
	
	

}
