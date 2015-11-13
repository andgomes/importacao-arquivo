package br.ufc.arquivo.model;

import java.util.Date;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TestPessoa {

	@Test
	public void testEquals() {
		
		Pessoa p1 = new Pessoa("John", 23, 
				"Analista", new Date(), 1234L);
		
		Pessoa p2 = new Pessoa("John", 23, 
				"Analista", new Date(), 1234L);
	
		Pessoa p3 = new Pessoa("John", 21, 
				"Analista", new Date(), 1234L);
		
		assertEquals(p1, p2);
		assertNotEquals(p1, p3);
		
	}
//
//	@Test(expected=Runtime.class)
//	public void testCPFNotValidNotAcceptedInSetCPF
	
}