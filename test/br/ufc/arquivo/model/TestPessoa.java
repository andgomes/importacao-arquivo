package br.ufc.arquivo.model;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

public class TestPessoa {

	@Test
	public void testEquals() {

		String nome = "John";
		String cargo = "Analista";
		Date dtNascimento = new Date();
		long cpf1 = 1234L;
		long cpf2 = 3235L;

		Pessoa p1 = new Pessoa(nome, cargo, dtNascimento, cpf1);

		Pessoa p2 = new Pessoa(nome, cargo, dtNascimento, cpf1);

		Pessoa p3 = new Pessoa(nome, cargo, dtNascimento, cpf2);

		assertEquals(p1, p2);
		assertNotEquals(p1, p3);
	}
	
	@Test
	public void testGetIdade() throws ParseException {
		
		Pessoa pessoa1 = new Pessoa(null, null, 
				new SimpleDateFormat("MM/dd/yy").parse("08/29/94"), null);
		
		Pessoa pessoa2 = new Pessoa(null, null, 
				new SimpleDateFormat("MM/dd/yy").parse("12/01/94"), null);
			
		assertEquals(21, pessoa1.getIdade());
		assertEquals(20, pessoa2.getIdade());
		
	}
}