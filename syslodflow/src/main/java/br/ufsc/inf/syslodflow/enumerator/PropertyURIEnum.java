package br.ufsc.inf.syslodflow.enumerator;

public enum PropertyURIEnum {
	NAME("http://ldwpo.aksw.org/terms/1.0/name"),
	DESCRIPTION("http://ldwpo.aksw.org/terms/1.0/description"),
	GOAL("http://ldwpo.aksw.org/terms/1.0/goal"),
	HOMEPAGE("http://ldwpo.aksw.org/terms/1.0/homepage"),
	LOCATION("http://ldwpo.aksw.org/terms/1.0/location"),
	VALUE("http://ldwpo.aksw.org/terms/1.0/value"),
	CREATOR("http://ldwpo.aksw.org/terms/1.0/creator"),
	REPORT("http://ldwpo.aksw.org/terms/1.0/report"),
	PRECONDITION("http://ldwpo.aksw.org/terms/1.0/precondition"),
	POSTCONDITION("http://ldwpo.aksw.org/terms/1.0/precondition"),
	LDWORKFLOW("http://ldwpo.aksw.org/terms/1.0/ldworkflow"),
	LDWSTEP("http://ldwpo.aksw.org/terms/1.0/ldwStep"),
	FIRSTLDWSTEP("http://ldwpo.aksw.org/terms/1.0/firstLdwStep"),
	LDWORKFLOWEXECUTION("http://ldwpo.aksw.org/terms/1.0/ldWorkflowExecution");
	
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
