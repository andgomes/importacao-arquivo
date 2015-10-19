package br.ufc.arquivo;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class TestDatabase {
	
	@Test
	public void seSalvarUmRegistroEntãoDeveEstarNoBanco() throws SQLException {
		
		// um registro
		String col0 = "oi";
		String col1 = "quem";
		String col2 = "fala?";
		
		String[] dataRow = new String[3];
		dataRow[0] = col0;
		dataRow[1] = col1;
		dataRow[2] = col2;
		
		ArrayList<String[]> data = new ArrayList<String[]>(1);
		data.add(dataRow);
		
		// db
		Database db = new Database();
		
		// limpa db
		db.reset();
		
		// exercise
		db.salvar(data);
		
		// verifica se foi adicionado corretamente
		List<String[]> allRows = db.all();
		
		assertNotNull(allRows);
		assertEquals(1, allRows.size());
		
		String[] row0 = allRows.get(0);
		
		assertEquals(col0, row0[0]);
		assertEquals(col1, row0[1]);
		assertEquals(col2, row0[2]);
		
	}
}
