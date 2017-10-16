package br.ufsc.inf.syslodflow.web;

import java.io.FileNotFoundException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;

import org.apache.commons.io.FilenameUtils;
import org.primefaces.event.FileUploadEvent;
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
	//private UploadedFile smlScriptUploaded;
	//private StreamedContent smlScriptDownload;
	private StreamedContent xmlDownload;
	private UploadedFile xmlUploaded;
	//private UploadedFile xmlScriptUploaded;
	//private StreamedContent xmlScriptDownload;
	private StreamedContent csvDownload;
	private UploadedFile csvUploaded;
	
	
	/**
	 *  Tools lists
	 */
	private List<Tool> listToolsStep01;
	private List<Tool> listToolsStep02;
	private List<Tool> listToolsStep03;
	private List<Tool> listToolsStep04;
	private List<Tool> listToolsStep05;
	
	@PostConstruct
	public void init() {
		this.tab = 0;

	}
	
	public void xmlUpload(FileUploadEvent event) {
		this.xmlUploaded = event.getFile();
	    this.sendFileUploadSuccessMessage(xmlUploaded.getFileName());
	}
	
	public void smlUpload(FileUploadEvent event) {
		this.smlUploaded = event.getFile();
	    this.sendFileUploadSuccessMessage(smlUploaded.getFileName());
	}
	
	public void csvUpload(FileUploadEvent event) {
		this.csvUploaded = event.getFile();
	    this.sendFileUploadSuccessMessage(csvUploaded.getFileName());
	}
	
	/*public void xmlScriptUpload(FileUploadEvent event) {
		this.xmlScriptUploaded = event.getFile();
	    this.sendFileUploadSuccessMessage(xmlScriptUploaded.getFileName());
	}
	
	public void smlScriptUpload(FileUploadEvent event) {
		this.smlScriptUploaded = event.getFile();
	    this.sendFileUploadSuccessMessage(smlScriptUploaded.getFileName());
	}*/
	
	public void sendFileUploadSuccessMessage(String fileName) {
		FacesMessage message = new FacesMessage("Sucesso. " + fileName + " foi carregado.");
        FacesContext.getCurrentInstance().addMessage(null, message);
	}
	
	public String doEdit(LDWProjectDTO projectSelected){ //Alterar isso para carregar os arquivos de conf uploadeados e mostrar os botões de download : Bruno
		this.ldwProjectSelected = projectSelected;
		this.model = ldwpoService.doLoadModel(projectSelected.getPath());
		LDWProject ldwProject = ldwProjectService.getLDWProject(model);
		this.ldWorkflow = ldwProject.getLdWorkFlow();	
		if(ldWorkflow == null) {
			doNew();
		}
		this.doLoadTools();
		
		this.xmlUploaded = null;
		this.smlUploaded = null;
		this.csvUploaded = null;
		
		return Navegacao.LDWORKFLOW_MAIN;
	}
	
	public void doNew() {
		ldWorkflow = ldWorkflowService.doNewWorkflow(model);
	}
	public void doSave() { //Alterar isto para exibir uma msg de sucesso ao final : Bruno
		model = ldWorkflowService.writeLdwWorkflow(model, ldWorkflow, ldwProjectSelected);
		
		if(smlUploaded != null) {
			ldwpoService.saveFile(smlUploaded, ldwProjectSelected.getName(), "mapping", "tool_configs");
		}
		if(xmlUploaded != null) {
			ldwpoService.saveFile(xmlUploaded, ldwProjectSelected.getName(), "linkingMapping", "tool_configs");
		}
		if(csvUploaded != null) {
			ldwpoService.saveFile(csvUploaded, ldwProjectSelected.getName(), "dataset", "csv");
		}
		this.ldwpoService.doSaveModel(model, ldwProjectSelected.getFileName());

	}
	
	public void doLoadTools() {
		this.listToolsStep01 = this.ldwStepService.getListToolsStep01(model);
		this.listToolsStep02 = this.ldwStepService.getListToolsStep02(model);
		this.listToolsStep03 = this.ldwStepService.getListToolsStep03(model);
		this.listToolsStep04 = this.ldwStepService.getListToolsStep04(model);
		this.listToolsStep05 = this.ldwStepService.getListToolsStep05(model);
		
		this.step01 = ldWorkflow.getLdwSteps().get(0);
		this.step02 = ldWorkflow.getLdwSteps().get(1);
		this.step03 = ldWorkflow.getLdwSteps().get(2);
		this.step04 = ldWorkflow.getLdwSteps().get(3);
		this.step05 = ldWorkflow.getLdwSteps().get(4);
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
		if(smlUploaded == null) {
			MessageUtil.showError("crud.file.notfound");
			return false;
		}
		if (smlUploaded.getSize() <= 0) {
			MessageUtil.showError("crud.file.notfound");
			
		}
		String extensao = FilenameUtils.getExtension(smlUploaded.getFileName());
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
	
	public StreamedContent getXmlDownload() {
		return xmlDownload;
	}

	public void setXmlDownload(StreamedContent xmlDownload) {
		this.xmlDownload = xmlDownload;
	}

	public UploadedFile getXmlUploaded() {
		return xmlUploaded;
	}

	public void setXmlUploaded(UploadedFile xmlUploaded) {
		this.xmlUploaded = xmlUploaded;
	}

	public List<Tool> getListToolsStep01() {
		return listToolsStep01;
	}

	public void setListToolsStep01(List<Tool> listToolsStep01) {
		this.listToolsStep01 = listToolsStep01;
	}

	public List<Tool> getListToolsStep02() {
		return listToolsStep02;
	}
	
	public void setListToolsStep02(List<Tool> listToolsStep02) {
		this.listToolsStep02 = listToolsStep02;
	}

	public List<Tool> getListToolsStep03() {
		return listToolsStep03;
	}

	public void setListToolsStep03(List<Tool> listToolsStep03) {
		this.listToolsStep03 = listToolsStep03;
	}

	public List<Tool> getListToolsStep04() {
		return listToolsStep04;
	}

	public void setListToolsStep04(List<Tool> listToolsStep04) {
		this.listToolsStep04 = listToolsStep04;
	}

	public List<Tool> getListToolsStep05() {
		return listToolsStep05;
	}

	public void setListToolsStep05(List<Tool> listToolsStep05) {
		this.listToolsStep05 = listToolsStep05;
	}

	public StreamedContent getCsvDownload() {
		return csvDownload;
	}

	public void setCsvDownload(StreamedContent csvDownload) {
		this.csvDownload = csvDownload;
	}

	public UploadedFile getCsvUploaded() {
		return csvUploaded;
	}

	public void setCsvUploaded(UploadedFile csvUploaded) {
		this.csvUploaded = csvUploaded;
	}

	/*public UploadedFile getXmlScriptUploaded() {
		return xmlScriptUploaded;
	}

	public void setXmlScriptUploaded(UploadedFile xmlScriptUploaded) {
		this.xmlScriptUploaded = xmlScriptUploaded;
	}

	public UploadedFile getSmlScriptUploaded() {
		return smlScriptUploaded;
	}

	public void setSmlScriptUploaded(UploadedFile smlScriptUploaded) {
		this.smlScriptUploaded = smlScriptUploaded;
	}*/

	/*public StreamedContent getSmlScriptDownload() {
		return smlScriptDownload;
	}

	public void setSmlScriptDownload(StreamedContent smlScriptDownload) {
		this.smlScriptDownload = smlScriptDownload;
	}

	public StreamedContent getXmlScriptDownload() {
		return xmlScriptDownload;
	}

	public void setXmlScriptDownload(StreamedContent xmlScriptDownload) {
		this.xmlScriptDownload = xmlScriptDownload;
	}*/
}