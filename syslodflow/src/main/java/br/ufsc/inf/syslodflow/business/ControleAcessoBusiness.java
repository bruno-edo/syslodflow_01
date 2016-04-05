package br.ufsc.inf.syslodflow.business;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.ejb.Stateless;
import javax.inject.Inject;

import br.ufsc.inf.syslodflow.SysLodFlowException;
import br.ufsc.inf.syslodflow.dao.ControleAcessoDAO;
import br.ufsc.inf.syslodflow.entity.AbstractEntity;
import br.ufsc.inf.syslodflow.entity.Usuario;

/**
 * EJB implementando todas as regras de neg&oacute;cio para autentica&ccedil;&atilde;o e autoriza&ccedil;&atilde;o.<br/>
 * @author carloshp
 * (c)2014-2015 COMDAT ltda. Todos os direitos reservados.
 *
 */
@Stateless
public class ControleAcessoBusiness {

	@Inject 
	private ControleAcessoDAO dao;
	private Logger logger = Logger.getLogger(ControleAcessoBusiness.class.getSimpleName());
	
	protected ControleAcessoDAO getDAO() {
		return dao;
	}
	
	// usado apenas para injetar o DAO durante testes unitarios 
	public void setDAO(ControleAcessoDAO dao) {
		this.dao = dao;
	}

	
	public List<AbstractEntity> findByLogin(String login) {
		return getDAO().findByLogin(login);
	}
	
	public boolean authorize(Usuario usuario, int funcao) {
		return dao.checkLoginFuncao(usuario.getLogin(), funcao);
	}
	
	public Usuario autenthicate(String login, String senha) throws SysLodFlowException {
		Usuario result = null;
		List<AbstractEntity> usuarios = null;
		
			
			usuarios = getDAO().findByLogin(login);		
			if(usuarios.size() == 0){
				throw new SysLodFlowException("Tentativa de login invalida: " + login);
			}
			
			result = (Usuario) usuarios.get(0);
			String pswd = senha;
			if(!result.getSenha().equalsIgnoreCase(pswd)){
				throw new SysLodFlowException("Tentativa de login invalida: " + login);
			}			
		
		if (result != null) {
			result = (Usuario) getDAO().update(result);
		}
		
		return result; 
	}

	
	@SuppressWarnings("rawtypes")
	public List<AbstractEntity> findAllActive (Class c) {
		return dao.findAllActive(c);
	}	

	public List<Usuario> findUsuarioByLogin(String login)	{
		List<Usuario> result = new ArrayList<Usuario>();
		
		List<AbstractEntity> tmp = getDAO().findByLogin(login);
		for (AbstractEntity item : tmp) {
			result.add((Usuario) item);
		}
		
		return result;
	}

	public Logger getLogger() {
		return logger;
	}
}
