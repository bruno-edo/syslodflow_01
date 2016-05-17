package br.ufsc.inf.syslodflow.web;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

import org.primefaces.event.TabChangeEvent;

import com.hp.hpl.jena.ontology.OntModel;

import br.ufsc.inf.syslodflow.dto.LDWProjectDTO;
import br.ufsc.inf.syslodflow.entity.LDWProject;
import br.ufsc.inf.syslodflow.entity.LDWorkflow;
import br.ufsc.inf.syslodflow.service.LdwProjectService;
import br.ufsc.inf.syslodflow.service.LdwpoService;
import br.ufsc.inf.syslodflow.util.Navegacao;

/**
 * @author jeanmorais
 */
@ManagedBean(name="ldWorkflowBean")
@SessionScoped
public class LdWorkflowBean {

	@Inject 
	private LdwpoService ldwpoService;
	@Inject 
	private LdwProjectService ldwProjectService;
	
	
	private int tab;
	private LDWorkflow ldWorflow;
	
	@PostConstruct
	public void init() {
		this.tab = 0;
		

	}
	public String doEdit(LDWProjectDTO projectSelected){
		OntModel model = ldwpoService.doLoadModel(projectSelected.getPath());
		LDWProject ldwProject = ldwProjectService.getLDWProject(model);
		this.ldWorflow = ldwProject.getLdWorkFlow();
		return Navegacao.LDWORKFLOW_MAIN;
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
