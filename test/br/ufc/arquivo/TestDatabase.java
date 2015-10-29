package br.ufc.arquivo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

// TODO: resolver o teste
// TODO; refatorar
public class TestDatabase {

	private static final String FILE_PATH_ARQUIVO_100K_REGISTROS = "./resources/pessoas.csv";

	private static final String FILE_PATH_ARQUIVO_100K_REGISTROS_LINHA_1_CORROMPIDA = "./resources/pessoas_linha_1_corrompida.csv";

	private static final String STRING_SIZE_GT_50 = "AAAAAAAAAA_BBBBBBBBBB_CCCCCCCCCC_DDDDDDDDDD_EEEEEEEEEE";

	private static final String PATH_DB = "jdbc:hsqldb:mem:/"
			+ TestDatabase.class.getName();

	private static Database db;

	@BeforeClass
	public static void setUp() throws SQLException {

		db = new Database(PATH_DB);
		db.criarTabela();
	} // end setUp method

	@After
	public void tearDown() throws SQLException {

		db.reset();
	} // end tearDown method

	private void testSalvarRegistro(String[] dataRow) throws SQLException {

		ArrayList<String[]> data = new ArrayList<String[]>(1);
		data.add(dataRow);

		// exercise
		db.salvar(data);

		// verifica se foi adicionado corretamente
		List<Object[]> allRows = db.all();

		assertNotNull(allRows);
		assertEquals(1, allRows.size());

		Object[] row0 = allRows.get(0);

		for (int i = 0; i < dataRow.length; i++) {

			if (i == 1) {

				assertEquals(Integer.parseInt(dataRow[i]), (int) row0[i]);
			} else {
				assertEquals(dataRow[i], row0[i]);
			}
		}
	}

	@Test
	public void testSalvarRegistroCom0Colunas() throws SQLException {

		testSalvarRegistro(new String[] {});
	}

	@Test
	public void testSalvarRegistroCom3Colunas() throws SQLException {

		String[] dataRow = { "oi", "30", "fala?" };
		testSalvarRegistro(dataRow);
	}

	@Test
	public void testSalvarRegistroCom4Colunas() throws SQLException {

		String[] dataRow = { "Joao", "32", "Analista", "29/11/1970" };
		testSalvarRegistro(dataRow);
	} // end testSalvarRegistroComQuatroColunas method

	@Test(expected = IllegalArgumentException.class)
	// lança NumberFormatException, que é subclasse de IAException
	public void seIdadeNaoIntegerEntaoLancaException() throws SQLException {

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
	 */

	@Test(expected = BatchUpdateException.class)
	public void seHouverRegistroCorrompidoEntaoNenhumDeveSerSalvo()
			throws SQLException {

		List<String[]> registros = new ArrayList<>(2);

		String[] registroOk = { "Joao", "45", "Analista" };
		String[] registroNaoOk = { STRING_SIZE_GT_50, "34", "Programador" };

		registros.add(registroOk);
		registros.add(registroNaoOk);

		db.setChunkSize(1);

		db.salvar(registros);

		assertEquals(0, db.quantidadeDeRegistros().intValue());
	} // end testRegistroCorrompido method

	@Test(timeout = 3000)
	// engraçado -> performance melhorou de ~3s para ~1.5s com as últimas
	// alterações
	public void seArquivoCom100KRegistrosEntaoSalvarEmMenosDe3Segundos()
			throws SQLException, FileNotFoundException, IOException {

		List<String[]> records = LeitorArquivo
				.lerRecords(FILE_PATH_ARQUIVO_100K_REGISTROS);

		db.salvar(records);

		assertEquals(100000, db.quantidadeDeRegistros().intValue());
	} // end testSalvarArquivo method

	@Test(expected = IllegalArgumentException.class, timeout = 1000)
	public void seArquivoCom100KRegistrosEComRegistroCorrompidoNaPrimeiraLinhaEntaoLancaExceptionEmMenosDe1Segundo()
			throws FileNotFoundException, IOException, SQLException {

		List<String[]> records = LeitorArquivo
				.lerRecords(FILE_PATH_ARQUIVO_100K_REGISTROS_LINHA_1_CORROMPIDA);

		db.salvar(records);
	}

	@Test
	public void seColunaIdadeNullEntaoDeveRetornarNull() throws SQLException {

		String[] dataRow = { "oi", "", "fala?" };

		ArrayList<String[]> data = new ArrayList<String[]>(1);
		data.add(dataRow);

		// exercise
		db.salvar(data);
		
		Object[] row0 = db.all().get(0);
		
		assertNull(row0[1]);
	}

}