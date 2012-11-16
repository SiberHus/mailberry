package jasypt;

import java.security.Provider;
import java.security.Security;

import org.jasypt.encryption.pbe.PooledPBEStringEncryptor;

public class Main {

	public static void main(String[] args) {
//		StandardPBEStringEncryptor enc = new StandardPBEStringEncryptor();
		PooledPBEStringEncryptor enc = new PooledPBEStringEncryptor();
		enc.setPoolSize(4);
//		BasicTextEncryptor enc = new BasicTextEncryptor();
		enc.setAlgorithm("PBEWithMD5AndDES");
		enc.setPassword("jasypt");
		System.out.println(enc.encrypt("Hello How are you? Are you OK/"));
		
		for (Provider provider : Security.getProviders()){
			System.out.println("Provider: " + provider.getName());
			for (Provider.Service service : provider.getServices()){
				System.out.println("  Algorithm: " + service.getAlgorithm());
			}
		}
	}
	
}
