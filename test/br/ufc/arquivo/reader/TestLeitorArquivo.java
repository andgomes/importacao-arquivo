package br.ufc.arquivo.reader;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import br.ufc.arquivo.model.Pessoa;

@RunWith(Parameterized.class)
public class TestLeitorArquivo {

	private static SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yy");

	private LeitorArquivo leitorArquivo;
	private int numRows;
	private Pessoa firstPessoa;
	private Pessoa lastPessoa;

	public TestLeitorArquivo(String filepath, int numRows, Pessoa firstPessoa,
			Pessoa lastPessoa) throws IOException {

		this.leitorArquivo = new LeitorArquivo(filepath);
		this.numRows = numRows;
		this.firstPessoa = firstPessoa;
		this.lastPessoa = lastPessoa;
	}

	@Parameters
	public static List<Object[]> getParameters() throws ParseException {

		ArrayList<Object[]> parameters = new ArrayList<Object[]>(4);

		parameters.add(new Object[] {
				"./resources/pessoas5_10000registros.csv",
				9980,
				new Pessoa("Abelardo 3", "Analista de Nada", sdf
						.parse("01/04/89"), 27900l),
				new Pessoa("Abelardo 9979", "Analista de Nada", sdf
						.parse("03/17/97"), 1002802620789l) });

		return parameters;
	}

	@Test
	public void testLerRecordsQtdOk() throws IOException {

		int count = 0;
		
		Iterator<Pessoa> iteratorPessoa = leitorArquivo.iterator();
		
		while (iteratorPessoa.hasNext()) {
			iteratorPessoa.next();
			++count;
		}
		
		assertEquals(this.numRows, count);
	}

	@Test
	public void seLerRecordsEntaoPrimeiroEUltimoRecordsOK() throws IOException {

		Iterator<Pessoa> iteratorPessoa = leitorArquivo.iterator();
		
		assertEquals(this.firstPessoa, iteratorPessoa.next());
		
		Pessoa pessoa = null;
		
		while (iteratorPessoa.hasNext()) {
			pessoa = iteratorPessoa.next();
		}
		
		assertEquals(this.lastPessoa, pessoa);
	}
}
