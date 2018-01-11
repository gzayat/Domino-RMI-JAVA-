
/**
 * Autores: Gabriel Zayat Sant'Anna e Rafael de Souza Abreu
 * Projeto UNISUAM SD
 */

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/*
 * TODA A LÓGICA DO JOGO ESTÁ AQUI, CLASS MESA INTERAGE DIRETO COM O SERVIDOR,
 * O CLIENTE APENAS RECEBE AS ATUALIZAÇÕES ENVIADAS PELO SERVIDOR
 * TODA A LÓGICA DE ENVIO DE NOVA PEÇA, POSIÇÃO É FEITA AQUI
 * 
 */
public class UnisuamDomino_Mesa implements Serializable {

	private static final long serialVersionUID = 1L;
	public List<UnisuamDomino_Objeto> todasPecas;
	public UnisuamDomino_ListaEncadeada pecasEmJogo;
	public UnisuamDomino_Lados lado;

	public UnisuamDomino_Mesa() {
		this.todasPecas = new ArrayList<UnisuamDomino_Objeto>();
		this.pecasEmJogo = new UnisuamDomino_ListaEncadeada();
	}

	public void inicializaPecas() {

		for (int i = 0; i <= 6; i++) {
			for (int j = i; j <= 6; j++) {
				UnisuamDomino_Objeto p = new UnisuamDomino_Objeto(i, j);
				todasPecas.add(p);
			}
		}
	}

	public List<UnisuamDomino_Objeto> distribuirPecas() {

		List<UnisuamDomino_Objeto> listaRetornada = new ArrayList<UnisuamDomino_Objeto>();

		for (int i = 0; i < 3; i++) {
			UnisuamDomino_Objeto p = todasPecas.remove(0);
			listaRetornada.add(p);
		}

		return listaRetornada;
	}

	public UnisuamDomino_Objeto obtemProximaPeca() {
		if (todasPecas.size() != 0)
			return todasPecas.remove(0);
		return null;
	}

	public void embaralhaPecas() {
		Collections.shuffle(todasPecas);
	}

	public boolean adicionaPeca(UnisuamDomino_Objeto p, UnisuamDomino_Lados l) {

		UnisuamDomino_Objeto auxiliar;

		if (pecasEmJogo.isEmpty()) {
			pecasEmJogo.insertFirst(p);
			return true;
		}

		if (l == UnisuamDomino_Lados.DIREITA) {
			auxiliar = pecasEmJogo.retornarUltimo().peca;
			if (p.face1 == auxiliar.face2) {
				pecasEmJogo.insertLast(p);
				return true;
			} else {
				p.inverterPeca();
				if (p.face1 == auxiliar.face2) {
					pecasEmJogo.insertLast(p);
					return true;
				}
			}
		} else {
			auxiliar = pecasEmJogo.retornarPrimeiro().peca;

			if (p.face2 == auxiliar.face1) {
				pecasEmJogo.insertFirst(p);
				return true;
			} else {
				p.inverterPeca();
				if (p.face2 == auxiliar.face1) {
					pecasEmJogo.insertFirst(p);
					return true;
				}
			}
		}
		return false;
	}

	public static void main(String[] args) {

		UnisuamDomino_Mesa t = new UnisuamDomino_Mesa();
		t.inicializaPecas();
		t.embaralhaPecas();		
		
		while (t.obtemProximaPeca() != null) 
			t.obtemProximaPeca();
	}

	// FIM DA CLASSE PRINCIPAL LEMBRAR DE NÃO EXCLUIR
	// \\\\\\\\\\\\\\\\\\\\GABRIEL ZAYAT SANT'ANNA & RAFAEL DE SOUZA ABREU
	// *********
}