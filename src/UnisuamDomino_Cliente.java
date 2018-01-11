
/**
 * Autores: Gabriel Zayat Sant'Anna e Rafael de Souza Abreu
 * Projeto UNISUAM SD
 */

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.net.InetAddress;
import java.rmi.Naming;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;


import javax.swing.ButtonGroup;
import javax.swing.DefaultComboBoxModel;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;



import java.awt.Toolkit;
import javax.swing.JSeparator;

public class UnisuamDomino_Cliente extends UnicastRemoteObject implements UnisuamDomino_ClienteItf {

	/**
	 * Autores: Gabriel Zayat Sant'Anna e Rafael de Souza Abreu Projeto UNISUAM
	 * SD
	 */
	private static final long serialVersionUID = 1L;
	
	/////////////// INICIO DECLARAÇÃO VARIÁVEIS GLOBAIS \\\\\\\\\\\\\\\
	public static final String[] local_rede = { "Local", "Rede", "Buscar_Automaticamente" };
	public String ipServidor, stringTabuleiro, nomeCliente,ipserver_unisuamdomino=null;
	private String keyUnisuam = "UNISUAM2015SDALE";
	byte[] textoCriptografia;
	public int idCliente;
	public static int quantidadeJogadores = 0;
	
	//DECLARAÇÃO DO FRAME E SEUS ITENS
	public JFrame frame,frame2;
	public JLabel label1, label2,label3,label4,label5;
	public JTextField campoEnvio;
	public JButton botaoEnvio, btComecarJogo, btPedirPeca, btEnviarPeca, btPassarVez;
	public JTextArea areaChat, areaStatusJogo;
	public JComboBox<Icon> combo;
	public DefaultComboBoxModel<Icon> model;
	public JRadioButton radioEsquerda, radioDireita;
	public ButtonGroup radioGroup;
	private JMenuBar bar;
	private JMenu menu, menu_ajuda;
	private JMenuItem itemSair, autores, regrasdojogo;
	private JSeparator separator;
	Boolean botaoglob=false;
	
	//FUNÇÃO DESIGN FRAME
	private GridBagLayout gridBag;
	private GridBagConstraints constraints;

	//DECLARAÇÃO DE FUNCIONALIDADES DO JOGO
	private AreaDesenhoTabuleiro desenho;
	public UnisuamDomino_Movimento transporte;
	List<UnisuamDomino_Objeto> listaPecasJogador;
	List<UnisuamDomino_Objeto> listaPecasTabuleiro;	
	UnisuamDomino_ServidorItf Inv;	
	
	/////////////// INICIO \\\\\\\\\\\\\\\
	public UnisuamDomino_Cliente() throws RemoteException {

		idCliente = ++quantidadeJogadores;
		System.out.println("Id Cliente: " + idCliente);

		/////////////// INICIO DO PROGRAMA SOLICITA INFORMAÇÕES AO JOGADOR
		/////////////// DO PARA QUE O JOGADOR NÃO DEIXE EM BRANCO O NOME OU
		/////////////// APENAS CLIQUE EM OK 
		do {
			this.nomeCliente = JOptionPane.showInputDialog("Digite o seu nome ou Apelido:", "Entre com o seu nome");
		} while (nomeCliente.equals("") | nomeCliente.equals("Entre com o seu nome"));

		/////////////// JOPTION PARA O CLIENTE ESCOLHER LOCAL DO SERVIDOR \\\\\\\\\\\\\\\
		String opcao = (String) JOptionPane.showInputDialog(frame, "Servidor Local ou na Rede ?\nSe não souber pode usar a opção de varrer o servidor na rede Automaticamente !", "Mapear Servidor",
															JOptionPane.QUESTION_MESSAGE, null, local_rede, local_rede[0]);

		/////////////// IF EM CONDIÇÃO A OPÇÃO DE LOCALIDADE DO SERVIDOR \\\\\\\\\\\\\\\
		try {
			InetAddress ip = InetAddress.getLocalHost();
			if (opcao == "Local")
				this.ipServidor = ip.getHostAddress(); 
			if(opcao == "Rede")
				this.ipServidor = JOptionPane.showInputDialog("Entre com o ip do Servidor:",
						"Informe o IP do Servidor");
			if(opcao == "Buscar_Automaticamente")
				this.ipServidor = encontrarServidor(); 
		} catch (Exception f) {
			JOptionPane.showMessageDialog(null,
					"Atenção: Não foi possível obter o IP da máquina, verifique se a mesma possui conexão. \n"
					+ f.getMessage(),"Problema na Conexão", JOptionPane.ERROR_MESSAGE);
			System.exit(0);
		}

		/////////////// INICIALIZAÇÃO DO FRAME PRINCIPAL \\\\\\\\\\\\\\\
		frame = new JFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("imagens/uni.jpg"));
		frame.setTitle("Unisuam Dominó -- CLIENTE -- | Jogador: " + nomeCliente);
		transporte = new UnisuamDomino_Movimento();
		transporte.jogador = nomeCliente;

		/////////////// CONSTRUÇÃO DAS OPÇÕES E DO MENU BAR \\\\\\\\\\\\\\\
		autores = new JMenuItem("Autores");
		autores.addActionListener(new MenuEvento());
		itemSair = new JMenuItem("Sair");
		itemSair.addActionListener(new MenuEvento());
		regrasdojogo = new JMenuItem("Regras do Jogo");
		regrasdojogo.addActionListener(new MenuEvento());

		/////////////// INICIALIZAÇÃO DOS SUBMENUS AUTORES/AJUDA/SAIR/REGRAS \\\\\\\\\\\\\\\
		menu = new JMenu("Arquivo");
		menu.add(autores);
		separator = new JSeparator();
		menu.add(separator);
		menu.add(itemSair);

		menu_ajuda = new JMenu("Ajuda");
		menu_ajuda.add(regrasdojogo);

		bar = new JMenuBar();
		bar.add(menu);
		bar.add(menu_ajuda);
		frame.setJMenuBar(bar);

		/////////////// GRID PARA DEFINIÇÕES DE TAMANHO DO LAYOUT \\\\\\\\\\\\\\\
		gridBag = new GridBagLayout();
		frame.getContentPane().setLayout(gridBag);
		constraints = new GridBagConstraints();

