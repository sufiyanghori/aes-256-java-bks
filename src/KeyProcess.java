import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.Key;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.Enumeration;

import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

public class KeyProcess {

	static String keyStoreLocation = null;
	static String storePassword = null;
	static String keyPassword = null;
	static String keyAlias = null;
	static String password = null;
	static KeyStore store;

	private static SecretKey keyFromPassword() throws NoSuchAlgorithmException,
			InvalidKeySpecException {

		byte[] saltBytes = { 115, 117, 102, 105, 121, 97, 110, 103, 104, 111,
				114, 105 };

		SecretKeyFactory factory = SecretKeyFactory
				.getInstance("PBKDF2WithHmacSHA1");

		PBEKeySpec spec = new PBEKeySpec(password.toCharArray(), saltBytes,
				2 ^ 32, 256);

		SecretKey secretKey = factory.generateSecret(spec);

		System.out.println("Key Generated Successfully !");
		return secretKey;
	}

	public static void addKeytoStore() throws InvalidKeySpecException,
			UnrecoverableEntryException {

		try {

			KeyStore.SecretKeyEntry skEntry = new KeyStore.SecretKeyEntry(
					keyFromPassword());

			store.setEntry(keyAlias, skEntry, new KeyStore.PasswordProtection(
					keyPassword.toCharArray()));

			OutputStream op = new FileOutputStream(keyStoreLocation);
			store.store(op, storePassword.toCharArray());
			System.out.println("New Key with Alias: " + keyAlias
					+ " has been added in Key Store.");

		} catch (KeyStoreException | CertificateException | IOException
				| NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	public static Key loadKey() throws InvalidKeySpecException,
			UnrecoverableEntryException {

		try {

			if (!store.containsAlias(keyAlias)) {
				System.out.print("No Key with this Alias");
				System.exit(0);
			}

			Key key = store.getKey(keyAlias, keyPassword.toCharArray());
			System.out.println("Key with Alias: " + keyAlias
					+ " has been loaded!");

			return key;

		} catch (UnrecoverableKeyException | KeyStoreException
				| NoSuchAlgorithmException e) {
			throw new RuntimeException(e);
		}
	}

	static KeyStore loadStore() throws KeyStoreException,
			FileNotFoundException, IOException, NoSuchAlgorithmException,
			CertificateException {

		KeyStore store = KeyStore.getInstance("BKS");

		InputStream keystoreStream = new FileInputStream(keyStoreLocation);
		store.load(keystoreStream, storePassword.toCharArray());
		System.out.println("Key Store loaded!\n");
		return store;
	}

	public static String checkAlias() throws KeyStoreException,
			FileNotFoundException, NoSuchAlgorithmException,
			CertificateException, IOException {

		int i = 0;
		while (store.containsAlias(keyAlias) == true) {
			System.out.println("Key with alias '" + keyAlias
					+ "' has already taken!");
			keyAlias = keyAlias.concat(String.valueOf(i));
			System.out.println("New Key Alias is: " + keyAlias);
			i++;
		}
		return keyAlias;
	}

	private static void getAliases() throws KeyStoreException,
			FileNotFoundException, NoSuchAlgorithmException,
			CertificateException, IOException {
		if (store.size() == 0)
			System.out.println("Store is Empty!");
		Enumeration<String> enumeration = store.aliases();
		while (enumeration.hasMoreElements()) {
			String alias = (String) enumeration.nextElement();
			System.out.println("Key Alias: " + alias);
		}
	}

	public static void showKeyAliases() throws KeyStoreException,
			FileNotFoundException, NoSuchAlgorithmException,
			CertificateException, IOException {
		getAliases();
	}

	public static void deleteAlias(String alias) throws KeyStoreException {
		store.deleteEntry(alias);
	}

	public static void createStore(String path, String keyStoreName,
			String storePassword) throws KeyStoreException,
			NoSuchAlgorithmException, CertificateException, IOException {
		KeyStore store = KeyStore.getInstance("BKS");
		char[] password = storePassword.toCharArray();
		store.load(null, password);

		FileOutputStream fos = new FileOutputStream(path + keyStoreName);
		store.store(fos, password);
		System.out.println("New Store Created !");
		fos.close();
	}
}