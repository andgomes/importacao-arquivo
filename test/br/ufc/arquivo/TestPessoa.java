package br.ufc.arquivo;

import java.text.ParseException;
import java.util.List;
import java.util.LinkedList;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.*;

public class TestPessoa {

	private static final String PATH_DB = "jdbc:hsqldb:mem:/"
			+ TestPessoa.class.getName();

	private static Connection conn;
	private static Database db;
	
	@BeforeClass
	public static void before() throws SQLException {
		
		conn = DriverManager.getConnection(PATH_DB);
		db = new Database(conn);
		db.criarTabela();
	}
	
	@AfterClass
	public static void after() throws SQLException {
		
		conn.close();
	} // end after method
	
	@Test
	public void testConverterIdade() throws SQLException, ParseException {

		List<String[]> rows = new LinkedList<>();

		rows.add(new String[] {"Joao 1", "23", "Analista"});
		rows.add(new String[] {"Joao 2", "21", "Programador"});

		db.salvar(rows);

		Converter converter = new Converter(db.all());
		
		Pessoa pessoa1 = new Pessoa();
		Pessoa pessoa2 = new Pessoa();
		
		pessoa1.setIdade(converter.getIdade(0));
		pessoa2.setIdade(converter.getIdade(1));
		
		assertEquals(new Integer(23), pessoa1.getIdade());
		assertEquals(new Integer(21), pessoa2.getIdade());

	} // end testConverterIdade method
	
	@Test(expected=ValueNotPresentException.class)
	public void testGetIdadeNaoSetada() throws SQLException, ParseException {
		
		List<String[]> rows = new LinkedList<>();
		
		rows.add(new String[] {"Joao", "23", "Analista"});
		
		db.salvar(rows);

		// qual a necessidade do código acima?
		Pessoa pessoa = new Pessoa();
		
		pessoa.getIdade();
	} // end testGetIdadeNaoSetada method

} // end TestConverter class