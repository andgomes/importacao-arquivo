package br.ufc.arquivo;

import br.ufc.arquivo.model.Pessoa;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;

// XXX: iterator
public class Converter {

	private List<Object[]> rows;
	private int next = 0;
	
	public Converter(List<Object[]> rows) {
		
		this.rows = rows;
		
	}
	
	public Pessoa nextPessoa() {
		
		if (next >= rows.size()) {
			throw new NoSuchElementException();
		}
		
		String nome = null;
		Integer idade = null;
		String cargo = null;
		Date nascimento = null;
		
		if (rows.get(next)[0] != null) {
			nome = (String) rows.get(next)[0];
		}
		
		if (rows.get(next)[1] != null) {
			idade = (Integer) rows.get(next)[1];
		}
		
		if (rows.get(next)[2] != null) {
			cargo = (String) rows.get(next)[2];
		}
		
		if (rows.get(next)[3] != null) {
			nascimento = (Date) rows.get(next)[3];
		}
		
		++next;
		
		return new Pessoa(nome, idade, cargo, nascimento);
		
	}
	
}