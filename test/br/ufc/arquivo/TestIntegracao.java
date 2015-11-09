package br.ufc.arquivo;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import br.ufc.arquivo.database.Database;
import br.ufc.arquivo.model.Pessoa;
import br.ufc.arquivo.reader.LeitorArquivo;

@RunWith(Parameterized.class)
public class TestIntegracao {

	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");

	@Parameters(name = "path do arquivo {0}")
	public static List<Object[]> getParameters() throws ParseException {

		List<Object[]> col = new LinkedList<>();

		col.add(new Object[] {
				"./resources/pessoas1_10000registros.csv",
				new Object[] { "Abelardo 0", 20, "Analista de TI", null },
				new Object[] { "Abelardo 9997", 97, "Analista de Marmotagem",
						null } });

		col.add(new Object[] {
				"./resources/pessoas2_10000registros.csv",
				new Object[] { "Abelardo 3", 23, "Analista de Nada",
						sdf.parse("01/04/89") },
				new Object[] { "Abelardo 9979", 79, "Analista de Nada",
						sdf.parse("03/17/97") } });

		col.add(new Object[] {
				"./resources/pessoas3_10000registros.csv",
				new Object[] { "Abelardo 3", 23, "Analista de Nada",
						sdf.parse("01/04/89") },
				new Object[] { "Abelardo 9979", 79, "Analista de Nada",
						sdf.parse("03/17/97") } });

		return col;
	}

	public TestIntegracao(String filePath, Object[] expectedFirstRegister,
			Object[] expectedLastRegister) {

		this.filePath = filePath;
		this.expectedFirstRegister = expectedFirstRegister;
		this.expectedLastRegister = expectedLastRegister;
	}

	private String filePath;
	private Object[] expectedFirstRegister;
	private Object[] expectedLastRegister;

	private static final String PATH_DB = "jdbc:hsqldb:mem:/"
			+ TestIntegracao.class.getName();

	private static Database db;

	private static Connection conn;

	@BeforeClass
	public static void setUp() throws SQLException {

		conn = DriverManager.getConnection(PATH_DB);
		db = new Database(conn);
		db.criarTabela();
	}

	@After
	public void tearDown() throws SQLException {

		db.reset();
	}

	@Test
	public void lerArquivoESalvaNoBanco() throws IOException, SQLException,
			ParseException {

		List<String[]> records = LeitorArquivo.lerRecords(filePath);

		db.salvar(records);

		List<Object[]> all = db.all();

		Object[] firstRow = all.get(0);
		Object[] lastRow = all.get(all.size() - 1);

		assertArrayEquals(this.expectedFirstRegister, firstRow);
		assertArrayEquals(this.expectedLastRegister, lastRow);
	}

	@Test
	public void testConverterIdade() throws SQLException, ParseException {

		List<String[]> rows = new LinkedList<>();

		rows.add(new String[] { "Joao 1", "23", "Analista" });
		rows.add(new String[] { "Joao 2", "21", "Programador" });

		db.salvar(rows);

		Converter converter = new Converter(db.all());

		Pessoa pessoa1 = converter.nextPessoa();
		Pessoa pessoa2 = converter.nextPessoa();

		assertEquals(new Integer(23), pessoa1.getIdade());
		assertEquals(new Integer(21), pessoa2.getIdade());

	} // end testConverterIdade method

	@Test
	public void testGetIdadeNaoSetadaIgualANull() throws SQLException,
			ParseException {

		List<String[]> rows = new LinkedList<>();

		rows.add(new String[] { "Joao", "23", "Analista" });

		db.salvar(rows);

		// qual a necessidade do código acima?
		Pessoa pessoa = new Pessoa();

		assertEquals(null, pessoa.getIdade());
	} // end testGetIdadeNaoSetada method

}