		/////////////// JLABEL'S
		/////// LABEL 1 -> Status da Partida
		label1 = new JLabel("Status da Partida", JLabel.CENTER);
		constraints.fill = GridBagConstraints.BOTH;
		addComponent(label1, 0, 0, 2, 1);

		/////// LABEL 2 -> Suas Peças
		label2 = new JLabel("Suas Peças", JLabel.CENTER);
		constraints.fill = GridBagConstraints.BOTH;
		addComponent(label2, 0, 2, 1, 1);
		
		/////// LABEL 3 -> PEÇAS
		label3 = new JLabel("PEÇAS", JLabel.CENTER);
		constraints.fill = GridBagConstraints.BOTH;
		addComponent(label3, 1, 2, 1, 4);
		
		/////// LABEL 4 -> JOGADOR
		label4 = new JLabel("JOGADOR:", JLabel.CENTER);
		constraints.fill = GridBagConstraints.BOTH;
		addComponent(label4, 1, 2, 1, 5);

		/////// LABEL 5 -> JOGADOR
		label5 = new JLabel("JOGADOR", JLabel.CENTER);
		constraints.fill = GridBagConstraints.BOTH;
		addComponent(label5, 1, 2, 1, 6);
			
		/////////////// AREA DE DIGITAÇÃO DO TEXTO CHAT \\\\\\\\\\\\\\\
		campoEnvio = new JTextField();
		constraints.weightx = 0.9;
		constraints.fill = GridBagConstraints.BOTH;
		addComponent(campoEnvio, 6, 0, 1, 1);
		campoEnvio.addKeyListener(new KeyListener(){		    
		    public void keyPressed(KeyEvent e){
		        if(e.getKeyCode() == KeyEvent.VK_ENTER){
		        botaoEnvio.doClick();
		        }
		    }		   
		    public void keyTyped(KeyEvent e) {
		    }		   
		    public void keyReleased(KeyEvent e) {
		    }
		});
		
		/////////////// INICIO DA DECLARAÇÃO DOS JBUTTON'S 
		/////// BOTÃOENVIO DE ENVIO DO CHAT -> ENVIAR
		botaoEnvio = new JButton("Enviar");
		botaoEnvio.addActionListener(new EnviaInformacao());
		constraints.weightx = 0.1;
		// constraints.weighty = 0.2;
		constraints.fill = GridBagConstraints.BOTH;
		addComponent(botaoEnvio, 6, 1, 1, 1);

		/////// BTCOMECARJOGO -> INICIAR PARTIDA
		btComecarJogo = new JButton("Iniciar Partida !");
		btComecarJogo.addActionListener(new EnviaInformacao());
		constraints.weightx = 0.2;
		// constraints.weighty = 0.2;
		constraints.fill = GridBagConstraints.HORIZONTAL;
		addComponent(btComecarJogo, 6, 2, 1, 1);
		btComecarJogo.setEnabled(false);

		/////// BTPEDIRPECA -> SOLICITAR PEÇA
		btPedirPeca = new JButton("Solicitar Peça");
		btPedirPeca.addActionListener(new EnviaInformacao());
		btPedirPeca.setEnabled(false);
		addComponent(btPedirPeca, 5, 2, 1, 1);

		/////// BOTÃO DE ENVIAR A PEÇA PARA A MESA
		btEnviarPeca = new JButton("Enviar Peça");
		btEnviarPeca.addActionListener(new EnviaInformacao());
		btEnviarPeca.setEnabled(false);
		constraints.fill = GridBagConstraints.BOTH;
		addComponent(btEnviarPeca, 4, 2, 1, 1);

		/////// BOTÃO DE PASSAR A VEZ
		btPassarVez = new JButton("Passar Vez");
		btPassarVez.setEnabled(false);
		btPassarVez.addActionListener(new EnviaInformacao());
		constraints.fill = GridBagConstraints.HORIZONTAL;
		addComponent(btPassarVez, 3, 2, 1, 1);

		/////////////// DEFINIÇÃO DA AREA DO CHAT \\\\\\\\\\\\\\\
		areaChat = new JTextArea();
		areaChat.setEditable(false);
		JScrollPane scrollAreaChat = new JScrollPane(areaChat);
		constraints.weightx = 0.8;
		constraints.weighty = 1.3;
		constraints.fill = GridBagConstraints.BOTH;
		addComponent(scrollAreaChat, 4, 0, 2, 2);

		/////////////// DEFINIÇÃO DA AREA DA MESA INTERNA \\\\\\\\\\\\\\\
		stringTabuleiro = "";
		desenho = new AreaDesenhoTabuleiro();
		desenho.setPreferredSize(new Dimension(8000, desenho.getWidth()));

		/////////////// DEFINIÇÃO DA SUBAREA JSCROLL \\\\\\\\\\\\\\\
		JScrollPane scrollDesenho = new JScrollPane(desenho);
		scrollDesenho.getHorizontalScrollBar().setValue(scrollDesenho.getHorizontalScrollBar().getValue() + 1);
		scrollDesenho.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

		/////////////// DEFINIÇÃO DA AREA DA MESA EXTERNA \\\\\\\\\\\\\\\
		areaStatusJogo = new JTextArea();
		areaStatusJogo.setEditable(false);
		constraints.weightx = 0.8;
		constraints.weighty = 1.6;
		constraints.fill = GridBagConstraints.BOTH;
		addComponent(scrollDesenho, 1, 0, 2, 3);

		/////////////// INICIO E CONSTRUÇÃO DO COMBOBOX, LOCAL ONDE FICA O DOMINO
		model = new DefaultComboBoxModel<Icon>();
		combo = new JComboBox<Icon>(model);
		combo.setEnabled(false);
		constraints.weightx = 0.2;
		// constraints.weighty = 0;
		constraints.fill = GridBagConstraints.BOTH;
		addComponent(combo, 1, 2, 1, 1);

		/////////////// RADIOBUTTON OPÇÃO ESQUERDA \\\\\\\\\\\\\\\
		radioEsquerda = new JRadioButton("Esquerda");
		radioEsquerda.setMnemonic(KeyEvent.VK_E);
		radioEsquerda.setSelected(true);
		radioEsquerda.setEnabled(false);

		/////////////// RADIOBUTTON OPÇÃO DIREITA \\\\\\\\\\\\\\\
		radioDireita = new JRadioButton("Direita");
		radioDireita.setMnemonic(KeyEvent.VK_D);
		radioDireita.setEnabled(false);

