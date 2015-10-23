package br.ufc.arquivo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

// TODO: estudar como funciona try with resources(como implementar um closeable?)
public class Database {

	private static final String QUERY_INSERT = "insert into pessoa values (?, ?, ?);";

	private static final String QUERY_SELECT_ALL = "select * from pessoa";

	private static final String QUERY_DELETE_ALL = "delete from pessoa";
	
	private static final String QUERY_COUNT = "select count(*) from pessoa";

	private static final String PATH_DB = 
			"jdbc:sqlite:/home/abevieiramota/Documents/recursoshumanos.db";

	public void reset() {


		try (Connection conn = DriverManager.getConnection(PATH_DB);
			 PreparedStatement stmt = 
					 conn.prepareStatement(QUERY_DELETE_ALL)) {
			
			stmt.execute();
		} catch (SQLException e) {
			e.printStackTrace();
		} 

	}

	public List<String[]> all() {

		List<String[]> pessoas = null;

		try (Connection conn = DriverManager.getConnection(PATH_DB);
			 Statement stmt = conn.createStatement();
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
			e.printStackTrace();
		}

		return pessoas;
		
	}

	// TODO: usar batch
	public void salvar(List<String[]> data) {

		try (Connection conn = DriverManager.getConnection(PATH_DB);
			 PreparedStatement stmt = 
					 conn.prepareStatement(QUERY_INSERT)) {

			int i = 0;
			
			for (String[] dataRow : data) {

				System.out.println("Salvando registro: " + i);
				
				stmt.setString(1, dataRow[0]);
				stmt.setString(2, dataRow[1]);
				stmt.setString(3, dataRow[2]);

				stmt.addBatch();
				
				++i;
			}
			
			stmt.executeBatch();
			
		} catch (SQLException e) {
			e.printStackTrace();
		} 
		
	}

	public int quantidadeDeRegistros() {

		int counter = 0;
		
		try (Connection conn = DriverManager.getConnection(PATH_DB);
			 Statement stmt = conn.createStatement();
			 ResultSet rs = stmt.executeQuery(QUERY_COUNT)) {
			
			counter = rs.getInt(1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return counter;

	} // end quantidadeDeRegistros method

} // end Database class