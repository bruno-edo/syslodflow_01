package br.ufsc.inf.syslodflow.web;

import java.io.IOException;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;

import com.hp.hpl.jena.ontology.OntModel;

import br.ufsc.inf.syslodflow.dto.LDWProjectDTO;
import br.ufsc.inf.syslodflow.entity.LDWProject;
import br.ufsc.inf.syslodflow.entity.LDWStepExecution;
import br.ufsc.inf.syslodflow.entity.LDWorkflow;
import br.ufsc.inf.syslodflow.entity.LDWorkflowExecution;
import br.ufsc.inf.syslodflow.service.LdWorkflowExecutionService;
import br.ufsc.inf.syslodflow.service.LdWorkflowService;
import br.ufsc.inf.syslodflow.service.LdwProjectService;
import br.ufsc.inf.syslodflow.service.LdwpoService;
import br.ufsc.inf.syslodflow.util.MessageUtil;

/**
 * @author jeanmorais
 */
@ManagedBean(name="ldWorkflowExecutionBean")
@SessionScoped
public class LDWorkflowExecutionBean {
	
	
	@Inject 
	private LdwpoService ldwpoService;
	@Inject 
	private LdwProjectService ldwProjectService;
	@Inject
	private LdWorkflowService ldWorkflowService;
	@Inject
	private LdWorkflowExecutionService ldwWorkflowExecutionService;
	
	private OntModel model;
	private DataModel<LDWorkflowExecution> listWorkflowExecutions;
	private LDWorkflowExecution ldWorkflowExecution;
	private LDWorkflow workflow;
	private LDWProjectDTO ldwProjectSelected;
	private List<LDWProjectDTO> listLdwProjects;
	
	private String ldWorkflowExecutionName;
	
	/**
	 * Steps
	 */
	private LDWStepExecution stepExecution01;
	private LDWStepExecution stepExecution02;
	private LDWStepExecution stepExecution03;
	private LDWStepExecution stepExecution04;
	private LDWStepExecution stepExecution05;

	@PostConstruct
	private void init() {
		ldWorkflowExecution = new LDWorkflowExecution();
		listLdwProjects = ldwProjectService.getListLdwProjectDTO();
	}
	
	public void doSave() {
		model = ldwWorkflowExecutionService.writeLDWorkflowExecution(model, ldWorkflowExecution, workflow);
		this.ldwpoService.doSaveModel(model, ldwProjectSelected.getFileName());
	}
	
