package br.ufc.arquivo.reader;

import java.io.Closeable;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

//TODO: pessoas4 -> adicionada coluna CPF(fake - n achei fonte de 10k cpfs válidos)
//descobrir o padrão, realizar a leitura e validação
//TODO: ver http://stackoverflow.com/questions/839178/why-is-javas-iterator-not-an-iterable
public class LeitorArquivoV2 implements Iterable<String[]>, Closeable {

	private static final char FILE_SEPARATOR = ';';
	private static final char FILE_QUOTE_CHAR = '"';
	private static final CSVFormat FILE_FORMAT = CSVFormat
			.newFormat(FILE_SEPARATOR).withHeader().withQuote(FILE_QUOTE_CHAR);

	private CSVParser parser;
	private FileReader reader;
	private List<String[]> rows;

	public LeitorArquivoV2(String filePath) throws IOException {

		this.reader = new FileReader(filePath);
		this.parser = new CSVParser(reader, FILE_FORMAT);
	}

	public List<String[]> getRows() {

		if (this.rows == null) {

			this.rows = new ArrayList<String[]>();

			for (String[] row : this) {

				this.rows.add(row);
			}
		}

		return this.rows;
	}

	@Override
	public Iterator<String[]> iterator() {

		int nCols = this.parser.getHeaderMap().keySet().size();

		return new LeitorArquivoIterator(this.parser.iterator(), nCols);
	}

	private class LeitorArquivoIterator implements Iterator<String[]> {

		private Iterator<CSVRecord> csvRecords;
		private int nCols;

		public LeitorArquivoIterator(Iterator<CSVRecord> csvRecords, int nCols) {

			this.csvRecords = csvRecords;
		}

		@Override
		public boolean hasNext() {

			return this.csvRecords.hasNext();
		}

		@Override
		public String[] next() {
			CSVRecord record = this.csvRecords.next();

			return record.toMap().values().toArray(new String[this.nCols]);
		}
	}

	@Override
	public void close() throws IOException {

		this.parser.close();
		this.reader.close();
	}

} // end LeitorArquivo class