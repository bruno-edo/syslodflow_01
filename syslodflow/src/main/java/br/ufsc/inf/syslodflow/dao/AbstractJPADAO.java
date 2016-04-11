package br.ufsc.inf.syslodflow.dao;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceContext;

import org.hibernate.exception.ConstraintViolationException;

import br.ufsc.inf.syslodflow.SysLodFlowException;
import br.ufsc.inf.syslodflow.entity.AbstractEntity;

/**
 * Implementa&ccedil;&atilde;o completa de <code>GenericDAO</code> baseada em JPA.<br>
 * Esta vers&atilde;o deve funcionar de forma transparente tanto com JTA como com transa&ccedil;&otilde;es gerenciadas localmente (<i>RESOURCE_LOCAL</i>).<br>
 * Quando usando transa&ccedil;&otilde;es locais o commit &eacute; aplicado automaticamente a cada opera&ccedil;&atilde;o de <code>insert(), update()</code> ou <code>delete()</code>.<br>
 * Opera&ccedil;&otilde;es em lote somente s&atilde;o suportadas em modo JTA, gerando uma <code>ComdatException</code> quando invocadas de outra forma.
 * @see GenericDAO
 * @author silvachp
 * (c)2014-2015 COMDAT ltda. Todos os direitos reservados.
 *
 */
public abstract class AbstractJPADAO implements GenericDAO {
	
	public abstract String getPUName();

	@PersistenceContext
	private EntityManager em = null;

	private boolean useJTA = true;
	private boolean checkForJTA = true;	
	private int batchCount = 50;	
	private String login = null;
	
//	private static Logger logger = Logger.getLogger(AbstractJPADAO.class.getSimpleName());
		
	public AbstractJPADAO() {
		
	}
	
	public AbstractJPADAO (String login) {
		this.login = login;
	}
	
	/**
	 * Verifica se o suporte a opera&ccedil;&otilde;es em lote est&aacute; habilitado.<br>
	 * Basicamente isso depende da configura&ccedil;&atilde;o de datasource em uso (habilitado para modo <code>JTA</code>, desabilidado para <code>RESOURCE_LOCAL</code>)<br>
	 * 
	 * @return
	 */
	public boolean isBatchEnabled () {
		if (checkForJTA) {
			getEntityManager();
		}
		
		return useJTA;
	}
	

	/**
	 * Usado apenas para testes unit&aacute;rios.
	 * @param em
	 */
	public void setEntityManager (EntityManager em) {
		this.em = em; 
	}
	
	
	/**
	 * Recupera o <code>EntityManager</code> em uso pelo DAO.<br>
	 * Adicionalmente, seta a flag <code>useJTA</code>.
	 * @return
	 */
	protected EntityManager getEntityManager() {
		if (checkForJTA) {
			checkForJTA = false;
			try {
				em.getTransaction().begin();
				em.getTransaction().rollback();
				useJTA = false;
			} catch (IllegalStateException e) {
				useJTA = true;
			}
		}
		
		return em;
	}

	/**
	 * Como este &eacute; o &uacute;nico m&eacute;todo desta classe onde a auditoria &eacute; ativada <i>ap&oacute;s</i> 
	 * a opera&ccedil;&atilde;o realizada (a fim de obter o id da entidade), em caso de falha no m&eacute;todo 
	 * <code>logOperation()</code> a transa&ccedil;&atilde;o &eacute; revertida (<i>rollback</i>) e o estado da entidade 
	 * passada como argumento &eacute; alterado (espec&iacute;ficamente, o valor de <code>id</code> &eacute; atribu&iacute;do), 
	 * devendo ser <strong>DESCONSIDERADO</strong>.<br>
	 * @see br.ufsc.inf.syslodflow.dao.com.comdat.entity.GenericDAO#insert(br.com.comdat.entity.AbstractEntity)
	 */
	@Override
	public AbstractEntity insert(AbstractEntity e) throws SysLodFlowException {
		try {
			if (!useJTA)	{
				getEntityManager().getTransaction().begin();
			}
			
			getEntityManager().persist(e);
			if (!useJTA)	{
				getEntityManager().getTransaction().commit();
			}
			
			return e;
		}catch (Exception ex) {
			if (!useJTA)	{
				if (getEntityManager().getTransaction().isActive())	{								
					getEntityManager().getTransaction().rollback();				
				}
			}
			if(ex.getCause() instanceof ConstraintViolationException){
				throw new SysLodFlowException(ex.getCause());			
			}else{
				throw new SysLodFlowException(ex);
			}
		}
	}

