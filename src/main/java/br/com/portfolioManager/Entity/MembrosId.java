package br.com.portfolioManager.Entity;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class MembrosId implements Serializable {
	private static final long serialVersionUID = 1L;

	private Long projeto;
	private Long pessoa;
}
