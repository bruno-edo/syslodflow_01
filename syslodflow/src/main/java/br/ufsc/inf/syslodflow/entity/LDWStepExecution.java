package br.ufsc.inf.syslodflow.entity;


public class LDWStepExecution extends BaseModel {
	
	private Status status;
	private Message message;
	private Person contributor;
	private String description;
	private String name;
	private String startedDate;
	private String endedDate;
	private String uri;
	private int order;
	
	public LDWStepExecution() {
		
	}
	
	public LDWStepExecution(String name,
			String description, Status status, Message message,
			Person contributor, String startedDate,
			String endedDate, int order, String uri) {
		
		this.name = name;
		this.description = description;
		this.status = status;
		this.message = message;
		this.contributor = contributor;
		this.startedDate = startedDate;
		this.endedDate = endedDate;
		this.order = order;
		this.uri = uri;
	}

	public Status getStatus() {
		return status;
	}
	public void setStatus(Status status) {
		this.status = status;
	}
	public Message getMessage() {
		return message;
	}
	public void setMessage(Message message) {
		this.message = message;
	}
	public Person getContributor() {
		return contributor;
	}
	public void setContributor(Person contributor) {
		this.contributor = contributor;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getStartedDate() {
		return startedDate;
	}
	public void setStartedDate(String startedDate) {
		this.startedDate = startedDate;
	}
	public String getEndedDate() {
		return endedDate;
	}
	public void setEndedDate(String endedDate) {
		this.endedDate = endedDate;
	}
	
	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
	
	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
	
	
	
}