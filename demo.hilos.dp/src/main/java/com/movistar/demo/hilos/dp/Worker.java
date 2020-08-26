package com.movistar.demo.hilos.dp;

import java.security.KeyManagementException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Component
public class Worker {
	private static final Logger log = LoggerFactory.getLogger(Worker.class);
	
	@Autowired
	private RestTemplate restTemplate;
	
	@Value("${tiempoEspera}")
	int tiempoEspera;
	
	@Value("${numeroIntentos}")
	int numeroIntentos;
	
	@Async("pool")
	public void polling(String url)  {
		
		for(long i = 1; i<=numeroIntentos; i++) {
			long start = System.nanoTime();
			llamarServicio(url);
			long elapsed = System.nanoTime() - start;
	        log.info("Hilo: {}, intento: {}, Elapsed in poll (ms): {}", Thread.currentThread().getName(), i, elapsed / 1000000);
	        try {
				Thread.sleep(tiempoEspera);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
		}
	}

	@Async("pool")
	public void writelog(String message)  {
		
		long count = 0L;
		
		while(count < Long.MAX_VALUE) {
			log.info("Hilo: {}, Cuenta: {}, message: {}", Thread.currentThread().getName(), count++, message);
	        try {
				Thread.sleep(tiempoEspera/5);
			} catch (InterruptedException e) {
				e.printStackTrace();
			} 
		}
        
	}
	
	private void llamarServicio(String url) {
		deshabilitarVerificacionSSL();
		String respuesta = "";
		try {
			respuesta = restTemplate.getForObject(url, String.class);
		}
		catch(HttpServerErrorException e) {
			respuesta = e.toString();
		}
		log.info("Hilo: {} se llama al servicio={}, resultado={}", Thread.currentThread().getName(), url, respuesta.subSequence(0, 50));
	}

	private void deshabilitarVerificacionSSL(){
		TrustManager[] trustAllCerts = new TrustManager[] {new X509TrustManager() {
			public java.security.cert.X509Certificate[] getAcceptedIssuers() {
				return null;
			}
			public void checkClientTrusted(X509Certificate[] certs, String authType) throws CertificateException {
				if( certs == null || authType == null) throw new CertificateException("lista de certificados y authTyoe no pueden ser null");
			}
			public void checkServerTrusted(X509Certificate[] certs, String authType) throws CertificateException {
				if( certs == null || authType == null) throw new CertificateException("lista de certificados y authTyoe no pueden ser null");
			}
			
		}
		};

		// Install the all-trusting trust manager
		SSLContext sc;
		try {
			sc = SSLContext.getInstance("TLSv1.2");
			sc.init(null, trustAllCerts, new java.security.SecureRandom());
			HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory());
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (KeyManagementException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		

		// Create all-trusting host name verifier
		HostnameVerifier allHostsValid = new HostnameVerifier() {
			public boolean verify(String hostname, SSLSession session) {
				return hostname != null && session != null;
			}
		};

		// Install the all-trusting host verifier
		HttpsURLConnection.setDefaultHostnameVerifier(allHostsValid);
		
	}

}
