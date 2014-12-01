import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileOp {

	/*
	 * This method (not used) returns File data as an array, 
	 * [0] = File Name without extension. 
	 * [1] = File Size in Megabytes. 
	 * [2] = File Extension.
	 */
	public static String[] getFileData(String fileName) {
		File file = new File(fileName);
		String fileExt = null;
		if (fileName.lastIndexOf(".") != -1 && fileName.lastIndexOf(".") != 0)
			fileExt = fileName.substring(fileName.lastIndexOf(".") + 1);
		String[] data = { file.getName().replaceFirst("[.][^.]+$", ""),
				file.length() / (1024 * 1024) + "mb", fileExt };
		return data;
	}

	public static byte[] readFromFile(String fileName) throws IOException {

		File file = new File(fileName);

		// Get the size of the file
		long length = file.length();

		if (length > Integer.MAX_VALUE) {
			throw new IOException("Could not completely read file "
					+ file.getName() + " as it is too long (" + length
					+ " bytes, max supported " + Integer.MAX_VALUE + ")");
		}

		// Create the byte array to hold the data
		byte[] bytes = new byte[(int) length];

		// Read in the bytes
		InputStream is = new FileInputStream(file);
		int offset = 0;
		int numRead = 0;
		while (offset < bytes.length
				&& (numRead = is.read(bytes, offset, bytes.length - offset)) >= 0) {
			offset += numRead;
		}

		// Close the input stream and return bytes
		is.close();

		// Ensure all the bytes have been read in
		if (offset < bytes.length) {
			throw new IOException("Could not completely read file "
					+ file.getName());
		}

		return bytes;
	}

	public static void writeToFile(String filePath, byte[] bytes)
			throws IOException {
		Files.write(Paths.get(filePath), bytes);
	}

}
