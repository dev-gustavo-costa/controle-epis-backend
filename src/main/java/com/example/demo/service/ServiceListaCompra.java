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
	
	private static final int DIAS_VALIDADE = 15;
	
	@Autowired
	private ListaCompraRepository listaCompraRepository;
	
	@Autowired
	private EpiRepository epiRepository;
	
	// Prévia: não salva nada, só sugere quais EPIs entrariam na lista.
	public List<Epi> sugestao() {
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
