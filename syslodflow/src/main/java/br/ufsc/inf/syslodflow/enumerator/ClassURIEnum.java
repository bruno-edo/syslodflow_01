package br.ufsc.inf.syslodflow.enumerator;

public enum ClassURIEnum {
	LDWPROJECT("http://ldwpo.aksw.org/terms/1.0/LDWProject"),
	LDWORKFLOW("http://ldwpo.aksw.org/terms/1.0/LDWorkflow"),
	LDWSTEP("http://ldwpo.aksw.org/terms/1.0/LDWStep"),
	CONDITION("http://ldwpo.aksw.org/terms/1.0/Condition"),
	PERSON("http://ldwpo.aksw.org/terms/1.0/Person"),
	HOMEPAGE("http://ldwpo.aksw.org/terms/1.0/Homepage"), 
	LOCATION("http://ldwpo.aksw.org/terms/1.0/Location"),
	TOOL("http://ldwpo.aksw.org/terms/1.0/Tool"),
	TOOLCONFIGURATION("http://ldwpo.aksw.org/terms/1.0/ToolConfiguration"),
	REPORT("http://ldwpo.aksw.org/terms/1.0/Report"),
	FORMAT("http://ldwpo.aksw.org/terms/1.0/Format"),
	DATASET("http://ldwpo.aksw.org/terms/1.0/Dataset"),
	LICENSE("http://ldwpo.aksw.org/terms/1.0/License");
	
	private String uri;
	
	ClassURIEnum(String uri) {
		this.uri = uri;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
	
}
