package br.ufc.arquivo;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

import br.ufc.arquivo.reader.LeitorArquivo;

public class TestLeitorArquivo {

	private static List<String[]> recordsDoArquivo1;
	private static final int FILE_SIZE_PESSOAS1 = 10000;
	private static final String[] FILE_PESSOAS1_PRIMEIRA_LINHA = {
			"Abelardo 0", "20", "Analista de TI" };
	private static final String[] FILE_PESSOAS1_ULTIMA_LINHA = {
			"Abelardo 9997", "97", "Analista de Marmotagem" };
	private static final String FILEPATH_PESSOAS1 = "./resources/pessoas1_10000registros.csv";

	private static List<String[]> recordsDoArquivo2;
	private static final int FILE_SIZE_PESSOAS2 = 9980;
	private static final String[] FILE_PESSOAS2_PRIMEIRA_LINHA = {
			"Abelardo 3", "23", "Analista de Nada", "01/04/89" };
	private static final String[] FILE_PESSOAS2_ULTIMA_LINHA = {
			"Abelardo 9979", "79", "Analista de Nada", "03/17/97" };
	private static final String FILEPATH_PESSOAS2 = "./resources/pessoas2_10000registros.csv";
	
	private static List<String[]> recordsDoArquivo3;
	private static final int FILE_SIZE_PESSOAS3 = 9980;
	private static final String[] FILE_PESSOAS3_PRIMEIRA_LINHA = {
			"Abelardo 3", "23", "Analista de Nada", "01/04/89" };
	private static final String[] FILE_PESSOAS3_ULTIMA_LINHA = {
			"Abelardo 9979", "79", "Analista de Nada", "03/17/97" };
	private static final String FILEPATH_PESSOAS3 = "./resources/pessoas3_10000registros.csv";

	@BeforeClass
	public static void setUp() throws IOException {

		recordsDoArquivo1 = LeitorArquivo.lerRecords(FILEPATH_PESSOAS1);
		recordsDoArquivo2 = LeitorArquivo.lerRecords(FILEPATH_PESSOAS2);
		recordsDoArquivo3 = LeitorArquivo.lerRecords(FILEPATH_PESSOAS3);
	}

	@Test
	public void testLerRecordsQtdOk() throws IOException {

		assertEquals(FILE_SIZE_PESSOAS1, recordsDoArquivo1.size());
		assertEquals(FILE_SIZE_PESSOAS2, recordsDoArquivo2.size());
		assertEquals(FILE_SIZE_PESSOAS3, recordsDoArquivo3.size());
	}

	@Test
	public void seLerRecordsEntaoPrimeiroEUltimoRecordsOK() throws IOException {

		assertArrayEquals(FILE_PESSOAS1_PRIMEIRA_LINHA,
				recordsDoArquivo1.get(0));
		assertArrayEquals(FILE_PESSOAS1_ULTIMA_LINHA,
				recordsDoArquivo1.get(recordsDoArquivo1.size() - 1));

		assertArrayEquals(FILE_PESSOAS2_PRIMEIRA_LINHA,
				recordsDoArquivo2.get(0));
		assertArrayEquals(FILE_PESSOAS2_ULTIMA_LINHA,
				recordsDoArquivo2.get(recordsDoArquivo2.size() - 1));
		
		assertArrayEquals(FILE_PESSOAS3_PRIMEIRA_LINHA,
				recordsDoArquivo3.get(0));
		assertArrayEquals(FILE_PESSOAS3_ULTIMA_LINHA,
				recordsDoArquivo3.get(recordsDoArquivo3.size() - 1));
	}

}
