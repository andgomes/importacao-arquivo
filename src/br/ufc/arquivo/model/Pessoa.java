package br.ufc.arquivo.model;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Date;
import java.util.Objects;

public class Pessoa {

	private String nome;
	private String cargo;
	private Date dataNascimento;
	private Long cpf;

	public Pessoa(String nome, String cargo, Date nascimento, Long cpf) {

		this.setNome(nome);
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
	
	public int getIdade() {
		
		Instant instant = Instant.ofEpochMilli(dataNascimento.getTime());
		
		LocalDate localDate = LocalDateTime.ofInstant(instant, ZoneId.systemDefault()).toLocalDate();
		
		Period period = Period.between(localDate, LocalDate.now());
		
		return period.getYears();
		
	}
	
	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Long getCpf() {
		return cpf;
	}

	public void setCpf(Long cpf) {
		this.cpf = cpf;
	}

	@Override
	public int hashCode() {

		return Objects.hash(cargo, cpf, dataNascimento, nome);
	}

	@Override
	public boolean equals(Object obj) {

		Boolean equals = null;

		if (obj instanceof Pessoa) {

			Pessoa other = (Pessoa) obj;

			Object[] thisObj = { cargo, cpf, dataNascimento, nome };
			Object[] otherObj = { other.cargo, other.cpf, other.dataNascimento,
					other.nome };

			equals = Arrays.equals(thisObj, otherObj);
		} else {

			equals = false;
		}

		return equals;
	}
	
	@Override
	public String toString() {
		
		return String.format("%s, %s, %s, %s",
				getNome(), getCargo(), getDataNascimento(), 
				getCpf());
		
	}

}