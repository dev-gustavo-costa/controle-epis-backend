package com.example.demo.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.example.demo.dto.ItemRecebidoDTO;
import com.example.demo.exception.ListaCompraNaoEncontradaException;
import com.example.demo.exception.OperacaoInvalidaException;
import com.example.demo.model.Epi;
import com.example.demo.model.ItemListaCompra;
import com.example.demo.model.ListaCompra;
import com.example.demo.model.StatusListaCompra;
import com.example.demo.repository.EpiRepository;
import com.example.demo.repository.ListaCompraRepository;

import jakarta.transaction.Transactional;

@Service
public class ServiceListaCompra {
	
	/* -----------------------------------
	   ----- private static final int DIAS_VALIDADE = 15; -----
	   
	   static -> diz que essa variavel pertence a classe em si, não a cada objeto/intencia dela.
	   		Ou seja não importa quantos objetos ServiceListaCompra existem na aplicação, existe so uma
	   		copia de DIAS_VALIDADE na memoria, compartilhada por todos.
	   		
	   final -> significa que, uma vez atribuida o valor (15), ele nunca pode ser alterado depois,
	   		é uma constante de verdade. Se alguem tentar fazer DIAS_VALIDADE = 20 em qualquer lugar do codigo,
	   		o compilador vai acusar erro.
	   	
	   DIAS_VALIDADE -> em maiusculo com underline (SCREAMING_SNAKE_CASE) é a covenção padrão do java para 
	   		constates ( static final ), só de ver o nome assim, já se reconhece que é um valor fixo que não muda.
	   -----------------------------------
	 */
	private static final int DIAS_VALIDADE = 15;
	
	@Autowired
	private ListaCompraRepository listaCompraRepository;
	
	@Autowired
	private EpiRepository epiRepository;
	
	
	/*
	  Métado de previa: não cria e nem salva nenhuma ListaCompra ou ItemListaCompra no banco,
	  só sugere quais EPIs estão com estoque baixo e mereceriam entrar numa lista de compras.
	 */
	public List<Epi> sugestao() {
		/*
		   Reaproveita a query já existente no EpiRepository (estoque < estoqueMin). Por isso esse service,
		   mesmo sendo o de listaCompra, tambem precisa ter o EpiRepository injetado (@Autowired), além do
		   seu proprio ListaCompraRepository.
		 */
		return epiRepository.buscarAbaixoDoEstoqueMinimo();
	}
	
	public ListaCompra criar(List<ItemListaCompra> itens) {
		ListaCompra lista = new ListaCompra();
		lista.setDataCriacao(LocalDate.now());
		lista.setDataExpiracao(LocalDate.now().plusDays(DIAS_VALIDADE));
		lista.setStatus(StatusListaCompra.ABERTA);
		
		for (ItemListaCompra item: itens) {
			item.setListaCompra(lista);
		}
		lista.setItens(itens);
		
		return listaCompraRepository.save(lista);
	}
	
	
	public List<ListaCompra> listarTodos() {
		return listaCompraRepository.findAll();
	}
	
	public ListaCompra buscarPorId(int id) {
		return listaCompraRepository.findById(id).orElseThrow(() -> 
		new ListaCompraNaoEncontradaException("Lista de compra não encontrada com o id: " +  id));
	}
	@Transactional
	public ListaCompra aplicar(int id, List<ItemRecebidoDTO> itensRecebidos) {
		ListaCompra lista = buscarPorId(id);
		
		if (lista.getStatus() != StatusListaCompra.ABERTA) {
			throw new OperacaoInvalidaException("Essa lista já foi aplicada ou está expirada.");
		}
		
		for (ItemRecebidoDTO recebido : itensRecebidos) {
			ItemListaCompra item = lista.getItens().stream()
					.filter(i -> i.getId() == recebido.getItemId())
					.findFirst()
					.orElseThrow(() -> new OperacaoInvalidaException("Item " + 
					recebido.getItemId() + " não pertence a essa lista."));
			
			item.setQuantidadeRecebida(recebido.getQuantidadeRecebida());
			
			Epi epi = item .getEpi();
			epi.setEstoque(epi.getEstoque() + recebido.getQuantidadeRecebida());
			epiRepository.save(epi);
		}
		
		lista.setStatus(StatusListaCompra.APLICADA);
		return listaCompraRepository.save(lista);
	}
	
	@Scheduled(cron = "0 0 0 * * *")
	public void expirarListasVenciadas() {
		List<ListaCompra> listasVencidas = listaCompraRepository
				.findByStatusAndDataExpiracaoBefore(StatusListaCompra.ABERTA, LocalDate.now());
		
		for (ListaCompra lista : listasVencidas) {
			lista.setStatus(StatusListaCompra.EXPIRADA);
		}
		
		listaCompraRepository.saveAll(listasVencidas);
	}

}
