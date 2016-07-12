package com.wipro.nt.provisioning;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.context.web.SpringBootServletInitializer;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@SpringBootApplication
@RestController
@RequestMapping("/subscribe")
public class TenantSubscriptionApplication extends SpringBootServletInitializer{
	
	@Autowired
	JenkinsConfig jenkinsConfig;
	
	JenkinsConnector jenkinsConnector =  new JenkinsConnector();

	@RequestMapping(value="/json",method = RequestMethod.POST)  
    public String subscribeByJson(@RequestBody Tenant tenant) {
		System.out.println("tenantId:"+tenant.getTenantId()+",mode:"+tenant.getMode()+",resources:"+tenant.getResources());
		// calling jenkins rest service for existing job build
		String result = jenkinsConnector.talk2Jenkins(jenkinsConfig, tenant.getMode(), "build");
		return "{\"subscribe\":\"successful\",\"result\":\""+result+"\"}";
    }
	@RequestMapping(value="/html",method = RequestMethod.POST)  
    public String subscribe(@RequestParam String tenantId,@RequestParam String mode,@RequestParam String resources) {
		System.out.println("tenantId:"+tenantId+",mode:"+mode+",resources:"+resources);
		// invoke baking
		String result = jenkinsConnector.talk2Jenkins(jenkinsConfig, mode, "build");
        return "{\"subscribe\":\"successful\",\"result\":\""+result+"\"}";
    }
   
	@Override
	protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
		return application.sources(TenantSubscriptionApplication.class);
	}

	
    public static void main(String[] args) {
        SpringApplication.run(TenantSubscriptionApplication.class, args);
    }
}
