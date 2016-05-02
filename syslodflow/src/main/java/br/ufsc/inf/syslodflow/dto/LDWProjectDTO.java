package br.ufsc.inf.syslodflow.dto;

import java.nio.file.Path;

public class LDWProjectDTO {
	
	private String name;
	private String creator;
	private Path path;
	
	public LDWProjectDTO() {
		
	}

	public LDWProjectDTO(String name, String creator) {
		this.name = name;
		this.creator = creator;
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

	public Path getPath() {
		return path;
	}

	public void setPath(Path path) {
		this.path = path;
	}
	
	

	

}
