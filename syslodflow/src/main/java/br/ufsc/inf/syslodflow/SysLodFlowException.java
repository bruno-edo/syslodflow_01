package br.ufsc.inf.syslodflow;
/**
 * Exce&ccedil;&atilde;o base.
 * @author carloshp
 * (c)2014-2015 COMDAT Ltda. Todos os direitos reservados.
 */
@SuppressWarnings("serial")
public class SysLodFlowException extends Exception {

	public SysLodFlowException() {
		super();
	}

	public SysLodFlowException(String message, Throwable cause) {
		super(message, cause);
	}

	public SysLodFlowException(String message) {
		super(message);
	}

	public SysLodFlowException(Throwable cause) {
		super(cause);
	}
	
}
