/**
 * Autores: Gabriel Zayat Sant'Anna e Rafael de Souza Abreu
 * Projeto UNISUAM SD
 */

import java.io.Serializable;

/*
 * LISTA DUPLAMENTE ENCADEADA, SERVER PARA QUE O SERVIDOR FAÇA UMA LISTA DAS PEÇAS 
 * DUPLAMENTE ENCADEADA
 * 
 */

class Ligacao implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public UnisuamDomino_Objeto peca; // 
	public Ligacao next; // PRÓXIMO ITEM NA LISTA
	public Ligacao previous; // ITEM ANTERIOR DA LISTA
	// -------------------------------------------------------------

	public Ligacao(UnisuamDomino_Objeto d) // CONSTRUTOR
	{
		peca = d;
	}

	// -------------------------------------------------------------
	public void displayLigacao() // LIGAÇÃO
	{
		System.out.print(peca + " ");
	}

	public String displayUnisuamDomino_Objeto() {
		return (peca + " ");
	}
}

class UnisuamDomino_ListaEncadeada implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Ligacao first; // REFERENCIA O PRIMEIRO ITEM
	private Ligacao last; // REFERENCIA O ULTIMO ITEM
	// -------------------------------------------------------------

	public UnisuamDomino_ListaEncadeada() // CONSTRUTOR
	{
		first = null; // LISTA VAZIA
		last = null;
	}

	// -------------------------------------------------------------
	public boolean isEmpty() // LISTA VAZIA TRUE
	{
		return first == null;
	}

	// -------------------------------------------------------------
	public void insertFirst(UnisuamDomino_Objeto dd) // INSERIR NO PRIMEIRO DA LISTA
	{
		Ligacao newLigacao = new Ligacao(dd); // FAZER UMA NOVA LIGAÇÃO

		if (isEmpty()) // SE IF LISTA VAZIA
			last = newLigacao; // NOVA LIGAÇÃO POR ÚLTIMO 
		else
			first.previous = newLigacao; // LIGAÇÃO ULTIMA RECEBE NOVA
		newLigacao.next = first; //NOVA LIGAÇÃO RECEBE PRIMEIRA
		first = newLigacao; // PRIMEIRA RECEBE A NOVA LIGAÇÃO
	}

	// -------------------------------------------------------------
	public void insertLast(UnisuamDomino_Objeto dd) // INSERIR NO FINAL NA LISTA
	{
		Ligacao newLigacao = new Ligacao(dd); // FAZER UMA NOVA LIGAÇÃO
		if (isEmpty()) // SE LISTA VAZIA
			first = newLigacao; // PRIMEIRO RECEBE NOVA LIGAÇÃO
		else {
			last.next = newLigacao; // ANTERIOR RECEBE A NOVA LIGAÇÃO
			newLigacao.previous = last; // NOVA LIGAÇÃO RECEBE ULTIMO
		}
		last = newLigacao; // ULTIMA RECEBE NOVA LIGAÇÃO
	}

	// -------------------------------------------------------------
	public Ligacao deleteFirst() // EXCLUIR PRIMEIRA LIGAÇÃO
	{ // (TOMANDO BASE QUE A LISTA NÃO ESTÁ VAZIA Z)
		Ligacao temp = first;
		if (first.next == null) // SE A LISTA A PENAS 1 ITEM
			last = null; // ULTIMO RECEBE NULL
		else
			first.next.previous = null; //PROXIMA RECEBE NULL
		first = first.next; // PRIMEIRA RECEBE PROXIMA
		return temp;
	}

	// -------------------------------------------------------------
	public Ligacao deleteLast() // DELETANDO ULTIMO DA LISTA
	{ // (TOMANDO BASE QUE A LISTA NÃO ESTÁ VAZIA)
		Ligacao temp = last;
		if (first.next == null) // SE A LISTA A PENAS 1 ITEM
			first = null; // PRIMEIRO RECEBE NULL
		else
			last.previous.next = null; // oULTIMO RECEBE NULL
		last = last.previous; // ULTIMO RECEBE PENULTIMO
		return temp;
	}

	
	public boolean insertAfter(UnisuamDomino_Objeto key, UnisuamDomino_Objeto dd) { // (TOMANDO BASE QUE A LISTA NÃO ESTÁ VAZIA)
		Ligacao current = first; // INICIAR
		while (current.peca != key) // ATE ALGO FOR ENCONTRADO
		{
			current = current.next; // MOVER PARA PROXIMA
			if (current == null)
				return false; // NÃO ENCONTRADO
		}
		Ligacao newLigacao = new Ligacao(dd); // FAZER NOVA LIGAÇÃO

		if (current == last) // SE A LIGAÇÃO FOR A ULTIMA
		{
			newLigacao.next = null; // NOVA LIGAÇÃO RECEBE NULL
			last = newLigacao; // ULTIMA LIGAÇÃO RECEBE NOVA
		} else // CASO
		{
			newLigacao.next = current.next; // NOVA LIGAÇÃO RECEBE PROXIMO
			current.next.previous = newLigacao;
		}
		newLigacao.previous = current; 
		current.next = newLigacao; 
		return true; 
	}

	// -------------------------------------------------------------
	public Ligacao deleteKey(UnisuamDomino_Objeto key) 
	{ 
		Ligacao current = first; 
		while (current.peca != key){
			current = current.next; 
			if (current == null)
				return null; 
		}
		if (current == first) 
			first = current.next; 
		else			
			current.previous.next = current.next;

		if (current == last) 
			last = current.previous; 
		else			
			current.next.previous = current.previous;
		return current; 
	}

	// -------------------------------------------------------------
	public Ligacao retornarUltimo() {
		Ligacao current = first; 
		while (current != null) {
			if (current.next == null) 
				break;
			
			current = current.next; // move to next Ligacao
		}
		return current;
	}

	// -------------------------------------------------------------
	public void displayForward() {		
		Ligacao current = first; 
		while (current != null) {
			current.displayLigacao(); 
			current = current.next; 
		}
		System.out.println("");
	}

	// -------------------------------------------------------------
	public String displayPecaForward() {

		String UnisuamDomino_Objetos = "";		
		
		Ligacao current = first; 
		while (current != null) {
			UnisuamDomino_Objetos = UnisuamDomino_Objetos.concat( current.displayUnisuamDomino_Objeto() ); 
			current = current.next; 
		}
		System.out.println("");

		return UnisuamDomino_Objetos;
	}

	// -------------------------------------------------------------
	public Ligacao retornarPrimeiro() {
		Ligacao current = last; 
		while (current != null) {
			if (current.previous == null) 
				break;
			
			current = current.previous; 
		}
		return current;
	}

	// -------------------------------------------------------------
	public void displayBackward() {
		System.out.print("List (last-->first): ");
		Ligacao current = last; 
		while (current != null) {
			current.displayLigacao(); 
			current = current.previous; 
		}
		System.out.println("");
	}
	
	 // GABRIEL ZAYAT SANT'ANNA & RAFAEL DE SOUZA ABREU *********
	//DoublyLigacaoedApp*/
	//CLASSE USADA DA WEB COM REFERENCIA ACIMA, PORÉM MODIFICA E ADAPTADA AO GAME
	
	//FIM DA CLASSE PRINCIPAL LEMBRAR DE NÃO EXCLUIR
}

