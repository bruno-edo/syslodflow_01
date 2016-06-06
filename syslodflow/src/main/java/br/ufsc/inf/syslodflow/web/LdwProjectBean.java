package br.ufsc.inf.syslodflow.web;

import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;

import org.primefaces.context.RequestContext;
import org.primefaces.event.TabChangeEvent;

import br.ufsc.inf.syslodflow.dto.LDWProjectDTO;
import br.ufsc.inf.syslodflow.entity.LDWProject;
import br.ufsc.inf.syslodflow.entity.Person;
import br.ufsc.inf.syslodflow.enumerator.PropertyURIEnum;
import br.ufsc.inf.syslodflow.service.LdwProjectService;
import br.ufsc.inf.syslodflow.service.LdwpoService;
import br.ufsc.inf.syslodflow.service.PersonService;
import br.ufsc.inf.syslodflow.util.MessageUtil;
import br.ufsc.inf.syslodflow.util.Navegacao;
import br.ufsc.inf.syslodflow.util.StringUtils;

import com.hp.hpl.jena.ontology.OntModel;


@ManagedBean(name="ldwProjectBean")
@SessionScoped
public class LdwProjectBean {

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
		listLdwProjects = new ListDataModel<LDWProjectDTO>(ldwProjectService.getListLdwProjectDTO());
	}
	
	/* NAVEGACAO */ 
	
	public String doNew() {
		this.ldwProject =  new LDWProject();
		this.ontModel = ldwpoService.doNewModel();
		this.personsList = personService.listPersons(ontModel);
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
		//Valida nome do projeto 
		if(StringUtils.isValidName(ldwProject.getName())){
			if(ldwProject.getUri() == null){
				ldwProject.setFileName(StringUtils.formatName(ldwProject.getName()).concat(".owl"));
			}
			ontModel = this.ldwProjectService.writeLdwProject(ontModel, ldwProject);
//			ontModel.getIndividual(ldwProject.getUri()).getPropertyValue(ontModel.getProperty(PropertyURIEnum.DESCRIPTION.getUri())).asLiteral().getString();
//			ontModel.getIndividual(ldwProject.getUri()).getPropertyResourceValue(ontModel.getProperty(PropertyURIEnum.CREATOR.getUri())).getURI();
			this.ldwpoService.doSaveModel(ontModel, ldwProject.getFileName());
			
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
		if(person.getName() != null || person.getName().isEmpty()) {
			if(StringUtils.isValidName(person.getName())) {
				String uri = StringUtils.createUri(person.getName(), person.toString());
				person.setUri(uri);
				this.ontModel = personService.writePerson(ontModel, person); 
				this.ldwpoService.doSaveModel(ontModel, ldwProject.getFileName());
				this.personsList = personService.listPersons(ontModel);
				person = new Person();
			}
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
