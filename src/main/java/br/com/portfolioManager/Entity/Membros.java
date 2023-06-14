package br.com.portfolioManager.Entity;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
@IdClass(MembrosId.class)
public class Membros implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idprojeto")
	private Projeto projeto;

	@Id
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idpessoa")
	private Pessoa pessoa;

	public Membros() {
		// TODO Auto-generated constructor stub
	}

	public Membros(Projeto projeto, Pessoa pessoa) {
		super();
		this.projeto = projeto;
		this.pessoa = pessoa;
	}

	public Projeto getProjeto() {
		return projeto;
	}

	public void setProjeto(Projeto projeto) {
		this.projeto = projeto;
	}

	public Pessoa getPessoa() {
		return pessoa;
	}

	public void setPessoa(Pessoa pessoa) {
		this.pessoa = pessoa;
	}

	@Override
	public int hashCode() {
		return Objects.hash(pessoa, projeto);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Membros other = (Membros) obj;
		return Objects.equals(pessoa, other.pessoa) && Objects.equals(projeto, other.projeto);
	}

	@Override
	public String toString() {
		return "Membros [projeto=" + projeto + ", pessoa=" + pessoa + "]";
	}
}
