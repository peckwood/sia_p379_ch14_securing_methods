package spittr.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.GlobalMethodSecurityConfiguration;

import spittr.security.SpittlePermissionEvaluator;
import spittr.service.ExpressionSecuredSpittleService;
import spittr.service.SecuredSpittleService;
import spittr.service.SpittleService;

@Configuration
@EnableGlobalMethodSecurity(prePostEnabled=true, securedEnabled=true)
public class ExpressionSecuredConfig extends GlobalMethodSecurityConfiguration {
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth
			.inMemoryAuthentication()
			.withUser("user").password("user").roles("USER")
			.and()
			.withUser("premium").password("premium").roles("PREMIUM");
	}
	
	@Bean
	public SpittleService spittleService(){
		return new ExpressionSecuredSpittleService();
	}
	
	//for custom PermissionEvaluator
	@Override
	protected MethodSecurityExpressionHandler createExpressionHandler() {
		DefaultMethodSecurityExpressionHandler expressionHandler = new DefaultMethodSecurityExpressionHandler();
		expressionHandler.setPermissionEvaluator(new SpittlePermissionEvaluator());
		return expressionHandler;
	}

}
