import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class UnisuamDomino_Criptografia {
	 static String IV = "AAAAAAAAAAAAAAAA"; 
	 
	public static byte[] encrypt(String textopuro, String chaveencriptacao) throws Exception { 
		Cipher encripta = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE"); 
		SecretKeySpec key = new SecretKeySpec(chaveencriptacao.getBytes("UTF-8"), "AES");
		encripta.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8"))); 
		return encripta.doFinal(textopuro.getBytes("UTF-8")); 
	} 
		
	public static String decrypt(byte[] textoencriptado, String chaveencriptacao) throws Exception{ 
		Cipher decripta = Cipher.getInstance("AES/CBC/PKCS5Padding", "SunJCE"); 
		SecretKeySpec key = new SecretKeySpec(chaveencriptacao.getBytes("UTF-8"), "AES"); 
		decripta.init(Cipher.DECRYPT_MODE, key,new IvParameterSpec(IV.getBytes("UTF-8"))); 
		return new String(decripta.doFinal(textoencriptado),"UTF-8"); 
	}
	
}
