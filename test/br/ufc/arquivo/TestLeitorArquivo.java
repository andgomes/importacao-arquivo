package br.ufc.arquivo;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.List;

import org.apache.commons.csv.CSVRecord;
import org.junit.BeforeClass;
import org.junit.Test;

public class TestLeitorArquivo {
	
	private static List<CSVRecord> recordsDoArquivo;
	
	private static final int QTD_RECORDS_ARQUIVO = 10000;
	
	private static final String[] primeiraLinha = {"Abelardo 0", "20", "Analista de TI"};
	private static final String[] ultimaLinha = {"Abelardo 9997", "97", "Analista de Marmotagem"};
	
	@BeforeClass
	public static void setUp() throws IOException {
		
		recordsDoArquivo = LeitorArquivo.lerRecords();
	}

	@Test
	public void testLerRecordsQtdOk() throws IOException {
		
		int qtdRecordsLidos = recordsDoArquivo.size();
		
		assertEquals(QTD_RECORDS_ARQUIVO, qtdRecordsLidos);
	}
	
	@Test
	public void testLerRecordsPrimeiroRecordOK() throws IOException {
		
		CSVRecord primeiroRecord = recordsDoArquivo.get(0);
		
		assertEquals(primeiraLinha[0], primeiroRecord.get(0));
		assertEquals(primeiraLinha[1], primeiroRecord.get(1));
		assertEquals(primeiraLinha[2], primeiroRecord.get(2));
	}
	
	@Test
	public void testLerRecordsUltimoRecordOK() throws IOException {
		
		CSVRecord ultimoRecord = recordsDoArquivo.get(recordsDoArquivo.size() - 1);
		
		assertEquals(ultimaLinha[0], ultimoRecord.get(0));
		assertEquals(ultimaLinha[1], ultimoRecord.get(1));
		assertEquals(ultimaLinha[2], ultimoRecord.get(2));
	}
	
}
