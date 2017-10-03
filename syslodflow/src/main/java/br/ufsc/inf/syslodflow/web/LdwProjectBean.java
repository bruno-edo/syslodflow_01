package br.ufsc.inf.syslodflow.web;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.nio.file.Path;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;

import org.apache.commons.fileupload.util.Streams;
import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;

import br.ufsc.inf.syslodflow.dto.LDWProjectDTO;
import br.ufsc.inf.syslodflow.entity.LDWProject;
import br.ufsc.inf.syslodflow.entity.Person;
import br.ufsc.inf.syslodflow.service.LdwProjectService;
import br.ufsc.inf.syslodflow.service.LdwpoService;
import br.ufsc.inf.syslodflow.service.PersonService;
import br.ufsc.inf.syslodflow.util.MessageUtil;
import br.ufsc.inf.syslodflow.util.Navegacao;
import br.ufsc.inf.syslodflow.util.StringUtils;

import com.hp.hpl.jena.ontology.OntModel;


@ManagedBean(name="ldwProjectBean")
@SessionScoped
public class LdwProjectBean implements Serializable {

	@Inject
	private LdwpoService ldwpoService;
	@Inject
	private LdwProjectService ldwProjectService;
	@Inject
	private PersonService personService;
	
	
	private int tab;
	private List<Person> personsList;
	private LDWProject ldwProject;
	private LDWProjectDTO ldwProjectDTOSelected;
	private DataModel<LDWProjectDTO> listLdwProjects;
	private OntModel ontModel;
	private Person person;
	
	@PostConstruct
	public void init() {
		ldwProject = new LDWProject();
		this.tab = 0;
		listLdwProjects = new ListDataModel<LDWProjectDTO>(ldwProjectService.getListLdwProjectDTO()); // Verificar isso para corrigir DataTables iniciando errado Bruno
	}
	
	/* NAVEGACAO */ 
	
	public String doNew() {
		this.ldwProject =  new LDWProject();
		this.ontModel = ldwpoService.doNewModel();
		this.personsList = personService.listPersons(ontModel); //Alterar isto Bruno
		person = new Person();
		return Navegacao.LDWPROJECT_CRUD;
	}
	
	public String doEdit() {
		this.ldwProjectDTOSelected =  listLdwProjects.getRowData();
		this.ontModel = ldwpoService.doLoadModel(ldwProjectDTOSelected.getPath());
		this.ldwProject = ldwProjectService.getLDWProject(ontModel);
		this.ldwProject.setFileName(ldwProjectDTOSelected.getFileName());
		this.personsList = personService.listPersons(ontModel);
		return Navegacao.LDWPROJECT_CRUD;
	}
	
	public String doSave() {
		System.out.println("doSave method called");
		//Valida nome do projeto 
		if(StringUtils.isValidName(ldwProject.getName())){
			if(ldwProject.getUri() == null){
				ldwProject.setFileName(StringUtils.formatName(ldwProject.getName()).concat(".owl"));
			}
			ontModel = this.ldwProjectService.writeLdwProject(ontModel, ldwProject);
//			ontModel.getIndividual(ldwProject.getUri()).getPropertyValue(ontModel.getProperty(PropertyURIEnum.DESCRIPTION.getUri())).asLiteral().getString();
//			ontModel.getIndividual(ldwProject.getUri()).getPropertyResourceValue(ontModel.getProperty(PropertyURIEnum.CREATOR.getUri())).getURI();
			this.ldwpoService.doSaveModel(ontModel, ldwProject.getFileName());
			listLdwProjects = new ListDataModel<LDWProjectDTO>(ldwProjectService.getListLdwProjectDTO());
			return Navegacao.LDWPROJECT_LIST;
		} else {
			MessageUtil.showError("crud.invalid.name");
			return Navegacao.MESMA_PAGINA;
		}
	}	
	
	/* CONTROLE TAB */
	public void onTabChange(TabChangeEvent event) {
	    String id = event.getTab().getId();
	    if (id.equals("tab0")) {
	        this.setTab(0);
	    } else if (id.equals("tab1")) {
	        this.setTab(1);
	    }else if (id.equals("tab2")) {
	        this.setTab(2);
	    }else if (id.equals("tab3")){
	    	this.setTab(3);
	    }else if (id.equals("tab4")){
	    	this.setTab(4);
	    }else if (id.equals("tab5")){
	    	this.setTab(5);
	    }
	}
	
	public void doNewPerson() {
		person = new Person();
		RequestContext.getCurrentInstance().execute("PF('dialog_person').show();");
	}
	
	public void doSavePerson() {
		String name = person.getName();
		
		if((name != null || name.isEmpty()) && StringUtils.isValidName(name)) {
			String uri = StringUtils.createUri(name, person.toString());
			person.setUri(uri);
			this.ontModel = personService.writePerson(ontModel, person); 
			this.personsList = personService.listPersons(ontModel);
		}
		else {
			MessageUtil.showError("crud.invalid.person.name");
		}
		
		person = new Person();
	}
	
	public void downloadOwl() {
		LDWProjectDTO ldwProjectDTOSelected =  listLdwProjects.getRowData();
		File file = new File(ldwProjectDTOSelected.getPath().toString());
		String fileName = file.getName();
		
		FacesContext fc = FacesContext.getCurrentInstance();
	    ExternalContext ec = fc.getExternalContext();

	    ec.responseReset();
	    String contentType = ec.getMimeType(fileName);
	    ec.setResponseContentLength((int) file.length());
	    ec.setResponseContentType(contentType);
	    ec.setResponseHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
	    
	    try {
	    	FileInputStream input = new FileInputStream(file);
	    	OutputStream output = ec.getResponseOutputStream();
	    	
	    	byte[] buffer = new byte[1024];
	    	int len;
	    	while((len=input.read(buffer)) >  0) {
	    		output.write(buffer, 0, len);
	    	}
	    	
	    	input.close();
	    	fc.responseComplete();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
	}
	
	public void deleteOwl(){
		try {
			LDWProjectDTO ldwProjectDTOSelected =  listLdwProjects.getRowData();
			File file = new File(ldwProjectDTOSelected.getPath().toString());
			
			file.delete();
			this.init();  // Reloads DataTable
			
		} catch(Exception e){
    		e.printStackTrace();
    	}
		
		
	}

	public LDWProject getLdwProject() {
		return ldwProject;
	}


	public void setLdwProject(LDWProject ldwProject) {
		this.ldwProject = ldwProject;
	}

	public DataModel<LDWProjectDTO> getListLdwProjects() {
		return listLdwProjects;
	}


	public void setListLdwProjects(DataModel<LDWProjectDTO> listLdwProjects) {
		this.listLdwProjects = listLdwProjects;
	}
	
	public void nextTab() {
		
	}

	public int getTab() {
		return tab;
	}

	public void setTab(int tab) {
		this.tab = tab;
	}

	public List<Person> getPersonsList() {
		return personsList;
	}

	public void setPersonsList(List<Person> personsList) {
		this.personsList = personsList;
	}

	public Person getPerson() {
		return person;
	}

	public void setPerson(Person person) {
		this.person = person;
	}
	
}
