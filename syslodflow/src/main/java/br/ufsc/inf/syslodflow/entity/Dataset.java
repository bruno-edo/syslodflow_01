package br.ufsc.inf.syslodflow.entity;

public class Dataset {
	
	private Format format;
	private License license;
	private Location location;
	private String name;
	
	public Dataset() {
		
	}
	
	public Dataset(String name, Format format, License license,
			Location location) {
		this.name = name;
		this.format = format;
		this.location = location;
		this.license = license;
	}

	public Format getFormat() {
		return format;
	}
	public void setFormat(Format format) {
		this.format = format;
	}
	public License getLicense() {
		return license;
	}
	public void setLicense(License license) {
		this.license = license;
	}
	public Location getLocation() {
		return location;
	}
	public void setLocation(Location location) {
		this.location = location;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	
}