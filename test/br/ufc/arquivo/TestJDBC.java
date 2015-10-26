package br.ufc.arquivo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashSet;
import java.util.Set;

import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestJDBC {

	private static final String PATH_DB = "jdbc:hsqldb:mem:/teste";

	@BeforeClass
	// cria db
	public static void beforeClass() throws SQLException {

		try (Connection conn = DriverManager.getConnection(PATH_DB);
				Statement stmt = conn.createStatement()) {

			stmt.execute("create table pessoa (nome varchar(50), idade integer)");

		}
	}

	@After
	// limpa tabela pessoa
	public void tearDown() throws SQLException {

		try (Connection conn = DriverManager.getConnection(PATH_DB);
				Statement stmt = conn.createStatement()) {
			
			stmt.execute("delete from pessoa");
		}

	}

	// PreparedStatement mantém os parâmetros entre uma chamada e outra
	// para limpar, chamar pstmt.clearParameters()
	@Test
	public void sePreparedStatementForReutilizadoEntaoParametrosSaoMantidos() throws SQLException {

		int idade = 25;

		try (Connection conn = DriverManager.getConnection(PATH_DB);
				PreparedStatement pstmt = conn
						.prepareStatement("insert into pessoa values (?, ?)");
				Statement stmt = conn.createStatement()) {

			pstmt.setString(1, "Abelardo");
			pstmt.setInt(2, idade);

			// executando com (Abelardo, 25)
			pstmt.execute();

			pstmt.setString(1, "João");

			// executando com (João, ????)
			pstmt.execute();

			// consultando idades
			ResultSet rs = stmt.executeQuery("select idade from pessoa");

			// verificando valores
			Set<Integer> idadesNoDB = new HashSet<Integer>();

			while (rs.next()) {

				idadesNoDB.add(rs.getInt(1));
			}

			assertEquals(1, idadesNoDB.size());

			assertEquals(idade, idadesNoDB.iterator().next().intValue());

		}

	}

	// Deve lançar exceção informando que falta setar um parâmetro
	@Test
	public void sePreparedStatementForReutilizadoComClearParametersEntaoParametrosNaoSaoMantidos() {

		int idade = 25;

		try (Connection conn = DriverManager.getConnection(PATH_DB);
				PreparedStatement pstmt = conn
						.prepareStatement("insert into pessoa values (?, ?)");
				Statement stmt = conn.createStatement()) {

			pstmt.setString(1, "Abelardo");
			pstmt.setInt(2, idade);

			// executando com (Abelardo, 25)
			pstmt.execute();
			pstmt.clearParameters();

			pstmt.setString(1, "João");

			// executando com (João, ????)
			pstmt.execute();

			// deve lançar exceção no execute
			fail();

		} catch (SQLException e) {

			assertEquals("Parameter not set", e.getMessage());
		}

	}

}
