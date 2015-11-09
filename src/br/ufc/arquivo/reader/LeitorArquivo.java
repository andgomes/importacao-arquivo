package br.ufc.arquivo.reader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

// XXX: iterator
public class LeitorArquivo {
	
	private static final char FILE_SEPARATOR = ';';
	private static final char FILE_QUOTE_CHAR = '"';
	private static final CSVFormat FILE_FORMAT = CSVFormat
			.newFormat(FILE_SEPARATOR).withHeader().withQuote(FILE_QUOTE_CHAR);

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
	
} // end LeitorArquivo class