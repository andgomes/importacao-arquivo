package br.ufc.arquivo.model;

import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class Pessoa {

	private String nome;
	private Integer idade;
	private String cargo;
	private Date dataNascimento;
	private Long cpf;

	public Pessoa(String nome, Integer idade, String cargo, Date nascimento, Long cpf) {

		this.setNome(nome);
		this.setIdade(idade);
		this.setCargo(cargo);
		this.setDataNascimento(nascimento);
		this.setCpf(cpf);
	}

	public String getCargo() {
		return cargo;
	}

	public void setCargo(String cargo) {
		this.cargo = cargo;
	}

	public Date getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Date dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Integer getIdade() {
		return idade;
	}

	public void setIdade(Integer idade) {
		this.idade = idade;
	}


	public Long getCpf() {
		return cpf;
	}

	public void setCpf(Long cpf) {
		this.cpf = cpf;
	}

	@Override
	public int hashCode() {
		
		return Objects.hash(cargo, cpf, 
				dataNascimento, idade, nome);
		
	}

	@Override
	public boolean equals(Object obj) {
		
		if (obj instanceof Pessoa) {
		
			Pessoa p = (Pessoa) obj;
			
			Object[] thisOb = {cargo, cpf, dataNascimento, idade, nome};
			Object[] comparedOb = {p.cargo, p.cpf, p.dataNascimento, 
					p.idade, p.nome};
			
			return Arrays.equals(thisOb, comparedOb);
		
		} 
		
		return false;

	}

}