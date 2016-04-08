package br.ufsc.inf.syslodflow.util;

import java.util.Comparator;

import javax.faces.model.SelectItem;
/**
 * Implementa&ccedil;&atilde;o de <code>Comparator</code> para ordena&ccedil;&atilde;o de <code>SelectItem</code>.
 * @author silvachp
 *
 */
public class SelectItemComparator implements Comparator<SelectItem> {

	@Override
	public int compare(SelectItem o1, SelectItem o2) {
		if ((o1 == null) || (o2 == null)) {
			return 0;
		}
		
		return o1.getLabel().compareTo(o2.getLabel());
	}

}
