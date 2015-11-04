package br.ufc.arquivo;

import java.util.List;

public class Converter {

	private List<Object[]> rows;

	public Converter(List<Object[]> rows) {
		
		this.rows = rows;
		
	} // end Converter constructor
	
	public int getIdade(int row) {
		
		int idade = Integer.parseInt(
				rows.get(row)[1].toString());
		
		return idade;
		
	} // end getIdade method
	
} // end Converter class