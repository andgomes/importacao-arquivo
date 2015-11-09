package br.ufc.arquivo.model;

import java.util.Date;

public class Pessoa {

	private String nome;
	private Integer idade;
	private String cargo;
	private Date nascimento;
	
	public Pessoa() {
		this(null, null, null, null);
	}
	
	public Pessoa(String nome) {
		this(nome, null, null, null);
	}
	
	public Pessoa(String nome, Integer idade) {
		this(nome, idade, null, null);
	}
	
	public Pessoa(String nome, Integer idade, String cargo) {
		this(nome, idade, cargo, null);
	}
	
	public Pessoa(String nome, Integer idade, String cargo, Date nascimento) {
		
		this.nome = nome;
		this.idade = idade;
		this.cargo = cargo;
		this.nascimento = nascimento;
		
	}
	
	public String getNome() {
		
		return nome;
		
	}
	
	public Integer getIdade() {
		
		return idade;
		
	}
	
}