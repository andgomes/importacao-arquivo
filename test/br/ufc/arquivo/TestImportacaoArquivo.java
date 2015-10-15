package br.ufc.arquivo;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TestImportacaoArquivo {

	@Test
	public void testLerArquivoESalvarNoBanco() throws IOException, SQLException {
		
		List<CSVRecord> records = LeitorArquivo.lerRecords();
		
		Database db = new Database();
		
		db.salvar(records);
		
		int quantidadeDeRegistrosNoBanco = db.quantidadeDeRegistros();
		
		// records.size() - 1 porque o cabeçalho do arquivo não é salvo
		assertEquals(records.size() - 1, quantidadeDeRegistrosNoBanco);
		
	} // end testLerArquivoESalvarNoBanco method
	
} // end TestImportacaoArquivo class