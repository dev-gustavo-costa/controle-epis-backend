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
	
	/*-------------------------------------
	 ----- @Enumerated(EnumType.STRING) -----
	 
	 É uma anotaçãodo JPA que diz como um campo do tipo enum deve ser guardado no Banco de dados.
	 
	 Com EnumType.STRING, o JPA salva o neum como uma string no banco de dados.
	  
	  -------------------------------------
	 */
	@Enumerated(EnumType.STRING)
	private StatusListaCompra status;
	
	/* -------------------------------------
	   ----- @OneToMany(mappedBy = "listaCompra", cascade = CascadeType.ALL, orphanRemoval = true) -----
	   
	   @OneToMany  -> uma lista de compra tem muitos itens.
	   
	   mappedBy = "listaCompra"  -> diz ao JPA que esse relacionamento já está mapeado do outro lado, no campo
	   listaCompra da classe itemListaCompra, não crie uma tabela nova ou coluna nova para isso aqui.
	   
	   O cascade = CascadeType.All -> diz que qualquer operação feita na listaCompra deve propagar automaticamente para os itens dela.
	   
	   orphanRemocal = true -> Caso seja apagado um item da listaCompra por exemplo um epi luva que estava na posição 0 da lista,
	   Ele tambem sera apagado dentro de itenListaCompra na tabela, sem essa anotação o hibernate detecta que aquele itemListaCompra não esta
	   mais dentro da coleção itens da listaCompra. Como ele não tem ordem para deletar (não tem orfhamRemoval) ele tenta apenas desligar o item
	   da lista, ou seja ele dispara um update na tabela item_lista_compra tentando colocar lista_compra_id = NULL na linha da luva, 
	   para refletir que ela não pertence mais aquela lista.
	   
	   
	   -------------------------------------
	 */
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
