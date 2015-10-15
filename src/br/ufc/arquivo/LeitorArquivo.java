package br.ufc.arquivo;

import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

public class LeitorArquivo {

	private static String path = "./pessoas.csv";

	public static List<CSVRecord> lerRecords() throws IOException {

		CSVFormat format = CSVFormat.newFormat(';');

		FileReader reader = new FileReader(path);

		CSVParser parser = new CSVParser(reader, format);
		
		List<CSVRecord> records = parser.getRecords();
		
		parser.close();
		
		return records;

	} // end lerRecords method

} // end LeitorArquivo class