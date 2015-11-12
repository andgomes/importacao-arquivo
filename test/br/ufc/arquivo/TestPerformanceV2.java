package br.ufc.arquivo;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.LinkedList;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

import br.ufc.arquivo.database.Database;
import br.ufc.arquivo.reader.LeitorArquivoV2;

// 7.73
@Ignore
@RunWith(Parameterized.class)
public class TestPerformanceV2 {

	private static final String PATH_DB = "jdbc:hsqldb:mem:/"
			+ TestPerformanceV2.class.getName();

	private static final String FILE_PATH_ARQUIVO_100K_REGISTROS = "./resources/pessoas.csv";

	private static Database db;

	private static Connection conn;

	private int chunkSize;
	
	private LeitorArquivoV2 leitor;
	
	public TestPerformanceV2(int chunkSize) throws FileNotFoundException, IOException {

		this.chunkSize = chunkSize;
		this.leitor = new LeitorArquivoV2(FILE_PATH_ARQUIVO_100K_REGISTROS);
	}

	@Parameters(name = "chunkSize = {0}")
	public static List<Integer> getParameters() throws ParseException {

		List<Integer> col = new LinkedList<>();

		col.add(1);
		col.add(2);
		col.add(5);
		col.add(10);
		col.add(20);
		col.add(50);
		col.add(100);
		col.add(200);
		col.add(500);
		col.add(1000);
		col.add(2000);
		col.add(5000);
		col.add(10000);
		col.add(20000);
		col.add(50000);
		col.add(100000);
		col.add(1000000);

		return col;

	} // end getParameters method

	@BeforeClass
	public static void beforeClass() throws SQLException {

		conn = DriverManager.getConnection(PATH_DB);
		db = new Database(conn);
		db.createTable();
	}

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
	}

	@Test
	public void testSalvar100KRegistros() throws FileNotFoundException, IOException, SQLException, ParseException {

		db.setChunkSize(this.chunkSize);
		db.save(this.leitor);
	}

}