	/* (non-Javadoc)
	 * @see br.com.comdat.entity.GenericDAO#update(br.com.comdat.entity.AbstractEntity)
	 */
	@Override
	public AbstractEntity update(AbstractEntity e) throws SysLodFlowException {
		try{
			AbstractEntity e2 = getEntityManager().find(e.getClass(), e.getId());
			if (e2 != null) {
				if (!useJTA)
				{
					getEntityManager().getTransaction().begin();
				}
	
				e = getEntityManager().merge(e);		
				getEntityManager().flush();
				if (!useJTA)
				{
					getEntityManager().getTransaction().commit();
				}
				
				return e;
			}
		}catch (Exception ex) {
			if (!useJTA)	{
				if (getEntityManager().getTransaction().isActive())	{								
					getEntityManager().getTransaction().rollback();				
				}
			}
			if(ex.getCause() instanceof ConstraintViolationException){
				throw new SysLodFlowException(ex.getCause());			
			}else{
				throw new SysLodFlowException(ex);
			}
		}
		throw new SysLodFlowException ("Nao foi encontrado " + e.getClass().getSimpleName() + " com id: " + e.getId());
	}
	
	/* (non-Javadoc)
	 * @see br.com.comdat.entity.GenericDAO#delete(br.com.comdat.entity.AbstractEntity)
	 */	
	@Override
	public void delete (AbstractEntity e) throws SysLodFlowException {
		try{
			AbstractEntity e2 = getEntityManager().find(e.getClass(), e.getId()); 		
			if (e2 != null) {
				if (!useJTA)
				{
					getEntityManager().getTransaction().begin();
				}
				
				getEntityManager().remove(e2);			
				if (!useJTA)
				{
					getEntityManager().getTransaction().commit();
				}
				
				return;
			}
		}catch (Exception ex) {
			if (!useJTA)	{
				if (getEntityManager().getTransaction().isActive())	{								
					getEntityManager().getTransaction().rollback();				
				}
			}
			throw new SysLodFlowException(ex);
			
		}
		throw new SysLodFlowException ("Nao foi encontrado " + e.getClass().getSimpleName() + " com id: " + e.getId());
	}
	
	/* (non-Javadoc)
	 * @see br.com.comdat.entity.GenericDAO#deleteById(java.lang.Class, int)
	 */	
	@Override
	@SuppressWarnings("rawtypes")
	public void deleteById (Class c, int id) throws SysLodFlowException {
		try{
			StringBuilder sb = new StringBuilder(); 
			
			sb.append("DELETE FROM ")
				.append(c.getSimpleName())
				.append("  WHERE id = :id");
			
			if (!useJTA)
			{
				getEntityManager().getTransaction().begin();
			}
			
			AbstractEntity e = findById(c, id);
			if (e == null) {
				if (!useJTA)
				{
					getEntityManager().getTransaction().rollback();
				}
				
				throw new SysLodFlowException ("Nao foi encontrado " + c.getSimpleName() + " com id = " + id);
			}
			
			getEntityManager().createQuery(sb.toString()).setParameter("id", id).executeUpdate();				
			if (!useJTA)
			{
				getEntityManager().getTransaction().commit();
			}
		}catch (Exception ex) {
			if (!useJTA)	{
				if (getEntityManager().getTransaction().isActive())	{								
					getEntityManager().getTransaction().rollback();				
				}
			}
			if(ex.getCause() instanceof ConstraintViolationException){
				throw new SysLodFlowException(ex.getCause());			
			}else{
				throw new SysLodFlowException(ex);
			}		
		}
	}
	
	
	/* (non-Javadoc)
	 * @see br.com.comdat.entity.GenericDAO#findById(java.lang.Class, int)
	 */
	@Override
	@SuppressWarnings("rawtypes")
	public AbstractEntity findById (Class c, int id) {
		try {
			StringBuilder sb = new StringBuilder();
			
			sb.append("SELECT e FROM ")
				.append(c.getSimpleName())
				.append(" e WHERE e.id = :id");
			
			// TODO Como tratar auditoria neste caso ?
			return (AbstractEntity) getEntityManager()
				.createQuery(sb.toString())
				.setParameter("id", id)
				.getSingleResult();		
		} catch (NoResultException nre) {
			return null;
		}
	}	
	
	/* (non-Javadoc)
	 * @see br.com.comdat.entity.GenericDAO#findAll(java.lang.Class)
	 */
	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<AbstractEntity> findAll (Class c) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT e FROM ")
			.append(c.getSimpleName())
			.append(" e ORDER BY e.id");
		
		// TODO Como tratar auditoria neste caso ?
		return (List<AbstractEntity>) getEntityManager()
			.createQuery(sb.toString())
			.getResultList();		
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<AbstractEntity> findAllMax (Class c, int max) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT e FROM ")
			.append(c.getSimpleName())
			.append(" e WHERE e.ativo = 1 ")
			.append(" ORDER BY e.id");
		
