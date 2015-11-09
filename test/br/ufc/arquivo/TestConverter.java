package br.ufc.arquivo;

import br.ufc.arquivo.model.Pessoa;
import java.util.LinkedList;
import java.util.List;
import org.junit.Test;
import static org.junit.Assert.*;

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

}