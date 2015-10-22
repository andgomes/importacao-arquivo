package br.ufc.arquivo;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

public class LeitorArquivo {

	private static final char FILE_SEPARATOR = ';';
	private static final char FILE_QUOTE_CHAR = '"';
	private static final CSVFormat FILE_FORMAT = CSVFormat
			.newFormat(FILE_SEPARATOR).withHeader().withQuote(FILE_QUOTE_CHAR);

	public static List<CSVRecord> lerRecords(String filepath) {

		List<CSVRecord> records = null;

		try (FileReader reader = new FileReader(filepath);
				CSVParser parser = new CSVParser(reader, FILE_FORMAT)) {

			try {
				
				records = parser.getRecords();
			} catch (IOException e) {
			}

		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}

		return records;
		
	} // end lerRecords method

} // end LeitorArquivo class