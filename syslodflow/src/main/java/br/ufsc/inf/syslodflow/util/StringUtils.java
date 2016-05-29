package br.ufsc.inf.syslodflow.util;

import java.text.Normalizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtils {
	public static String tirarAcentuacao(String str){
		if(str != null){
			return Normalizer.normalize(str, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
		}else{
			return "";
		}		
	}
	 /**
	  * Validação do nome do Projeto. Apenas espaços em branco e letras.
	  * @param str
	  * @return valid
	  */
	public static boolean isValidNameProject(String str) {
		Pattern p = Pattern.compile("^[A-Za-záàâãéèêíïóôõöúçñÁÀÂÃÉÈÍÏÓÔÕÖÚÇÑ ]+$");
		Matcher m = p.matcher(str);
		return m.matches();
		
	}
}
