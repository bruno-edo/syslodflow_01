package br.ufsc.inf.syslodflow.business;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import br.ufsc.inf.syslodflow.SysLodFlowException;
import br.ufsc.inf.syslodflow.dao.AbstractJPADAO;
import br.ufsc.inf.syslodflow.entity.AbstractEntity;
import br.ufsc.inf.syslodflow.util.JSFUtil;

/**
 * Superclasse para os EJB's de neg&oacute;cio.<br>
 * Implementa os principais m&eacute;todos de CRUD e algumas consultas comuns.
 * @author silvachp
 * (c)2014-2015 COMDAT ltda. Todos os direitos reservados.
 */

@SuppressWarnings("rawtypes")
public abstract class AbstractBusiness  {
	
	public AbstractJPADAO getDAO() throws SysLodFlowException {
		throw new SysLodFlowException ("Nao implementado");
	}

	// para uso dos testes unitarios
	public void setDAO(AbstractJPADAO newDAO) throws SysLodFlowException {
		throw new SysLodFlowException ("Nao implementado");
	}
	
	protected abstract Logger getLogger(); 
	
	protected void setupDAO()  {
		try {
			if(JSFUtil.getUserInSession()!=null) {
				getDAO().setLogin(JSFUtil.getUserInSession().getLogin());
			}else {
				getDAO().setLogin(null);
				getLogger().severe("Falha ao registrar login para auditoria.");
			}
		} catch (SysLodFlowException e) {
			getLogger().severe(e.getMessage());
		}		
	}
		
	public AbstractEntity insert(AbstractEntity ae) throws SysLodFlowException {
		setupDAO();
		return getDAO().insert(ae);
	}
	
	public AbstractEntity update(AbstractEntity ae) throws SysLodFlowException {
		setupDAO();
		return getDAO().update(ae);
	}
	
	public void deleteById(Class c, int id) throws SysLodFlowException {	
		setupDAO();
		getDAO().deleteById(c, id);
	}

	public AbstractEntity findById(Class c, int id) throws SysLodFlowException {
		setupDAO();
		return getDAO().findById(c, id);
	}
	
	public List<AbstractEntity> findAll(Class c) throws SysLodFlowException {
		setupDAO();
		return getDAO().findAll(c);
	}
	
	/**
	 * M&eacute;todo utilit&aacute;rio para converter o <code>List</code> retornado pelas consultas.
	 * @param l Lista de <code>AbstractEntity</code> retornada pela chamada ao DAO.
	 * @param c Classe real dos elementos do <code>List</code>.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public List castTo(List<AbstractEntity> l, Class c) {
		List result = new ArrayList();
		for (AbstractEntity item : l) {
			result.add(c.cast(item));
		}
		
		return result;
	}
	
	public List<AbstractEntity> findAllMax (Class c, int max) throws SysLodFlowException {
		setupDAO();
		return getDAO().findAllMax(c, max);
	}
}
