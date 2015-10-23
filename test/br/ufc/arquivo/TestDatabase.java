package br.ufc.arquivo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertSame;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.DriverManager;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

// TODO: <quando houver coluna do tipo int>
// testar comportamento de getInt, no caso de o valor da coluna
// no registro ser nula
public class TestDatabase {

	@Test
	public void seSalvarUmRegistroEntãoDeveEstarNoBanco() {

		// um registro
		String col0 = "oi";
		String col1 = "quem";
		String col2 = "fala?";

		String[] dataRow = new String[3];
		dataRow[0] = col0;
		dataRow[1] = col1;
		dataRow[2] = col2;

		ArrayList<String[]> data = new ArrayList<String[]>(1);
		data.add(dataRow);

		// db
		Database db = new Database();

		// limpa db
		db.reset();

		// exercise
		db.salvar(data);

		// verifica se foi adicionado corretamente
		List<String[]> allRows = db.all();

		assertNotNull(allRows);
		assertEquals(1, allRows.size());

		String[] row0 = allRows.get(0);

		assertEquals(col0, row0[0]);
		assertEquals(col1, row0[1]);
		assertEquals(col2, row0[2]);

	}
	
	@Test(timeout = 120000)
	public void testSalvarArquivo() {
		
		List<String[]> records = LeitorArquivo.lerRecords("./resources/pessoas.csv");
		
		Database db = new Database();
		
		db.reset();
		
		db.salvar(records);
		
	} // end testSalvarArquivo method

	@Test
	public void testQuantidadeDeRegistrosOk() throws SQLException {

		Database db = new Database();

		db.reset();

		String col0 = "oi";
		String col1 = "quem";
		String col2 = "fala?";

		String[] dataRow = new String[3];
		dataRow[0] = col0;
		dataRow[1] = col1;
		dataRow[2] = col2;

		ArrayList<String[]> data = new ArrayList<String[]>(1);
		data.add(dataRow);
		
		db.salvar(data);
		
		assertEquals(data.size(), db.quantidadeDeRegistros());

	} // end testQuantidadeDeRegistrosOk method
	
	@Test
	public void testStatementComDoisResultSetAbertos() throws SQLException {
		
		String path = 
				"jdbc:sqlite:/home/abevieiramota/Documents/recursoshumanos.db";
		
		Connection connection = DriverManager.getConnection(path);
		
		Statement statement = connection.createStatement();
	
		statement.execute("delete from pessoa");
		
		Statement statementInsert = connection.createStatement();
		
		statementInsert.execute("insert into pessoa (nome, idade, profissao) " + 
					"values (\"Anderson\", \"21\", \"Programador\")");
		statementInsert.execute("insert into pessoa (nome, idade, profissao) " + 
					"values (\"Gomes\", \"21\", \"Programador\")");
		
		ResultSet rs1 = statement.executeQuery("select profissao from pessoa");
		ResultSet rs2 = statement.executeQuery("select nome from pessoa");
		
		assertTrue(rs1.next());
		assertEquals(rs1.getRow(), 1);
		
		assertTrue(rs2.next());
		assertEquals(rs2.getRow(), 2);
		
		assertSame(rs1, rs2);
		
		rs1.close();
		rs2.close();
		
		connection.close();
		
	} // end testUmStatementPodeRetornarUmResultSetComOutroAberto method

}
