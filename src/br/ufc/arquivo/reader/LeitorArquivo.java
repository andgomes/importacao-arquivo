package br.ufc.arquivo.reader;

import java.io.Closeable;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import br.ufc.arquivo.model.Pessoa;

//TODO: validar coluna CPF
public class LeitorArquivo implements Iterable<Pessoa>, Closeable {

	private static final char FILE_SEPARATOR = ';';
	private static final char FILE_QUOTE_CHAR = '"';
	private static final CSVFormat FILE_FORMAT = CSVFormat
			.newFormat(FILE_SEPARATOR).withHeader().withQuote(FILE_QUOTE_CHAR);

	private static final String COL_NOME = "NO_PESSOA";
	private static final String COL_CARGO = "NO_PROFISSAO";
	private static final String COL_DT_NASCIMENTO = "DT_NASCIMENTO";
	private static final String COL_CPF = "NU_CPF";

	private SimpleDateFormat dataNascimentoFormat = new SimpleDateFormat(
			"MM/dd/yy");

	private CSVParser parser;
	private Reader reader;

	public LeitorArquivo(String filePath) throws IOException {
		
		this.reader = new FileReader(filePath);
		this.parser = new CSVParser(reader, FILE_FORMAT);
		
	}

	@Override
	public Iterator<Pessoa> iterator() {

		return new LeitorArquivoIterator(this.parser.iterator());
	}

	private class LeitorArquivoIterator implements Iterator<Pessoa> {

		private Iterator<CSVRecord> csvRecords;

		public LeitorArquivoIterator(Iterator<CSVRecord> csvRecords) {

			this.csvRecords = csvRecords;
		}

		@Override
		public boolean hasNext() {

			return this.csvRecords.hasNext();
		}

		@Override
		public Pessoa next() {

			CSVRecord record = this.csvRecords.next();

			Pessoa pessoa = null;

			try {

				String nome = record.isSet(COL_NOME) ? record.get(COL_NOME)
						: null;
				String cargo = record.isSet(COL_CARGO) ? record.get(COL_CARGO)
						: null;
				Date dataNascimento = record.isSet(COL_DT_NASCIMENTO) ? dataNascimentoFormat
						.parse(record.get(COL_DT_NASCIMENTO)) : null;
				Long cpf = record.isSet(COL_CPF) ? Long.parseLong(record
						.get(COL_CPF)) : null;

				pessoa = new Pessoa(nome, cargo, dataNascimento, cpf);

			} catch (ParseException e) {
				e.printStackTrace();
			}

			return pessoa;
		}
	}

	@Override
	public void close() throws IOException {

		this.parser.close();
		this.reader.close();
	}

}