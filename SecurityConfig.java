package com.hiveag.geepy.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true,prePostEnabled=true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		 http
		   /* .sessionManagement()
		         .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
		         .and()*/
		 	.cors()
	 			.and()
		 	.headers()
		 		.disable()
			.csrf()
				.disable()
			/*.exceptionHandling()
				.accessDeniedPage("/accessDenied")
				.and()*/
			.requestMatchers()
				.antMatchers(HttpMethod.OPTIONS, "/**")
				.antMatchers(HttpMethod.GET, "/**")
				.antMatchers(HttpMethod.POST, "/**")
				.antMatchers(HttpMethod.PUT, "/**")
				.antMatchers(HttpMethod.DELETE, "/**")
				.antMatchers(HttpMethod.PATCH, "/**")
				.and()
	        .authorizeRequests()
	        	.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
	            .and()
	        /*.formLogin()
	        	.loginPage("/login")
	        	.failureUrl("/login?login_error=1")
	            .and()
	        .logout()
	        	.logoutSuccessUrl("/")                                    
                .permitAll()                
                .and()*/;
	}
	
	
    @Bean
    public CorsFilter corsFilter() {

		CorsConfiguration configuration = new CorsConfiguration();
		configuration.setAllowedOrigins(Arrays.asList("*"));
		configuration.setAllowedMethods(Arrays.asList("GET","POST","PUT","DELETE","PATCH","OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("Origin","Content-Type",
        		"Authorization",
        		"Access-Control-Allow-Headers", "Access-Control-Allow-Methods",
                "Access-Control-Allow-Origin", "Access-Control-Expose-Headers",
                "Access-Control-Request-Headers", "Access-Control-Request-Method"));
        configuration.setAllowCredentials(true);
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
        return new CorsFilter(source);
        
    }
	

	@Override
	@Bean
	protected AuthenticationManager authenticationManager() throws Exception {
		return super.authenticationManager();
	}

		
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	
	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth, UserDetailsService userDetailsService) throws Exception {
		
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());

	}	

}
