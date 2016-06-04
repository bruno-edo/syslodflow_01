package br.ufsc.inf.syslodflow.enumerator;

public enum IndividualEnum {
	
	LOCATION_REPORT("http://ldwpo.aksw.org/terms/1.0/location_reports"),
	TASK_EXTRACT_DATA_LEGACYSYS("http://ldwpo.aksw.org/terms/1.0/task_extracting_data_from_legacy_systems"),
	TASK_CONVERTING_DATA("http://ldwpo.aksw.org/terms/1.0/task_extracting_converting_data"),
	TASK_STORAGE_GRAPH("http://ldwpo.aksw.org/terms/1.0/task_storage_graph"),
	TASK_INTERLINKING("http://ldwpo.aksw.org/terms/1.0/task_interlinking_test"),
	FORMAT_CSV("http://ldwpo.aksw.org/terms/1.0/format_csv");
	
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
