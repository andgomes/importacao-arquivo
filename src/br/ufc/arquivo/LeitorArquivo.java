package br.ufc.arquivo;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

public class LeitorArquivo {

	private static final char FILE_SEPARATOR = ';';
	private static final char FILE_QUOTE_CHAR = '"';
	private static final CSVFormat FILE_FORMAT = CSVFormat
			.newFormat(FILE_SEPARATOR).withHeader().withQuote(FILE_QUOTE_CHAR);

	public static List<String[]> lerRecords(String filepath) {

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
		
		/* Transforma List<CSVRecord> em List<String[]> */

		List<String[]> recordsAsString = 
				new LinkedList<String[]>();
				
		for (CSVRecord record : records) {
			
			Iterator<String> iteratorCSV = record.iterator();
			
			String[] recordAsStringArray = new String[record.size()];
			
			int i = 0;
			
			while (iteratorCSV.hasNext()) {
				
				recordAsStringArray[i] = iteratorCSV.next();
				++i;
				
			}
			
			recordsAsString.add(recordAsStringArray);
			
		}
		
		return recordsAsString;
		
	} // end lerRecords method

} // end LeitorArquivo class