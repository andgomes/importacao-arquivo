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
public class Database {

	private static final String QUERY_INSERT = "insert into pessoa values (?, ?, ?);";

	private static final String QUERY_SELECT_ALL = "select * from pessoa";

	private static final String QUERY_DELETE_ALL = "delete from pessoa";

	// TODO: seria melhor usar string template, no lugar de concatenar strings
	private static final String MSG_ERRO_EXECUCAO_SQL = "Erro na execução do SQL - ";
	
	private static final String MSG_ERRO_CLOSE_STATEMENT = "Erro ao tentar fechar o statement";
	
	private static final String MSG_ERRO_CLOSE_RESULT_SET = "Erro ao tentar fechar o resultset";

	private static final String PATH_DB = "jdbc:sqlite:/home/abevieiramota/Documents/recursoshumanos.db";

	private Connection conn;

	public void reset() {

		openConnection();

		PreparedStatement stmt = null;

		try {

			stmt = this.conn.prepareStatement(QUERY_DELETE_ALL);
			stmt.execute();
		} catch (SQLException e) {

			System.out.println(MSG_ERRO_EXECUCAO_SQL + QUERY_DELETE_ALL);
			e.printStackTrace();
		} finally {

			if (stmt != null) {

				try {

					stmt.close();
				} catch (SQLException e) {

					System.out.println(MSG_ERRO_CLOSE_STATEMENT);
					e.printStackTrace();
				}
			}

			closeConnection();
		}

	}

	public List<String[]> all() {

		openConnection();

		List<String[]> pessoas = null;
		ResultSet rs = null;
		Statement stmt = null;

		try {

			stmt = this.conn.createStatement();
			rs = stmt.executeQuery(QUERY_SELECT_ALL);

			pessoas = new ArrayList<String[]>();

			while (rs.next()) {

				String[] row = new String[3];
				pessoas.add(row);

				row[0] = rs.getString(1);
				row[1] = rs.getString(2);
				row[2] = rs.getString(3);
			}

		} catch (SQLException e) {

			System.out.println(MSG_ERRO_EXECUCAO_SQL + QUERY_SELECT_ALL);
			e.printStackTrace();
		} finally {

			if (rs != null) {

				try {

					rs.close();
				} catch (SQLException e) {

					System.out.println(MSG_ERRO_CLOSE_RESULT_SET);
					e.printStackTrace();
				}
			}

			if (stmt != null) {

				try {

					stmt.close();
				} catch (SQLException e) {

					System.out.println(MSG_ERRO_CLOSE_STATEMENT);
					e.printStackTrace();
				}
			}

			closeConnection();
		}

		return pessoas;
	}

	public void salvar(List<String[]> data) {

		openConnection();

		PreparedStatement stmt = null;

		try {
			stmt = this.conn.prepareStatement(QUERY_INSERT);

			for (String[] dataRow : data) {

				stmt.setString(1, dataRow[0]);
				stmt.setString(2, dataRow[1]);
				stmt.setString(3, dataRow[2]);

				stmt.execute();
			}
		} catch (SQLException e) {

			System.out.println(MSG_ERRO_EXECUCAO_SQL + QUERY_INSERT);
			e.printStackTrace();
		} finally {

			if (stmt != null) {

				try {

					stmt.close();
				} catch (SQLException e) {

					System.out.println(MSG_ERRO_CLOSE_STATEMENT);
					e.printStackTrace();
				}
			}

			closeConnection();
		}

	}

	// TODO: sério? select count(*) from pessoa né!!!!!!
	// refatorar seguindo o padrão dos outros métodos e mudando a consulta para
	// usar o count
	public int quantidadeDeRegistros() throws SQLException {

		openConnection();

		Statement statement = this.conn.createStatement();

		ResultSet rs = statement.executeQuery("select * from pessoa;");

		int count = 0;

		while (rs.next()) {
			++count;
		}

		rs.close();
		this.conn.close();

		return count;

	} // end quantidadeDeRegistros method

	// --- private methods

	private void openConnection() {

		// TODO: pesquisar o que faz Class.forName e qual a necessidade para a
		// abertura de conexão jdbc
		try {

			this.conn = DriverManager.getConnection(PATH_DB);
		} catch (SQLException e) {

			System.out.println("Não foi possível abrir conexão");
			e.printStackTrace();
		}
	} // end openConnection method

	private void closeConnection() {

		if (this.conn != null) {

			try {

				this.conn.close();
			} catch (SQLException e) {

				System.out.println("Não foi possível fechar a conexão");
				e.printStackTrace();
			}
		}
	}

} // end Database class