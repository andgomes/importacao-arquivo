package br.ufc.arquivo;

import static org.junit.Assert.assertArrayEquals;

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

@RunWith(Parameterized.class)
public class TestIntegracao {
	
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");
	
	@Parameters(name="path do arquivo {0}")
	public static List<Object[]> getParameters() throws ParseException {
		
		List<Object[]> col = new LinkedList<>();
		
		
		col.add(new Object[] {"./resources/pessoas1_10000registros.csv", 
					new Object[] {"Abelardo 0", 20, "Analista de TI", null},
					new Object[] {"Abelardo 9997", 97, "Analista de Marmotagem", null}});
		
		col.add(new Object[] {"./resources/pessoas2_10000registros.csv", 
					new Object[] {"Abelardo 3", 23, "Analista de Nada", sdf.parse("01/04/89")},
					new Object[] {"Abelardo 9979", 79, "Analista de Nada", sdf.parse("03/17/97")}});
		
		col.add(new Object[] {"./resources/pessoas3_10000registros.csv", 
					new Object[] {"Abelardo 3", 23, "Analista de Nada", sdf.parse("01/04/89")},
					new Object[] {"Abelardo 9979", 79, "Analista de Nada", sdf.parse("03/17/97")}});
		
		return col;
		
	} // end getParameters method
	
	public TestIntegracao(String filePath, Object[] expectedFirstRegister, Object[] expectedLastRegister) {
		
		this.filePath = filePath;
		this.expectedFirstRegister = expectedFirstRegister;
		this.expectedLastRegister = expectedLastRegister;
	} // end TestIntegracao constructor -- String, String, String
	
	private String filePath;
	private Object[] expectedFirstRegister;
	private Object[] expectedLastRegister;
	
	private static final String PATH_DB = "jdbc:hsqldb:mem:/" + TestIntegracao.class.getName();
	
	private static Database db;
	
	private static Connection conn;
	
	@BeforeClass
	public static void setUp() throws SQLException {

		conn = DriverManager.getConnection(PATH_DB);
		db = new Database(conn);
		db.criarTabela();
	} // end setUp method
	
	@After
	public void tearDown() throws SQLException {
		
		db.reset();
	}

	@Test
	public void lerArquivoESalvaNoBanco() throws IOException, SQLException, ParseException {
		
		List<String[]> records = LeitorArquivo.lerRecords(filePath);
		
		db.salvar(records);
		
		List<Object[]> all = db.all();
		
		Object[] firstRow = all.get(0);
		Object[] lastRow = all.get(all.size() - 1);
		
		assertArrayEquals(this.expectedFirstRegister, firstRow);
		assertArrayEquals(this.expectedLastRegister, lastRow);
	} // end lerArquivoESalvaNoBanco method
	
	
} // end TestLeitorArquivoDatabase class