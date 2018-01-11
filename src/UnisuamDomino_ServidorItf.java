/**
 * Autores: Gabriel Zayat Sant'Anna e Rafael de Souza Abreu
 * Projeto UNISUAM SD
 */

import java.rmi.Remote; 
import java.rmi.RemoteException;

/*INTERFACE DO SERVIDOR, SERVE PARA QUANDO O CLIENTE ENVIA ALGO PARA O SERVIDOR
 * ASSIM CADA FUNÇÃO ABAIXO É DECLARADA NO SERVIDOR
 */
public interface UnisuamDomino_ServidorItf extends Remote {
	
	void login(String a) throws  RemoteException;
	
	void recebeTransporte(UnisuamDomino_Movimento tr) throws RemoteException;
	
	void envia(UnisuamDomino_Movimento tr)throws  RemoteException;
	
	void enviaParaTodos(UnisuamDomino_Movimento tr)throws  RemoteException;
	
	void ganhouJogo(Boolean a, String b)throws RemoteException;
	
	
	
//FIM DA CLASSE PRINCIPAL LEMBRAR DE NÃO EXCLUIR  	 \\\\\\\\\\\\\\\\\\\\GABRIEL ZAYAT SANT'ANNA & RAFAEL DE SOUZA ABREU *********
}