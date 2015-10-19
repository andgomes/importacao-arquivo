package br.ufc.arquivo;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import org.apache.commons.csv.CSVRecord;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;

public class LeitorArquivo {

	private static final String MSG_ERRO_CLOSE_PARSER = "Erro ao tentar fechar o parser";
	private static final String MSG_ERRO_CLOSE_READER = "Erro ao tentar fechar o reader";
	private static final String MSG_ERRO_LEITURA_ARQUIVO = "Erro na leitura do arquivo";
	private static final String MSG_ERRO_HEADER_ARQUIVO = "Erro na leitura do cabeçalho do arquivo";
	private static final String MSG_ERRO_ARQUIVO_NAO_ENCONTRADO = "Arquivo não encontrado";
	private static final char FILE_SEPARATOR = ';';
	private static final char FILE_QUOTE_CHAR = '"';
	private static final String FILE_PATH = "./pessoas_10000registros.csv";
	private static final CSVFormat FILE_FORMAT = CSVFormat.newFormat(FILE_SEPARATOR).withHeader()
			// !!!!! é preciso chamar withHeader, mas passando nada
			// assim ele considera que a primeira row é o header! - deixei as linhas
			// abaixo para ver como estava antes

					// .withHeader(FILE_HEADER)
					// TODO: estranho o withHeader já não ignorar a primeira linha
					// .withSkipHeaderRecord(true)

					.withQuote(FILE_QUOTE_CHAR);

	// TODO: estruturar o código de forma a tratar corretamente as exceções
	public static List<CSVRecord> lerRecords() {

		FileReader reader = null;
		CSVParser parser = null;
		List<CSVRecord> records = null;
		
		try {
		
			reader = new FileReader(FILE_PATH);
		} catch (FileNotFoundException e) {
		
			System.out.println(MSG_ERRO_ARQUIVO_NAO_ENCONTRADO);
			e.printStackTrace();
		}

		
		try {
		
			parser = new CSVParser(reader, FILE_FORMAT);
		} catch (IOException e) {

			System.out.println(MSG_ERRO_HEADER_ARQUIVO);
			e.printStackTrace();
			// se entrar aqui, o reader que foi aberto continuará aberto!
		}

		try {
			
			records = parser.getRecords();
		} catch (IOException e) {
			
			System.out.println(MSG_ERRO_LEITURA_ARQUIVO);
			e.printStackTrace();
			// se entrar aqui, reader e parser continuarão abertos!
		}
		
		try {
		
			reader.close();
		} catch (IOException e) {
			
			System.out.println(MSG_ERRO_CLOSE_READER);
			e.printStackTrace();
			// se entrar aqui, parser continuará aberto!
		}

		
		try {
		
			parser.close();
		} catch (IOException e) {

			System.out.println(MSG_ERRO_CLOSE_PARSER);
			e.printStackTrace();
		}

		return records;
	} // end lerRecords method

} // end LeitorArquivo class