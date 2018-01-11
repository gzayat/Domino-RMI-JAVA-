/**
 * Autores: Gabriel Zayat Sant'Anna e Rafael de Souza Abreu
 * Projeto UNISUAM SD
 */

import java.rmi.Remote;
import java.rmi.RemoteException;

/*INTERFACE DO CLIENTE, SERVE PARA QUANDO O SERVIDOR QUE ENVIAR ALGO PARA O CLIENTE
 * ASSIM CADA FUNÇÃO ABAIXO É DECLARADA NO CLIENTE
 */
public interface UnisuamDomino_ClienteItf extends Remote{
		void recebe(UnisuamDomino_Movimento t)throws RemoteException;	
		void AtivarBotao(Boolean recebe)throws RemoteException;
		void subBotoes(Boolean recebe)throws RemoteException;
		void subBotoes_jogo(Boolean recebe)throws RemoteException;
		void subBotoes_jogo_passvez(Boolean recebe)throws RemoteException;
		byte[] pontuacao()throws RemoteException, Exception;
//FIM DA CLASSE PRINCIPAL LEMBRAR DE NÃO EXCLUIR  	 \\\\\\\\\\\\\\\\\\\\GABRIEL ZAYAT SANT'ANNA & RAFAEL DE SOUZA ABREU *********
}