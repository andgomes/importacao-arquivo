package br.ufc.arquivo;

import static org.junit.Assert.assertArrayEquals;

import java.util.Arrays;

import org.junit.Test;

public class TestArrays {
	
	@Test
	public void copyOfRangeToSignificaAPosicaoDoUltimoElemento() {
		
		String[] nomes = {"ab", "ju", "del"};
		
		String[] nomesCopy = Arrays.copyOfRange(nomes, 0, nomes.length);
		
		assertArrayEquals(nomes, nomesCopy);
	}

}
