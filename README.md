aes-256-java-bks
================

**(JAVA) AES-256 Encryption / Decryption with Key Store Management using Bouncy Castle Keystore (BKS)**

Overview:
===================

This simple code allows you to encrypt/decrypt any kind of file using AES-256 standard. It uses Bouncy Castle Keystore for Key Management. Beside Encryption, the code allows you to manage your keystore, like Creating a new Keystore, Loading an existing keystore, adding key to an existing keystore, generating new Key with user Password, deleting key from a keystore or displaying keys from given keystore, all these features could be accessed at runtime, all you need to do is execute the program.

**(Don't forget to provide the correct path of a file which is needed to be encrypted inside Main.java)**

      encryption (Key key, String IV, String FilePath) 

**NOTE:** This example is built using **JDK7**, ultimate strength JCE (JDK7) and **Eclipse**

 before you can begin, you need to add BKS as you security provider in your Java security file,

Download Bouncycastle jar from, http://goo.gl/qFtPCE.

**Configure BouncyCastle for PC using below method.**

        Adding the BC Provider Statically (Recommended),
                Copy jar file to each
                        jre\lib\ext (JDK (bundled JRE)
                        \lib\ext (JRE)
					
        Modify the java.security file under,
                jre\lib\security
                lib\security
	and add the following entry,
		security.provider.7=org.bouncycastle.jce.provider.BouncyCastleProvider

You can now use either "keytool" utility or Java code (explained later) to generate new 
Keystore,

<a name="keytool"></a>**Using keytool,**

Open cmd.exe in Windows,

Change directory to, your java **directory\jre\bin**, and execute the following command,

    "keytool -genseckey -keystore mykeystore.keystore -storetype BKS -storepass mystorepass -keyalg AES -keysize 256 -alias myfirsykey -keypass mykeypass -providerpath c:\bcprov-ext-jdk15on-1.46.jar -provider org.bouncycastle.jce.provider.BouncyCastleProvider" 

(change -providerpath with your bks jar path).

This will create a new keystore and add a new AES256 key with the given parameters.  
		 
<a name="mymethod"></a>**Creating new Keystore, the easy way,**
I have written the following custom method to easily create a keystore from within the code,

        KeyProcess.createStore (String path, String keyStoreName, String
        password)
        
        path = Path where you want your Store to be created, 
        keyStoreName = Your Key Store name with extension, password = Password required for
        Key Store protection.
        
        Example, KeyProcess.createStore ( "D:/", "keys.keystore", "sufiyanghori" );
  
  If you are running the program for the first time then call this method in **Main.java** file.

How to Run
===========

1. If you are running for the first time then first create a Keystore using either [keytool](#keytool) or [above method](#mymethod).
2. Provide correct path of a file needed to be ecnrypted on line **137** in **Main.java**
3. Execute **Main.java**
4. Follow the instruction, to either **load** or **display** existing keys from keystore or to **generate** a new key.
5. Choose wether you want to **Encrypt** or **Decrypt** a file.
6. If **Decryption** is chosen then provide the correct encrypted file path.
7. Rest of the instruction will be available during Runtime.

One more thing,

        Due to import regulations in some countries, the Oracle implementation provides a 
        default cryptographic jurisdiction policy file that limits the strength of cryptographic 
        algorithms.
        
        If stronger algorithms are needed (for example, AES with 256-bit keys), the JCE 
        Unlimited Strength Jurisdiction Policy Files must be obtained and installed in the 
        JDK/JRE.

        link: http://www.oracle.com/technetwork/java/javase/downloads/index.html


Copywrite &copy;2014 - Use of this code and it's concepts are considered a Proof-of-concept and should not be used directly in any environment
