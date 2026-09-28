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
	
	/* --------------------------
	  ----- @ManyToOne -----
	  
	  Indica que essa entidade (a classe onde esse campo está) tem uma relção de "muitos para um" com epi,
	  
	  Ou seja o pode aparecer mais de um epi e aparecer em varias lista de compra diferentes sem limitação.
	  
	  ----- @JoinColumn -----
	  
	  diz onde, no banco de dados, essa referencia fica guardada: na tabela itens_lista_compra 
	  vai existir uma coluna chamada epi_id , que é uma chave extrageira apontando para o id na tabela epi.
	  
	  É assim que o banco sabe "esse item da lista se refere a esse EPI especifico".
	  --------------------------
	*/
	@ManyToOne
	@JoinColumn(name = "epi_id")
	private Epi epi;
	
	/* --------------------------
	 ----- @JsonIgnore -----
	 
	 Quando for serializado esse objetopara JSON, Ignore esse campo, não coloque ele na resposta.
	 
	 Se o Jackson tentasse serializar isso sem restrição, ele ia gerar um JSON ItemListaCompra -> que
	 contém ListaCompra -> que contem a lista de itens - que contem ListaCompra de novo -> e assim infinitamente,
	 até estourar em StackOverflowError.
	 
	 Vale notar que isso so afeta a serializalção JSON(a resposta da API), para as demais funciona normalmente.
	   --------------------------
	 */
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

	// Geters e seters:
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
