package br.ufc.arquivo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

public class Database {

	private int chunkSize = 1000;
	
//	private static final String QUERY_CREATE_TABLE = "create table pessoa (nome varchar(50), " + 
//			"idade varchar(50), profissão varchar(50))";
	
	private static final String QUERY_CREATE_TABLE = "create table pessoa (nome varchar(50), " + 
			"idade varchar(50), profissão varchar(50), data_nascimento varchar(10))";

//	private static final String QUERY_INSERT = "insert into pessoa values (?, ?, ?);";
	
	private static final int NUMBER_COLUMNS_DATABASE = 4;
	
	private static final String QUERY_INSERT = "insert into pessoa values (?, ?, ?, ?);";

	private static final String QUERY_SELECT_ALL = "select * from pessoa";

	private static final String QUERY_DELETE_ALL = "delete from pessoa";

	private static final String QUERY_COUNT = "select count(*) from pessoa";

	private static final String PATH_DB = "jdbc:hsqldb:mem:/recursoshumanos";

	public void reset() throws SQLException {

		try (Connection conn = DriverManager.getConnection(PATH_DB);
				Statement stmt = conn.createStatement()) {

			stmt.execute(QUERY_DELETE_ALL);
		}
	}

	public void criarTabela() throws SQLException {

		try (Connection conn = DriverManager.getConnection(PATH_DB);
				Statement stmt = conn.createStatement()) {

			stmt.execute(QUERY_CREATE_TABLE);
		}
	}

	public List<String[]> all() throws SQLException {

		List<String[]> pessoas = null;

		try (Connection conn = DriverManager.getConnection(PATH_DB);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(QUERY_SELECT_ALL)) {

			pessoas = new ArrayList<String[]>();

			while (rs.next()) {

				String[] row = new String[NUMBER_COLUMNS_DATABASE];
				pessoas.add(row);

				for (int i = 1; i <= row.length; i++) {
					row[i - 1] = rs.getString(i);
				}
				
			}

		}

		return pessoas;

	}
	
	public void salvar(List<String[]> data) throws SQLException {

		try (Connection conn = DriverManager.getConnection(PATH_DB);
				PreparedStatement stmt = conn.prepareStatement(QUERY_INSERT)) {
			
			try {

				conn.setAutoCommit(false);
				
				int batchSize = 0;
				
				for(String[] dataRow: data) {
					
					int colsPreenchidas = 0;
					
					for ( ; colsPreenchidas < dataRow.length; colsPreenchidas++) {
						stmt.setString(colsPreenchidas + 1, dataRow[colsPreenchidas]);
					}
					
					while (++colsPreenchidas <= NUMBER_COLUMNS_DATABASE) {
						
						stmt.setNull(colsPreenchidas, Types.VARCHAR);
						++colsPreenchidas;
						
					}
					
					stmt.addBatch();
					batchSize++;
					
					if(batchSize == this.chunkSize) {
						
						stmt.executeBatch();
						batchSize = 0;
					}
				}
				
				if(batchSize > 0) { // necessário, pois o driver do hsqldb lança exceção caso seja chamado
					// executeBatch sem nenhum addBatch antes
					
					stmt.executeBatch();
				}
				
				conn.commit();

			} catch (SQLException e) {
				
				conn.rollback();
				throw e;
			}

		}
	}
	
	public Integer quantidadeDeRegistros() throws SQLException {

		Integer counter = null;

		try (Connection conn = DriverManager.getConnection(PATH_DB);
				Statement stmt = conn.createStatement();
				ResultSet rs = stmt.executeQuery(QUERY_COUNT)) {

			rs.next();
			counter = rs.getInt(1);
		}

		return counter;
	} // end quantidadeDeRegistros method

	public void setChunkSize(int size) {
		
		this.chunkSize = size;
	} // end setChunckSize method

} // end Database class