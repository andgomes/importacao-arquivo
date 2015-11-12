package br.ufc.arquivo.reader;

import java.io.Closeable;
import java.io.FileReader;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

@Deprecated
public class LeitorArquivo implements Iterable<String[]>, Closeable {

	private static final char FILE_SEPARATOR = ';';
	private static final char FILE_QUOTE_CHAR = '"';
	private static final CSVFormat FILE_FORMAT = CSVFormat
			.newFormat(FILE_SEPARATOR).withHeader().withQuote(FILE_QUOTE_CHAR);

	private List<String[]> rows = new LinkedList<>();
	
	public LeitorArquivo(String filePath) throws IOException {

		try (FileReader reader = new FileReader(filePath);
				CSVParser parser = new CSVParser(reader, FILE_FORMAT)) {

			int nCols = parser.getHeaderMap().keySet().size();

			// XXX: isso não é iterator - está percorrendo todos os registros do
			// leitor, carregando num array e retornando o iterator do array
			for (CSVRecord record : parser) {

				String[] recordAsStringArray = record.toMap().values()
						.toArray(new String[nCols]);

				rows.add(recordAsStringArray);
			}
		}
	}

	/*
	 * XXX: método para os testes anteriores que utilizavam a lista das linhas
	 * diretamente passarem mais fácil
	 */
	public List<String[]> getRows() {

		return rows;
	}

	@Override
	public Iterator<String[]> iterator() {

		return this.rows.iterator();
	}
	
	private class LeitorArquivoIterator implements Iterator<String[]> {

		@Override
		public boolean hasNext() {
			return false;
		}

		@Override
		public String[] next() {
			return null;
		}
	}

	@Override
	public void close() throws IOException {
		
	}

} // end LeitorArquivo class