package com.example.demo.model;

import jakarta.persistence.Column;
// Import das anotações JPA usadas para mapear esta classe como entidade.
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.PositiveOrZero;

@Entity
@Table(name =  "almoxarifado_epi")
public class Epi {
	
	// Atributos mapeados para as colunas da tabela almoxarifado_epi.
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)// Avisa que o PostgreSQl gera automaticamente o ID.
	private int id;
	
	@Column(name =  "nome_epi")
	@NotBlank(message = "O nome do epi é obrigatório.")
	private String nomeEpi;
	
	@Column(name =  "ca")
	@Positive(message = "O CA deve ser um número positivo.")
	private int ca;
	
	@Column(name =  "estoque")
	@PositiveOrZero(message = "O estoque não pode ser negativo.")
	private int estoque;
	
	@Column(name =  "media_gasta")
	@PositiveOrZero(message = "A média gasta não pode ser negativa.")
	private int mediaGasta;
	
	@Column(name =  "estoque_min")
	@PositiveOrZero(message = "O estoque mínimo não pode ser negativo.")
	private int estoqueMin;

	// Construtor vazio, necessário para o Spring,
	// Atenção: ao adicionar um construtor com parâmetros, mantenha este construtor vazio — o JPA exige um construtor sem argumentos.
	public Epi() {
		
	}
	
	// Métodos de acesso (getters e setters)
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getNomeEpi() {
		return nomeEpi;
	}

	public void setNomeEpi(String nomeEpi) {
		this.nomeEpi = nomeEpi;
	}

	public int getCa() {
		return ca;
	}

	public void setCa(int ca) {
		this.ca = ca;
	}

	public int getEstoque() {
		return estoque;
	}

	public void setEstoque(int estoque) {
		this.estoque = estoque;
	}

	public int getMediaGasta() {
		return mediaGasta;
	}

	public void setMediaGasta(int mediaGasta) {
		this.mediaGasta = mediaGasta;
	}

	public int getEstoqueMin() {
		return estoqueMin;
	}

	public void setEstoqueMin(int estoqueMin) {
		this.estoqueMin = estoqueMin;
	}
	
	
	@Override
	public String toString() { //Retorna no console os dados em formato pre ordenado ao uso de print.
		return "Epi [ id=" + id + ", epi=" + nomeEpi + ", ca="+ ca + ", estoque=" + estoque + ", media=" + mediaGasta + ", EstoqueMin="
			+ estoqueMin + "]";
	}
	
}


