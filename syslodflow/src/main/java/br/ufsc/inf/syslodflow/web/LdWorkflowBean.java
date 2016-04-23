package br.ufsc.inf.syslodflow.web;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;

import org.primefaces.event.TabChangeEvent;

import br.ufsc.inf.syslodflow.entity.LDWorkflow;

/**
 * @author jeanmorais
 */
@ManagedBean(name="ldWorkflowBean")
@SessionScoped
public class LdWorkflowBean {

	private int tab;
	private LDWorkflow ldWorflow;
	
	@PostConstruct
	public void init() {
		this.tab = 0;
		

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