	public void doExecute() { //TODO: Finish implementing this method in a way that the LODFlowEngine can read and interpret the .owl document		
		if(ldWorkflowExecutionName != null && !ldWorkflowExecutionName.equals("")) {
			FacesContext fc = FacesContext.getCurrentInstance();
			
			String lodflowEnginePath = fc.getExternalContext().getRealPath("/");
			lodflowEnginePath += "WEB-INF/lib/lodflowEngine.jar";
			
			String[] projectUriIdentifierArr = ldwProjectSelected.getUri().split("/");
			String projectUriIdentifier = projectUriIdentifierArr[projectUriIdentifierArr.length - 1];
			
			String projectParam = "ldwpo:" + projectUriIdentifier;
			String ldworkflowexecutionParam = "ldwpo:" + ldWorkflowExecutionName;
			
			String ontologypath = fc.getExternalContext().getInitParameter("filePath").toString() + ldwProjectSelected.getFileName();
			
			String lodflowExecutionCommand = "java -jar " + lodflowEnginePath;
			lodflowExecutionCommand += " \"" + ontologypath + "\"";
			lodflowExecutionCommand += " \"" + projectParam + "\"";
			lodflowExecutionCommand += " \"" + ldworkflowexecutionParam + "\"";
			
			System.out.println("lodflowExecutionCommand: " + lodflowExecutionCommand);
			
			try {
				Runtime.getRuntime().exec(lodflowExecutionCommand);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}		
	}
	public void doChangeProject() {
		try {
			this.model = ldwpoService.doLoadModel(ldwProjectSelected.getPath());
			LDWProject ldwProject = ldwProjectService.getLDWProject(model);
			this.workflow = ldwProject.getLdWorkFlow();
			this.listWorkflowExecutions = new ListDataModel<LDWorkflowExecution>(ldwProject.getLdWorkFlow().getLdWorkFlowExecutions());
		} catch(Exception e) {
			e.printStackTrace();
			MessageUtil.showError("crud.ldworkflowexecution.notfound");
		}
		
	}
	
	public void doLoadStepsExecution() {
		this.stepExecution01 = ldWorkflowExecution.getLdwStepExecutions().get(0);
		this.stepExecution02 = ldWorkflowExecution.getLdwStepExecutions().get(1);
		this.stepExecution03 = ldWorkflowExecution.getLdwStepExecutions().get(2);
		this.stepExecution04 = ldWorkflowExecution.getLdwStepExecutions().get(3);
		this.stepExecution05 = ldWorkflowExecution.getLdwStepExecutions().get(4);
		
	}
	
	public void doEdit() {
		this.ldWorkflowExecution = listWorkflowExecutions.getRowData();
		this.doLoadStepsExecution();
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		try {
			context.redirect(context.getRequestContextPath() + "/management/ldworkflowexecution_view.xhtml");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public LDWProjectDTO getLdwProjectSelected() {
		return ldwProjectSelected;
	}


	public void setLdwProjectSelected(LDWProjectDTO ldwProjectSelected) {
		this.ldwProjectSelected = ldwProjectSelected;
	}

	public List<LDWProjectDTO> getListLdwProjects() {
		return listLdwProjects;
	}

	public void setListLdwProjects(List<LDWProjectDTO> listLdwProjects) {
		this.listLdwProjects = listLdwProjects;
	}

	public DataModel<LDWorkflowExecution> getListWorkflowExecutions() {
		return listWorkflowExecutions;
	}

	public void setListWorkflowExecutions(
			DataModel<LDWorkflowExecution> listWorkflowExecutions) {
		this.listWorkflowExecutions = listWorkflowExecutions;
	}

	public LDWorkflowExecution getLdWorkflowExecution() {
		return ldWorkflowExecution;
	}

	public void setLdWorkflowExecution(LDWorkflowExecution ldWorkflowExecution) {
		this.ldWorkflowExecution = ldWorkflowExecution;
	}

	public LDWorkflow getWorkflow() {
		return workflow;
	}

	public void setWorkflow(LDWorkflow workflow) {
		this.workflow = workflow;
	}

	public LDWStepExecution getStepExecution01() {
		return stepExecution01;
	}

	public void setStepExecution01(LDWStepExecution stepExecution01) {
		this.stepExecution01 = stepExecution01;
	}

	public LDWStepExecution getStepExecution02() {
		return stepExecution02;
	}

	public void setStepExecution02(LDWStepExecution stepExecution02) {
		this.stepExecution02 = stepExecution02;
	}

	public LDWStepExecution getStepExecution03() {
		return stepExecution03;
	}

	public void setStepExecution03(LDWStepExecution stepExecution03) {
		this.stepExecution03 = stepExecution03;
	}

	public LDWStepExecution getStepExecution04() {
		return stepExecution04;
	}

	public void setStepExecution04(LDWStepExecution stepExecution04) {
		this.stepExecution04 = stepExecution04;
	}

	public LDWStepExecution getStepExecution05() {
		return stepExecution05;
	}

	public void setStepExecution05(LDWStepExecution stepExecution05) {
		this.stepExecution05 = stepExecution05;
	}

	public String getLdWorkflowExecutionName() {
		return ldWorkflowExecutionName;
	}


	public void setLdWorkflowExecutionName(String ldWorkflowExecutionName) {
		this.ldWorkflowExecutionName = ldWorkflowExecutionName;
	}
}
