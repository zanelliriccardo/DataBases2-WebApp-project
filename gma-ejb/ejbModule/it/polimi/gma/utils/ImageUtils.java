package it.polimi.gma.utils;

import java.io.*;

import net.sf.jmimemagic.Magic;
import net.sf.jmimemagic.MagicException;
import net.sf.jmimemagic.MagicMatch;
import net.sf.jmimemagic.MagicMatchNotFoundException;
import net.sf.jmimemagic.MagicParseException;

public class ImageUtils {

	public static byte[] readImage(InputStream imageInputStream) throws IOException {

		ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
		byte[] buffer = new byte[4096];// image can be maximum of 4MB
		int bytesRead = -1;

		try {
			while ((bytesRead = imageInputStream.read(buffer)) != -1) {
				outputStream.write(buffer, 0, bytesRead);
			}

			byte[] imageBytes = outputStream.toByteArray();
			return imageBytes;
		} catch (IOException e) {
			throw e;
		}

	}
	
	public static Boolean isJPEG(byte[] file){		
		
		try {
			MagicMatch match;
			match = Magic.getMagicMatch(file);
			String mimeType = match.getMimeType();
			if(mimeType.equals("image/jpeg")) {
			    return true;
			}
		} catch (MagicParseException | MagicMatchNotFoundException | MagicException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
	public static Boolean isPNG(byte[] file){		
		try {
			MagicMatch match;
			match = Magic.getMagicMatch(file);
			String mimeType = match.getMimeType();
			if(mimeType.equals("image/png")) {
			    return true;
			}
		} catch (MagicParseException | MagicMatchNotFoundException | MagicException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}
	
}
