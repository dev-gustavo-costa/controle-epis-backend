package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "item_lista_compra")
public class ItemListaCompra {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@ManyToOne
	@JoinColumn(name = "epi_id")
	private Epi epi;
	
	@ManyToOne
	@JoinColumn(name = "lista_compra_id")
	@JsonIgnore
	private ListaCompra listaCompra;
	
	@Column(name = "quantidade_orcada")
	private int quantidadeOrcada;
	
	@Column(name = "quantidade_recebida")
	private Integer quantidadeRecebida;
	
	public ItemListaCompra() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Epi getEpi() {
		return epi;
	}

	public void setEpi(Epi epi) {
		this.epi = epi;
	}

	public ListaCompra getListaCompra() {
		return listaCompra;
	}

	public void setListaCompra(ListaCompra listaCompra) {
		this.listaCompra = listaCompra;
	}

	public int getQuantidadeOrcada() {
		return quantidadeOrcada;
	}

	public void setQuantidadeOrcada(int quantidadeOrcada) {
		this.quantidadeOrcada = quantidadeOrcada;
	}

	public Integer getQuantidadeRecebida() {
		return quantidadeRecebida;
	}

	public void setQuantidadeRecebida(Integer quantidadeRecebida) {
		this.quantidadeRecebida = quantidadeRecebida;
	}
	
	
}
