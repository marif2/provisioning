package com.wipro.nt.provisioning;


import org.apache.http.HttpHost;
import org.apache.http.HttpVersion;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
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
		RestTemplate restTemplate = createRestTemplate(jenkinsConfig);
		return restTemplate.getForObject(jenkinsConfig.getProtocol()+"://"+jenkinsConfig.getHostname()+ ((jenkinsConfig.getPort()  > 0 && jenkinsConfig.getPort() != 80) ? ":"+ jenkinsConfig.getPort() : "") + "/job/"+jobName+"/"+command, String.class);
	}
	
	RestTemplate createRestTemplate(JenkinsConfig jenkinsConfig ) {
	    return new RestTemplate(this.createSecureTransport( jenkinsConfig ));
	}
	private ClientHttpRequestFactory createSecureTransport( JenkinsConfig jenkinsConfig  ){
		
		String username = jenkinsConfig.getUsername();
		String password = jenkinsConfig.getPassword();
		String host = jenkinsConfig.getHostname(); 
		int port = jenkinsConfig.getPort();
		
		String proxyusername = jenkinsConfig.getProxyUsername();
		String proxypassword = jenkinsConfig.getProxyPassword();
		String proxyhost = jenkinsConfig.getProxyHostname();
		int proxyport = jenkinsConfig.getProxyPort();
		
	    HttpComponentsClientHttpRequestFactory requestFactory = new HttpComponentsClientHttpRequestFactory();
	    // initial/default http params
	    HttpParams httpParams = new BasicHttpParams();
	    HttpConnectionParams.setConnectionTimeout(httpParams, 5000);
	    HttpConnectionParams.setSoTimeout(httpParams, 1000);
	    
	    DefaultHttpClient httpClient = new DefaultHttpClient(httpParams);
	    httpClient.getParams().setParameter("http.protocol.version", HttpVersion.HTTP_1_1);
	    httpClient.getParams().setParameter("http.socket.timeout", new Integer(1000));
	    httpClient.getParams().setParameter("http.protocol.content-charset", "UTF-8");
	    httpClient.getParams().setParameter("http.protocol.content-charset", "UTF-8");
	    
	    if( host != null && port > 0) {
		   	UsernamePasswordCredentials credentials = new UsernamePasswordCredentials( username, password );
		   	httpClient.getCredentialsProvider().setCredentials( new AuthScope( host, port ), credentials );
	    }
	    if( proxyhost != null && proxyport > 0) {
	    	HttpHost proxy = new HttpHost(proxyhost, proxyport);
	    	UsernamePasswordCredentials credentials = new UsernamePasswordCredentials( username, password );
		   	httpClient.getCredentialsProvider().setCredentials( new AuthScope( proxyhost, proxyport ), credentials );
	    	httpClient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
	    }
	    
	    
	    requestFactory.setHttpClient(httpClient);
	    return requestFactory;
	    
	}
}
