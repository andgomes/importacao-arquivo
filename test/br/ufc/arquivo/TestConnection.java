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

		Connection conn1 = DriverManager.getConnection(PATH_DB);

		Statement stmt1 = conn1.createStatement();

		conn1.setAutoCommit(false);

		stmt1.execute("CREATE TABLE foo (id INTEGER)");
		stmt1.execute("INSERT INTO foo (id) VALUES (1)");
		ResultSet rs1 = stmt1.executeQuery("SELECT count(*) FROM foo");
		rs1.next();

		assertEquals(1, rs1.getInt(1));

		rs1.close();
		stmt1.close();
		conn1.close();
	} // end testSelectInTransactionNotCommited method

} // end TestConnection class