		/////////////// RADIOBUTTON GRUPO ADICIONANDO OPÇÕES DIREITA && ESQUERDA
		radioGroup = new ButtonGroup();
		radioGroup.add(radioEsquerda);
		radioGroup.add(radioDireita);

		/////////////// PAINEL DE SELEÇÃO RADIOBUTTON \\\\\\\\\\\\\\\
		JPanel painelRadioButton = new JPanel();
		painelRadioButton.setLayout(new FlowLayout());
		painelRadioButton.add(radioEsquerda);
		painelRadioButton.add(radioDireita);
		addComponent(painelRadioButton, 2, 2, 1, 1);

		/////////////// DEFINIÇÕES DO FRAME FINAL \\\\\\\\\\\\\\\
		frame.setBounds(400, 200, 954, 451);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.setResizable(false);

		/////////////// CHAMA A FUNÇÃO CONECTARAOSERVIDOR COM O NOME DO JOGADOR
		conectarAoServidor(nomeCliente);
	}

	/* FUNÇÃO PARA CONECTAR-SE AO SERVIDOR
	*  ENVIA COMO PARAMETRO DE IDENTIFICAÇÃO 
	*  O IP E O NOME DO CLIENTE PARA INTERFACE DO SERVIDOR
	* */
	public void conectarAoServidor(String nome) {
		try {
			//OBTER IP DA MÁQUINA
			InetAddress ip = InetAddress.getLocalHost();
			
			//SE O IP DO SERVIDOR FOR IGUAL AO DO CLIENTE ENTÃO NÃO ABRE A PORTA 1099
			//SE TENTAR ABRIR VAI APRESENTAR ERRO POR JÁ ESTÁ ABERTA
			//ABRIR PORTA NO CLIENTE NECESSARIO PARA IDENTIFICAR O CLIENTE NO SERVIDOR DE NOMES
			//ONDE O SERVIDOR PODE TER QUE ENVIAR INFORMAÇÕES AO CLIENTE.
			if (ipServidor != ip.getHostAddress())
				LocateRegistry.createRegistry(1099);

			//REGISTRA O CLIENTE USANDO O NOME DO JOGADOR PARA O SERVIÇO DE NOMES
			// CONECTA-SE AO SERVIDOR E CHAMA NA INTERFACE DO SERVIDOR A OPÇÃO DE LOGIN.
			Naming.rebind(nome, (Remote) this);
			Inv = (UnisuamDomino_ServidorItf) Naming.lookup("rmi://" + ipServidor + "/UnisuamDomino");
			nome = "rmi://" + ip.getHostAddress() + "/" + nome;
			this.nomeCliente = "rmi://" + ip.getHostAddress() + "/" + nomeCliente;
			Inv.login(nome);
		} catch (Exception e) {
			//NOVO TRY FOI INSERIDO EM CASO DE 2 CLIENTES RODAREM NA MESMA MÁQUINA 
			//E O SERVER EM MÁQUINA DIFERENTE, ASSIM AO ERRO DE PORTA JÁ ABERTA NO SEGUNDO CLIENTE
			//ENTÃO ELA TENTA REGISTRA NO SERVIÇO DE NOMES, CASO TENHA ERRO DENOVO ENTÃO PROBLEMA COM SERVER
			try {
				InetAddress ip = InetAddress.getLocalHost();
				Naming.rebind(nome, (Remote) this);
				Inv = (UnisuamDomino_ServidorItf) Naming.lookup("rmi://" + ipServidor + "/UnisuamDomino");
				nome = "rmi://" + ip.getHostAddress() + "/" + nome;
				this.nomeCliente = "rmi://" + ipServidor + "/" + nomeCliente;
				Inv.login(nome);
			} catch (Exception f) {
				JOptionPane.showMessageDialog(null,
						"Atenção: Erro na conexão com o Servidor, IP errado ou Servidor desligado. " + e.getMessage(),
						"Problema na Conexão", JOptionPane.ERROR_MESSAGE);
				System.exit(0);
			}
		}
	}

	/////////////// INICIO DA CLASSE ONDE CONTÉM AS AÇÕES PRINCIPAIS DO JOGO
	/////////////// AÇÕES ESSAS QUE SÃO ENVIADAS PARA O SERVIDOR
	private class EnviaInformacao implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			
			/////// OPÇÃO 1: AÇÃO AO CLICAR NO BOTÃO INICIAR A PARTIDA
			if (e.getSource() == btComecarJogo) {				
				UnisuamDomino_Movimento t1 = new UnisuamDomino_Movimento();
				t1.acao = 1;
				t1.jogador = nomeCliente;
				t1.idJogador = idCliente;
				try {
					Inv.recebeTransporte(t1);
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}

			/////// OPÇÃO 2: AÇÃO AO CLICAR NO BOTÃO ENVIAR PEÇA PARA A MESA
			if (e.getSource() == btEnviarPeca) {
				int indiceDaPeca = combo.getSelectedIndex();
				UnisuamDomino_Movimento transporte2 = new UnisuamDomino_Movimento();
				
				transporte2.jogador = nomeCliente;
				transporte2.peca = listaPecasJogador.get(indiceDaPeca);
				transporte2.acao = 2;
				transporte2.idJogador = idCliente;

				boolean esq = radioGroup.isSelected(radioEsquerda.getModel());
				boolean dir = radioGroup.isSelected(radioDireita.getModel());

				if (dir)
					transporte2.lado = UnisuamDomino_Lados.DIREITA;

				if (esq)
					transporte2.lado = UnisuamDomino_Lados.ESQUERDA;

				try {
					Inv.recebeTransporte(transporte2);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

			/////// OPÇÃO 3: AÇÃO AO CLICAR NO BOTÃO DE SOLICITAR PEÇA AO SERVIDOR
			if (e.getSource() == btPedirPeca) {
				UnisuamDomino_Movimento transporte3 = new UnisuamDomino_Movimento();
				transporte3.acao = 3;
				transporte3.idJogador = idCliente;
				transporte3.jogador = nomeCliente;
				
				//textoCriptografia = UnisuamDomino_Criptografia.encrypt(campoEnvio.getText(), keyUnisuam);
						
				
				try {
					Inv.recebeTransporte(transporte3);
					UnisuamDomino_Movimento t4 = new UnisuamDomino_Movimento();
					t4.acao = 4;
					t4.jogador = nomeCliente;
					t4.idJogador = idCliente;
					//t4.textoDoChat = "SISTEMA: " + nomeCliente + " pediu uma peça ao Servidor.\n";
					textoCriptografia = UnisuamDomino_Criptografia.encrypt("Solicito peça do Servidor.", keyUnisuam);
					t4.cript = textoCriptografia;
					Inv.recebeTransporte(t4);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

			/////// OPÇÃO 4: AÇÃO AO CLICAR NO BOTÃO DE ENVIAR TEXTO PARA O CHAT
			if (e.getSource() == botaoEnvio) {
				UnisuamDomino_Movimento t4 = new UnisuamDomino_Movimento();
				t4.acao = 4;
				t4.jogador = nomeCliente;
				try {
					textoCriptografia = UnisuamDomino_Criptografia.encrypt(campoEnvio.getText(), keyUnisuam);
				} catch (Exception e2) {
					// TODO Auto-generated catch block
					e2.printStackTrace();
				}			
				t4.cript = textoCriptografia;
				campoEnvio.setText("");
				campoEnvio.requestFocus();
				try {
					Inv.recebeTransporte(t4);
				} catch (RemoteException e1) {
					e1.printStackTrace();
				}
			}

			/////// OPÇÃO 6: PASSAR A VEZ !
			if (e.getSource() == btPassarVez) {
				UnisuamDomino_Movimento t6 = new UnisuamDomino_Movimento();
				t6.idJogador = idCliente;
				t6.jogador = nomeCliente;
				t6.acao = 6;
				try {
					Inv.recebeTransporte(t6);
					UnisuamDomino_Movimento t4 = new UnisuamDomino_Movimento();
					t4.acao = 4;
					t4.jogador = nomeCliente;
					//t4.textoDoChat = "Passou a vez.\n";
					textoCriptografia = UnisuamDomino_Criptografia.encrypt("Passei a vez !", keyUnisuam);
					t4.cript = textoCriptografia;
					Inv.recebeTransporte(t4);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}
		}
	}

	///////////////FUNÇÃO PARA VERIFICAR AS PEÇAS DO JOGADOR, FAZ SOMA E RETORNA O VALOR TOTAL
	public int obtemSomatorioDePecas() {
		int resultado = 0;

		for (int cont = 0; cont < listaPecasJogador.size(); cont++) {
			resultado = resultado + listaPecasJogador.get(cont).somaDosLados();
		}
		return resultado;
	}

	/////////////// FUNÇÃO APÓS TERMINAR A PARTIDA AÇÕES PARA DEIXAR PRONTO PARA UM NOVO JOGO
	public void finalizaJogo() {
		listaPecasJogador.removeAll(listaPecasJogador);
		btComecarJogo.setEnabled(true);
		btEnviarPeca.setEnabled(false);
		btPassarVez.setEnabled(false);
		btPedirPeca.setEnabled(false);
		model.removeAllElements();
		combo.removeAllItems();
	}

	/////////////// ATUALIZAR COMBOBOX DO JOGADOR COM PEÇAS \\\\\\\\\\\\\\\\\\\
	public void atualizarCombo() {
		combo.setEnabled(true);
		removePecasCombo();
		for (int i = 0; i < listaPecasJogador.size(); i++) {
			UnisuamDomino_Objeto p = listaPecasJogador.get(i);
			Icon imagem = new ImageIcon("imagens/" + p.face1 + "" + p.face2 + ".jpg");
			// model.addElement(p.toString());
			model.addElement(imagem);
		}
	}

	/////////////// REMOVER PEÇAS DO COMBOBOX \\\\\\\\\\\\\\\\\\\
	public void removePecasCombo() {
		model.removeAllElements();
	}

	/////////////// AÇÕES DO MENU DO JOGO, INFORMAÇÕES DOS DESENVOLVEDORES,
	/////////////// REGRA DO JOGO E SAIR
	private class MenuEvento implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent e) {
			if (e.getSource() == autores)
				JOptionPane.showMessageDialog(null, "Desenvolvedores:\n" + "Gabriel Zayat Sant'Anna\nRafael de Souza Abreu" +
							"\n\nJogo Desenvolvido visando o Curso de Sistemas Distribuidos,\nutilizando o método de invocação Remota(RMI), também utilizando\na interface gráfica no Cliente e Servidor.\n\nAgradecimento em especial ao Professor Alex Avellar.","Desenvolvedores do Jogo",JOptionPane.ERROR_MESSAGE,new ImageIcon("imagens/uni.jpg"));

			if (e.getSource() == itemSair)
				System.exit(0);

			if (e.getSource() == regrasdojogo)
				JOptionPane.showMessageDialog(null,
						"/\\ Regras para jogar o DominoUnisuam\n " + "-> 2 Jogadores. \n"
								+ "-> Peças - 28 peças com lados variando de 0 a 6.\n"
								+ "-> Distribuição - 3 peças iniciais para cada participante. \n"
								+ "-> Objetivo - não ficar com nenhuma peça.\n\n" + "/\\ Definições\n"
								+ "->Peça de dominó - é uma peça composta por duas pontas, cada uma com um número (exemplos de peças: 2-5, 6-6, 0-1).\n"
								+ "->Encaixar peça - quando uma peça é colocada ao lado de outra que tem pelo menos um número em comum (exemplo: 2-5 encaixa com 5-6).\n"
								+ "->Extremidades do jogo - são as peças livres da ponta, cujos lados estão em aberto para que outras peças sejam encaixadas.\n"
								+ "->Se O jogador não tiver a peça, solicitar ao servidor. \n"
								+ "->Passar a vez - quando o jogador não tem nenhuma peça que encaixe em qualquer extremidade e quando a peça do servidor tenha acabado.\n"
								+ "->Jogo trancado - quando nenhum jogador possui alguma peça que encaixe em qualquer extremidade.\n"
								+ "->Bater o jogo - quando um dos jogadores consegue ficar sem peças na mão, tendo encaixado todas elas.\n\n\n"
								+ "/\\ Obrigado por jogar nosso game, feito pelo projeto de SD Unisuam, Prof: Alex Avellar. \n"
								+ "/\\ Alunos desenvolvedores: Gabriel Zayat Sant'Anna && Rafael de Souza Abreu ",
						"Regra do Unisuam Domino",JOptionPane.ERROR_MESSAGE,new ImageIcon("imagens/regras.jpg"));
		}
	}

	/////////////// ADDCOMPONENT FUNÇÃO COM O DESIGN DO GAME \\\\\\\\\\\\\\\\\\\
	private void addComponent(Component component, int linha, int coluna, int width, int height) {
		constraints.gridy = linha;
		constraints.gridx = coluna;
		constraints.gridwidth = width;
		constraints.gridheight = height;
		constraints.insets = new Insets(2, 2, 2, 2);
		gridBag.setConstraints(component, constraints);
		frame.getContentPane().add(component);
	}

	/* ÁREA DE DESENHO DA MESA ONDE O NÚMERO DO DOMINO É
	* TRANSFORMADO EM IMAGEM E TAMBÉM É DADO FORMA A MESA
	* */
	private class AreaDesenhoTabuleiro extends JPanel {
		private static final long serialVersionUID = 1L;

		@Override
		public void paint(Graphics arg) {
			Graphics2D g = (Graphics2D) arg;
			g.clearRect(0, 0, this.getWidth(), this.getHeight());
			Color c = new Color(33, 84, 33);
			g.setColor(c);
			g.fillRect(0, 0, this.getWidth(), this.getHeight());
			String array[] = stringTabuleiro.split(";");
			
			Image imagem5 = new ImageIcon("imagens/mes.gif").getImage();
			g.drawImage(imagem5, 55, 1, this);
			
			
			int[] imagensgif = new int[] { 00, 11, 22, 33, 44, 55, 66 };
			Image imagem2, imagem3 = new ImageIcon("imagens_mesa/00.png").getImage();

			for (int i = 0; i < array.length; i++) {
				// System.out.print(array[i] + " ");
				Image imagem = new ImageIcon("imagens_mesa/" + array[i].trim() + ".png").getImage();
				for (int r = 0; r < imagensgif.length; r++) {
					imagem2 = new ImageIcon("imagens_mesa/" + imagensgif[r] + ".png").getImage();
					if (imagem == imagem2)
						imagem = new ImageIcon("imagens_mesa/" + array[i].trim() + ".gif").getImage();
					if (imagem == imagem3)
						imagem = new ImageIcon("imagens_mesa/" + array[i].trim() + ".gif").getImage();
				}
				g.drawImage(imagem, (i * 52), this.getHeight() / 2, this);
			}
			g.dispose();
		}
	}
	
	/* RECEBER COMANDOS VINDOS DO SERVIDOR
	* NORMALMENTE UMA AÇÃO DO CLIENTE ENVIANDO PARA O SERVIDOR
	* EXISTE UM RETORNO DO SERVIDOR INFORMANDO TALVEZ A PRÓXIMA
	* AÇÃO DADA AQUELA DETERMINADA JOGADA
	*/
	public void recebe(UnisuamDomino_Movimento t) throws RemoteException {

		/////////////// OPÇÃO RECEBIDA 1 - JOGADOR RECEBE AS PEÇAS INICIAIS DO JOGO
		if (t.acao == 1) {
			
			listaPecasJogador = t.pecasDoCliente;
			
			areaChat.append("SISTEMA: Peças criptografadas Recebidas do Servidor: \n");
			areaChat.setCaretPosition(areaChat.getText().length());
			for (int i=0; i<t.cript.length; i++){
				areaChat.append("" + new Integer(t.cript[i]));
				areaChat.setCaretPosition(areaChat.getText().length());
			}
			
			areaChat.append("\n");
		
			try {
				areaChat.append(UnisuamDomino_Criptografia.decrypt(t.cript, keyUnisuam));
				areaChat.setCaretPosition(areaChat.getText().length());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			atualizarCombo();
			btEnviarPeca.setEnabled(true);
			radioDireita.setEnabled(true);
			btPedirPeca.setEnabled(true);
			radioEsquerda.setEnabled(true);
			btComecarJogo.setEnabled(false);
			label4.setText(t.jogador + ":" + t.pecasDoCliente.size());
			label5.setText(t.adversario);
		}

		/////////////// OPÇÃO RECEBIDA 2 - JOGADOR RECEBE TEXTO DO CHAT CRIPTOGRAFADO E DESCRIPTOGRAFA
		if (t.acao == 2) {	
			
			try {
				areaChat.append(t.jogador + " enviou->> " + UnisuamDomino_Criptografia.decrypt(t.cript, keyUnisuam) + "\n");
				areaChat.setCaretPosition(areaChat.getText().length());
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}			
		}

		/////////////// OPÇÃO RECEBIDA 3 - JOGADA FOI REALIZADA
		if (t.acao == 3) {				
			if (t.jogadaFeita == true) {
				for (int i = 0; i < listaPecasJogador.size(); i++) {
					if (t.peca.equals(listaPecasJogador.get(i))) {
						listaPecasJogador.remove(i);
						model.removeElementAt(i);
						atualizarCombo();
						break;
					}
				}
				//label4.setText(t.jogador + ":" + listaPecasJogador.size());
				if(t.adversariob!=null){
					try {
						label4.setText(UnisuamDomino_Criptografia.decrypt(t.adversariob, keyUnisuam));
						label5.setText(UnisuamDomino_Criptografia.decrypt(t.adversariob1, keyUnisuam));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				
			/*	label4.setText(t.adversario1 );
				label5.setText(t.adversario );*/
				
			} else {
				areaChat.append("SISTEMA: " + nomeCliente
						+ " Peça não é permitida, tente outra ou solicite peça ao servidor !\n");
				areaChat.setCaretPosition(areaChat.getText().length());
			}
			
			/*SE PEÇAS DO JOGADOR IGUAL A 0, ENVIA PARA O SERVIDOR QUE GANHOU
			 *SERVIDOR ENVIA PARA OS JOGADORES O VENCEDOR E ACIONA PROCEDIMENTOS
			 *DO SERVIDOR INICIANDO UMA NOVA PARTIDA.
			*/
			if (listaPecasJogador.size() == 0) {
				try {
					// configura transporte
					UnisuamDomino_Movimento transporte2 = new UnisuamDomino_Movimento();
					transporte2.jogador = nomeCliente;
					transporte2.acao = 5;					
					Inv.ganhouJogo(true, nomeCliente);					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}

		/////////////// OPÇÃO RECEBIDA 4 - CLIENTES RECEBEM ATUALIZAÇÃO DA MESA
		if (t.acao == 4) {
			try {
				areaChat.append(UnisuamDomino_Criptografia.decrypt(t.cript, keyUnisuam));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			areaChat.setCaretPosition(areaChat.getText().length());
			areaStatusJogo.setText(t.tabuleiro);
			areaStatusJogo.setCaretPosition(areaStatusJogo.getText().length());
			stringTabuleiro = t.tabuleiro;
			desenho.setSize(desenho.getWidth() + 80, desenho.getHeight());
			desenho.repaint();
			
			if(t.adversariob!=null){
				try {
					label4.setText(UnisuamDomino_Criptografia.decrypt(t.adversariob, keyUnisuam));
					label5.setText(UnisuamDomino_Criptografia.decrypt(t.adversariob1, keyUnisuam));
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			//label5.setText(t.adversario);
			//label4.setText(t.adversario1 );
		}

		/////////////// OPÇÃO RECEBIDA 5 - PEÇA RECEBIDA COMO RESPOSTA AO PEDIDO DE PEÇA
		if (t.acao == 5) {
			if (t.peca != null) {
				listaPecasJogador.add(t.peca);
				atualizarCombo();
				
			} else {
				try {
					UnisuamDomino_Movimento t4 = new UnisuamDomino_Movimento();
					t4.acao = 4;
					t4.jogador = nomeCliente;
					//t4.textoDoChat = "/\\/\\/\\/\\/\\/\\/\\/\\ ATENÇÃO: Não há mais peça disponível.\n";
					textoCriptografia = UnisuamDomino_Criptografia.encrypt("/\\/\\/\\/\\/\\/\\/\\/\\ ATENÇÃO: Não há mais peça disponível.", keyUnisuam);
					t4.cript = textoCriptografia;
					Inv.recebeTransporte(t4);
				} catch (Exception e1) {
					e1.printStackTrace();
				}
				btPassarVez.setEnabled(true);
				btPedirPeca.setEnabled(false);
			}
		}

		/////////////// OPÇÃO RECEBIDA 6 - NOTIFICAÇÃO DE QUE NÃO É A VEZ DO JOGADOR
		if (t.acao == 6) {
			areaChat.append(t.notificacao + "\n");
			areaChat.setCaretPosition(areaChat.getText().length());
		}

		/////////////// OPÇÃO RECEBIDA 9 - NOTIFICAÇÃO DE QUE JOGO ESTÁ TRANCADO
		if (t.acao == 9) {			
			try {
				// configura transporte
				UnisuamDomino_Movimento transporteSomatorioDasPecas = new UnisuamDomino_Movimento();
				transporteSomatorioDasPecas.jogador = nomeCliente;
				transporteSomatorioDasPecas.idJogador = idCliente;
				transporteSomatorioDasPecas.somatorioJogador = obtemSomatorioDePecas();
				transporteSomatorioDasPecas.acao = 7;// Cliente informando o
														// somatório das peças
				// envia
				Inv.recebeTransporte(transporteSomatorioDasPecas);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		/////////////// OPÇÃO RECEBIDA 10 - NOTIFICAÇÃO DE QUE ALGUÉM GANHOU
		/////////////// QUANDO O JOGO ESTÁ TRANCADO \\\\\\\\\\\
		if (t.acao == 10) {
			areaChat.append(t.notificacao + "\n");
			areaChat.setCaretPosition(areaChat.getText().length());
			finalizaJogo();
		}

		/////////////// OPÇÃO RECEBIDA 15 - NOTIFICA QUE ESTÁ LOGADO E GERA ID
		if (t.acao == 15) {
			this.idCliente = t.idJogador;
			areaChat.append("Conectado ao UnisuamDomino e registrado com a ID = " + this.idCliente + ".\n");
			areaChat.setCaretPosition(areaChat.getText().length());
		}
		if (t.acao == 16)
			System.out.println(t.notificacao);
		
		if(t.acao == 25){
			/*label5.setText(t.adversario);
			label4.setText(t.adversario1 );*/
			try {
				label4.setText(UnisuamDomino_Criptografia.decrypt(t.adversariob, keyUnisuam));
				label5.setText(UnisuamDomino_Criptografia.decrypt(t.adversariob1, keyUnisuam));
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				
			}
			

		}
	}
	
	//COMANDO VINDO DO SERVIDOR, PARA ATIVAR BOTÃO INICIAR PARTIDA
	public void AtivarBotao(Boolean recebe) throws RemoteException {
		// TODO Auto-generated method stub
		btComecarJogo.setEnabled(recebe);
	}
	
	public byte[] pontuacao() throws RemoteException, Exception {
		// TODO Auto-generated method stub
		byte[] teste1 ;
		String str = Integer.toString (listaPecasJogador.size());
		//UnisuamDomino_Movimento t = new UnisuamDomino_Movimento();
		
		
			teste1 = UnisuamDomino_Criptografia.encrypt(str, keyUnisuam);
			return teste1;
		
		
		
		
	}
	
	//COMANDO VINDO DO SERVIDOR A PARTIR DE INICIADA A PARTIDA PARA ATIVAR BOTÕES DO JOGO
	public void subBotoes(Boolean recebe)throws RemoteException{
		btEnviarPeca.setEnabled(recebe);
		combo.setEnabled(recebe);
		radioEsquerda.setEnabled(recebe);
		radioDireita.setEnabled(recebe);
		combo.setEnabled(recebe);
	}
	
	//COMANDO VINDO DO SERVIDOR EM VEZ DO JOGADOR ATIVANDO OU DESATIVANDO
	public void subBotoes_jogo(Boolean recebe)throws RemoteException{
		btEnviarPeca.setEnabled(recebe);		
		btPedirPeca.setEnabled(recebe);		
		
		if(botaoglob == true){
			btPedirPeca.setEnabled(false);
			btPassarVez.setEnabled(recebe);
		}
			
	}
	
	//COMANDO VINDO DO SERVIDOR EM VEZ DO JOGADOR ATIVANDO OU DESATIVANDO PASSVEZ
	public void subBotoes_jogo_passvez(Boolean recebe)throws RemoteException{
		btEnviarPeca.setEnabled(recebe);		
		btPedirPeca.setEnabled(false);
		btPassarVez.setEnabled(recebe);
		botaoglob= true;		
	}
		 
	/////////////// FUNÇÃO DESCOBRIR SERVIDOR NA REDE
	public String encontrarServidor() throws IOException {		
		new Thread(t1).start(); 
		new Thread(t2).start(); 
		new Thread(t3).start(); 
		new Thread(t4).start(); 
		new Thread(t5).start(); 
		new Thread(t6).start(); 
		new Thread(t7).start(); 
		new Thread(t8).start(); 
		new Thread(t9).start(); 
		new Thread(t10).start(); 		
		
		int u=1;			
		
		frame2 = new JFrame();
		frame2.setIconImage(Toolkit.getDefaultToolkit().getImage("imagens/uni.jpg"));
		frame2.setTitle("Procurando...");
		
		final JProgressBar pb = new JProgressBar();
	    pb.setMinimum(0);
	    pb.setMaximum(500);	        
	    pb.setStringPainted(true);
	        
	    frame2.getContentPane().setLayout(new FlowLayout());
	    frame2.getContentPane().add(pb);
	    frame2.setSize(300, 200);
	    frame2.setVisible(true);
		frame2.setBounds(400, 200, 250, 100);
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame2.setVisible(true);
		frame2.setResizable(false);
		
		do{		
			final int currentValue = u++;
			if(currentValue == 700)
				ipserver_unisuamdomino = "127.0.0.1";			
			SwingUtilities.invokeLater(new Runnable() {
                 public void run() {
                     pb.setValue(currentValue);
                 }
             });
             try {
				java.lang.Thread.sleep(100);
			} catch (InterruptedException e1) { }			
		}while(ipserver_unisuamdomino == null);
		
		frame2.dispose();
		return ipserver_unisuamdomino;
	}
	
	 private  Runnable t1 = new Runnable() { 
		 	public void run() { 
					String gateway;				
					InetAddress ip;					
					try{ 
						ip = InetAddress.getLocalHost();
						gateway = ip.getHostAddress(); 	
						StringBuilder b = new StringBuilder(gateway);
						b.replace(gateway.lastIndexOf("."), gateway.lastIndexOf("") + 1, ".1" );
						gateway= b.toString();
							
						for(int i=0; i<=25; i++) {
							if(i>= 0 && i<=10){
								gateway = gateway.substring(0, gateway.length() - 1);										
								gateway = gateway + i;																		
							}else{								 
								gateway = gateway.substring(0, gateway.length() - 2);											
								gateway = gateway + i;																				
							}	
							if(InetAddress.getByName(gateway).isReachable(1100)) {							 
								try {											
									Inv = (UnisuamDomino_ServidorItf) Naming.lookup("rmi://" + gateway + "/UnisuamDomino");
									ipserver_unisuamdomino = gateway;
									break;	
								}catch (Exception e) {	}
							}					
								 System.out.println(gateway);
						 }
					}catch (Exception e){}				 
				} 
		}; 
	 
	 private  Runnable t2 = new Runnable() { 
		 public void run() {
			 String gateway;				
				InetAddress ip;
				try{ 
					ip = InetAddress.getLocalHost();
					gateway = ip.getHostAddress(); 	
					StringBuilder b = new StringBuilder(gateway);
					b.replace(gateway.lastIndexOf("."), gateway.lastIndexOf("") + 1, ".26" );
					gateway= b.toString();
						
					for(int i=26; i<=50; i++) {						
						if(i>= 0 && i<=10){								 	
							gateway = gateway.substring(0, gateway.length() - 1);								
							gateway = gateway + i;										
						}else{								 
							gateway = gateway.substring(0, gateway.length() - 2);								
							gateway = gateway + i;	
						}					 
						if(InetAddress.getByName(gateway).isReachable(1100)) {							 
							try {											
								Inv = (UnisuamDomino_ServidorItf) Naming.lookup("rmi://" + gateway + "/UnisuamDomino");
								ipserver_unisuamdomino = gateway;
								break;										
							}catch (Exception e) {	}
						}
							 System.out.println(gateway);
					 }							 
				 }catch (Exception e){}
			 } 
	 };
	 
	 private Runnable t3 = new Runnable() { 
		 public void run() {
			 	String gateway;				
				InetAddress ip;
				try{ 
					ip = InetAddress.getLocalHost();
					gateway = ip.getHostAddress(); 	
					StringBuilder b = new StringBuilder(gateway);
					b.replace(gateway.lastIndexOf("."), gateway.lastIndexOf("") + 1, ".51" );
					gateway= b.toString();
						
					 for(int i=51; i<=75; i++) {			 
						gateway = gateway.substring(0, gateway.length() - 2);									
						gateway = gateway + i;										
							 				
						if(InetAddress.getByName(gateway).isReachable(1100)) {							 
							try {											
								Inv = (UnisuamDomino_ServidorItf) Naming.lookup("rmi://" + gateway + "/UnisuamDomino");
								ipserver_unisuamdomino = gateway;
								break;										
							}catch (Exception e) {	}
						}
							 System.out.println(gateway);
					 }
				 }catch (Exception e){}
			 } 
	 };
	 
	 private  Runnable t4 = new Runnable() { 
		 public void run() {
			 	String gateway;				
				InetAddress ip;				
				
				try{ 
					ip = InetAddress.getLocalHost();
					gateway = ip.getHostAddress(); 	
					StringBuilder b = new StringBuilder(gateway);
					b.replace(gateway.lastIndexOf("."), gateway.lastIndexOf("") + 1, ".76" );
					gateway= b.toString();
						
					for(int i=76; i<=100; i++) {
						gateway = gateway.substring(0, gateway.length() - 2);										
						gateway = gateway + i;										
							 				 
						if(InetAddress.getByName(gateway).isReachable(1100)) {							 
							try {											
								Inv = (UnisuamDomino_ServidorItf) Naming.lookup("rmi://" + gateway + "/UnisuamDomino");
								ipserver_unisuamdomino = gateway;
								break;										
							}catch (Exception e) {}
						}		
							 System.out.println(gateway);
					 }
				 }catch (Exception e){}
			 } 
	 };
	 
	 private Runnable t5 = new Runnable() { 
		 public void run() {
			 	String gateway;			
				InetAddress ip;				
				try{ 
					ip = InetAddress.getLocalHost();
					gateway = ip.getHostAddress(); 	
					StringBuilder b = new StringBuilder(gateway);
					b.replace(gateway.lastIndexOf("."), gateway.lastIndexOf("") + 1, ".101" );
					gateway= b.toString();
						
					for(int i=101; i<=125; i++) {	
						gateway = gateway.substring(0, gateway.length() - 3);										
						gateway = gateway + i;										
							 	
						if(InetAddress.getByName(gateway).isReachable(1100)) {							 
							try {											
								Inv = (UnisuamDomino_ServidorItf) Naming.lookup("rmi://" + gateway + "/UnisuamDomino");
								ipserver_unisuamdomino = gateway;
								break;
							}catch(Exception e) {	}
						}	
							 System.out.println(gateway);
					 }
				 }catch (Exception e){}
			 } 
	 };
	
	 private Runnable t6 = new Runnable() { 
		 public void run() {
			 	String gateway;			
				InetAddress ip;				
				try{ 
					ip = InetAddress.getLocalHost();
					gateway = ip.getHostAddress(); 	
					StringBuilder b = new StringBuilder(gateway);
					b.replace(gateway.lastIndexOf("."), gateway.lastIndexOf("") + 1, ".126" );
					gateway= b.toString();
						
					for(int i=126; i<=150; i++) {	 
						gateway = gateway.substring(0, gateway.length() - 3);										
						gateway = gateway + i;										
							 	
						if(InetAddress.getByName(gateway).isReachable(1100)) {							 
							try {											
								Inv = (UnisuamDomino_ServidorItf) Naming.lookup("rmi://" + gateway + "/UnisuamDomino");
								ipserver_unisuamdomino = gateway;
								break;										
							}catch (Exception e) {	}
						}	
							 System.out.println(gateway);
					 }
				 }catch (Exception e){}
			 } 
	 };
	
	 private Runnable t7 = new Runnable() { 
		 public void run() {
			String gateway;			
			InetAddress ip;	
				
				try{ 
					ip = InetAddress.getLocalHost();
					gateway = ip.getHostAddress(); 	
					StringBuilder b = new StringBuilder(gateway);
					b.replace(gateway.lastIndexOf("."), gateway.lastIndexOf("") + 1, ".151" );
					gateway= b.toString();
					
					for(int i=151; i<=175; i++) {	
						gateway = gateway.substring(0, gateway.length() - 3);										
						gateway = gateway + i;										
						
						if(InetAddress.getByName(gateway).isReachable(1100)) {							 
							try {											
								Inv = (UnisuamDomino_ServidorItf) Naming.lookup("rmi://" + gateway + "/UnisuamDomino");
								ipserver_unisuamdomino = gateway;
								break;										
							}catch (Exception e) {	}
						}	
							 System.out.println(gateway);
					 }
				 }catch (Exception e){}
			 } 
	 };
	
	 private Runnable t8 = new Runnable() { 
		 public void run() {
			 String gateway;			
			 InetAddress ip;			 
				try{ 
					ip = InetAddress.getLocalHost();
					gateway = ip.getHostAddress(); 	
					StringBuilder b = new StringBuilder(gateway);
					b.replace(gateway.lastIndexOf("."), gateway.lastIndexOf("") + 1, ".176" );
					gateway= b.toString();
						
					 for(int i=176; i<=200; i++) {				 
						gateway = gateway.substring(0, gateway.length() - 3);										
						gateway = gateway + i;										
							
						if(InetAddress.getByName(gateway).isReachable(1100)) {							 
							try {											
								Inv = (UnisuamDomino_ServidorItf) Naming.lookup("rmi://" + gateway + "/UnisuamDomino");
								ipserver_unisuamdomino = gateway;
								break;										
							}catch (Exception e) {	}
						}
							 System.out.println(gateway);
					 }
				 }catch (Exception e){}
			 } 
	 };
	
	
	 private Runnable t9 = new Runnable() { 
		 public void run() {
			 String gateway;			
				InetAddress ip;				
				try{ 
					 ip = InetAddress.getLocalHost();
						gateway = ip.getHostAddress(); 	
						StringBuilder b = new StringBuilder(gateway);
						b.replace(gateway.lastIndexOf("."), gateway.lastIndexOf("") + 1, ".201" );
						gateway= b.toString();
						
					 for(int i=201; i<=225; i++) {	
						gateway = gateway.substring(0, gateway.length() - 3);										
						gateway = gateway + i;										
							 
						if(InetAddress.getByName(gateway).isReachable(1100)) {							 
							try {											
								Inv = (UnisuamDomino_ServidorItf) Naming.lookup("rmi://" + gateway + "/UnisuamDomino");
								ipserver_unisuamdomino = gateway;
								break;										
							} catch (Exception e) {	}
						}	
							 System.out.println(gateway);
					 }
				 }catch (Exception e){}
			 } 
	 };

	 private Runnable t10 = new Runnable() { 
		 public void run() {
			 String gateway;			
			 InetAddress ip;				
				try{ 
					ip = InetAddress.getLocalHost();
					gateway = ip.getHostAddress(); 	
					StringBuilder b = new StringBuilder(gateway);
					b.replace(gateway.lastIndexOf("."), gateway.lastIndexOf("") + 1, ".226" );
					gateway= b.toString();
						
					 for(int i=226; i<=255; i++) {
						gateway = gateway.substring(0, gateway.length() - 3);										
						gateway = gateway + i;										
							 
						if(InetAddress.getByName(gateway).isReachable(1100)) {							 
							try {											
								Inv = (UnisuamDomino_ServidorItf) Naming.lookup("rmi://" + gateway + "/UnisuamDomino");
								ipserver_unisuamdomino = gateway;
								break;										
							}catch (Exception e) {	}
						}	
							 System.out.println(gateway);
					 }
				 }catch (Exception e){}
			 } 
	 };
	
	
	
	
	/////////////// MAIN E INICIO DO CLIENTE \\\\\\\\\\\\\\\\\\\
	public static void main(String[] args) throws RemoteException {
		new UnisuamDomino_Cliente();
	}

	// FIM DA CLASSE PRINCIPAL LEMBRAR DE NÃO EXCLUIR
	// \\\\\\\\\\\\\\\\\\\\GABRIEL ZAYAT SANT'ANNA & RAFAEL DE SOUZA ABREU ***********************
	// *********
}