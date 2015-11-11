package br.ufc.arquivo.reader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

// XXX: iterator
// TODO: pessoas4 -> adicionada coluna CPF(fake - n achei fonte de 10k cpfs válidos)
// descobrir o padrão, realizar a leitura e validação
public class LeitorArquivo implements Iterator<String[]> {
	
	private static final char FILE_SEPARATOR = ';';
	private static final char FILE_QUOTE_CHAR = '"';
	private static final CSVFormat FILE_FORMAT = CSVFormat
			.newFormat(FILE_SEPARATOR).withHeader().withQuote(FILE_QUOTE_CHAR);

	private int nextElement = 0;
	private List<String[]> rows = new LinkedList<>();
	
	public LeitorArquivo(String filePath) throws IOException {
		
		try (FileReader reader = new FileReader(filePath);
				CSVParser parser = new CSVParser(reader, FILE_FORMAT)) {
			
			int nCols = parser.getHeaderMap().keySet().size();

			for (CSVRecord record : parser) {

				String[] recordAsStringArray = record.toMap().values()
						.toArray(new String[nCols]);

				rows.add(recordAsStringArray);
				
			}

		}
		
	}
	
//	public static List<String[]> lerRecords(String filepath)
//			throws FileNotFoundException, IOException {
//		
//		List<String[]> recordsAsString = new LinkedList<String[]>();
//
//		try (FileReader reader = new FileReader(filepath);
//				CSVParser parser = new CSVParser(reader, FILE_FORMAT)) {
//			
//			int nCols = parser.getHeaderMap().keySet().size();
//
//			for (CSVRecord record : parser) {
//
//				String[] recordAsStringArray = record.toMap().values()
//						.toArray(new String[nCols]);
//
//				recordsAsString.add(recordAsStringArray);
//			}
//
//		}
//
//		return recordsAsString;
//
//	}
	
	/*XXX: método para os testes anteriores que utilizavam a lista das linhas
	 	diretamente passarem mais fácil*/
	public List<String[]> getRows() {
		
		return rows;
		
	}
	
	public boolean hasNext() {

		return (rows.size() > 0);
		
	}
	
	public String[] next() {
		
		if (nextElement >= rows.size()) {
			throw new NoSuchElementException();
		}
		
		return rows.get(nextElement++);
		
	}
	
	public int size() {
		
		return rows.size();
		
	}
	
} // end LeitorArquivo class