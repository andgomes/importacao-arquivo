package br.ufc.arquivo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static org.junit.Assert.*;

import org.junit.Test;

public class TestConnection {

	private static final String PATH_DB = "jdbc:hsqldb:mem:/"
			+ TestConnection.class.getSimpleName();

	/**
	 * Teste para verificar o comportamento de um select numa tabela caso haja
	 * um insert não commitado, os dois comandos na mesma conexão.
	 * 
	 * @throws SQLException
	 */
	@Test
	public void testSelectReturnsValueNotCommitedInSameConnection()
			throws SQLException {

		Connection conn = DriverManager.getConnection(PATH_DB);

		Statement stmt = conn.createStatement();

		conn.setAutoCommit(false);

		stmt.execute("CREATE TABLE foo (id INTEGER)");
		stmt.execute("INSERT INTO foo (id) VALUES (1)");
		ResultSet rs = stmt.executeQuery("SELECT count(*) FROM foo");
		rs.next();

		assertEquals(1, rs.getInt(1));

		rs.close();
		stmt.close();
		conn.close();
	} 

}