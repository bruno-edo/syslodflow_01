package br.ufsc.inf.syslodflow.dao;

import java.util.List;

import br.ufsc.inf.syslodflow.SysLodFlowException;
import br.ufsc.inf.syslodflow.entity.AbstractEntity;

/**
 * Interface padr&atilde;o para todas as implementa&ccedil;&otilde;es do pattern <code>Data Acquisition Object</code>.<br>
 * 
 * @author silvachp
 * (c)2014,2015 COMDAT Sistemas Ltda. Todos os direitos reservados.
 */
public interface GenericDAO {

	/**
	 * Insere um novo <code>AbstractEntity</code>.
	 * @param e
	 * @return
	 * @throws ComdatException
	 */
	public AbstractEntity insert(AbstractEntity e)	throws SysLodFlowException;

	/**
	 * Altera um <code>AbstractEntity</code>.
	 * @param e <code>AbstractEntity</code> com o id do objeto que se deseja alterar. Note que n&atilde;o precisa ser um objeto gerenciado pelo EntityManager; basta que o id seja o de um objeto j&aacute; existente na base.
	 * @return
	 * @throws ComdatException
	 */
	public AbstractEntity update(AbstractEntity e)	throws SysLodFlowException;

	/**
	 * Exclui um <code>AbstractEntity</code>.
	 * @param e <code>AbstractEntity</code> com o id do objeto que se deseja excluir. Note que n&atilde;o precisa ser um objeto gerenciado pelo EntityManager; basta que o id seja o de um objeto j&aacute; existente na base.
	 * @return
	 * @throws ComdatException
	 */
	public void delete(AbstractEntity e) throws SysLodFlowException;

	/**
	 * Exclui um <code>AbstractEntity</code>.
	 * @param e <code>AbstractEntity</code> com o id do objeto que se deseja excluir. Note que n&atilde;o precisa ser um objeto gerenciado pelo EntityManager; basta que o id seja o de um objeto j&aacute; existente na base.
	 * @return
	 * @throws ComdatException
	 */
	@SuppressWarnings("rawtypes")
	public void deleteById(Class c, int id) throws SysLodFlowException;

	/**
	 * Recupera um objeto que extende <code>AbstractEntity</code> a partir do seu id.
	 * @param c Classe do objeto buscado. Deve necessariamente extender <code>AbstractEntity</code>.
	 * @param id 
	 * @return Objeto buscado, ou null se este n&atilde;o existir.
	 */
	@SuppressWarnings("rawtypes")
	public AbstractEntity findById(Class c, int id);

	/**
	 * Retorna uma lista de todos os objetos da classe informada existentes na base, ordenados por id.
	 * @param c Classe do objeto buscado. Deve extender <code>AbstractEntity</code>.
	 * @return <code>List&lt;AbstractEntity&gt;</code>
	 */
	@SuppressWarnings("rawtypes")
	public List<AbstractEntity> findAll(Class c);
	
	/**
	 * Permite a inclus&atilde;o de m&uacute;ltiplos itens em uma &uacute;nica transa&ccedil;&atilde;o.<br>
	 * Pode n&atilde;o ser suportado ou n&atilde;o ter diferen&ccedil;a significativa de performance em algumas implementa&ccedil;&otilde;es.
	 * @param l Cole&ccedil;&atilde;o de <code>AbstractEntity</code> para inserir.
	 * @throws ComdatException 
	 */
	public void insert (List<AbstractEntity> l) throws SysLodFlowException;
	
	/**
	 * Permite a altera&ccedil;&atilde;o de m&uacute;ltiplos itens em uma &uacute;nica transa&ccedil;&atilde;o.<br>
	 * Pode n&atilde;o ser suportado ou n&atilde;o ter diferen&ccedil;a significativa de performance em algumas implementa&ccedil;&otilde;es.
	 * @param l Cole&ccedil;&atilde;o de <code>AbstractEntity</code> para alterar.
	 * @throws ComdatException 
	 */
	public void update (List<AbstractEntity> l) throws SysLodFlowException;

	/**
	 * Permite a exclus&atilde;o de m&uacute;ltiplos itens em uma &uacute;nica transa&ccedil;&atilde;o.<br>
	 * Pode n&atilde;o ser suportado ou n&atilde;o ter diferen&ccedil;a significativa de performance em algumas implementa&ccedil;&otilde;es.
	 * @param l Cole&ccedil;&atilde;o de <code>AbstractEntity</code> para excluir.
	 * @throws ComdatException 
	 */
	public void delete (List<AbstractEntity> l) throws SysLodFlowException;

	/**
	 * Seta o n&uacute;mero de itens para opera&ccedil;&atilde;o em lote.
	 * @param batchCount
	 */
	public void setBatchCount (int batchCount);
	
	/**
	 * Retorna o n&uacute;mero de itens para opera&ccedil;&atilde;o em lote.
	 * @return N&uacute;mero de itens para opera&ccedil;&atilde;o em lote.
	 */
	public int getBatchCount ();
}