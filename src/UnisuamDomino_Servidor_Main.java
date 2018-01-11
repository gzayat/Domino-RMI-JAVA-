
/**
 * Autores: Gabriel Zayat Sant'Anna e Rafael de Souza Abreu
 * Projeto UNISUAM SD
 */

import java.awt.Toolkit;

import javax.swing.JFrame;

import java.net.InetAddress;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.ImageIcon;
import javax.swing.JButton;

import java.awt.Font;
import java.awt.SystemColor;
import java.awt.Color;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class UnisuamDomino_Servidor_Main extends UnicastRemoteObject implements UnisuamDomino_ServidorItf {
	/**
	 * 
	 */

	private static final long serialVersionUID = 1L;
	public static JTextArea textArea;
	public static Registry registry1;
	public UnisuamDomino_Mesa tabuleiro;
	public UnisuamDomino_Movimento transporte;

	public InetAddress ip;
	public boolean vez[];
	public boolean passouVez[];
	public int idPrimeiroJogadorQueZerou;
	public int nClientesConectados = 0;

	public ArrayList<String> nome_cliente;
	public String ip_client;
	private String keyUnisuam = "UNISUAM2015SDALE";
	byte[] textoCriptografia;

	Map<Integer, Integer> mapa;
	Map<Integer, String> mapa2;
	public JFrame frame;
	public JMenuBar menuBar;
	public JMenu MenuTolbar;
	public JMenuItem Menu_Autores, Menu_Sair;
	public JLabel lblMonitorServidor, lblNomeDoServidor, lblNewLabel_Porta, lblInformaesDoServidor, lblNewLabel,
			lblNewLabel_Ip, lblPorta, lblNomeDoServer;
	public static JButton btnNewButton, btnNewButton_1;

	//////////////// INICIO CLASS SERVIDOR \\\\\\\\\\\\\\\\\\\\\\\
	public UnisuamDomino_Servidor_Main() throws RemoteException {

		super();

		mapa = new HashMap<Integer, Integer>();
		mapa2 = new HashMap<Integer, String>();
		nome_cliente = new ArrayList<String>();

		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("imagens/uni.jpg"));
		frame.setTitle("Unisuam Dominó -- SERVIDOR --");

		menuBar = new JMenuBar();
		MenuTolbar = new JMenu("Arquivo");
		menuBar.add(MenuTolbar);
		frame.setJMenuBar(menuBar);

		Menu_Autores = new JMenuItem("Autores");
		MenuTolbar.add(Menu_Autores);
		JSeparator separator = new JSeparator();
		MenuTolbar.add(separator);
		Menu_Sair = new JMenuItem("Sair");
		MenuTolbar.add(Menu_Sair);

		textArea = new JTextArea();
		textArea.setEditable(false);
		textArea.setBounds(10, 77, 414, 160);
		frame.getContentPane().add(textArea);

		JScrollPane scroll = new JScrollPane(textArea, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
		scroll.setBounds(10, 77, 414, 160);
		frame.getContentPane().add(scroll);

		JSeparator separator_1 = new JSeparator();
		// separator_1.setBounds(10, 69, 414, 2);
		frame.getContentPane().add(separator_1);

		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(10, 12, 181, 2);
		frame.getContentPane().add(separator_2);

		btnNewButton_1 = new JButton("Desligar");
		btnNewButton_1.setEnabled(false);
		btnNewButton_1.setBounds(335, 34, 89, 23);
		frame.getContentPane().add(btnNewButton_1);

		btnNewButton = new JButton("Ligar");
		btnNewButton.setBounds(335, 11, 89, 23);
		frame.getContentPane().add(btnNewButton);

		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent a) {

				btnNewButton_1.setEnabled(true);
				btnNewButton.setEnabled(false);
				try {

					UnisuamDomino_Servidor_Main obj = new UnisuamDomino_Servidor_Main();
					LocateRegistry.createRegistry(1099);
					Naming.rebind("rmi://" + ip.getHostAddress() + "/UnisuamDomino", (Remote) obj);
					textArea.append("Servidor Criado!\n");
					textArea.setCaretPosition(textArea.getText().length());
					textArea.append("Servidor UnisuamDomino Registrado e Pronto.\n");
					textArea.setCaretPosition(textArea.getText().length());
					check();
					frame.dispose();

				} catch (Exception e) {
					System.out.println("Erro" + e.getMessage());
					e.printStackTrace();
				}
			}
		});

		btnNewButton_1.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		lblInformaesDoServidor = new JLabel("Informa\u00E7\u00F5es do Servidor");
		lblInformaesDoServidor.setForeground(new Color(255, 51, 0));
		lblInformaesDoServidor.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblInformaesDoServidor.setBounds(10, 0, 181, 14);
		frame.getContentPane().add(lblInformaesDoServidor);

		lblMonitorServidor = new JLabel("MONITOR DO SERVIDOR");
		lblMonitorServidor.setForeground(SystemColor.textHighlight);
		lblMonitorServidor.setFont(new Font("Tahoma", Font.BOLD, 14));
		lblMonitorServidor.setBounds(10, 57, 181, 14);
		frame.getContentPane().add(lblMonitorServidor);

		lblNomeDoServidor = new JLabel("UnisuamDomino");
		lblNomeDoServidor.setBounds(105, 12, 138, 14);
		frame.getContentPane().add(lblNomeDoServidor);

		lblNewLabel = new JLabel("HostName/IP:");
		lblNewLabel.setBounds(10, 25, 79, 14);
		frame.getContentPane().add(lblNewLabel);

		lblNewLabel_Ip = new JLabel("New label");
		lblNewLabel_Ip.setBounds(93, 25, 243, 14);
		frame.getContentPane().add(lblNewLabel_Ip);
		try {
			ip = InetAddress.getLocalHost();
			lblNewLabel_Ip.setText(ip.getHostName() + " / " + ip.getHostAddress());
		} catch (Exception e) {
			lblNewLabel_Ip.setText("Problema ao capturar Ip/nome Servidor"); /// *\\\\
		}

		lblPorta = new JLabel("Porta:");
		lblPorta.setBounds(10, 38, 35, 14);
		frame.getContentPane().add(lblPorta);

		lblNewLabel_Porta = new JLabel("1099");
		lblNewLabel_Porta.setBounds(44, 38, 79, 14);
		frame.getContentPane().add(lblNewLabel_Porta);

		lblNomeDoServer = new JLabel("Nome do Server:");
		lblNomeDoServer.setBounds(10, 12, 102, 14);
		frame.getContentPane().add(lblNomeDoServer);

		Menu_Autores.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JOptionPane.showMessageDialog(null, "Desenvolvedores:\n" + "Gabriel Zayat Sant'Anna\nRafael de Souza Abreu" +
						"\n\nJogo Desenvolvido visando o Curso de Sistemas Distribuidos,\nutilizando o método de invocação Remota(RMI), também utilizando\na interface gráfica no Cliente e Servidor.\n\nAgradecimento em especial ao Professor Alex Avellar.","Desenvolvedores do Jogo",JOptionPane.ERROR_MESSAGE,new ImageIcon("imagens/uni.jpg"));
				
			}
		});
		Menu_Sair.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0);
			}
		});

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);
		frame.setBounds(100, 100, 450, 300);
		frame.setVisible(true);
		frame.setResizable(false);

		// Fim da Classe Principal
	}

	public static void check() {
		btnNewButton.setEnabled(false);
		btnNewButton_1.setEnabled(true);
	}

	public void iniciarJogo() {
		vez = new boolean[nClientesConectados];
		passouVez = new boolean[nClientesConectados];
	}

	public void zeraPassouVez() {
		for (int i = 0; i < passouVez.length; i++) {
			passouVez[i] = false;
		}
	}

	public boolean jogoTrancado() {
		for (int i = 0; i < passouVez.length; i++) {
			if (passouVez[i] == false)
				return false;
		}
		return true;
	}

	public void setPassaVez(int idJogador, boolean passouAVez) {
		passouVez[idJogador] = passouAVez;

	}

	public boolean passouVez(int idJogador) {
		return passouVez[idJogador];
	}

	public boolean ehPrimeiraJogada() {
		for (int i = 0; i < vez.length; i++) {
			if (vez[i] == true) {
				return false;
			}
		}
		return true;
	}

	public boolean ehVezDoJogador(int indiceJogador) {
		return vez[indiceJogador - 1];
	}

	public void setPassaVez(int i) {
		if (i == (passouVez.length - 1)) {
			passouVez[i] = false;
			passouVez[0] = true;
		} else {
			passouVez[i] = false;
			passouVez[i + 1] = true;
		}
	}

	public void setVez(int i) {

		if (i == (vez.length - 1)) {
			vez[i] = false;
			vez[0] = true;
		} else {
			vez[i] = false;
			vez[i + 1] = true;
		}
	}

	public List<UnisuamDomino_Objeto> pecasDoTabuleiro() {
		List<UnisuamDomino_Objeto> list = new ArrayList<UnisuamDomino_Objeto>();
		UnisuamDomino_Objeto peca = tabuleiro.pecasEmJogo.retornarPrimeiro().peca;
		while (peca != null) {
			list.add(peca);
			peca = peca.next;
		}
		return list;
	}

	// SERVIDOR RECEBE NOME DO JOGADOR CONECTADO
	public void login(String a) throws RemoteException {
		// Recebe o nome do cliente, e adiciona no Array
		nome_cliente.add(a);

		// Informa que o cliente se conectou
		textArea.append("Cliente " + a + " , Se conectou ao Servidor.\n");
		textArea.setCaretPosition(textArea.getText().length());

		// LIMITE, INFORMANDO QUE O SERVIDOR JÁ POSSUI 2 JOGADORES QUE PODEM
		// INICIAR O GAME
		nClientesConectados++;
		if (nClientesConectados >= 2) {
			textArea.append("2 Jogadores conectados no servidor,Já podem iniciar a partida.\n");
			textArea.setCaretPosition(textArea.getText().length());
			
			
			// Chama a função para ativar o botão iniciar a partida no cliente
			AtivarBotao(true);
		}

		UnisuamDomino_Movimento tr = new UnisuamDomino_Movimento();
		tr.idJogador = nClientesConectados;
		tr.acao = 15;

		UnisuamDomino_ClienteItf cl;
		try {
			ip = InetAddress.getLocalHost();
			cl = (UnisuamDomino_ClienteItf) Naming.lookup(a);
			cl.recebe(tr);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	/*
	 * SERVIDOR RECEBE AÇÃO DO CLIENTE E A PARTIR DESSA AÇÃO INICIA ALGUM PROCEDIMENTO
	 * PROCEDIMENTOS ESSES COMO SEGUEM ABAIXO DENTRO DO IF DEPENDENDO DA AÇÃO
	 */
	public void recebeTransporte(UnisuamDomino_Movimento tr) throws RemoteException {
		
		// AÇÃO 1: ACONTECEDEVIDO AO CLIENTE INICIAR O JOGO, SERVIDOR GERA UM
		// NOVO TABULEIRO
		
		// E ENVIAR PEÇAS AO CLIENTE
		if (tr.acao == 1) {
			textArea.append("Jogo iniciado !.\n");
			textArea.setCaretPosition(textArea.getText().length());
			
			iniciarJogo();

			tabuleiro = new UnisuamDomino_Mesa();
			tabuleiro.inicializaPecas();
			tabuleiro.embaralhaPecas();

			subBotoes(true);
			
			//if(t.idJogador == 1)
				//t.adversario = nome_cliente.get(0) ;
			
			for (String nome : nome_cliente) {
				UnisuamDomino_Movimento t = new UnisuamDomino_Movimento();
				
				if(nome_cliente.get(0) == nome)
					t.adversario = nome_cliente.get(1) + ": 3";
				else
					t.adversario = nome_cliente.get(0) + ": 3";
				t.acao = 1;
				t.jogador = nome;
				t.pecasDoCliente = tabuleiro.distribuirPecas();		
				
				try {
					t.cript = UnisuamDomino_Criptografia.encrypt("SISTEMA: " + nome + " Peças recebidas: "+ t.pecasDoCliente + "\n", keyUnisuam);
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}				
				envia(t);
				textArea.append("Enviando Peças ao Jogador " + nome + "\n");
				textArea.setCaretPosition(textArea.getText().length());

			}
		}

		// AÇÃO 2: MOSTRA A PEÇA ENVIADA PELO JOGADOR AO TABULEIRO
		if (tr.acao == 2) {
			textArea.append("Peça recebida " + tr.peca + " do Jogador:" + tr.jogador + "\n");
			textArea.setCaretPosition(textArea.getText().length());

			if (ehPrimeiraJogada()) {
				vez[tr.idJogador - 1] = true;
				subBotoes_jogo(false, tr.jogador);				
			}
			
			// CONFERE SE O JOGADOR PASSOU A VEZ
			if (passouVez(tr.idJogador - 1) == false) {
				textArea.append("Jogada Realizada !\n");
				textArea.setCaretPosition(textArea.getText().length());
				
				//INFORMA A VEZ DE QUEM É A JOGADA
				if (ehVezDoJogador(tr.idJogador)) {												
					try {
						UnisuamDomino_Movimento teste2 = new UnisuamDomino_Movimento();
						teste2.tabuleiro = "";
						teste2.jogadaFeita = tabuleiro.adicionaPeca(tr.peca, tr.lado);
						
						//SE A JOGADA FOI CONCLUÍDA INFORMA A TODOS
						if (teste2.jogadaFeita) {							
							textArea.append(tr.jogador + " Passou a vez !\n");
							textArea.setCaretPosition(textArea.getText().length());
							zeraPassouVez();
							setVez(tr.idJogador - 1);
							setPassaVez(tr.idJogador - 1, false);
							zeraPassouVez();
							subBotoes_jogo(false, tr.jogador);
							subBotoes_jogo1(true, nome_cliente.size() - tr.idJogador);
							
							
							//UnisuamDomino_ClienteItf cl = (UnisuamDomino_ClienteItf) Naming.lookup(nome_cliente.get(nome_cliente.size() - tr.idJogador));
							//teste2.adversario = nome_cliente.get(nome_cliente.size() - tr.idJogador) + ":" + cl.pontuacao();
							
						}
						boolean j = teste2.jogadaFeita;
						teste2.acao = 3;
						teste2.peca = tr.peca;
						teste2.jogador = tr.jogador;
						
						//textArea.append("Enviando teste2. Valor da jogada feita eh " + j + "\n");
						//textArea.setCaretPosition(textArea.getText().length());
						
						envia(teste2);
						UnisuamDomino_Movimento teste3 = new UnisuamDomino_Movimento();
						teste3.jogadaFeita = j;

						if (teste3.jogadaFeita) {								
							String pclient1,pcliente2;
							
							
							UnisuamDomino_ClienteItf cl = (UnisuamDomino_ClienteItf) Naming.lookup(nome_cliente.get(nome_cliente.size() - tr.idJogador));
							pclient1 = UnisuamDomino_Criptografia.decrypt(cl.pontuacao(), keyUnisuam);
							textArea.append(nome_cliente.get(nome_cliente.size() - tr.idJogador) + " Possui" + pclient1 + " Peças !\n");
							textArea.setCaretPosition(textArea.getText().length());							
							teste3.adversariob = UnisuamDomino_Criptografia.encrypt(nome_cliente.get(nome_cliente.size() - tr.idJogador) + ":" + pclient1, keyUnisuam);
							
							UnisuamDomino_ClienteItf cl1 = (UnisuamDomino_ClienteItf) Naming.lookup(tr.jogador);
							pcliente2 = UnisuamDomino_Criptografia.decrypt(cl1.pontuacao(), keyUnisuam);
							textArea.append(tr.jogador + " Possui" + pcliente2 + " Peças !\n");
							textArea.setCaretPosition(textArea.getText().length());							
							teste3.adversariob1 = UnisuamDomino_Criptografia.encrypt(tr.jogador + ":" + pcliente2, keyUnisuam);
							
							teste3.acao = 4;
							teste3.peca = tr.peca;
							teste3.jogador = tr.jogador;
							teste3.tabuleiro = tabuleiro.pecasEmJogo.displayPecaForward();
							try {
								teste3.cript = UnisuamDomino_Criptografia.encrypt("SISTEMA: "+ teste3.jogador + " enviou uma peça para o tabuleiro: " + teste3.peca + "\n", keyUnisuam);
							} catch (Exception e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
							
							enviaParaTodos(teste3);

						}
					} catch (Exception e) {
						textArea.append("Problema na Peça !");
						textArea.setCaretPosition(textArea.getText().length());
						e.printStackTrace();
					}
				} else {
					try {
						UnisuamDomino_Movimento teste2 = new UnisuamDomino_Movimento();
						teste2.tabuleiro = "";
						teste2.jogadaFeita = false;
						teste2.acao = 3;
						teste2.peca = tr.peca;
						teste2.jogador = tr.jogador;
						teste2.notificacao = "Espere sua vez!";
						envia(teste2);
					} catch (Exception e) {
						textArea.append("Problema na Peça !");
						textArea.setCaretPosition(textArea.getText().length());
						e.printStackTrace();
					}
				}
				
			}
		}
		
		//INICIA UM NOVO TRANSPORTE DE PEÇA E ENVIA PARA O JOGADOR
		if (tr.acao == 3) {
			UnisuamDomino_Movimento transporteNovo = new UnisuamDomino_Movimento();
			if (ehPrimeiraJogada())
				vez[tr.idJogador - 1] = true;

			transporteNovo.jogador = tr.jogador;
			if (ehVezDoJogador(tr.idJogador)) {
				UnisuamDomino_Objeto pecaTemporaria = tabuleiro.obtemProximaPeca();
				transporteNovo.peca = pecaTemporaria;
				transporteNovo.acao = 5;
				
			} else {
				transporteNovo.acao = 6;
				transporteNovo.notificacao = "Espere sua vez!";
			}			
			
			envia(transporteNovo);
			
			UnisuamDomino_Movimento transporteNovo2 = new UnisuamDomino_Movimento();
			transporteNovo2.acao =25;
			try{
				/*UnisuamDomino_ClienteItf cl = (UnisuamDomino_ClienteItf) Naming.lookup(nome_cliente.get(nome_cliente.size() - tr.idJogador));
				transporteNovo2.adversario = nome_cliente.get(nome_cliente.size() - tr.idJogador) + ":" + UnisuamDomino_Criptografia.decrypt(cl.pontuacao(), keyUnisuam) ;
				
				UnisuamDomino_ClienteItf cl1 = (UnisuamDomino_ClienteItf) Naming.lookup(tr.jogador);
				transporteNovo2.adversario1 = tr.jogador + ":" + UnisuamDomino_Criptografia.decrypt(cl1.pontuacao(), keyUnisuam);*/
				
				String pclient1,pcliente2;				
				UnisuamDomino_ClienteItf cl = (UnisuamDomino_ClienteItf) Naming.lookup(nome_cliente.get(nome_cliente.size() - tr.idJogador));
				pclient1 = UnisuamDomino_Criptografia.decrypt(cl.pontuacao(), keyUnisuam);
				textArea.append(nome_cliente.get(nome_cliente.size() - tr.idJogador) + " Possui" + pclient1 + " Peças !\n");
				textArea.setCaretPosition(textArea.getText().length());							
				transporteNovo2.adversariob = UnisuamDomino_Criptografia.encrypt(nome_cliente.get(nome_cliente.size() - tr.idJogador) + ":" + pclient1, keyUnisuam);
				
				UnisuamDomino_ClienteItf cl1 = (UnisuamDomino_ClienteItf) Naming.lookup(tr.jogador);
				pcliente2 = UnisuamDomino_Criptografia.decrypt(cl1.pontuacao(), keyUnisuam);
				textArea.append(tr.jogador + " Possui" + pcliente2 + " Peças !\n");
				textArea.setCaretPosition(textArea.getText().length());							
				transporteNovo2.adversariob1 = UnisuamDomino_Criptografia.encrypt(tr.jogador + ":" + pcliente2, keyUnisuam);				
			}catch(Exception e){
			}
			enviaParaTodos(transporteNovo2);
		}
		
		//SERVIDOR RECEBE TEXTO DO CHAT ENVIADO PELO JOGADOR, E REPASSA A TODOS
		if (tr.acao == 4) {
			UnisuamDomino_Movimento t4 = new UnisuamDomino_Movimento();
			t4.acao = 2;			
			//t4.textoDoChat = tr.textoDoChat;	
			t4.cript = tr.cript;
			t4.jogador = tr.jogador;
			enviaParaTodos(t4);
			
			
			textArea.append(t4.jogador + " Enviou uma MSG no CHAT Criptografada: \n");
			textArea.setCaretPosition(textArea.getText().length());
			for (int i=0; i<tr.cript.length; i++){
				textArea.append("" + new Integer(tr.cript[i]));
				textArea.setCaretPosition(textArea.getText().length());
			}
			textArea.append("\n");
			textArea.setCaretPosition(textArea.getText().length());

		}
		
		//VERIFICA A JOGADA SE O JOGO ESTÁ TRANCADO SE SIM, ENVIA PARA TODOS E FINALIZA O JOGO
		if (tr.acao == 6) {
			if (jogoTrancado()) {
				UnisuamDomino_Movimento transporteJogoTrancado = new UnisuamDomino_Movimento();
				transporteJogoTrancado.acao = 9;
				transporteJogoTrancado.notificacao = "Jogo trancado!";
				enviaParaTodos(transporteJogoTrancado);
			} else {
				textArea.append(tr.jogador + " Passou a vez!\n");
				textArea.setCaretPosition(textArea.getText().length());

				tr.notificacao = tr.jogador + " Passou a vez!\n";
				setPassaVez(tr.idJogador - 1, true);
				setVez(tr.idJogador - 1);

				subBotoes_jogo_passvez(false, tr.jogador);
				subBotoes_jogo_passvez1(true, nome_cliente.size() - tr.idJogador);

				// VERIFICA NOVAMENTE SE O JOGO FICOU TRANCADO NA PASSAGEM DE JOGO
				if (jogoTrancado()) {
					textArea.append("Jogo Trancado ! Iniciando uma nova Partida \n");
					textArea.setCaretPosition(textArea.getText().length());
					UnisuamDomino_Movimento transporte4 = new UnisuamDomino_Movimento();
					transporte4.acao = 9;
					transporte4.notificacao = "Jogo Trancado, Iniciando uma nova Partida";
					enviaParaTodos(transporte4);
				}
			}			
		}
		
		//VERIFICA SOMATORIO DE PEÇAS DO JOGADOR E ATUALIZADA A MESA
		if (tr.acao == 7) {
			mapa.put(tr.idJogador, tr.somatorioJogador);
			mapa2.put(tr.somatorioJogador, tr.jogador);

			//IF VERIFICANDO AS PEÇAS DA MESA QUEM FICA COM O MENOR VALOR DE PEÇAS GANHA
			if (mapa.size() == nClientesConectados) {
				int menorValor = 1000;
				for (int i = 0; i < mapa.size(); i++) {
					if (menorValor > mapa.get(i + 1))
						menorValor = mapa.get(i + 1);
				}

				UnisuamDomino_Movimento transporteGanhadorJogoTrancado = new UnisuamDomino_Movimento();
				transporteGanhadorJogoTrancado.acao = 10;
				//transporteGanhadorJogoTrancado.notificacao = mapa2.get(menorValor) + " GANHOU !";
				transporteGanhadorJogoTrancado.notificacao = "-->>>Jogo Finalizado, Jogador " + mapa2.get(menorValor) + " GANHOU !";
				enviaParaTodos(transporteGanhadorJogoTrancado);
			}
		}
	}

	// ENVIA PARA CLIENTE A CLASS UNISUAMDOMINO ATUALIZADA 
	public void envia(UnisuamDomino_Movimento tr) throws RemoteException {
		for (int i = 0; i < nome_cliente.size(); i++) {

			if (tr.jogador.equals(nome_cliente.get(i))) {
				try {
					UnisuamDomino_ClienteItf cl = (UnisuamDomino_ClienteItf) Naming.lookup(nome_cliente.get(i));
					cl.recebe(tr);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}

	// ENVIA PARA TODOS OS CLIENTES A CLASS UNISUAMDOMINO ATUALIZADA
	public void enviaParaTodos(UnisuamDomino_Movimento tr) {

		for (int i = 0; i < nome_cliente.size(); i++) {
			try {
				UnisuamDomino_ClienteItf cl = (UnisuamDomino_ClienteItf) Naming.lookup(nome_cliente.get(i));
				cl.recebe(tr);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	//AÇÃO DO SERVIDOR PARA OS CLIENTES INFORMANDO VENCEDOR DA PARTIDA E RESETANDO A MESMA
	public void ganhouJogo(Boolean a, String user) throws RemoteException {
		UnisuamDomino_Movimento transporte4 = new UnisuamDomino_Movimento();
		transporte4.acao = 9;
		//transporte4.notificacao = "Jogo Finalizado, Jogador " + user + " Ganhou !";
		enviaParaTodos(transporte4);
		textArea.append(user + " Ganhou a partida !\n");
		textArea.setCaretPosition(textArea.getText().length());
	}

	//ATIVA BOTÃO DOS CLIENTES DE INICIAR A PARTIDA
	public void AtivarBotao(Boolean t) throws RemoteException {
		// TODO Auto-generated method stub
		for (int i = 0; i < nome_cliente.size(); i++) {
			try {
				UnisuamDomino_ClienteItf cl = (UnisuamDomino_ClienteItf) Naming.lookup(nome_cliente.get(i));
				cl.AtivarBotao(t);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	//ATIVA BOTÃO DOS CLIENTES AÓS INICIADA A PARTIDA
	public void subBotoes(Boolean t) throws RemoteException {
		// TODO Auto-generated method stub
		for (int i = 0; i < nome_cliente.size(); i++) {
			try {
				UnisuamDomino_ClienteItf cl = (UnisuamDomino_ClienteItf) Naming.lookup(nome_cliente.get(i));
				cl.AtivarBotao(t);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	//ENVIA PARA O CLIENTE ESPECIFICO COMANDO ATIVANDO OU DESATIVANDO BOTÕES
	public void subBotoes_jogo(Boolean t, String nome2) throws RemoteException {
		try {
			UnisuamDomino_ClienteItf cl = (UnisuamDomino_ClienteItf) Naming.lookup(nome2);
			cl.subBotoes_jogo(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//ENVIA PARA O CLIENTE ESPECIFICO COMANDO ATIVANDO OU DESATIVANDO BOTÕES
	public void subBotoes_jogo1(Boolean t, int nome1) throws RemoteException {
		try {
			UnisuamDomino_ClienteItf cl = (UnisuamDomino_ClienteItf) Naming.lookup(nome_cliente.get(nome1));
			cl.subBotoes_jogo(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//ENVIA PARA O CLIENTE ESPECIFICO COMANDO ATIVANDO OU DESATIVANDO BOTÕES
	public void subBotoes_jogo_passvez(Boolean t, String nome2) throws RemoteException {
		try {
			UnisuamDomino_ClienteItf cl = (UnisuamDomino_ClienteItf) Naming.lookup(nome2);
			cl.subBotoes_jogo_passvez(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//ENVIA PARA O CLIENTE ESPECIFICO COMANDO ATIVANDO OU DESATIVANDO BOTÕES
	public void subBotoes_jogo_passvez1(Boolean t, int nome1) throws RemoteException {
		try {
			UnisuamDomino_ClienteItf cl = (UnisuamDomino_ClienteItf) Naming.lookup(nome_cliente.get(nome1));
			cl.subBotoes_jogo_passvez(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// MAIN
	public static void main(String[] args) throws RemoteException {
		new UnisuamDomino_Servidor_Main();
	}

	// FIM DA CLASSE PRINCIPAL LEMBRAR DE NÃO EXCLUIR
	// \\\\\\\\\\\\\\\\\\\\GABRIEL ZAYAT SANT'ANNA & RAFAEL DE SOUZA ABREU
	// *********
}
