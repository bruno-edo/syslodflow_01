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
		
		sb.append("virt_isql=\"isql-vt\"");

		sb.append("unzip_source=$1");
		sb.append("virt_graphName=$2");
		sb.append("virt_port=1111");
		sb.append("virt_userName=dba");
		sb.append("virt_passWord=dba");
				
		sb.append("echo \"concatenating the nt files\"");
		sb.append("for file in real/Graphs/QualisBrasil/nt/*.nt; ");
		sb.append("do");
		sb.append("FILESIZE=$(stat -c%s $file)");
		sb.append(" if [ $FILESIZE -eq 0 ]; ");
		sb.append("then");
		sb.append(" echo \"$file is empty\" >>  errors.txt");
		sb.append("$ERROR=1");
		sb.append("  else");
		sb.append("grep \"^<http\" $file;");
		sb.append("fi;");
		sb.append("done 2>/dev/null >> real/Graphs/QualisBrasil/QualisBrasil.nt");

		sb.append("echo \"SPARQL CLEAR GRAPH <$2>;\" | isql-vt -S \"$virt_port\" -U \"$virt_userName\" -P \"$virt_passWord\"");
		
		/**
		 * 	# Phase 2: Convert to n-triple
		 *	# FIXME Skip this step if the source file is already in n-triples format	
		 */
		
		sb.append("rapper_source=$unzip_target");
		sb.append("rapper_extension=${rapper_source##*.}");
		sb.append("rapper_target=\"${rapper_source%.*}.nt\"");

		sb.append("if [ $rapper_extension != \"nt\" ]; then");
		sb.append("rapper_target=`mktemp`");
		sb.append("rapper_target=\"$rapper_target.nt\"");
		sb.append("echo \"Converting to n-triples. File is $rapper_target\"");
		sb.append("rapper $rapper_source -i guess -o ntriples >> $rapper_target");
		sb.append("fi");
		
		sb.append("split_size=$(stat -c%s \"$rapper_target\")");

		sb.append("echo \"Size = $split_size\"");

		sb.append("if [ $split_size -gt 5000000 ]; then");
		sb.append("echo \"File is large.\"");
		
		
		/**
		 *  # Phase 3: Split
		 */
		
		
		sb.append("split_source=$rapper_target");
		sb.append("split_dir=`mktemp -d`");
		sb.append("echo \"Performing split on file $split_source\"");
		sb.append("split -a 10 -l 50000 $split_source \"${split_dir}/file\"");
		
		/**
		 * # Phase 4: Load
		 */
		
		sb.append("echo \"creating load statement\"");
		sb.append(" for file in `ls $split_dir`");
		sb.append(" do");
		sb.append("   load_target=\"$split_dir/$file\"");
		sb.append("   load_query=\"EXEC=TTLP_MT(file_to_string_output('$load_target'),'', '$virt_graphName', 255);\"");
		sb.append("   $virt_isql \"$virt_port\" \"$virt_userName\" \"$virt_passWord\" \"$load_query\"");
		
		sb.append("done;");
		sb.append("rm -rf /tmp/*");
		sb.append("echo \"done\"");
		sb.append("else");
		sb.append("echo \"File is small. Loading directly.\"");
		sb.append("load_source=$rapper_target");
		sb.append("load_target=`mktemp`");
		
		/**
		 *   # NOTE By default virtuoso restricts access 
  		 *	 # to files to only explicitely allowed directories. 
  		 *	 # By default /tmp is allowed, therefore we copy the file there.
		 */

		
		
		return "";
		
	}
	
	
	
	
	
	
	

}
