package br.ufc.arquivo.database;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import br.ufc.arquivo.model.Pessoa;

// TODO: CPF será a chave da entidade
// TODO: implementar método de update, recebendo um iterable<Pessoa> e atualizando os dados das entidades
public class Database {

	private int chunkSize = 1000;

	private static final String QUERY_CREATE_TABLE = "create table pessoa (nome varchar(50), "
			+ "profissão varchar(50), data_nascimento date, cpf bigint)";
	private static final String QUERY_INSERT = "insert into pessoa values (?, ?, ?, ?);";
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
				String cargo = rs.getString(2);
				Date dataNascimento = rs.getDate(3);
				Long cpf = rs.getLong(4);
				if (rs.wasNull()) {
					cpf = null;
				}

				pessoas.add(new Pessoa(nome, cargo, dataNascimento, cpf));
			}
		}

		return pessoas;
	}

	public void save(Iterable<Pessoa> data) throws SQLException, ParseException {

		try (PreparedStatement pstmt = this.conn.prepareStatement(QUERY_INSERT)) {

			try {

				conn.setAutoCommit(false);

				int batchSize = 0;

				for (Pessoa pessoa : data) {

					pstmt.setString(1, pessoa.getNome());
					
					pstmt.setString(2, pessoa.getCargo());
					
					if (pessoa.getDataNascimento() == null) {
						
						pstmt.setNull(3, Types.DATE);
					} else {
						pstmt.setDate(3, new Date(pessoa.getDataNascimento()
								.getTime()));
					}
					
					if (pessoa.getCpf() == null) {
						
						pstmt.setNull(4, Types.BIGINT);
					} else {

						pstmt.setLong(4, pessoa.getCpf());
					}

					pstmt.addBatch();
					batchSize++;

					if (batchSize == this.chunkSize) {

						pstmt.executeBatch();
						batchSize = 0;
					}
				}

				/*
				 * necessário, pois o driver do hsqldb lança exceção caso
				 * seja chamado executeBatch sem nenhum addBatch antes
				 */
				if (batchSize > 0) {

					pstmt.executeBatch();
				}

				conn.commit();

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