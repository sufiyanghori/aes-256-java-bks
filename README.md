aes-256-java-bks
================

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
