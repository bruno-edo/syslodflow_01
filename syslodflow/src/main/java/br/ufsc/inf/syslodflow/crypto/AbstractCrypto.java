package br.ufsc.inf.syslodflow.crypto;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.logging.Logger;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import br.ufsc.inf.syslodflow.SysLodFlowException;




/**
 * Classe utilit&aacute;ria de criptografia n&atilde;o revers&iacute;vel para senhas.
 * @author silvachp
 * (c)2014-2015 COMDAT ltda. Todos os direitos reservados.
 *
 */
public abstract class AbstractCrypto {
	/**
	 * Retorna o <em>"sal"</em> que é concatenado com o valor a criptografar, tornando a quebra mais dif&iacute;cil.<br>
	 * O valor de retorno deve:<br>
	 * <ul>
	 * <li>ter sempre 16 caracteres;</li>
	 * <li><em>N&Atilde;O ser declarado em c&oacute;digo</em>, de forma que decompilar as classes n&atilde;o revele as strings utilizadas;</li>
	 * <li>variar de forma direta e consistente com o parametro de entrada fornecido (ou seja, retornos diferentes para parametros diferentes, por&eacute;m sempre o mesmo retorno para o mesmo parametro de entrada);</li>
	 * <li>preferencialmente <em>NÃO SER</em> uma informação diretamente derivada da base de dados, para dificultar a quebra em caso de invas&otilde;es.</li>
	 * </ul>
	 * Uma sugest&atilde;o para implementa&ccedil;&atilde;o &eacute; o uso de uma lista de valores lidos de um arquivo de propriedades (.properties ou .xml), indexado a partir de um hash gerado pela String de entrada. Outras fontes de dados como LDAP/AD podem também ser usadas.
	 * @param input String a ser encriptada. 
	 * @return <em>"sal"</em> para concatenar com o valor a ser encriptado.
	 */
	public abstract String getInitVector (String input);
	
	/**
	 * Retorna a chave de criptografia utilizada.<br>
	 * O valor de retorno deve:<br>
	 * <ul>
	 * <li>ter sempre 16 caracteres;</li>
	 * <li><em>N&Atilde;O ser declarado em c&oacute;digo</em>, de forma que decompilar as classes n&atilde;o revele a string utilizada.</li>
	 * </ul>
	 * Uma sugest&atilde;o para implementa&ccedil;&atilde;o &eacute; ler o valor a partir de um arquivo de propriedades (.properties ou .xml). Outras fontes de dados como LDAP/AD podem também ser usadas.
	 * @return
	 */
	public abstract String getCryptKey ();
	
	
	/**
	 * Retorna uma string aleatoria para garantir que o valor a ser criptografado seja um m&uacute;ltiplo de 16.
	 * @return
	 */
	public abstract String getAppendString ();

	
	/**
	 * Retorna a instancia de <code>Logger</code> utilizada pela classe.
	 * @return
	 */
	public abstract Logger getLogger();
	
	
	/**
	 * Realiza a criptografia da <code>String</code> de entrada, utilizando a implementa&ccedil;&atilde;o padr&atilde;o da Sun para o algoritmo AES-256.<br>
	 * A <code>String</code> informada tem seu tamanho ajustado autom&aacute;ticamente para 16 caracteres.<br>
	 * A criptografia utilizada &eacute; n&atilde;o revers&iacute;vel.
	 * @param input <code>String</code> a ser encriptada.
	 * @return <code>String</code> encriptada e codificada em UTF-8. 
	 * @throws UtilException Em caso de falha.
	 */
	public String encrypt (String input) throws SysLodFlowException {
		String initVector 	= getInitVector(input);
		String key 			= getCryptKey();		
		String append 		= getAppendString ();
		
		try {
			// TODO Talvez parametrizar tambem o algoritmo usado ?
			Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding", "SunJCE");
	        SecretKey secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");
	        cipher.init(Cipher.ENCRYPT_MODE, secretKey, new IvParameterSpec(initVector.getBytes("UTF-8")));
	        String tmp = input.concat(append).substring(0, 16);
	        byte[] data = cipher.doFinal(tmp.getBytes("UTF-8"));
	        StringBuilder sb = new StringBuilder();
	        
	        
	        for (int i = 0; i < data.length; i++) {
	        	int x = Math.abs(data[i]);
	        	sb.append(x < 16 ? "0" : "");
	            sb.append(Integer.toHexString(x).toUpperCase());
	        }            
	            
	        return sb.toString();
	        
			} catch (NoSuchAlgorithmException e) {
	            getLogger().severe(e.getClass().getSimpleName() + ":" + e.getMessage());
	        } catch (NoSuchProviderException e) {
	        	getLogger().severe(e.getClass().getSimpleName() + ":" + e.getMessage());
	        } catch (NoSuchPaddingException e) {
	        	getLogger().severe(e.getClass().getSimpleName() + ":" + e.getMessage());
	        } catch (UnsupportedEncodingException e) {
	        	getLogger().severe(e.getClass().getSimpleName() + ":" + e.getMessage());
	        } catch (InvalidKeyException e) {
	        	getLogger().severe(e.getClass().getSimpleName() + ":" + e.getMessage());
	        } catch (InvalidAlgorithmParameterException e) {
	        	getLogger().severe(e.getClass().getSimpleName() + ":" + e.getMessage());
	        } catch (IllegalBlockSizeException e) {
	        	getLogger().severe(e.getClass().getSimpleName() + ":" + e.getMessage());
	        } catch (BadPaddingException e) {
	        	getLogger().severe(e.getClass().getSimpleName() + ":" + e.getMessage());
	        }
	        
			return null;
	}	
}
