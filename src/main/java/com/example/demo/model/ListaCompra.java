package com.example.demo.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "lista_compra")
public class ListaCompra {
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	
	@Column(name = "data_criacao")
	private LocalDate dataCriacao;
	
	@Column(name = "data_expiracao")
	private LocalDate dataExpiracao;
	
	@Enumerated(EnumType.STRING)
	private StatusListaCompra status;
	
	@OneToMany(mappedBy = "listaCompra", cascade = CascadeType.ALL, orphanRemoval = true)
	private List<ItemListaCompra> itens = new ArrayList<>();
	
	public ListaCompra() {
		
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public LocalDate getDataCriacao() {
		return dataCriacao;
	}

	public void setDataCriacao(LocalDate dataCriacao) {
		this.dataCriacao = dataCriacao;
	}

	public LocalDate getDataExpiracao() {
		return dataExpiracao;
	}

	public void setDataExpiracao(LocalDate dataExpiracao) {
		this.dataExpiracao = dataExpiracao;
	}

	public StatusListaCompra getStatus() {
		return status;
	}

	public void setStatus(StatusListaCompra status) {
		this.status = status;
	}

	public List<ItemListaCompra> getItens() {
		return itens;
	}

	public void setItens(List<ItemListaCompra> itens) {
		this.itens = itens;
	}

	
}
