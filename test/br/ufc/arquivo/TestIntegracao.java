package br.ufc.arquivo;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.junit.Test;

public class TestIntegracao {
	
	private static final String PATH_DB = "jdbc:hsqldb:mem:/" + TestIntegracao.class.getName();

	@Test
	public void lerArquivoESalvaNoBanco() throws IOException, SQLException {
		
		List<String[]> records = LeitorArquivo.lerRecords("./resources/pessoas_sub.csv");
		
		Database db = new Database(PATH_DB);
		db.criarTabela();
		
		db.salvar(records);
		
		Object[] firstRow = db.all().get(0);
		
		String[] firstRecord = records.get(0);
		
		assertEquals(Integer.parseInt(firstRecord[1]), (int) firstRow[1]);
	} // end lerArquivoESalvaNoBanco method
	
} // end TestLeitorArquivoDatabase class