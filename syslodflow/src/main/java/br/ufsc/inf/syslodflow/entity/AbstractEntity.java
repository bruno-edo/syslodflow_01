package br.ufsc.inf.syslodflow.entity;

import java.io.Serializable;


@SuppressWarnings("serial")
public abstract class AbstractEntity implements Serializable {
	public abstract int getId();
	
    @Override
    public String toString() {
    	return this.getClass().getName() + "[id=" + getId() + "]";
    }	
        
    @Override
    public boolean equals(Object object) {
    	if (!object.getClass().toString().equalsIgnoreCase(this.getClass().toString()))	{
    		return false;
    	}
    	
    	if ((((AbstractEntity) object).getId() > 0) && (((AbstractEntity) object).getId() == this.getId()))	{
    		return true;
    	}    		
    	
    	return false;
    }       
}
