package br.ufc.arquivo;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Iterator;
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
				new Pessoa("Abelardo 0", 20, "Analista de TI", null, null),
				new Pessoa("Abelardo 9997", 97, "Analista de Marmotagem", null,
						null) });

		col.add(new Object[] {
				"./resources/pessoas2_10000registros.csv",
				new Pessoa("Abelardo 3", 23, "Analista de Nada", sdf
						.parse("01/04/89"), null),
				new Pessoa("Abelardo 9979", 79, "Analista de Nada", sdf
						.parse("03/17/97"), null) });

		col.add(new Object[] {
				"./resources/pessoas3_10000registros.csv",
				new Pessoa("Abelardo 3", 23, "Analista de Nada", sdf
						.parse("01/04/89"), null),
				new Pessoa("Abelardo 9979", 79, "Analista de Nada", sdf
						.parse("03/17/97"), null) });

		col.add(new Object[] {
				"./resources/pessoas4_10000registros.csv",
				new Pessoa("Abelardo 3", 23, "Analista de Nada", sdf
						.parse("01/04/89"), 27900l),
				new Pessoa("Abelardo 9979", 79, "Analista de Nada", sdf
						.parse("03/17/97"), 1002802620789l) });

		return col;
	}

	public TestIntegracao(String filePath, Pessoa expectedFirstPessoa,
			Pessoa expectedLastPessoa) {

		this.filePath = filePath;
		this.expectedFirstPessoa = expectedFirstPessoa;
		this.expectedLastPessoa = expectedLastPessoa;
	}

	private String filePath;
	private Pessoa expectedFirstPessoa;
	private Pessoa expectedLastPessoa;

	private static final String PATH_DB = "jdbc:hsqldb:mem:/"
			+ TestIntegracao.class.getName();

	private static Database db;
	private static Connection conn;

	@BeforeClass
	public static void setUp() throws SQLException {

		conn = DriverManager.getConnection(PATH_DB);
		db = new Database(conn);
		db.createTable();
	}

	@After
	public void tearDown() throws SQLException {

		db.reset();
	}

	@Test
	public void lerArquivoESalvarNoBanco() throws IOException, SQLException,
			ParseException {

		try (LeitorArquivo leitor = new LeitorArquivo(filePath)) {

			db.save(leitor);

			List<Pessoa> pessoas = db.all();

			Pessoa firstPessoa = pessoas.get(0);
			Pessoa lastPessoa = pessoas.get(pessoas.size() - 1);

			assertEquals(this.expectedFirstPessoa, firstPessoa);
			assertEquals(this.expectedLastPessoa, lastPessoa);
		}

	}

	@Test
	public void testLeitorArquivoComoIterableDePessoa() throws SQLException, ParseException, IOException {
		
		LeitorArquivo leitor = new LeitorArquivo(filePath);
		
		Iterator<String[]> iterPessoa =  leitor.iterator();
		
		db.save(leitor);
		
		List<Pessoa> pessoas = db.all();
		
		Pessoa firstPessoa = pessoas.get(0);
		Pessoa lastPessoa = pessoas.get(pessoas.size() - 1);
		
		assertEquals(this.expectedFirstPessoa, firstPessoa);
		assertEquals(this.expectedLastPessoa, lastPessoa);
		
	}
	
}