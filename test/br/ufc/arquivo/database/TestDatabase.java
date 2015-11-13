package br.ufc.arquivo.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import br.ufc.arquivo.model.Pessoa;
import br.ufc.arquivo.reader.LeitorArquivo;

public class TestDatabase {

	private static final String FILE_PATH_ARQUIVO_100K_REGISTROS = "./resources/pessoas.csv";

	private static final String FILE_PATH_ARQUIVO_100K_REGISTROS_LINHA_1_CORROMPIDA = "./resources/pessoas_linha_1_corrompida.csv";

	private static final String STRING_SIZE_GT_50 = "AAAAAAAAAA_BBBBBBBBBB_CCCCCCCCCC_DDDDDDDDDD_EEEEEEEEEE";

	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");

	private static final String PATH_DB = "jdbc:hsqldb:mem:/"
			+ TestDatabase.class.getName();

	private static Database db;

	private static Connection conn;

	/* beforeClass apenas cria a tabela */
	@BeforeClass
	public static void beforeClass() throws SQLException {

		conn = DriverManager.getConnection(PATH_DB);
		db = new Database(conn);
		db.createTable();
	}

	@After
	public void tearDown() throws SQLException {

		db.reset();
	} // end tearDown method

	private void testSalvarRegistro(String[] dataRow) throws SQLException,
			ParseException {

		List<String[]> data = new ArrayList<String[]>(1);
		data.add(dataRow);

		// exercise
		db.save(data);

		// verify
		List<Pessoa> pessoas = db.all();

		assertNotNull(pessoas);
		assertEquals(1, pessoas.size());

		Pessoa firstPessoa = pessoas.get(0);

		String[] dataRowCompleted = Arrays.copyOf(dataRow, 5);

		assertEquals(dataRowCompleted[0], firstPessoa.getNome());
		assertEquals(
				dataRowCompleted[1] == null ? null
						: Integer.parseInt(dataRowCompleted[1]),
				firstPessoa.getIdade());
		assertEquals(dataRowCompleted[2], firstPessoa.getCargo());
		assertEquals(
				dataRowCompleted[3] == null ? null
						: sdf.parse(dataRowCompleted[3]),
				firstPessoa.getDataNascimento());
		assertEquals(
				dataRowCompleted[4] == null ? null
						: Long.parseLong(dataRowCompleted[4]),
						firstPessoa.getCpf());
	}

	@Test
	public void testSalvarRegistroCom0Colunas() throws SQLException,
			ParseException {

		testSalvarRegistro(new String[] {});
	}

	@Test
	public void testSalvarRegistroCom3Colunas() throws SQLException,
			ParseException {

		String[] dataRow = { "Juliana", "30", "Médico" };
		testSalvarRegistro(dataRow);
	}

	@Test
	public void testSalvarRegistroCom4Colunas() throws SQLException,
			ParseException {

		String[] dataRow = { "Joao", "32", "Analista", "29/11/1970" };
		testSalvarRegistro(dataRow);
	}

	@Test
	public void testSalvarRegistroCom5Colunas() throws SQLException,
			ParseException {

		String[] dataRow = { "Joao", "32", "Analista", "29/11/1970",
				"988444802133" };
		testSalvarRegistro(dataRow);
	}

	@Test(expected = IllegalArgumentException.class)
	public void seIdadeNaoIntegerEntaoLancaIllegalArgumentException()
			throws SQLException, ParseException {

		String[] dataRow = { "Aderbaldo", "NaoSouInteger", "Professor" };

		List<String[]> data = new ArrayList<String[]>(1);
		data.add(dataRow);

		// exercise
		db.save(data);
	}

	/**
	 * Serão inseridos 2 registros: o primeiro válido, o segundo inválido.
	 * Utilizando-se um chunkSize = 1, o primeiro registro será inserido em lote
	 * e espera-se que o segundo registro não seja inserido(inválido) e que isso
	 * cause um rollback na inserção do primeiro registro(primeiro lote).
	 * 
	 * @throws SQLException
	 * @throws ParseException
	 */

	@Test(expected = BatchUpdateException.class)
	public void seHouverRegistroCorrompidoEntaoNenhumRegistroDeveSerSalvo()
			throws SQLException, ParseException {

		List<String[]> registros = new ArrayList<>(2);

		String[] registroOk = { "Joao", "45", "Analista" };
		String[] registroNaoOk = { STRING_SIZE_GT_50, "34", "Programador" };

		registros.add(registroOk);
		registros.add(registroNaoOk);

		db.setChunkSize(1);

		db.save(registros);

		assertEquals(0, db.size());
	} // end testRegistroCorrompido method

	@Test(timeout = 2000)
	public void seArquivoCom100KRegistrosEntaoSalvarEmMenosDe2SegundosV2()
			throws SQLException, FileNotFoundException, IOException,
			ParseException {

		LeitorArquivo leitor = new LeitorArquivo(
				FILE_PATH_ARQUIVO_100K_REGISTROS);

		db.save(leitor);

		assertEquals(100000, db.size());
	} // end testSalvarArquivo method

	@Test(expected = IllegalArgumentException.class, timeout = 1000)
	public void seArquivoCom100KRegistrosEComRegistroCorrompidoNaPrimeiraLinhaEntaoLancaExceptionEmMenosDe1SegundoV2()
			throws FileNotFoundException, IOException, SQLException,
			ParseException {

		LeitorArquivo leitor = new LeitorArquivo(
				FILE_PATH_ARQUIVO_100K_REGISTROS_LINHA_1_CORROMPIDA);

		db.save(leitor);
	}

	@Test
	public void seColunaIdadeNullEntaoDeveRetornarNull() throws SQLException,
			ParseException {

		String[] dataRow = { "oi", "", "fala?" };

		List<String[]> data = new ArrayList<String[]>(1);
		data.add(dataRow);

		// exercise
		db.save(data);

		Pessoa firstPessoa = db.all().get(0);

		assertNull(firstPessoa.getIdade());
	}

}