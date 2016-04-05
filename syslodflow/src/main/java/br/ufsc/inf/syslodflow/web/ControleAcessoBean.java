package br.ufsc.inf.syslodflow.web;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.inject.Inject;

import br.ufsc.inf.syslodflow.SysLodFlowException;
import br.ufsc.inf.syslodflow.business.CadastroBusiness;
import br.ufsc.inf.syslodflow.business.ControleAcessoBusiness;
import br.ufsc.inf.syslodflow.entity.Usuario;

/**
 * Managed bean usado na autentica&ccedil;&atilde;o e autoriza&ccedil;&atilde;o. 
 * @author carloshp
 * (c)2014-2015 COMDAT Ltda. Todos os direitos reservados.
 */
@ManagedBean(name = "controleAcessoBean")
@SessionScoped
public class ControleAcessoBean {
	private Logger logger = Logger.getLogger(ControleAcessoBean.class.getSimpleName());
	private boolean islogged;
	@Inject
	private ControleAcessoBusiness ejb;
	
	@Inject
	private CadastroBusiness cadastroEjb;	
	
	private Usuario usuario;
	private List<Boolean> permissoes;


	private String novaSenha;
	private String confirmarSenha;	
		
	@PostConstruct
	public void init() {
		usuario = new Usuario();
		islogged = false;
		permissoes = new ArrayList<Boolean>();
	}	
	
	public String efetuarLogin(){
		try {
			StringBuilder sb = new StringBuilder();
			sb.append("Solicitação de autenticação login:").append(usuario.getLogin());
			logger.info(sb.toString());
			usuario=ejb.autenthicate(usuario.getLogin(), usuario.getSenha());
	
		} catch (SysLodFlowException e) {
			StringBuilder sb2 = new StringBuilder();
			sb2.append("Tentativa de login inválida para usuário:" + usuario.getLogin());			
			logger.warning(sb2.toString());
			JSFUtil.showErrorMessage("Login e/ou senha inválidos");
			return "";
		}
		
		StringBuilder sb = new StringBuilder();
		salvarPermissoes();
		
		sb.append("Usuário autenticado " + usuario.getLogin());
		logger.info(sb.toString());
		setIsLogged(true);
		return "index";
	}
	
	public void salvarPermissoes(){

	}

	public boolean isIsLogged() {
		return islogged;
	}

	public void setIsLogged(boolean islogged) {
		this.islogged = islogged;
	}

	public void efetuarLogoff(){
		usuario = new Usuario();
		setIsLogged(false);
		JSFUtil.redirect("login.xhtml");
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public List<Boolean> getPermissoes() {
		return permissoes;
	}

	public void setPermissoes(List<Boolean> permissoes) {
		this.permissoes = permissoes;
	}
	
	public boolean getAcesso(String nome){

		return false;
	}
			
	public String salvarPerfil() throws SysLodFlowException {
		usuario = JSFUtil.getUserInSession();
		
			if ((this.novaSenha == null) && (this.confirmarSenha != null) && (this.confirmarSenha.length() > 0)) {
				JSFUtil.showErrorMessage(JSFUtil.getLocalizedMessage("crud.user.pswd.new") 
							+ ": " 
							+ JSFUtil.getLocalizedMessage("error.required"));
				
				return Navegacao.MESMA_PAGINA;
			}
			
			if ((this.novaSenha != null) && (this.novaSenha.length() > 0) && (this.confirmarSenha == null)) {
				JSFUtil.showErrorMessage(JSFUtil.getLocalizedMessage("crud.user.pswd.confirm") 
							+ ": " 
							+ JSFUtil.getLocalizedMessage("error.required"));
				
				return Navegacao.MESMA_PAGINA;
			}
			
			if ((this.novaSenha != null) && (this.confirmarSenha != null) && (this.novaSenha.length() > 0) && (this.confirmarSenha.length() > 0)) {
				if (this.novaSenha.compareTo(this.confirmarSenha) != 0) {
					JSFUtil.showErrorMessage(JSFUtil.getLocalizedMessage("error.pswd.mismatch"));			
					return Navegacao.MESMA_PAGINA;
				}
	
				usuario.setSenha(this.novaSenha);
			}
		
				
		usuario = (Usuario) cadastroEjb.update(usuario);
		if(usuario!=null){
			JSFUtil.showMessage(JSFUtil.getLocalizedMessage("app.operation.ok"));						
		}else{
			JSFUtil.showErrorMessage(JSFUtil.getLocalizedMessage("err.unknown"));
		}
		
		return Navegacao.INDEX;
	}
		
	public boolean getAcessoMenu(String nome) {
	
		return true;
	}

	public String getNovaSenha() {
		return novaSenha;
	}

	public void setNovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}

	public String getConfirmarSenha() {
		return confirmarSenha;
	}

	public void setConfirmarSenha(String confirmarSenha) {
		this.confirmarSenha = confirmarSenha;
	}		
}
