import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class EncDec {

	private final SecretKeySpec secretKeySpec;
	private final Cipher cipher;
	private IvParameterSpec iv;

	public EncDec(Key key, byte[] iv) {
		try {
			this.secretKeySpec = new SecretKeySpec(key.getEncoded(), "AES");
			this.iv = new IvParameterSpec(iv);
			this.cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
		} catch (NoSuchAlgorithmException | NoSuchPaddingException e) {
			throw new RuntimeException(e);
		}
	}

	public byte[] getEncryptedMessage(byte[] message) {
		try {
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, iv);
			byte[] encryptedTextBytes = cipher.doFinal(message);
			return encryptedTextBytes;
		} catch (IllegalBlockSizeException | BadPaddingException
				| InvalidKeyException | InvalidAlgorithmParameterException e) {
			throw new RuntimeException(e);
		}
	}

	public byte[] getDecryptedMessage(byte[] message) {
		try {
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, iv);
			byte[] decryptedTextBytes = cipher.doFinal(message);
			return decryptedTextBytes;
		} catch (IllegalBlockSizeException | BadPaddingException
				| InvalidKeyException | InvalidAlgorithmParameterException e) {
			throw new RuntimeException(e);
		}
	}
}
