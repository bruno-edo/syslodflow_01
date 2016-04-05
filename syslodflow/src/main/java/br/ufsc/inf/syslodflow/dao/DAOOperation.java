package br.ufsc.inf.syslodflow.dao;

/**
 * Enumera&ccedil;&atilde;o que descreve as opera&ccedil;&otilde;es do DAO audit&aacute;veis.<br>
 * @author silvachp
 * (c)2014-2015 COMDAT Ltda. Todos os direitos reservados.
 */
public enum DAOOperation {
	CREATE {
		@Override
		public String toString () {
			return "C";
		}
	},
	RETRIEVE {
		@Override
		public String toString () {
			return "R";
		}
	},
	UPDATE {
		@Override
		public String toString () {
			return "U";
		}
	},
	DELETE {
		@Override
		public String toString () {
			return "D";
		}
	}
}
