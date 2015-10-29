package br.ufc.arquivo;

import static org.junit.Assert.*;
import org.junit.Test;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

public class TestLeitorArquivoDatabase {

	@Test
	public void lerArquivoESalvaNoBanco() throws IOException, SQLException {
		
		List<String[]> records = LeitorArquivo.lerRecords("./resources/pessoas_sub.csv");
		
		Database db = new Database();
		db.criarTabela();
		
		db.salvar(records);
		
		String[] firstRow = db.all().get(0);
		
		String[] firstRecord = {"Abelardo 0", "20", "Analista de TI"};
		
		String[] firstRowTruncated = 
				Arrays.copyOfRange(firstRow, 0, firstRecord.length);
		
		assertArrayEquals(firstRecord, firstRowTruncated);
		
	} // end lerArquivoESalvaNoBanco method
	
} // end TestLeitorArquivoDatabase class