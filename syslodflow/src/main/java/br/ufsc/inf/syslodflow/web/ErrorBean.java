package br.ufsc.inf.syslodflow.web;

import java.io.Serializable;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.context.FacesContext;

@SuppressWarnings("serial")
@ManagedBean
@RequestScoped
public class ErrorBean implements Serializable {

	public ErrorBean() {		
	}
	
	private Object getInfo(String key) {
		return FacesContext.getCurrentInstance().getExternalContext().getRequestMap().get(key);
	}
	
	public String getStatusCode () {
		Integer i = (Integer)getInfo("javax.servlet.error.status_code"); 
		return (i != null ? i.toString() : "???");
	}	

	public String getMessage() {
		String str = (String)getInfo("javax.servlet.error.message"); 
		return (str != null ? str : "???");
	}
	
	public String getExceptionType () {
		String str = (String)getInfo("javax.servlet.error.exception_type"); 
		return (str != null ? str : "???");
	}
	
	public String getException () {
		Exception e = (Exception)getInfo("javax.servlet.error.exception");
		return (e != null ? e.toString() : "???");
	}
	
	public String getRequestUri () {
		String str = (String)getInfo("javax.servlet.error.request_uri"); 
		return (str != null ? str : "???");
	}
	
	public String getServletName () {
		String str = (String)getInfo("javax.servlet.error.servlet_name"); 
		return (str != null ? str : "???");		
	}
}
