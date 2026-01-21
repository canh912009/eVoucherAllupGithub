
package com.evoucher.partner.service.vnpt;

import com.evoucher.partner.service.config.CryptoUtils;
import com.evoucher.partner.service.vnpt.bean.InterfacesSoapBindingStub;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.axis.AxisFault;
import org.apache.axis.utils.Options;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.xml.ws.EndpointReference;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.spec.InvalidKeySpecException;

@Configuration
public class VnptConfiguration {
	@Value("${partner.vnpt.default.uri}")
	private String vnptUrl;
	@Value("${partner.vnpt.private-key}")
	private String privateKey;

	@Bean("vnptSoapService")
	InterfacesSoapBindingStub getVnptSoapService() throws MalformedURLException, AxisFault {
		URL oUrl = new URL(vnptUrl);
		InterfacesSoapBindingStub service = new InterfacesSoapBindingStub(oUrl, null);
		service.setTimeout(300000);
		return service;
	}

	@Bean("vnptPrivateKey")
	PrivateKey getPrivateKey() throws NoSuchAlgorithmException, InvalidKeySpecException {
		return CryptoUtils.loadPrivateKey(privateKey);
	}

//	@Bean
//	ObjectMapper getMapper() {
//		return new ObjectMapper();
//	}
}
