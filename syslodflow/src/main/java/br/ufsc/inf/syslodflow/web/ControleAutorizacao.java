package br.ufsc.inf.syslodflow.web;

import javax.faces.context.FacesContext;
import javax.faces.event.PhaseEvent;
import javax.faces.event.PhaseId;
import javax.faces.event.PhaseListener;
import javax.servlet.http.HttpSession;

import br.ufsc.inf.syslodflow.web.JSFUtil;

/**
 * Usado para controle de acesso.
 * @author jeanmorais
 */
@SuppressWarnings("serial")
public class ControleAutorizacao implements PhaseListener {

	public void afterPhase(PhaseEvent event) {

		FacesContext facesContext = event.getFacesContext();
		String currentPage = facesContext.getViewRoot().getViewId();

		boolean isLoginPage = (currentPage.lastIndexOf("login.xhtml") > -1);
		if(!isLoginPage){
			HttpSession session = (HttpSession) facesContext.getExternalContext()
					.getSession(true);
			ControleAcessoBean sessao=null;
			if( session.getAttribute("controleAcessoBean")!=null){
				sessao = (ControleAcessoBean) session
					.getAttribute("controleAcessoBean");
			}
	
			if (sessao==null || !sessao.isIsLogged()) {
				redicionarLogin();
			}
		}

	}

	public void beforePhase(PhaseEvent event) {
	}

	public PhaseId getPhaseId() {
		return PhaseId.RESTORE_VIEW;
	}

	public void redicionarLogin() {
		JSFUtil.redirect("login.xhtml");
	}
}