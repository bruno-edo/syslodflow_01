package br.ufsc.inf.syslodflow.web;

import java.util.ArrayList;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.model.DataModel;
import javax.faces.model.ListDataModel;
import javax.inject.Inject;

import br.ufsc.inf.syslodflow.SysLodFlowException;
import br.ufsc.inf.syslodflow.business.CadastroBusiness;
import br.ufsc.inf.syslodflow.entity.Usuario;

/**
 * Managed bean do usu&aacute;rio do sistema.
 * @author jeanmorais
 */
@ManagedBean(name="usuarioBean")
@SessionScoped
public class UsuarioBean {
	
	@Inject
	private CadastroBusiness ejb;

	private DataModel<Usuario> modelUsuarios;
	private Usuario usuario;
	private String novaSenha;
	private String confirmarSenha;	

	
	@PostConstruct
	public void init() {
		usuario = new Usuario();	

	}
	
	public String cadastrar(){
		usuario = new Usuario();
		return Navegacao.USUARIO_CADASTRAR;
	}
	
		
	public String salvarAD() throws SysLodFlowException{
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
		
		
		if(usuario.getId() == 0) {			
			usuario.setAtivo(1);	
			if(ejb.insert(usuario)==null){
				return Navegacao.MESMA_PAGINA;
			}	
		}else{
			if(ejb.update(usuario)==null){
				return Navegacao.MESMA_PAGINA;
			}
		}

		JSFUtil.showErrorMessage(JSFUtil.getLocalizedMessage("app.operation.ok"));
		usuario= new Usuario();
		return Navegacao.USUARIO_LISTAR;
		
	}
	
	
	public String editar() {
		this.usuario =  modelUsuarios.getRowData();
		return Navegacao.USUARIO_CADASTRAR; 
	}
	
	public void excluir() throws SysLodFlowException {		
		this.usuario = modelUsuarios.getRowData();
		this.usuario.setAtivo(usuario.getAtivo() == 1 ? 0 : 1);
		ejb.update(usuario);
		JSFUtil.showErrorMessage(JSFUtil.getLocalizedMessage("app.operation.ok"));
	}
	
	@SuppressWarnings("unchecked")
	public void carregarUsuarios() {
		modelUsuarios = new ListDataModel<Usuario>(ejb.castTo(ejb.findAllActive(Usuario.class),Usuario.class));
	}
	
	public CadastroBusiness getEjb() {
		return ejb;
	}
	public void setEjb(CadastroBusiness ejb) {
		this.ejb = ejb;
	}
	
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getConfirmarSenha() {
		return confirmarSenha;
	}

	public void setConfirmarSenha(String confirmarSenha) {
		this.confirmarSenha = confirmarSenha;
	}
	
	/* GETTER AND SETTERS */
	public DataModel<Usuario> getUsuarios() {
		if (modelUsuarios.getRowCount() <= 0) {
			List<Usuario> temp = new ArrayList<Usuario>();
			temp.add(new Usuario());
			modelUsuarios = new ListDataModel<Usuario>(temp);
		}
		return modelUsuarios;
	}
	

	public String getNovaSenha () {
		return novaSenha;
	}

	public void setnovaSenha(String novaSenha) {
		this.novaSenha = novaSenha;
	}

	
	
}
