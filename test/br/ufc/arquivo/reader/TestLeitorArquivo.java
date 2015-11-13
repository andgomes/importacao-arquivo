package br.ufc.arquivo.reader;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.BeforeClass;
import org.junit.Test;

// TODO: pode se tornar parametrizado
public class TestLeitorArquivo {

	private static LeitorArquivo leitorArquivo1;
	private static final int FILE_SIZE_PESSOAS1 = 10000;
	private static final String[] FILE_PESSOAS1_PRIMEIRA_LINHA = {
			"Abelardo 0", "20", "Analista de TI" };
	private static final String[] FILE_PESSOAS1_ULTIMA_LINHA = {
			"Abelardo 9997", "97", "Analista de Marmotagem" };
	private static final String FILEPATH_PESSOAS1 = "./resources/pessoas1_10000registros.csv";

	private static LeitorArquivo leitorArquivo2;
	private static final int FILE_SIZE_PESSOAS2 = 9980;
	private static final String[] FILE_PESSOAS2_PRIMEIRA_LINHA = {
			"Abelardo 3", "23", "Analista de Nada", "01/04/89" };
	private static final String[] FILE_PESSOAS2_ULTIMA_LINHA = {
			"Abelardo 9979", "79", "Analista de Nada", "03/17/97" };
	private static final String FILEPATH_PESSOAS2 = "./resources/pessoas2_10000registros.csv";

	private static LeitorArquivo leitorArquivo3;
	private static final int FILE_SIZE_PESSOAS3 = 9980;
	private static final String[] FILE_PESSOAS3_PRIMEIRA_LINHA = {
			"Abelardo 3", "23", "Analista de Nada", "01/04/89" };
	private static final String[] FILE_PESSOAS3_ULTIMA_LINHA = {
			"Abelardo 9979", "79", "Analista de Nada", "03/17/97" };
	private static final String FILEPATH_PESSOAS3 = "./resources/pessoas3_10000registros.csv";

	private static LeitorArquivo leitorArquivo4;
	private static final int FILE_SIZE_PESSOAS4 = 9980;
	private static final String[] FILE_PESSOAS4_PRIMEIRA_LINHA = {
			"Abelardo 3", "23", "Analista de Nada", "01/04/89", "27900" };
	private static final String[] FILE_PESSOAS4_ULTIMA_LINHA = {
			"Abelardo 9979", "79", "Analista de Nada", "03/17/97", "1002802620789" };
	private static final String FILEPATH_PESSOAS4 = "./resources/pessoas4_10000registros.csv";

	@BeforeClass
	public static void setUp() throws IOException {

		leitorArquivo1 = new LeitorArquivo(FILEPATH_PESSOAS1);
		leitorArquivo2 = new LeitorArquivo(FILEPATH_PESSOAS2);
		leitorArquivo3 = new LeitorArquivo(FILEPATH_PESSOAS3);
		leitorArquivo4 = new LeitorArquivo(FILEPATH_PESSOAS4);
	}

	@Test
	public void testLerRecordsQtdOk() throws IOException {

		assertEquals(FILE_SIZE_PESSOAS1, leitorArquivo1.getRows().size());
		assertEquals(FILE_SIZE_PESSOAS2, leitorArquivo2.getRows().size());
		assertEquals(FILE_SIZE_PESSOAS3, leitorArquivo3.getRows().size());
		assertEquals(FILE_SIZE_PESSOAS4, leitorArquivo4.getRows().size());
	}

	@Test
	public void seLerRecordsEntaoPrimeiroEUltimoRecordsOK() throws IOException {

		assertArrayEquals(FILE_PESSOAS1_PRIMEIRA_LINHA, leitorArquivo1
				.getRows().get(0));
		assertArrayEquals(FILE_PESSOAS1_ULTIMA_LINHA, leitorArquivo1.getRows()
				.get(leitorArquivo1.getRows().size() - 1));

		assertArrayEquals(FILE_PESSOAS2_PRIMEIRA_LINHA, leitorArquivo2
				.getRows().get(0));
		assertArrayEquals(FILE_PESSOAS2_ULTIMA_LINHA, leitorArquivo2.getRows()
				.get(leitorArquivo2.getRows().size() - 1));

		assertArrayEquals(FILE_PESSOAS3_PRIMEIRA_LINHA, leitorArquivo3
				.getRows().get(0));
		assertArrayEquals(FILE_PESSOAS3_ULTIMA_LINHA, leitorArquivo3.getRows()
				.get(leitorArquivo3.getRows().size() - 1));

		assertArrayEquals(FILE_PESSOAS4_PRIMEIRA_LINHA, leitorArquivo4
				.getRows().get(0));
		assertArrayEquals(FILE_PESSOAS4_ULTIMA_LINHA, leitorArquivo4.getRows()
				.get(leitorArquivo4.getRows().size() - 1));
	}

}
