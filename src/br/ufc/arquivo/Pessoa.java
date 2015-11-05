package br.ufc.arquivo;

import java.util.BitSet;

public class Pessoa {

	private static final int COLUNA_IDADE = 1;
	
	private Integer idade;
	private BitSet bitSet = new BitSet();
	
	public void setIdade(int idade) {
		
		bitSet.set(COLUNA_IDADE);
		
		this.idade = idade;
	} // end setIdade method
	
	public Integer getIdade() {
		
		if (bitSet.get(COLUNA_IDADE)) {
			
			return idade;
		} else {
			
			throw new ValueNotPresentException("Idade");		
		}	
		
	} // end getIdade method
	
} // end Pessoa class