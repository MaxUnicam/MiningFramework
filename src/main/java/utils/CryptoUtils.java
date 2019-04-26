package utils;

import org.bouncycastle.jcajce.provider.digest.Keccak;
import org.bouncycastle.util.encoders.Hex;

import java.nio.charset.StandardCharsets;


public class CryptoUtils {

    static final Keccak.Digest256 digest = new Keccak.Digest256();

    public static String digestKeccak256(String data) {
        byte[] hashbytes = digest.digest(data.getBytes(StandardCharsets.UTF_8));
        return new String(Hex.encode(hashbytes));
    }

}
