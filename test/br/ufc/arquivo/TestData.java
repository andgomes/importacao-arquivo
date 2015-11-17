package br.ufc.arquivo;

import static org.junit.Assert.assertEquals;

import java.time.LocalDate;
import java.time.Month;
import java.time.Period;

import org.junit.Test;

// TODO: pesquisar sobre ISO-8601
// TODO: ler http://uaihebert.com/seja-um-desenvolvedor-nao-um-liquidificador/ -> são várias páginas, index no fim de cada post
public class TestData {
	
	@Test
	public void seDiferencaEntreDuasDatasEntaoCalculaQtdAnosCorretamente() {
		
		LocalDate dtNascimento = LocalDate.of(1989, Month.NOVEMBER, 29);
		LocalDate hoje = LocalDate.of(2015, Month.NOVEMBER, 17);
		
		Period periodo = Period.between(dtNascimento, hoje);
		
		assertEquals(25, periodo.getYears());
	}
	
	// XXX: no dia eu já incremento a idade? :o
	@Test
	public void seDiferencaNoDiaDoAniversarioEntaoCalculaQtdAnosCorretamente() {
		
		LocalDate dtNascimento = LocalDate.of(1989, Month.NOVEMBER, 29);
		LocalDate hoje = LocalDate.of(2015, Month.NOVEMBER, 29);
		
		Period periodo = Period.between(dtNascimento, hoje);
		
		assertEquals(26, periodo.getYears());
	}

}
