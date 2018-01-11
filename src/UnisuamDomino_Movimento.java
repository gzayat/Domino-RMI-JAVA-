/**
 * Autores: Gabriel Zayat Sant'Anna e Rafael de Souza Abreu
 * Projeto UNISUAM SD
 */


import java.io.Serializable;
import java.util.List;

/*
 * TODAS AS A��ES QUE ENVOLVEM CLIENTE E SERVIDOR TOMAM COMO BASE A CLASSE UNISUAMDOMINO_MOVIMENTO,
 * NA QUAL DEFINI OS NOME DO JOGADOR A A��O REALIZADA POR ELE, SUAS PE�AS E DEMAIS FUNCIONALIDADES
 * POR ISSO QUE SEMPRE CLIENTE E SERVIDOR INICIAM UMA NOVA DECLARA��O A PARTIR DESSA CLASSE
 */
public class UnisuamDomino_Movimento implements Serializable {
	
	private static final long serialVersionUID = 1L;
	public String jogador,adversario,adversario1;
	public Integer acao;
	public UnisuamDomino_Objeto peca;
	public List<UnisuamDomino_Objeto> pecasDoCliente;
	public String textoDoChat;
	public UnisuamDomino_Lados lado;
	public boolean jogadaFeita;
	public String tabuleiro;
	public int idJogador,pecatotal;
	public String notificacao;
	public int somatorioJogador;
	public byte[] cript,adversariob,adversariob1;
	
	//public List<Peca> listaPecasTabuleiro;
	
	
	@Override
	public String toString() {
		return "Transporte: [jogador = "+jogador+"], [acao = "+acao+"], [peca = "+peca+"], [textoDoChat = " + textoDoChat+"]"
		+"[notificacao = " +notificacao+"][idJogador = "+idJogador+"][jogada feita = " + jogadaFeita + "][lado = " + lado + "][Situ��o do tabuleiro = "+tabuleiro+"][pecasDoCliente = "+ pecasDoCliente + "]";
	}
	
//FIM DA CLASSE PRINCIPAL LEMBRAR DE N�O EXCLUIR  	 \\\\\\\\\\\\\\\\\\\\GABRIEL ZAYAT SANT'ANNA & RAFAEL DE SOUZA ABREU *********
}