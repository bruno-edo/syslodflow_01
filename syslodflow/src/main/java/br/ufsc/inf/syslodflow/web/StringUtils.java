package br.ufsc.inf.syslodflow.web;

import java.text.Normalizer;

/**
 * Classe utilit&aacute;ria.
 * @author carloshp
 * (c)2014-2015 COMDAT Ltda. Todos os direitos reservados.
 */
// FIXME Desnecessario
public class StringUtils {
	public static String tirarAcentuacao(String str){
		if(str != null){
			return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
		}else{
			return "";
		}		
	}
}