		// TODO Como tratar auditoria neste caso ?
		return (List<AbstractEntity>) getEntityManager()
			.createQuery(sb.toString()).setMaxResults(max)
			.getResultList();		
	}
	
	@Override
	public int getBatchCount () {
		return this.batchCount;
	}
	
	/**
	 * Seta o tamanho do lote para opera&ccedil;&otilde;es de <code>insert(), uptade()</code> ou <code>delete()</code>.<br>
	 * <strong>IMPORTANTE:</strong> Para que opera&ccedil;&otilde;es em lote funcionem corretamente, 
	 * &eacute; necess&aacute;rio configurar as seguintes propriedades no <code>persistence.xml</code>:<br><br>
	 * <code>&lt;property name="hibernate.jdbc.batch_size" value="</code><i>&lt;batchCount&gt;</i><code>" /&gt;<br>
	 * &lt;property name="hibernate.order_inserts" value="true" /&gt;</code><br><br>
	 * Onde <i>&lt;batchCount&gt;</i> deve ser o mesmo valor setado atrav&eacute;s deste m&eacute;todo.<br>
	 * Note tamb&eacute;m que se for usado outro provider JPA (ex: EclipseLink), as properties ser&atilde;o diferentes, devendo ser consultada a documenta&ccedil;&atilde;o da implementa&ccedil;&atilde;o.
	 * @see GenericDAO#setBatchCount(int)
	 */
	@Override
	public void setBatchCount (int newBatchCount) {
		this.batchCount = newBatchCount;
	}
	
	@Override
	public void insert (List<AbstractEntity> l) throws SysLodFlowException {
		Method setId = null;
		if (!useJTA) {
			throw new SysLodFlowException("Nao suportado para datasources do tipo RESOURCE_LOCAL");
		}

		try {
			
			if (l.size() == 0) {
				return;
			}
									
			AbstractEntity item = l.get(0); 			
			setId = item.getClass().getDeclaredMethod("setId", new Class[] {int.class});			
			for (int i = 0; i < l.size(); i++) {
				AbstractEntity e = l.get(i);
				
				// garante que a entidade nao possui um valor de id definido
				if (e.getId() > 0) {
					setId.invoke(e, new Integer(0));
				}
				
				getEntityManager().persist(e);
				if (i % batchCount == 0) {
					getEntityManager().flush();
					getEntityManager().clear();
				}
				
				l.set(i, e);
			}
			
			getEntityManager().flush();
			getEntityManager().clear();
		} catch (NoSuchMethodException e1) {
			throw new SysLodFlowException(e1);
		} catch (SecurityException e1) {
			throw new SysLodFlowException(e1);
		} catch (IllegalAccessException e1) {
			throw new SysLodFlowException(e1);
		} catch (IllegalArgumentException e1) {
			throw new SysLodFlowException(e1);
		} catch (InvocationTargetException e1) {
			throw new SysLodFlowException(e1);
		}		
	}
	
	public void update (List<AbstractEntity> l) throws SysLodFlowException {
		if (!useJTA) {
			throw new SysLodFlowException("Nao suportado para datasources do tipo RESOURCE_LOCAL");
		}
		
		for (int i = 0; i < l.size(); i++) {
			AbstractEntity e = l.get(i);
			AbstractEntity e2 = getEntityManager().find(e.getClass(), e.getId());
			if (e2 == null) {
				throw new SysLodFlowException("Nao foi encontrado " + e.getClass().getSimpleName() + " com id: " + e.getId());
			}
			
			// TODO Como tratar auditoria neste caso ?
			getEntityManager().merge(e);
			if (i % batchCount == 0) {
				getEntityManager().flush();
				getEntityManager().clear();
			}
		}
		
		getEntityManager().flush();
		getEntityManager().clear();
	}
	
	@Override
	public void delete (List<AbstractEntity> l) throws SysLodFlowException {
		if (!useJTA) {
			throw new SysLodFlowException("Nao suportado para datasources do tipo RESOURCE_LOCAL");
		}
		
		for (int i = 0; i < l.size(); i++) {
			AbstractEntity e = l.get(i);
			AbstractEntity e2 = getEntityManager().find(e.getClass(), e.getId());
			if (e2 == null) {
				throw new SysLodFlowException("Nao foi encontrado " + e.getClass().getSimpleName() + " com id: " + e.getId());
			}
			
			
			getEntityManager().remove(e);
			if (i % batchCount == 0) {
				getEntityManager().flush();
				getEntityManager().clear();
			}
		}
		
		getEntityManager().flush();
		getEntityManager().clear();				
	}
		


	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}
}
