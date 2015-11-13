package br.ufc.arquivo.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import br.ufc.arquivo.model.Pessoa;

// TODO: CPF será a chave da entidade
// TODO: implementar método de update, recebendo um iterable<String[]> e atualizando os dados das entidades
public class Database {

	private int chunkSize = 1000;
	private static final int NUMBER_COLUMNS_DATABASE = 5;
	private static SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");

	private static final String QUERY_CREATE_TABLE = "create table pessoa (nome varchar(50), "
			+ "idade integer, profissão varchar(50), data_nascimento date, cpf bigint)";
	private static final String QUERY_INSERT = "insert into pessoa values (?, ?, ?, ?, ?);";
	private static final String QUERY_SELECT_ALL = "select * from pessoa";
	private static final String QUERY_DELETE_ALL = "delete from pessoa";
	private static final String QUERY_COUNT = "select count(*) from pessoa";

	private Connection conn;

	public Database(Connection conn) {

		this.conn = conn;
	}

	public void reset() throws SQLException {

		try (Statement stmt = this.conn.createStatement()) {

			stmt.execute(QUERY_DELETE_ALL);
		}
	}

	public void createTable() throws SQLException {

		try (Statement stmt = this.conn.createStatement()) {

			stmt.execute(QUERY_CREATE_TABLE);
		}
	}

	public List<Pessoa> all() throws SQLException {

		List<Pessoa> pessoas = new ArrayList<Pessoa>();

		try (Statement stmt = this.conn.createStatement();
				ResultSet rs = stmt.executeQuery(QUERY_SELECT_ALL)) {

			while (rs.next()) {

				String nome = rs.getString(1);
				Integer idade = rs.getInt(2);
				if (rs.wasNull()) {
					idade = null;
				}
				String cargo = rs.getString(3);
				Date dataNascimento = rs.getDate(4);
				Long cpf = rs.getLong(5);
				if(rs.wasNull()) {
					cpf = null;
				}

				pessoas.add(new Pessoa(nome, idade, cargo, dataNascimento, cpf));
			}
		}

		return pessoas;

	}

	public void save(Iterable<String[]> data) throws SQLException,
			ParseException {

		try (PreparedStatement pstmt = this.conn.prepareStatement(QUERY_INSERT)) {

			try {

				conn.setAutoCommit(false);

				int batchSize = 0;

				for (String[] dataRow : data) {

					String[] dataRowCompleted = Arrays.copyOf(dataRow,
							NUMBER_COLUMNS_DATABASE);

					// nome
					pstmt.setString(1, dataRowCompleted[0]);
					// idade
					if (dataRowCompleted[1] == null
							|| dataRowCompleted[1].isEmpty()) {
						pstmt.setNull(2, Types.INTEGER);
					} else {
						pstmt.setInt(2, Integer.parseInt(dataRowCompleted[1]));
					}
					// cargo
					pstmt.setString(3, dataRowCompleted[2]);
					// data de nascimento
					if (dataRowCompleted[3] == null) {
						pstmt.setNull(4, Types.DATE);
					} else {
						pstmt.setDate(4, new Date(sdf
								.parse(dataRowCompleted[3]).getTime()));
					}

					if (dataRowCompleted[4] == null) {
						pstmt.setNull(5, Types.BIGINT);
					} else {
						pstmt.setLong(5, Long.parseLong(dataRowCompleted[4]));
					}

					pstmt.addBatch();
					batchSize++;

					if (batchSize == this.chunkSize) {

						pstmt.executeBatch();
						batchSize = 0;
					}
				}

				if (batchSize > 0) { // necessário, pois o driver do hsqldb
										// lança exceção caso seja chamado
					// executeBatch sem nenhum addBatch antes

					pstmt.executeBatch();
				}

				conn.commit();

				conn.setAutoCommit(true);
			} catch (SQLException e) {

				conn.rollback();
				throw e;
			}

		}

	}

	public int size() throws SQLException {

		int size;

		try (Statement stmt = this.conn.createStatement();
				ResultSet rs = stmt.executeQuery(QUERY_COUNT)) {

			rs.next();
			size = rs.getInt(1);
		}

		return size;
	}

	public void setChunkSize(int size) {

		this.chunkSize = size;
	}

}