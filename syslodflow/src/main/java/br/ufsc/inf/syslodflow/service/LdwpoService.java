package br.ufsc.inf.syslodflow.service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Properties;

import javax.annotation.PostConstruct;
import javax.ejb.Stateless;
import javax.faces.context.FacesContext;
import javax.servlet.ServletContext;

import br.ufsc.inf.syslodflow.enumerator.LangModelEnum;
import br.ufsc.inf.syslodflow.util.MyFileVisitor;

import com.hp.hpl.jena.ontology.OntModel;
import com.hp.hpl.jena.ontology.OntModelSpec;
import com.hp.hpl.jena.rdf.model.ModelFactory;

@Stateless
public class LdwpoService {
	
	@PostConstruct
	public void init() {

	}
	
	public OntModel doNewModel() {
		FacesContext facesContext = FacesContext.getCurrentInstance();
		ServletContext scontext = (ServletContext) facesContext.getExternalContext().getContext();
		String filePath = scontext.getRealPath("/ontology/");
		java.nio.file.Path pathLdwpo = Paths.get(filePath, "ldwpo.owl");
		
		OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        model.read(pathLdwpo.toUri().toString(), "");
        return model;
		
	}
	
	public OntModel doLoadModel(Path path) {
//		FacesContext fc = FacesContext.getCurrentInstance();
//		String filePath = fc.getExternalContext().getInitParameter("filePath").toString();
//		java.nio.file.Path pathLdwpo = Paths.get(filePath, nameProject);
		OntModel model = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
        model.read(path.toUri().toString(), "");
        
        
        return model;
	}
	
	public void doSaveModel(OntModel model, String fileName) {
		FacesContext fc = FacesContext.getCurrentInstance();
		String filePath = fc.getExternalContext().getInitParameter("filePath").toString();

		try {
      	  File file = new File(filePath + fileName);
      	  model.write(new FileOutputStream(file), LangModelEnum.RDFXMLABBREV.getType());
      } catch (IOException e) {
          e.printStackTrace();
      }
		
	}
	
	public List<Path> getOntologyFiles() {
		FacesContext fc = FacesContext.getCurrentInstance();
		String filePath = fc.getExternalContext().getInitParameter("filePath").toString();
		Path source = Paths.get(filePath);
		try {
			MyFileVisitor visitor = new MyFileVisitor();
			Files.walkFileTree(source, visitor);
			return visitor.getListFiles();
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public String getPropertie(String key) {
		InputStream is = getClass().getResourceAsStream("/configuration/configuration.properties");
		Properties props = new Properties();
		try {
			props.load(is);
			return props.getProperty(key);
			} catch (IOException e) {
			return null;
		}
	}


}
