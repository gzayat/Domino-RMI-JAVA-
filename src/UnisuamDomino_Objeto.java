/**
 * Autores: Gabriel Zayat Sant'Anna e Rafael de Souza Abreu
 * Projeto UNISUAM SD
 */

import java.io.Serializable;

/*
 * DEFINIÇÃO DAS PEÇAS DO JOGO, JUNTO A LÓGICA DAS FACES EM LADOS OPOSTOS
 */

public class UnisuamDomino_Objeto implements Serializable{
	
	private static final long serialVersionUID = 1L;
	public int face1;
	public int face2;
	public boolean virada;
	public boolean carrocao;
	
	public UnisuamDomino_Objeto next;
	public UnisuamDomino_Objeto previous;
	
	public UnisuamDomino_Objeto(Integer face1, Integer face2) {
		this.face1 = face1;
		this.face2 = face2;
		this.virada = false;
		this.carrocao = (face1==face2)? true:false;
	}
	
	public int somaDosLados()
	{
		return ( face1 + face2 );
	}
	
	public void inverterPeca(){
		int temp = face1;
		face1 = face2;
		face2 = temp;
		virada = !virada;
	}

	@Override
	public String toString() {
		return face1+""+face2+";";
	}
	
	public static void main(String[] args) {
		//DEFINIÇÃO PARA O NUMERO DE INICIO DE PEÇAS PARA CADA ADVERSÁRIO 4 
		UnisuamDomino_Objeto peca1 = new UnisuamDomino_Objeto(4,4);		
		peca1.inverterPeca();		
	}

	@Override
	public boolean equals(Object arg0) {
		UnisuamDomino_Objeto pecaRecebida = (UnisuamDomino_Objeto)arg0;
		
		if(pecaRecebida != null && pecaRecebida instanceof UnisuamDomino_Objeto)
		{
			if((this.face1 == pecaRecebida.face1) && (this.face2 == pecaRecebida.face2)	)
			{
				return true;
			}
			
			if((this.face1 == pecaRecebida.face2) && (this.face2 == pecaRecebida.face1))
			{
				return true;
			}
		}	
		
		return  false;
	}
	
	
//FIM DA CLASSE PRINCIPAL LEMBRAR DE NÃO EXCLUIR  	 \\\\\\\\\\\\\\\\\\\\GABRIEL ZAYAT SANT'ANNA & RAFAEL DE SOUZA ABREU *********
}