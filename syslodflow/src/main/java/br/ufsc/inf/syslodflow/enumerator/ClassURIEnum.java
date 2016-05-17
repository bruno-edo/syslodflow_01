package br.ufsc.inf.syslodflow.enumerator;

public enum ClassURIEnum {
	LDWPROJECT("http://ldwpo.aksw.org/terms/1.0/LDWProject"),
	PERSON("http://ldwpo.aksw.org/terms/1.0/Person"),
	HOMEPAGE("http://ldwpo.aksw.org/terms/1.0/Homepage"), 
	LOCATION("http://ldwpo.aksw.org/terms/1.0/Location"),
	REPORT("http://ldwpo.aksw.org/terms/1.0/Report");
	
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
