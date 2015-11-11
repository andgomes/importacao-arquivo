package br.ufc.arquivo.reader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

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

	private int next = 0;
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
	
	public static List<String[]> lerRecords(String filepath)
			throws FileNotFoundException, IOException {
		
		List<String[]> recordsAsString = new LinkedList<String[]>();

		try (FileReader reader = new FileReader(filepath);
				CSVParser parser = new CSVParser(reader, FILE_FORMAT)) {
			
			int nCols = parser.getHeaderMap().keySet().size();

			for (CSVRecord record : parser) {

				String[] recordAsStringArray = record.toMap().values()
						.toArray(new String[nCols]);

				recordsAsString.add(recordAsStringArray);
			}

		}

		return recordsAsString;

	} // end lerRecords method
	
	public boolean hasNext() {

		return (rows.size() > 0);
		
	}
	
	public String[] next() {
		
		return rows.get(next++);
		
	}
	
} // end LeitorArquivo class