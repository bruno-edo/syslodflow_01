package br.ufsc.inf.syslodflow.dto;

import java.nio.file.Path;

public class LDWProjectDTO {
	
	private String name;
	private String creator;
	private String fileName;
	private Path path;
	private String uri;
	
	public LDWProjectDTO() {
		
	}

	public LDWProjectDTO(String name, String creator, String uri) {
		this.name = name;
		this.creator = creator;
		this.uri = uri;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCreator() {
		return creator;
	}

	public void setCreator(String creator) {
		this.creator = creator;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}

	public String getUri() {
		return uri;
	}

	public void setUri(String uri) {
		this.uri = uri;
	}
	
	
	
	

}
