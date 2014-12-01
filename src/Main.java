import java.io.IOException;
import java.security.Key;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableEntryException;
import java.security.cert.CertificateException;
import java.security.spec.InvalidKeySpecException;
import java.util.Scanner;

class Main {

	private static Scanner input = new Scanner(System.in);
	
	public static void main(String[] args) throws InvalidKeySpecException,
			UnrecoverableEntryException, IOException, KeyStoreException,
			NoSuchAlgorithmException, CertificateException {
		
		// AES 256 Encryption/Decryption with Java Key Store Management 
		/* In this code, Bouncy Castle KeyStore (BKS) is used as an alternative repository , 
		 * before you can begin, you need to add BKS as you security provider in your Java security file,
		   
		   Download Bouncycastle jar from, http://goo.gl/qFtPCE.
		   Configure BouncyCastle for PC using below method.
			Adding the BC Provider Statically (Recommended),
				Copy jar file to each
					jre\lib\ext (JDK (bundled JRE)
					\lib\ext (JRE)
					
				Modify the java.security file under,
					jre\lib\security
					lib\security
					and add the following entry,
						security.provider.7=org.bouncycastle.jce.provider.BouncyCastleProvider
		 * 
		 * You can now use either "keytool" utility or Java code (explained later) to generate new Keystore,
		 * 
		 * 		Using keytool,
		 * 			Open cmd.exe in Windows,
		 * 			Change directory to, your java directory\jre\bin, and execute the following command,
		 * 			"keytool -genseckey -keystore mykeystore.keystore -storetype BKS 
		 * 				-storepass mystorepass -keyalg AES 
		 * 				-keysize 256 -alias myfirsykey -keypass mykeypass
		 *              -providerpath c:\bcprov-ext-jdk15on-1.46.jar 
		 *              -provider org.bouncycastle.jce.provider.BouncyCastleProvider" 
		 *              (change -providerpath with your bks jar path).
		 * 
		 * This will create a new keystore and add a new AES256 key with the given parameters.  
		 *
		 * Creating new Keystore, the easy way,
		 * 
		 * KeyProcess.createStore (String path, String keyStoreName, String
		 * password)
		 * 
		 * path = Path where you want your Store to be created, 
		 * keyStoreName = Your Key Store name with extension, password = Password required for
		 * Key Store protection.
		 * 
		 * Example, KeyProcess.createStore ( "D:/", "keys.keystore",
		 * "sufiyanghori" );
		 * 
		 * This will create an empty Keystore. KeyProcess.addKeytoStore() method could then be used,
		 * to generate and add key to this store. 
		 * 
		 * One more thing,
		 * Due to import regulations in some countries, the Oracle implementation provides a default cryptographic jurisdiction policy file that limits the strength of cryptographic algorithms.
		 * If stronger algorithms are needed (for example, AES with 256-bit keys), the JCE Unlimited Strength Jurisdiction Policy Files must be obtained and installed in the JDK/JRE.
		 * 
		 * link: http://www.oracle.com/technetwork/java/javase/downloads/index.html
		 * 
		 */

		// -------------------------------
		// LOADING EXISTING KEY STORE
		// -------------------------------
		// (or create your own before executing this code)
		KeyProcess.keyStoreLocation = "D:/aes-keystore.keystore";
		KeyProcess.storePassword = "sufiyan";
		KeyProcess.store = KeyProcess.loadStore();
		// -------------------------------

		System.out
				.println("Enter 'Y' to display Keys in Keystore, or any other key to continue");

		if (input.nextLine().equalsIgnoreCase("Y"))
			KeyProcess.showKeyAliases();

		// -------------------------------
		// MENU
		// -------------------------------
		// 1. Load Key from Key Store
		// 2. Generate New Key and add in Key Store
		// -------------------------------
		System.out.println("Input '1' or '2'");
		System.out
				.println("1. Load Key from Key Store\n2. Generate New Key and add in Key Store ");
		switch (Integer.parseInt(input.nextLine())) {

		case 1:
			System.out.println("Enter Key Alias:");
			KeyProcess.keyAlias = input.nextLine();
			System.out.println("Enter Key Password:");
			KeyProcess.keyPassword = input.nextLine();
			break;
		case 2:
			System.out.println("Enter New Key Alias:");
			KeyProcess.keyAlias = input.nextLine();
			KeyProcess.checkAlias();
			System.out.println("Enter Password for Key Protection:");
			KeyProcess.keyPassword = input.nextLine();
			System.out.println("Enter Password to generate Key:");
			KeyProcess.password = input.nextLine();
			KeyProcess.addKeytoStore();
			break;
		default:
			System.out.print("Invalid input");
			System.exit(0);
			break;
		}

		// -------------------------------
		// LOADING KEY FOR ENCRYPTION
		// -------------------------------
		Key keyFromKeyStore = KeyProcess.loadKey();
		// -------------------------------

		// -------------------------------
		// ENCRYPTION/DECRYPTION MENU
		// -------------------------------

		String iv = "c3VmaXlhbmdob3Jp";

		System.out.println("Input '1' or '2'");
		System.out.println("1. Encrypt File \n2. Decrypt File");
		switch (Integer.parseInt(input.nextLine())) {
		case 1:
			// encryption (Key key, String IV, String FilePath)
			encryption(keyFromKeyStore, iv, "D:/dell.exe");
			break;
		case 2:
			// decryption (Key key, String IV)
			decryption(keyFromKeyStore, iv);
			break;
		default:
			System.out.print("invalid input");
			System.exit(0);
		}
		input.close();
	}

	private static void decryption(Key keyFromKeyStore, String iv)
			throws IOException {
		// -------------------------------
		// DECRYPTION
		// -------------------------------
		EncDec cipher = new EncDec(keyFromKeyStore, iv.getBytes());
		System.out.println("Enter complete file Path to be decrypted, ");
		System.out.println("(example: D:/file.exe.encrypt)");
		String encryptedFile = input.nextLine();
		byte[] loadEncryptedM = FileOp.readFromFile(encryptedFile);

		// Decrypt the encrypted message,
		byte[] decryptedMessage = cipher.getDecryptedMessage((loadEncryptedM));

		// Save the Decrypted file
		String outputFile = encryptedFile.replace(".encrypt", "");
		FileOp.writeToFile(outputFile, decryptedMessage);
		System.out.print("Decrypted Successfully!");
	}

	private static void encryption(Key keyFromKeyStore, String iv,
			String filePath) throws IOException {
		// -------------------------------
		// ENCRYPTION
		// -------------------------------
		EncDec cipher = new EncDec(keyFromKeyStore, iv.getBytes());

		// -------------------------------
		// INPUT FILE DETAILS
		// -------------------------------
		// File to be generated after Encryption.
		String encryptedFile = filePath + ".encrypt";
		// -------------------------------
		// Load plain text,
		byte[] messageAsBytes = FileOp.readFromFile(filePath);
		// Encrypt Raw message,
		byte[] encryptedMessage = (cipher.getEncryptedMessage(messageAsBytes));
		// Store encrypted message in encrypted file,
		FileOp.writeToFile(encryptedFile, encryptedMessage);
		System.out.println(filePath + " Encrypted Successfully!");
		System.out.println(encryptedFile + " is generated!");
	}
}
