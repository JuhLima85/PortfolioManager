package br.com.portfolioManager.Entity;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
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
}
