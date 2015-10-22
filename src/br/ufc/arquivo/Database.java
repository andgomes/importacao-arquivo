package br.ufc.arquivo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// TODO: pesquisar sobre JDBC - arquitetura de statements, resultset, connection - visão geral
// -- http://www.cs.huji.ac.il/~dbi/recitations/jdbc/JDBC-PSQL.pdf
// TODO: refatorar o uso de connection com try with resources
// TODO: estudar como funciona try with resources(como implementar um closeable?)
public class Database {

	private static final String QUERY_INSERT = "insert into pessoa values (?, ?, ?);";

	private static final String QUERY_SELECT_ALL = "select * from pessoa";

	private static final String QUERY_DELETE_ALL = "delete from pessoa";
	
	private static final String QUERY_COUNT = "select count(*) from pessoa";

	private static final String PATH_DB = "jdbc:sqlite:/home/abevieiramota/Documents/recursoshumanos.db";

	private Connection conn;

	public void reset() {

		openConnection();

		try (PreparedStatement stmt = 
				this.conn.prepareStatement(QUERY_DELETE_ALL)) {
			
			stmt.execute();
		} catch (SQLException e) {
		} 

		closeConnection();
	}

	public List<String[]> all() {

		openConnection();

		List<String[]> pessoas = null;

		try (Statement stmt = this.conn.createStatement();
				ResultSet rs = stmt.executeQuery(QUERY_SELECT_ALL)) {

			pessoas = new ArrayList<String[]>();

			while (rs.next()) {

				String[] row = new String[3];
				pessoas.add(row);

				row[0] = rs.getString(1);
				row[1] = rs.getString(2);
				row[2] = rs.getString(3);
			}

		} catch (SQLException e) {
		}
		
		closeConnection();

		return pessoas;
		
	}

	// TODO: usar batch
	public void salvar(List<String[]> data) {

		openConnection();

		try (PreparedStatement stmt = 
				this.conn.prepareStatement(QUERY_INSERT)) {
			
			for (String[] dataRow : data) {

				stmt.setString(1, dataRow[0]);
				stmt.setString(2, dataRow[1]);
				stmt.setString(3, dataRow[2]);

				stmt.execute();
			}
			
		} catch (SQLException e) {
		} 
		
		closeConnection();
		
	}

	public int quantidadeDeRegistros() {

		openConnection();
		
		int counter = 0;
		
		try (Statement stmt = this.conn.createStatement();
				ResultSet rs = stmt.executeQuery(QUERY_COUNT)) {
			
			counter = rs.getInt(1);
			
		} catch (SQLException e) {
		}
		
		closeConnection();

		return counter;

	} // end quantidadeDeRegistros method

	// --- private methods

	private void openConnection() {

		// TODO: pesquisar o que faz Class.forName e qual a necessidade para a
		// abertura de conexão jdbc
		try {

			this.conn = DriverManager.getConnection(PATH_DB);
		} catch (SQLException e) {
		}
	} // end openConnection method

	private void closeConnection() {

		if (this.conn != null) {

			try {

				this.conn.close();
			} catch (SQLException e) {
			}
		}
	}

} // end Database class