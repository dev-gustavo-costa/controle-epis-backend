package com.example.demo.service;

import java.awt.Color;
import java.io.ByteArrayOutputStream;

import org.springframework.stereotype.Component;

import com.example.demo.model.Epi;
import com.example.demo.model.ItemListaCompra;
import com.example.demo.model.ListaCompra;
import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.FontFactory;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfPCell;
import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfWriter;

@Component
public class GeradorPdfListaCompra {
	
	private static final Color AZUL_DESTAQUE = new Color(37, 99, 235); 
	
	public byte[] gerar(ListaCompra lista) {
		Document document = new Document();
		ByteArrayOutputStream saida = new ByteArrayOutputStream();
		
		try {
			PdfWriter.getInstance(document, saida);
			document.open();
			
			Font fonteTitulo = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 16);
			document.add(new Paragraph("Lista de compras #" + lista.getId(), fonteTitulo));
			document.add(new Paragraph("Data de criação: " + lista.getDataCriacao()));
			
			PdfPTable tabela = new PdfPTable(3);
			tabela.setWidthPercentage(100);
			tabela.setWidths(new float[] {3f, 1f, 1f});
			tabela.setSpacingBefore(20f);
			
			Font fonteCabecalho = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 11, Color.white);
			for (String titulo : new String[] {"EPI", "CA", "Quantidade"}) {
				PdfPCell celula = new PdfPCell(new Paragraph(titulo, fonteCabecalho));
				celula.setBackgroundColor(AZUL_DESTAQUE);
				celula.setPadding(6f);
				tabela.addCell(celula);
			}
			
			for (ItemListaCompra item : lista.getItens()) {
				Epi epi = item.getEpi();
				
				PdfPCell celulaNome = new PdfPCell(new Paragraph(epi.getNomeEpi()));
				celulaNome.setPadding(6f);
				tabela.addCell(celulaNome);
				
				PdfPCell celulaCa = new PdfPCell(new Paragraph(String.valueOf(epi.getCa())));
				celulaCa.setPadding(6f);
				tabela.addCell(celulaCa);
				
				PdfPCell celulaQuantidade = new PdfPCell(new Paragraph(String.valueOf(item.getQuantidadeOrcada())));
				celulaQuantidade.setPadding(6f);
				tabela.addCell(celulaQuantidade);
			}
			
			document.add(tabela);
			
			Font fonteRodape = FontFactory.getFont(FontFactory.HELVETICA_OBLIQUE, 9, Color.GRAY);
			Paragraph rodape = new Paragraph("Documento gerado automaticamente pelo Sistema de Controle de EPI's.", fonteRodape);
			rodape.setSpacingBefore(20f);
			document.add(rodape);
			
			document.close();
		} catch (Exception e) {
			throw new RuntimeException("Erro ao gerar PDF da lista de compras", e);
		}
		return saida.toByteArray();
	}
}
