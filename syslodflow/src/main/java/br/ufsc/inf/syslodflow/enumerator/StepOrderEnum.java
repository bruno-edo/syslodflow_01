package br.ufsc.inf.syslodflow.enumerator;

public enum StepOrderEnum {
	
	FIRST(1),
	SECOND(2),
	THIRD(3),
	FOURTH(4),
	FIFTH(5);
	
	private int order;

	private StepOrderEnum(int order) {
		this.order = order;
	}

	public int getOrder() {
		return order;
	}

	public void setOrder(int order) {
		this.order = order;
	}
	
}
