package br.ufsc.inf.syslodflow.web;

import java.io.FileNotFoundException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.TabChangeEvent;
import org.primefaces.model.StreamedContent;
import org.primefaces.model.UploadedFile;

import br.ufsc.inf.syslodflow.dto.LDWProjectDTO;
import br.ufsc.inf.syslodflow.entity.LDWProject;
import br.ufsc.inf.syslodflow.entity.LDWStep;
import br.ufsc.inf.syslodflow.entity.LDWorkflow;
import br.ufsc.inf.syslodflow.entity.Tool;
import br.ufsc.inf.syslodflow.service.LdWorkflowService;
import br.ufsc.inf.syslodflow.service.LdwProjectService;
import br.ufsc.inf.syslodflow.service.LdwStepService;
import br.ufsc.inf.syslodflow.service.LdwpoService;
import br.ufsc.inf.syslodflow.util.MessageUtil;
import br.ufsc.inf.syslodflow.util.Navegacao;

import com.hp.hpl.jena.ontology.OntModel;

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
	@Inject
	private LdwStepService ldwStepService;
	@Inject
	private LdWorkflowService ldWorkflowService;
	
	
	private int tab;
	private OntModel model;
	private LDWProjectDTO ldwProjectSelected;
	private LDWorkflow ldWorkflow;
	private LDWStep step01;
	private LDWStep step02;
	private LDWStep step03;
	private LDWStep step04;
	private LDWStep step05;
	private StreamedContent smlDownload;
	private UploadedFile smlUploaded;

	private List<Tool> listToolsStep02;
	
	@PostConstruct
	public void init() {
		this.tab = 0;

	}
	
	public String doEdit(LDWProjectDTO projectSelected){
		this.ldwProjectSelected = projectSelected;
		this.model = ldwpoService.doLoadModel(projectSelected.getPath());
		LDWProject ldwProject = ldwProjectService.getLDWProject(model);
		this.ldWorkflow = ldwProject.getLdWorkFlow();
		this.listToolsStep02 = this.ldwStepService.getListToolsStep02(model);
		this.step01 = ldWorkflow.getLdwSteps().get(0);
		this.step02 = ldWorkflow.getLdwSteps().get(1);
		this.step03 = ldWorkflow.getLdwSteps().get(2);
		this.step04 = ldWorkflow.getLdwSteps().get(3);
		this.step05 = ldWorkflow.getLdwSteps().get(4);
		
		return Navegacao.LDWORKFLOW_MAIN;
	}
	

	public void doSave() {
		if(validaUpload()) {
			ldwpoService.saveFile(smlUploaded, ldwProjectSelected.getName(), "mapping");
			//ldWorkflowService.writeLdwWorkflow(model, ldWorkflow);
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
	
	public boolean validaUpload() {
		String extensao = FilenameUtils.getExtension(smlUploaded.getFileName());
		if (smlUploaded.getSize() <= 0) {
			MessageUtil.showError("crud.file.notfound");
			return false;
		}
		if(!extensao.equalsIgnoreCase("sml")) {
			MessageUtil.showError("crud.file.invalidformat");
			return false;
		}
		return true;
	}

	public StreamedContent getSmlDownload() throws FileNotFoundException {  
		this.smlDownload = ldwpoService.downloadFile(ldwProjectSelected.getName(), "text/sml", "mapping.sml");
		return smlDownload;
	}
	
	public void nextTab() {

	}

	public int getTab() {
		return tab;
	}

	public void setTab(int tab) {
		this.tab = tab;
	}
	
	public LDWorkflow getLdWorkflow() {
		return ldWorkflow;
	}
	
	public void setLdWorkflow(LDWorkflow ldWorkflow) {
		this.ldWorkflow = ldWorkflow;
	}
	public LDWStep getStep01() {
		return step01;
	}
	public void setStep01(LDWStep step01) {
		this.step01 = step01;
	}
	public LDWStep getStep02() {
		return step02;
	}
	public void setStep02(LDWStep step02) {
		this.step02 = step02;
	}
	public LDWStep getStep03() {
		return step03;
	}
	public void setStep03(LDWStep step03) {
		this.step03 = step03;
	}
	public LDWStep getStep04() {
		return step04;
	}
	public void setStep04(LDWStep step04) {
		this.step04 = step04;
	}
	public LDWStep getStep05() {
		return step05;
	}
	public void setStep05(LDWStep step05) {
		this.step05 = step05;
	}
	
	public UploadedFile getSmlUploaded() {
		return smlUploaded;
	}
	public void setSmlUploaded(UploadedFile smlUploaded) {
		this.smlUploaded = smlUploaded;
	}
	
	public List<Tool> getListToolsStep02() {
		return listToolsStep02;
	}
	
	public void setListToolsStep02(List<Tool> listToolsStep02) {
		this.listToolsStep02 = listToolsStep02;
	}
	
	
	
	
	
	

	
	
	
	
	
	
}