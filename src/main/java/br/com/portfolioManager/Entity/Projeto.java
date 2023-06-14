package br.com.portfolioManager.Entity;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Projeto implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	private String nome;
	private Date dataInicio;
	private Date dataPrevisaoFim;
	private Date dataFim;
	private String descricao;
	private String status;
	private Double orcamento;
	private String risco;
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "idgerente")
	private Pessoa gerenteResponsavel;

}
