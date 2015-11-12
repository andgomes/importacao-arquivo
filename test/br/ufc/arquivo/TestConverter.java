package br.ufc.arquivo;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.junit.Test;

import br.ufc.arquivo.model.Pessoa;

@Deprecated
public class TestConverter {

	@Test
	public void testGetPessoa() {

		List<Object[]> pessoas = new LinkedList<>();

		pessoas.add(new Object[] { "John", new Integer(23), null, null });

		Converter converter = new Converter(pessoas);

		Pessoa pessoa = converter.nextPessoa();

		assertEquals("John", pessoa.getNome());
		assertEquals(new Integer(23), pessoa.getIdade());
	}

	@Test(expected = NoSuchElementException.class)
	public void seNaoTemMaisPessoaEntaoNextPessoaLancaException() {

		Converter emptyConverter = new Converter(new ArrayList<Object[]>());

		emptyConverter.nextPessoa();
	}

}