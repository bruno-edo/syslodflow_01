package br.ufsc.inf.syslodflow.enumerator;

public enum IndividualEnum {
	
	LOCATION_REPORT("http://ldwpo.aksw.org/terms/1.0/location_reports");
	
	private String uri;
	
	IndividualEnum(String uri) {
		this.uri = uri;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}

}
