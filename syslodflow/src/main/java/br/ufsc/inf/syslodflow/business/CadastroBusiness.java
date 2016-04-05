package br.ufsc.inf.syslodflow.business;

import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.ejb.EJBContext;
import javax.ejb.Stateless;
import javax.inject.Inject;

import br.ufsc.inf.syslodflow.SysLodFlowException;
import br.ufsc.inf.syslodflow.dao.AbstractJPADAO;
import br.ufsc.inf.syslodflow.dao.CadastroDAO;
import br.ufsc.inf.syslodflow.dao.ControleAcessoDAO;
import br.ufsc.inf.syslodflow.entity.AbstractEntity;
import br.ufsc.inf.syslodflow.web.JSFUtil;

/**
 * EJB Gen&eacute;rico para cadastros (CRUDs).
 * @author carloshp
 * (c)2014-2015 COMDAT ltda. Todos os direitos reservados.
 */

@SuppressWarnings("rawtypes")
@Stateless
public class CadastroBusiness extends AbstractBusiness  {
	private Logger logger = Logger.getLogger(CadastroBusiness.class.getSimpleName());
	
	@Resource
	private EJBContext ctx;
	
	
	@Inject 
	private ControleAcessoDAO controleAcessoDao;
	
	@Inject 
	private CadastroDAO dao;
	
	@Override
	protected Logger getLogger() {
		return logger;
	}		
	
	@Override
	public AbstractJPADAO getDAO() throws SysLodFlowException {
		return this.dao;
	}
	
	@Override
	@PostConstruct
	protected void setupDAO()  {
		if(JSFUtil.getUserInSession()!=null) {
			dao.setLogin(JSFUtil.getUserInSession().getLogin());
			controleAcessoDao.setLogin(JSFUtil.getUserInSession().getLogin());
		}else {
			dao.setLogin(null);
			controleAcessoDao.setLogin(null);
			getLogger().severe("Falha ao registrar login para auditoria.");
		}		
	}
 	
	
	// FIXME
	public List<AbstractEntity> findAllActive (Class c) {
		return dao.findAll(c);
	}
	
}