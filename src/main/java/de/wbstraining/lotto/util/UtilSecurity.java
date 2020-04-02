package de.wbstraining.lotto.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.enterprise.context.RequestScoped;
import javax.inject.Named;
import javax.xml.bind.DatatypeConverter;

@Named("utilSecurity")
@RequestScoped
public class UtilSecurity {

	final static String CRYPT_ALGO_MD5 = "MD5";
	private static final Logger logger = Logger.getLogger("wbs.corejpa.security");

	public static String str2Md(String str) {
		return str2Md(str, CRYPT_ALGO_MD5);
	}

	public static String str2Md(String str, String algo) {
		String mdStr = "";
		final String fmt = "%s: %s -> %s";
		try {
			MessageDigest md = MessageDigest.getInstance(algo);
			md.update(str.getBytes());
			byte[] digest = md.digest();
			mdStr = DatatypeConverter.printHexBinary(digest).toLowerCase();
			logger.log(Level.INFO, String.format(fmt, algo, str, mdStr));
		} catch (NoSuchAlgorithmException xc) {
			logger.log(Level.WARNING, xc.getMessage());
		}
		return mdStr;
	}

	public boolean verifyHash(String str, String hash) {
		return verifyHash(str, hash, CRYPT_ALGO_MD5);
	}

	public boolean verifyHash(String str, String hash, String algo) {
		String mdstr = str2Md(str, algo);
		return hash.equals(mdstr);
	}
}
