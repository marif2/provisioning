package com.wipro.nt.provisioning;


import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * 
 * @author marif2
 *
 */
public class JenkinsConnector {
		
	/**
	 * http://jenkins.wipdios.com:8080/job/NPTP-Test/build
	 * http://jenkins.wipdios.com:8080/job/<jobName>/<command>
	 * 
	 * @param jenkinsConfig - http://jenkins.wipdios.com:8080
	 * @param jobName - NPTP-Test
	 * @param command - build
	 * @return ACK/NACK
	 * @throws Exception 
	 */
	public String talk2Jenkins(JenkinsConfig jenkinsConfig,String jobName,String command)
	{
		RestTemplate restTemplate = createRestTemplate(jenkinsConfig.getProxyHostname(), jenkinsConfig.getProxyHostname(), jenkinsConfig.getProxyHostname(), jenkinsConfig.getProxyPort());
		return restTemplate.getForObject(jenkinsConfig.getProtocol()+"://"+jenkinsConfig.getHostname()+ ((jenkinsConfig.getPort()  > 0 && jenkinsConfig.getPort() != 80) ? ":"+ jenkinsConfig.getPort() : "") + "/job/"+jobName+"/"+command, String.class);
	}
	
	RestTemplate createRestTemplate(String username, String password, String host, int port ) {
	    return new RestTemplate(this.createSecureTransport( username, password, host, port ));
	}
	/*
	private ClientHttpRequestFactory createSecureTransport( String username, String password, String host, int port ){
		
	    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
	    DefaultHttpClient client = (DefaultHttpClient) requestFactory.getHttpClient();	    
	   	UsernamePasswordCredentials credentials = new UsernamePasswordCredentials( username, password );
	    client.getCredentialsProvider().setCredentials( new AuthScope( host, port ), credentials );
	    return requestFactory;
	    
	}
	*/
	private ClientHttpRequestFactory createSecureTransport( String username, String password, String host, int port ){
		
	    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
	    // initial/default http params
	    HttpClient client = requestFactory.getHttpClient();	  
	    DefaultHttpClient httpclient = new DefaultHttpClient(client.getParams());
	    if( host != null && port > 0) {
		   	UsernamePasswordCredentials credentials = new UsernamePasswordCredentials( username, password );
		   	httpclient.getCredentialsProvider().setCredentials( new AuthScope( host, port ), credentials );
	    }
	    requestFactory.setHttpClient(httpclient);
	    return requestFactory;
	    
	}
	
	
	
}



