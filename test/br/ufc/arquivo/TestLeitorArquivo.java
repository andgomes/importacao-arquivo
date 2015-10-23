package br.ufc.arquivo;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.junit.BeforeClass;
import org.junit.Test;

// TODO: novo requisito - ser capaz de ler pessoas3_10000registros.csv
// TODO: novo requisito - ser capaz de salvar no banco os dados dos arquivos pessoas2_10000registros.csv e pessoas3_10000registros.csv
// adicionar nova coluna para data(opcional, de forma a permitir que os dados de pessoas1_10000registros.csv também possam ser inseridos)
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

	@BeforeClass
	public static void setUp() throws IOException {

		recordsDoArquivo1 = LeitorArquivo.lerRecords(FILEPATH_PESSOAS1);
		recordsDoArquivo2 = LeitorArquivo.lerRecords(FILEPATH_PESSOAS2);
	}

	@Test
	public void testLerRecordsQtdOk() throws IOException {

		assertEquals(FILE_SIZE_PESSOAS1, recordsDoArquivo1.size());
		assertEquals(FILE_SIZE_PESSOAS2, recordsDoArquivo2.size());
	}

	@Test
	public void testLerRecordsPrimeiroRecordOK() throws IOException {

		String[] primeiroRecordPessoas1 = recordsDoArquivo1.get(0);

		assertEquals(FILE_PESSOAS1_PRIMEIRA_LINHA[0],
				primeiroRecordPessoas1[0]);
		assertEquals(FILE_PESSOAS1_PRIMEIRA_LINHA[1],
				primeiroRecordPessoas1[1]);
		assertEquals(FILE_PESSOAS1_PRIMEIRA_LINHA[2],
				primeiroRecordPessoas1[2]);

		String[] primeiroRecordPessoas2 = recordsDoArquivo2.get(0);

		assertEquals(FILE_PESSOAS2_PRIMEIRA_LINHA[0],
				primeiroRecordPessoas2[0]);
		assertEquals(FILE_PESSOAS2_PRIMEIRA_LINHA[1],
				primeiroRecordPessoas2[1]);
		assertEquals(FILE_PESSOAS2_PRIMEIRA_LINHA[2],
				primeiroRecordPessoas2[2]);
		assertEquals(FILE_PESSOAS2_PRIMEIRA_LINHA[3],
				primeiroRecordPessoas2[3]);
	}

	@Test
	public void testLerRecordsUltimoRecordOK() throws IOException {

		String[] ultimoRecordPessoas1 = recordsDoArquivo1
				.get(recordsDoArquivo1.size() - 1);

		assertEquals(FILE_PESSOAS1_ULTIMA_LINHA[0], ultimoRecordPessoas1[0]);
		assertEquals(FILE_PESSOAS1_ULTIMA_LINHA[1], ultimoRecordPessoas1[1]);
		assertEquals(FILE_PESSOAS1_ULTIMA_LINHA[2], ultimoRecordPessoas1[2]);

		String[] ultimoRecordPessoas2 = recordsDoArquivo2
				.get(recordsDoArquivo2.size() - 1);

		assertEquals(FILE_PESSOAS2_ULTIMA_LINHA[0], ultimoRecordPessoas2[0]);
		assertEquals(FILE_PESSOAS2_ULTIMA_LINHA[1], ultimoRecordPessoas2[1]);
		assertEquals(FILE_PESSOAS2_ULTIMA_LINHA[2], ultimoRecordPessoas2[2]);
		assertEquals(FILE_PESSOAS2_ULTIMA_LINHA[3], ultimoRecordPessoas2[3]);
	}
	
}
