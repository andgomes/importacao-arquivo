package br.ufc.arquivo;

import java.util.List;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import org.apache.commons.csv.CSVRecord;

public class Database {

	private static final String PATH_DB = 
			"jdbc:sqlite:/home/abevieiramota/Documents/recursoshumanos.db";

	private Connection connection;
	private Statement statement;
	
	public void salvar(List<CSVRecord> records) {

		try {

			Class.forName("org.sqlite.JDBC");

			connection = DriverManager
					.getConnection(PATH_DB);

			statement = connection.createStatement();

			statement.executeUpdate("drop table if exists pessoa;");

			statement
					.executeUpdate("create table pessoa (nome, idade, profissao);");

			PreparedStatement prep = connection
					.prepareStatement("insert into pessoa values (?, ?, ?);");

			// for inicia de 1 para ignorar a linha do cabeçalho
			for (int i = 1; i < records.size(); i++) {

				prep.setString(1, records.get(i).get(0));
				prep.setString(2, records.get(i).get(1));
				prep.setString(3, records.get(i).get(2));
				
				prep.addBatch();

			}

			connection.setAutoCommit(false);
			prep.executeBatch();
			connection.setAutoCommit(true);

			connection.close();

		} catch (Exception e) {
			e.printStackTrace();
		}

	} // end salvar method

	public int quantidadeDeRegistros() throws SQLException {

		connection = DriverManager
				.getConnection(PATH_DB);
		
		statement = connection.createStatement();
		
		ResultSet rs = statement.executeQuery("select * from pessoa;");
		
		int count = 0;
		
		while (rs.next()) {
			++count;
		}
		
		rs.close();
		connection.close();
		
		return count;

	} // end quantidadeDeRegistros method

} // end Database class