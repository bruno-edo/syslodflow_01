package br.ufsc.inf.syslodflow.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;

import org.primefaces.event.TabChangeEvent;

import br.ufsc.inf.syslodflow.business.LDWPOBusiness;
import br.ufsc.inf.syslodflow.entity.LDWProject;
import br.ufsc.inf.syslodflow.entity.Person;
import br.ufsc.inf.syslodflow.util.Navegacao;

/**
 * Managed bean do usu&aacute;rio do sistema.
 * @author jeanmorais
 */
@ManagedBean(name="ldwProjectBean")
@SessionScoped
public class LdwProjectBean {

	@Inject
	private LDWPOBusiness ldwpoBusiness;
	
	private int tab;
	private LDWProject ldwProject;
	private DataModel<LDWProject> listLdwProjects;
	
	@PostConstruct
	public void init() {
		ldwProject = new LDWProject();
		this.tab = 0;
		ldwpoBusiness.init();
	}
	
	/* NAVEGACAO */ 
	
	public String doNew() {
		ldwProject =  new LDWProject();
		return Navegacao.LDWPROJECT_CRUD;
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

	public DataModel<LDWProject> getListLdwProjects() {
		LDWProject t = new LDWProject();
		t.setName("Projeto Teste");
		Person p = new Person();
		p.setName("Jean Morais");
		t.setCreator(p);
		listLdwProjects = new ListDataModel<LDWProject>(); 
		if (listLdwProjects.getRowCount() <= 0) {
			List<LDWProject> temp = new ArrayList<LDWProject>();
			temp.add(t);
			listLdwProjects = new ListDataModel<LDWProject>(temp);
		}
		return listLdwProjects;
	}


	public void setListLdwProjects(DataModel<LDWProject> listLdwProjects) {
		this.listLdwProjects = listLdwProjects;
	}


	public void teste() {
		System.out.println("Teste");
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
