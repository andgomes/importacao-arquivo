package br.ufc.arquivo;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.BatchUpdateException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

// XXX: <quando houver coluna do tipo int>
// testar comportamento de getInt, no caso de o valor da coluna
// no registro ser nula
// ver TestJDBC#seNoBancoColunaEhNullEntaoVemComValor0
public class TestDatabase {

	private static final String FILE_PATH_ARQUIVO_100K_REGISTROS = "./resources/pessoas.csv";

	private static final String STRING_SIZE_GT_50 = "AAAAAAAAAA_BBBBBBBBBB_CCCCCCCCCC_DDDDDDDDDD_EEEEEEEEEE";

	private static Database db;

	@BeforeClass
	public static void setUp() throws SQLException {

		db = new Database();
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
		List<String[]> allRows = db.all();

		assertNotNull(allRows);
		assertEquals(1, allRows.size());

		String[] row0 = allRows.get(0);

		String[] row0Copy = Arrays.copyOfRange(row0, 0, dataRow.length);

		assertArrayEquals(dataRow, row0Copy);
	}

	@Test
	public void testSalvarRegistroCom0Colunas() throws SQLException {

		testSalvarRegistro(new String[]{});
	}

	@Test
	public void testSalvarRegistroCom3Colunas() throws SQLException {

		String[] dataRow = { "oi", "quem", "fala?" };
		testSalvarRegistro(dataRow);
	}

	@Test
	public void testSalvarRegistroCom4Colunas() throws SQLException {

		String[] dataRow = { "Joao", "32", "Analista", "29/11/1970" };
		testSalvarRegistro(dataRow);
	} // end testSalvarRegistroComQuatroColunas method

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

	@Test(timeout = 40000)
	public void seArquivoCom100KRegistrosEntaoSalvarEmMenosDe3SegundosVersaoNaoGuava()
			throws SQLException, FileNotFoundException, IOException {

		List<String[]> records = LeitorArquivo
				.lerRecords(FILE_PATH_ARQUIVO_100K_REGISTROS);

		db.salvar(records);

		assertEquals(100000, db.quantidadeDeRegistros().intValue());
	} // end testSalvarArquivo method

}