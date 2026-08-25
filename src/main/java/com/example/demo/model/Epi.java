package com.example.demo.model;

import jakarta.persistence.Column;
//Importe da biblioteca para permetir o arquivo entedera liguagem do Sprig
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
	
	//Variaveis que vão amazenar os valores temporariamente.
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)// Avisa que o PostgreSQl gera automaticamente o ID.
	private int id;
	
	@Column(name =  "nome_epi")
	@NotBlank(message = "O nome do epi é obrigatório.")
	private String nomeEpi;
	
	@Column(name =  "ca")
	@Positive(message = "O CA deve ser um numero prositivo.")
	private int ca;
	
	@Column(name =  "estoque")
	@PositiveOrZero(message = "O estoque não pode ser negativo.")
	private int estoque;
	
	@Column(name =  "media_gasta")
	@PositiveOrZero(message = "A media gasta não pode ser negativo.")
	private int mediaGasta;
	
	@Column(name =  "estoque_min")
	@PositiveOrZero(message = "O estoque mínimo não pode ser negativo.")
	private int estoqueMin;

	//Contrutor vazio, necessario para o SPRING, 
	// --- Alerta!!!  se criar um quiser dados serem ativos deve criar um segundo contrutor!!!!! ---
	public Epi() {
		
	}
	
	// Metados de acesso
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


