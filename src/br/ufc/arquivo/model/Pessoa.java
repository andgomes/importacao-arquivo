package br.ufc.arquivo.model;

import java.util.Date;

public class Pessoa {

	private String nome;
	private Integer idade;
	private String cargo;
	private Date dataNascimento;

	// XXX: prefiro deixar um construtor só, com o máximo de parâmetros, ficando
	// responsabilidade de quem for utilizar especificar quais serão null
	// e tu criou esses construtores sem sequer ter
	// necessidade!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
	// código só quando há a necessidade!!!!!!!!!
	// public Pessoa() {
	// this(null, null, null, null);
	// }
	// public Pessoa(String nome) {
	// this(nome, null, null, null);
	// }
	//
	// public Pessoa(String nome, Integer idade) {
	// this(nome, idade, null, null);
	// }
	//
	// public Pessoa(String nome, Integer idade, String cargo) {
	// this(nome, idade, cargo, null);
	// }

	public Pessoa(String nome, Integer idade, String cargo, Date nascimento) {

		this.setNome(nome);
		this.setIdade(idade);
		this.setCargo(cargo);
		this.setDataNascimento(nascimento);
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

	// hashCode & equals -- gerado pelo eclipse
	@Override
	public int hashCode() {

		final int prime = 31;
		int result = 1;
		result = prime * result + ((cargo == null) ? 0 : cargo.hashCode());
		result = prime * result
				+ ((dataNascimento == null) ? 0 : dataNascimento.hashCode());
		result = prime * result + ((idade == null) ? 0 : idade.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Pessoa other = (Pessoa) obj;
		if (cargo == null) {
			if (other.cargo != null)
				return false;
		} else if (!cargo.equals(other.cargo))
			return false;
		if (dataNascimento == null) {
			if (other.dataNascimento != null)
				return false;
		} else if (!dataNascimento.equals(other.dataNascimento))
			return false;
		if (idade == null) {
			if (other.idade != null)
				return false;
		} else if (!idade.equals(other.idade))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}

}