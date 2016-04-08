package br.ufsc.inf.syslodflow.util;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Logger;

import javax.faces.application.FacesMessage;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.faces.model.SelectItem;
import javax.servlet.http.HttpSession;

import br.ufsc.inf.syslodflow.entity.Usuario;
import br.ufsc.inf.syslodflow.web.ControleAcessoBean;

/**
 * Classe utilit&aacute;ria com m&eacute;todos para redirecionamento, exibi&ccedil;&atilde;o de mensagens, etc.
 * @author carloshp
 * (c)2014-2015 COMDAT Ltda. Todos os direitos reservados.
 */
public class JSFUtil {
	// FIXME 
	private static final String MSGBUNDLEPATH = "br.com.connectionltda.messages";
	private static Logger logger = Logger.getLogger(ControleAcessoBean.class.getSimpleName());
	
	public static void showMessage(String mensagem) {	
		FacesContext context = FacesContext.getCurrentInstance();
		FacesMessage facesMessage = new FacesMessage(mensagem);
		context.addMessage(null, facesMessage);		
	}
	
	public static void showErrorMessage(String mensagem) {
		FacesContext.getCurrentInstance().addMessage(null, new FacesMessage(FacesMessage.SEVERITY_ERROR,mensagem, ""));
	}
	
	public static Usuario getUserInSession() {
		FacesContext context = FacesContext.getCurrentInstance();
		if(context!=null){
			HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
			ControleAcessoBean sessao= (ControleAcessoBean )session.getAttribute("controleAcessoBean");
			return sessao.getUsuario();
		}
		return null;		
	}
	
	public static void redirect(String path){
		StringBuilder sb = new StringBuilder();
		ExternalContext context = FacesContext.getCurrentInstance().getExternalContext();
		sb.append(context.getRequestContextPath()).append("/").append(path);
		try {
			context.redirect(sb.toString());
		} catch (IOException e) {
			logger.warning("Falha ao redirecionar para: " + path);	
		}
	}
	
	/**
	 * M&eacute;todo de conveni&ecirc;ncia para retornar mensagens no locale default do usu&aacute;rio.<br>
	 * Note que se n&atilde;o for definido um locale espec&iacute;fico para o usu&aacute;rio, &eacute; assumido o locale default da aplica&ccedil;&atilde;o (pt_BR).
	 * @param key Chave identificadora da mensagem no resource bundle 
	 * @return String equivalente no locale default do usu&aacute;rio.
	 */
	public static String getLocalizedMessage (String key) {
		return ResourceBundle.getBundle(MSGBUNDLEPATH).getString(key);
	}
	
	/**
	 * Retorna a lista de locales suportados.<br>
	 * Note que para habilitar suporte a novas linguagens &eacute; preciso adicionar o correspondente<br>arquivo <code>messages.properties</code> &agrave; aplica&ccedil;&atilde;o.
	 * 
	 * @return
	 */
	public static List<SelectItem> getSupportedLocaleList () {
		List<SelectItem> result = new ArrayList<SelectItem>();
		
		result.add(new SelectItem("pt_BR","Português (Brasil)"));
		// result.add(new SelectItem("pt-PT","Português (Portugal)"));
		// result.add(new SelectItem("es-AR","Español (Argentina)"));
		// result.add(new SelectItem("es-CO","Español (Colombia)"));
		// result.add(new SelectItem("es-CL","Español (Chile)"));
		result.add(new SelectItem("en_US","English (USA)"));
		// result.add(new SelectItem("en_GB","English (British)"));
		return result;
	}
}
