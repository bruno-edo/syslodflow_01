package br.ufsc.inf.syslodflow.web;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;

import org.primefaces.event.TabChangeEvent;

import br.ufsc.inf.syslodflow.dto.LDWProjectDTO;
import br.ufsc.inf.syslodflow.entity.LDWProject;
import br.ufsc.inf.syslodflow.entity.LDWorkflow;
import br.ufsc.inf.syslodflow.service.LdwProjectService;
import br.ufsc.inf.syslodflow.service.LdwpoService;
import br.ufsc.inf.syslodflow.util.Navegacao;
import br.ufsc.inf.syslodflow.util.StringUtils;

import com.hp.hpl.jena.ontology.OntModel;

/**
 * Managed bean do usu&aacute;rio do sistema.
 * @author jeanmorais
 */
@ManagedBean(name="ldwProjectBean")
@SessionScoped
public class LdwProjectBean {

	@Inject
	private LdwpoService ldwpoService;
	@Inject
	private LdwProjectService ldwProjectService;
	
	private int tab;
	private LDWProject ldwProject;
	private LDWProjectDTO ldwProjectDTOSelected;
	private DataModel<LDWProjectDTO> listLdwProjects;
	private OntModel ontModel;
	
	@PostConstruct
	public void init() {
		ldwProject = new LDWProject();
		this.tab = 0;
		listLdwProjects = new ListDataModel<LDWProjectDTO>(ldwProjectService.getListLdwProjectDTO());
		
	}
	
	/* NAVEGACAO */ 
	
	public String doNew() {
		ldwProject =  new LDWProject();
		ldwpoService.init();
		return Navegacao.LDWPROJECT_CRUD;
	}
	
	public String doEdit() {
		this.ldwProjectDTOSelected =  listLdwProjects.getRowData();
		this.ontModel = ldwpoService.doLoadModel(ldwProjectDTOSelected.getPath());
		this.ldwProject = ldwProjectService.getLDWProject(ontModel);
		this.ldwProject.setFileName(ldwProjectDTOSelected.getFileName());
		return Navegacao.LDWPROJECT_CRUD;
	}
	
	public String doSave() {
		//Valida nome do projeto 
		if(StringUtils.isValidNameProject(ldwProject.getName())){
			if(ldwProject.getUri() == null) {
				String uriProject = ldwProjectService.createUri(ldwProject.getName());
				ldwProject.setUri(uriProject);
			}

			this.ontModel = ldwProjectService.saveLdwProject(ontModel, ldwProject);
			this.ldwpoService.doSaveModel(ontModel, ldwProject.getFileName());
			return Navegacao.LDWPROJECT_LIST;
		} else {
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
	
	
	
}
