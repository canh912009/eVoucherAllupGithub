package com.evoucher.partner.service.config;



import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.axis.encoding.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

//import sun.misc.BASE64Decoder;
//import sun.misc.BASE64Encoder;


public class RSA {
    public static String private_key;
    public static String public_key;
    public static void genKey(){
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(1024);
            KeyPair keypair = keyGen.genKeyPair();
            PrivateKey privateKey = keypair.getPrivate();
            PublicKey publicKey = keypair.getPublic();
            //
//            BASE64Encoder encoder = new BASE64Encoder();
            private_key = Base64.encode(privateKey.getEncoded());
            public_key = Base64.encode(publicKey.getEncoded());

            System.out.println("This is privatekey: \n" + private_key);
            System.out.println("This is publickey: \n" + public_key);
            //Write to file:
            writeKeyBytesToFile(private_key.getBytes(), "/Users/daont/tools/KEY/private_key.pem");
            writeKeyBytesToFile(public_key.getBytes(), "/Users/daont/tools/KEY/public_key.pem");
            //
            RSAPublicKey rsaPublicKey = (RSAPublicKey) KeyFactory.getInstance(
                    "RSA").generatePublic(
                    new X509EncodedKeySpec(publicKey.getEncoded()));
            String xml = getRSAPublicKeyAsXMLString(rsaPublicKey);
            writeKeyBytesToFile(xml.getBytes(), "/Users/daont/tools/KEY/public_key.xml");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private static void writeKeyBytesToFile(byte[] key, String file)
            throws IOException {
        OutputStream out = new FileOutputStream(file);
        out.write(key);
        out.close();
    }
    //Read key
    public static void initializeKeys() {
        private_key = Readfile("/Users/daont/tools/KEY/private_key.pem");
        System.out.println("Read Private key:");
        System.out.println(private_key);
        public_key = Readfile("/Users/daont/tools/KEY/public_key.pem");
        System.out.println("Read Public key:");
        System.out.println(public_key);
    }
    private static String Readfile(String path) {
        String xau = "";
        try {
            FileInputStream fstream = new FileInputStream(path);
            DataInputStream in = new DataInputStream(fstream);
            BufferedReader br = new BufferedReader(new InputStreamReader(in));
            String strLine;
            while ((strLine = br.readLine()) != null) {
                xau += strLine + " ";
            }
            xau = xau.trim();
            xau = xau.replace(" ", "\n");
            br.close();
            in.close();
            fstream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return xau;
    }
    private static String getRSAPublicKeyAsXMLString(RSAPublicKey key)
            throws UnsupportedEncodingException, ParserConfigurationException,
            TransformerException {
        Document xml = getRSAPublicKeyAsXML(key);
        Transformer transformer = TransformerFactory.newInstance()
                .newTransformer();
        StringWriter sw = new StringWriter();
        transformer.transform(new DOMSource(xml), new StreamResult(sw));
        return sw.getBuffer().toString();
    }
    private static Document getRSAPublicKeyAsXML(RSAPublicKey key)
            throws ParserConfigurationException, UnsupportedEncodingException {
        Document result = DocumentBuilderFactory.newInstance()
                .newDocumentBuilder().newDocument();
        Element rsaKeyValue = result.createElement("RSAKeyValue");
        result.appendChild(rsaKeyValue);
        Element modulus = result.createElement("Modulus");
        rsaKeyValue.appendChild(modulus);

        byte[] modulusBytes = key.getModulus().toByteArray();
        modulusBytes = stripLeadingZeros(modulusBytes);
        modulus.appendChild(result.createTextNode(new String(
                Base64.encode(modulusBytes))));

        Element exponent = result.createElement("Exponent");
        rsaKeyValue.appendChild(exponent);

        byte[] exponentBytes = key.getPublicExponent().toByteArray();
        exponent.appendChild(result.createTextNode(new String(
                Base64.encode(exponentBytes))));

        return result;
    }
    private static byte[] stripLeadingZeros(byte[] a) {
        int lastZero = -1;
        for (int i = 0; i < a.length; i++) {
            if (a[i] == 0) {
                lastZero = i;
            } else {
                break;
            }
        }
        lastZero++;
        byte[] result = new byte[a.length - lastZero];
        System.arraycopy(a, lastZero, result, 0, result.length);
        return result;
    }

    //
//	public static String sign(String data, String key_private){
//		try {
//			BASE64Decoder decode = new BASE64Decoder();
//			byte[] privateKeyBytes = decode.decodeBuffer(key_private);
////			byte[] privateKeyBytes = key_private.getBytes();
//			PrivateKey privateKey = KeyFactory.getInstance("RSA")
//					.generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
//			Signature rsa = Signature.getInstance("SHA1withRSA");
//			rsa.initSign(privateKey);
//			rsa.update(data.getBytes());
//			//
//			BASE64Encoder encoder = new BASE64Encoder();
//			return encoder.encode(rsa.sign());
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return null;
//	}
//
//	public static boolean verify(String data, String sign, String key_public){
//		try {
//			BASE64Decoder decode = new BASE64Decoder();
//			byte[] publicKeyBytes = decode.decodeBuffer(key_public);
////			byte[] publicKeyBytes = key_public.getBytes();
//			PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(
//					new X509EncodedKeySpec(publicKeyBytes));
//			Signature rsa = Signature.getInstance("SHA1withRSA");
//			rsa.initVerify(publicKey);
//			rsa.update(data.getBytes());
//			byte[] signByte = decode.decodeBuffer(sign);
//			return (rsa.verify(signByte));
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return false;
//	}

    public static String sign(String data, String key_private) {
        try {
//            BASE64Decoder decode = new BASE64Decoder();
            byte[] privateKeyBytes = Base64.decode(key_private);
            //Security.addProvider(new BouncyCastleProvider());
            PrivateKey privateKey = KeyFactory.getInstance("RSA")
                    .generatePrivate(new PKCS8EncodedKeySpec(privateKeyBytes));
            Signature rsa = Signature.getInstance("SHA1withRSA");
            rsa.initSign(privateKey);
            rsa.update(data.getBytes());
            //
//            BASE64Encoder encoder = new BASE64Encoder();
            return Base64.encode(rsa.sign());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static boolean verify(String data, String sign, String key_public) {
        try {
//            BASE64Decoder decode = new BASE64Decoder();
            byte[] publicKeyBytes = Base64.decode(key_public);
//			byte[] publicKeyBytes = key_public.getBytes();
            PublicKey publicKey = KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(publicKeyBytes));
            Signature rsa = Signature.getInstance("SHA1withRSA");
            rsa.initVerify(publicKey);
            rsa.update(data.getBytes());
            byte[] signByte = Base64.decode(sign);
            return (rsa.verify(signByte));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public static void main(String[] args) {
//		genKey();
		initializeKeys();

        // array(6) { ["requestId"]=> string(29) "partnerTest_PHP_1627882145329" ["requestTime"]=> string(14) "20210802122905" ["partnerName"]=> string(15) "partnerTest_PHP" ["provider"]=> string(3) "MRA" ["contractNo"]=> string(9) "MRA000001" ["sign"]=> string(172) "Rzi8Ob0qTaEK7JSjrE+PUk4fwxH2NWNNs6nlUvX3s+01d1h/YEJoWLk6WizyIySCCa8YEZoJmK9afThNJA3+P288bdMO2s6UB2V/fAU8H3BwOE3DstKN0ZaKVeiKdXnFHmLhIzQ6/+E5xA+yujFqtpUeiBMesHwn+YaN2tNVKv8=" } object(stdClass)#3 (7) { ["bill"]=> NULL ["customer"]=> NULL ["data"]=> NULL ["errorCode"]=> int(21) ["hostTime"]=> string(14) "20210802122905" ["message"]=> string(14) "Sai chữ ký." ["requestId"]=> string(29) "partnerTest_PHP_1627882145329" }


//		Rzi8Ob0qTaEK7JSjrE+PUk4fwxH2NWNNs6nlUvX3s+01d1h/YEJoWLk6WizyIySCCa8YEZoJmK9afThNJA3+P288bdMO2s6UB2V/fAU8H3BwOE3DstKN0ZaKVeiKdXnFHmLhIzQ6/+E5xA+yujFqtpUeiBMesHwn+YaN2tNVKv8=
//
//		Rzi8Ob0qTaEK7JSjrE+PUk4fwxH2NWNNs6nlUvX3s+01d1h/YEJoWLk6WizyIySCCa8YEZoJmK9afThNJA3+P288bdMO2s6UB2V/fAU8H3BwOE3DstKN0ZaKVeiKdXnFHmLhIzQ6/+E5xA+yujFqtpUeiBMesHwn+YaN2tNVKv8=


        //String dataSign = requestId + requestTime + partnerName + provider + contractNo;
//        String data = "vntong_169941339020231108101630vntongEVN_HCMPE06000323814" ;
//        String publickey = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDNWC3jjiP5zt6mvrm7eTNq5hil5SDciec7xrVJ4rg6yhIWtAW6y6f7CK95cEyK23cWOfdmKncRl46bdqkbPC3JGe3vqEaTzbJag65LGVx35fOM7a2aAN25wexNPbihyU8plxci/xhKHpBtndkad8ga16qpg8GCVwY5ajniC7Ns0QIDAQAB";
//        String privateKey = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAM1YLeOOI/nO3qa+ubt5M2rmGKXlINyJ5zvGtUniuDrKEha0BbrLp/sIr3lwTIrbdxY592YqdxGXjpt2qRs8LckZ7e+oRpPNslqDrksZXHfl84ztrZoA3bnB7E09uKHJTymXFyL/GEoekG2d2Rp3yBrXqqmDwYJXBjlqOeILs2zRAgMBAAECgYBaE/2FXhwgAIShZvc46zEICsnzNDEXLZN3IHL0Z3VjrLMErJH64E3fDG9VeVLKcv2pjWIeujAjUnWncwU5wkterBY22XNPDPAzzS5WI5H6SxkZwAinOokj62yPM2q71wKgZYZegFSTIbKKFyz17uAYBMwRqE8ZrSn08DOIEY+LsQJBAPA26NlkbYklB7PxfhwchNTTQHTuMCb25hXg3WpLOjMglwVFUy1axsSln6qO56+Wn7ibchabCmbT1mRePQiwhOUCQQDa1qgQyQ08DgQStUzX2WVQH4Q2sHpgZPLHp1xXu7yTMdiIOzmS3SNo+BC/KVkeNkgQICPlJIcFOVcmzRC0MNV9AkBwrfEv+JIBISOp3v//A8mzY5z6vLhNrsdjP+Xc9IjKbuEokcpgnhJbMC3jfcFkdk1Z9WDhBb0tWvVYg3Qx3UuZAkEAjuqA9/VVX1MH9e/RL2YadCkg/1ZhlXJX22vBMsq4bKiw8Mc84lzpMzROO4mVWdW5Wk6jIpKoWxEHiZ+CuSrrQQJBAKutIVVKXRljtNoIT232ubouhS8c3X2t5dcTemRKJnwxRqMOvzjn8xOoZNDbeHrdF9Oll66E9rNjfNT9jCtkvlU=";
//        String sign = "ylZvIwH4JjiTtOxwHQTMvox3pkjFJs/pv5seFIZKRyDQieCxVjCGeKgTDg6Qu5RFNIpNqExCJQTPmOZ3c33qxa1P9siTq0kzPWfDejOy3avMGoVrt7G/yNAAAhICg5twO0CC0OjUtcLO0pf7v5rNdTX8KByjGaSikTQFol8SUfY=";
//        System.out.println("Data: " + data);
//        System.out.println("Sign: " + sign);
//        System.out.println("Verify: " + verify(data, sign, publickey));
//		System.out.println("Sign: " + sign(data, privateKey));

        String raw = "test";
        String encrypted = sign(raw, private_key);
        System.out.println(encrypted);
        System.out.println("verify:"+ verify(raw, encrypted, public_key));

    }
}
