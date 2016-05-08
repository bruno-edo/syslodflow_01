package br.ufsc.inf.syslodflow.enumerator;

public enum PropertyURIEnum {
	NAME("http://ldwpo.aksw.org/terms/1.0/name"),
	DESCRIPTION("http://ldwpo.aksw.org/terms/1.0/description"),
	GOAL("http://ldwpo.aksw.org/terms/1.0/goal"),
	HOMEPAGE("http://ldwpo.aksw.org/terms/1.0/homepage"),
	LOCATION("http://ldwpo.aksw.org/terms/1.0/location"),
	VALUE("http://ldwpo.aksw.org/terms/1.0/value"),
	CREATOR("http://ldwpo.aksw.org/terms/1.0/creator"),
	CONTRIBUTOR("http://ldwpo.aksw.org/terms/1.0/contributor"),
	REPORT("http://ldwpo.aksw.org/terms/1.0/report"),
	PRECONDITION("http://ldwpo.aksw.org/terms/1.0/precondition"),
	POSTCONDITION("http://ldwpo.aksw.org/terms/1.0/precondition"),
	LDWORKFLOW("http://ldwpo.aksw.org/terms/1.0/ldworkflow"),
	LDWSTEP("http://ldwpo.aksw.org/terms/1.0/ldwStep"),
	LDWSTEPEXECUTION("http://ldwpo.aksw.org/terms/1.0/ldwStepExecution"),
	FIRSTLDWSTEP("http://ldwpo.aksw.org/terms/1.0/firstLdwStep"),
	LDWORKFLOWEXECUTION("http://ldwpo.aksw.org/terms/1.0/ldWorkflowExecution"),
	COMMAND("http://ldwpo.aksw.org/terms/1.0/command"),
	TASK("http://ldwpo.aksw.org/terms/1.0/task"),
	INPUTDATASET("http://ldwpo.aksw.org/terms/1.0/inputDataset"),
	OUTPUTDATASET("http://ldwpo.aksw.org/terms/1.0/outputDataset"),
	FORMAT("http://ldwpo.aksw.org/terms/1.0/format"),
	LICENSE("http://ldwpo.aksw.org/terms/1.0/license"),
	TOOL("http://ldwpo.aksw.org/terms/1.0/tool"),
	STATUS("http://ldwpo.aksw.org/terms/1.0/status"),
	TOOLCONFIGURATION("http://ldwpo.aksw.org/terms/1.0/toolConfiguration"),
	MESSAGE("http://ldwpo.aksw.org/terms/1.0/message");
	
	
	private String uri;
	
	PropertyURIEnum(String uri) {
		this.uri = uri;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	

}
