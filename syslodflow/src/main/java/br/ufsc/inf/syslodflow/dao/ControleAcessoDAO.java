package br.ufsc.inf.syslodflow.dao;

import java.util.List;

import javax.enterprise.inject.Default;

import br.ufsc.inf.syslodflow.entity.AbstractEntity;


/**
 * DAO do m&oacute;dulo de autentica&ccedil;&atilde;o e autoriza&ccedil;&atilde;o.
 * @author carloshp
 * (c)2014-2015 COMDAT ltda. Todos os direitos reservados.
 */
@Default
public class ControleAcessoDAO extends AbstractJPADAO {

	@Override
	public String getPUName() {
		return "pu-mc3web";
	}

	public boolean checkLoginFuncao (String login, int fn) {
		Object o = getEntityManager().createNativeQuery(
			"SELECT COUNT(f.nome) AS possuiAcesso " +
			"FROM " +
			"en_funcao f " +
			"INNER JOIN en_grupo_funcao gf ON gf.funcao = f.id " + 
			"INNER JOIN en_grupo g ON gf.grupo = g.id  " + 
			"INNER JOIN en_usuario_grupo ug ON g.id = ug.grupo " +
			"INNER JOIN en_usuario u ON ug.usuario = u.id AND u.ativo = 1 " +
			"WHERE " + " u.login = ? " +    
			"AND f.id = ?")
				.setParameter(1, login)
				.setParameter(2, fn).getSingleResult();
			
		if (o instanceof Number) {
			return (((Number)o).intValue() == 1);
		}
						
		return false;
	}
	
	@SuppressWarnings("unchecked")
	public List<AbstractEntity> findByLogin (String login) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT e FROM Usuario")
			.append(" e WHERE e.login = :login and e.ativo = 1");
		
		return (List<AbstractEntity>) getEntityManager()
			.createQuery(sb.toString())
			.setHint("org.hibernate.cacheable", false)
			.setParameter("login", login)
			.getResultList();		
	}

	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<AbstractEntity> findAllActive (Class c) {
		StringBuilder sb = new StringBuilder();
		
		sb.append("SELECT e FROM ")
			.append(c.getSimpleName())
			.append(" e WHERE e.ativo = 1")
			.append(" ORDER BY e.id");
		
		return (List<AbstractEntity>) getEntityManager()
			.createQuery(sb.toString())
			.getResultList();		
	}
	
	
}
