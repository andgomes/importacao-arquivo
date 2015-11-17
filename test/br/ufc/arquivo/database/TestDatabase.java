package br.ufc.arquivo.database;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import br.ufc.arquivo.model.Pessoa;

public class TestDatabase {

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
		db.createTable();
	}

	@After
	public void tearDown() throws SQLException {

		db.reset();
	}

	private void testSalvarRegistro(Pessoa pessoa) throws SQLException,
			ParseException {

		List<Pessoa> data = new ArrayList<Pessoa>(1);
		data.add(pessoa);

		// exercise
		db.save(data);

		// verify
		List<Pessoa> pessoas = db.all();

		assertNotNull(pessoas);
		assertEquals(1, pessoas.size());

		Pessoa firstPessoa = pessoas.get(0);

		assertEquals(pessoa.getNome(), firstPessoa.getNome());
		assertEquals(pessoa.getCargo(), firstPessoa.getCargo());
		assertEquals(
				pessoa.getDataNascimento() == null ? null
						: pessoa.getDataNascimento(),
				firstPessoa.getDataNascimento());
		assertEquals(pessoa.getCpf() == null ? null : pessoa.getCpf(),
				firstPessoa.getCpf());
	}

	@Test
	public void testSalvarRegistroCom0Colunas() throws SQLException,
			ParseException {

		testSalvarRegistro(new Pessoa(null, null, null, null));
	}

	@Test
	public void testSalvarRegistroCom3Colunas() throws SQLException,
			ParseException {

		Pessoa pessoa = new Pessoa("Juliana", "Médico", null, null);
		testSalvarRegistro(pessoa);
	}

	@Test
	public void testSalvarRegistroCom4Colunas() throws SQLException,
			ParseException {

		Pessoa pessoa = new Pessoa("João", "Analista", sdf.parse("29/11/1970"),
				null);
		testSalvarRegistro(pessoa);
	}

	@Test
	public void testSalvarRegistroCom5Colunas() throws SQLException,
			ParseException {

		Pessoa pessoa = new Pessoa("João", "Analista", sdf.parse("29/11/70"),
				988444802133L);
		testSalvarRegistro(pessoa);
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

		List<Pessoa> registros = new ArrayList<Pessoa>(2);

		Pessoa pessoa1 = new Pessoa("Joao", "Analista", null, null);
		Pessoa pessoa2 = new Pessoa(STRING_SIZE_GT_50, "Programador", null,
				null);

		registros.add(pessoa1);
		registros.add(pessoa2);

		db.setChunkSize(1);

		db.save(registros);

		assertEquals(0, db.size());
	} 

}