package br.ufc.arquivo;

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
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestDatabase {

	private static final String FILE_PATH_ARQUIVO_100K_REGISTROS = "./resources/pessoas.csv";

	private static final String FILE_PATH_ARQUIVO_100K_REGISTROS_LINHA_1_CORROMPIDA = "./resources/pessoas_linha_1_corrompida.csv";

	private static final String STRING_SIZE_GT_50 = "AAAAAAAAAA_BBBBBBBBBB_CCCCCCCCCC_DDDDDDDDDD_EEEEEEEEEE";
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");

	private static final String PATH_DB = "jdbc:hsqldb:mem:/"
			+ TestDatabase.class.getName();

	private static Database db;

	private static Connection conn;

	@BeforeClass
	public static void beforeClass() throws SQLException {

		conn = DriverManager.getConnection(PATH_DB);
		db = new Database(conn);
		db.criarTabela();
	} // end setUp method

	@AfterClass
	public static void afterClass() {

		try {

			conn.close();
		} catch (SQLException e) {

			System.out.println("Não foi possível fechar a conexão");
		}
	}
	
	@After
	public void tearDown() throws SQLException {

		db.reset();
	} // end tearDown method

	private void testSalvarRegistro(String[] dataRow) throws SQLException,
			ParseException {

		ArrayList<String[]> data = new ArrayList<String[]>(1);
		data.add(dataRow);

		// exercise
		db.salvar(data);

		// verifica se foi adicionado corretamente
		List<Object[]> allRows = db.all();

		assertNotNull(allRows);
		assertEquals(1, allRows.size());

		Object[] firstRow = allRows.get(0);
		
		for (int i = 0; i < dataRow.length; i++) {

			if (i == 1) {

				assertEquals(Integer.parseInt(dataRow[i]), firstRow[i]);
			} else if (i == 3) {
				
				assertEquals(sdf.parse(dataRow[i]), firstRow[i]);
			} else {
				assertEquals(dataRow[i], firstRow[i]);
			}
		}

		for (int j = dataRow.length; j < firstRow.length; j++) {

			assertNull(firstRow[j]);
		}
	}

	@Test
	public void testSalvarRegistroCom0Colunas() throws SQLException,
			ParseException {

		testSalvarRegistro(new String[] {});
	}
	
	@Test
	public void testSalvarRegistroCom3Colunas() throws SQLException,
			ParseException {

		String[] dataRow = { "oi", "30", "fala?" };
		testSalvarRegistro(dataRow);
	}

	@Test
	public void testSalvarRegistroCom4Colunas() throws SQLException,
			ParseException {

		String[] dataRow = { "Joao", "32", "Analista", "29/11/1970" };
		testSalvarRegistro(dataRow);
	} // end testSalvarRegistroComQuatroColunas method

	@Test(expected = IllegalArgumentException.class)
	public void seIdadeNaoIntegerEntaoLancaException() throws SQLException,
			ParseException {

		String[] dataRow = { "oi", "NaoSouInteger", "fala?" };

		ArrayList<String[]> data = new ArrayList<String[]>(1);
		data.add(dataRow);

		// exercise
		db.salvar(data);
	}

	/**
	 * Dado o chunkSize = 1, o primeiro registro será inserido em batch e então
	 * virá o segundo registro, inválido Com a inserção em transação, nenhum
	 * registro deverá ser inserido no banco
	 * 
	 * @throws SQLException
	 * @throws ParseException
	 */

	@Test(expected = BatchUpdateException.class)
	public void seHouverRegistroCorrompidoEntaoNenhumDeveSerSalvo()
			throws SQLException, ParseException {

		List<String[]> registros = new ArrayList<>(2);

		String[] registroOk = { "Joao", "45", "Analista" };
		String[] registroNaoOk = { STRING_SIZE_GT_50, "34", "Programador" };

		registros.add(registroOk);
		registros.add(registroNaoOk);

		db.setChunkSize(1);

		db.salvar(registros);

		assertEquals(0, db.quantidadeDeRegistros());
	} // end testRegistroCorrompido method

	@Test(timeout = 2000)
	public void seArquivoCom100KRegistrosEntaoSalvarEmMenosDe2Segundos()
			throws SQLException, FileNotFoundException, IOException,
			ParseException {

		List<String[]> records = LeitorArquivo
				.lerRecords(FILE_PATH_ARQUIVO_100K_REGISTROS);

		db.salvar(records);

		assertEquals(100000, db.quantidadeDeRegistros());
	} // end testSalvarArquivo method

	@Test(expected = IllegalArgumentException.class, timeout = 1000)
	public void seArquivoCom100KRegistrosEComRegistroCorrompidoNaPrimeiraLinhaEntaoLancaExceptionEmMenosDe1Segundo()
			throws FileNotFoundException, IOException, SQLException,
			ParseException {

		List<String[]> records = LeitorArquivo
				.lerRecords(FILE_PATH_ARQUIVO_100K_REGISTROS_LINHA_1_CORROMPIDA);

		db.salvar(records);
	}

	@Test
	public void seColunaIdadeNullEntaoDeveRetornarNull() throws SQLException,
			ParseException {

		String[] dataRow = { "oi", "", "fala?" };

		ArrayList<String[]> data = new ArrayList<String[]>(1);
		data.add(dataRow);

		// exercise
		db.salvar(data);

		Object[] row0 = db.all().get(0);

		assertNull(row0[1]);
	}
	
	@Test
	// TODO: analisar por que esse teste conclui se executado sozinho, mas para caso executado com os demais
	// dica -> connection aberta lokando a tabela
	// TODO: corrigir o Database
	// TODO: dar um nome decente para esse teste
	public void test() throws FileNotFoundException, IOException, SQLException {
		
		List<String[]> records = LeitorArquivo
				.lerRecords(FILE_PATH_ARQUIVO_100K_REGISTROS);
		
		try(Connection conn = DriverManager.getConnection(PATH_DB)) {

			Database db = new Database(conn);
			
			db.salvar(records);
			db.reset();
		} catch (SQLException | ParseException e) {
			System.err.println(e);
		}
		
		try(Connection conn = DriverManager.getConnection(PATH_DB)) {
			
			Database db = new Database(conn);
			
			assertEquals(0, db.quantidadeDeRegistros());
		} catch (SQLException e) {
		}
	}

}