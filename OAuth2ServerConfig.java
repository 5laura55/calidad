package com.hiveag.geepy.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.redis.connection.jedis.JedisConnectionFactory;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.token.AuthorizationServerTokenServices;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.store.redis.RedisTokenStore;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.hiveag.geepy.util.CustomTokenEnhacer;

@Configuration
public class OAuth2ServerConfig {
	
	private final static String RESOURCE_ID = "Geepy";
	
	@Configuration
	@EnableResourceServer
	protected static class OAuth2ResourceServerConfig extends ResourceServerConfigurerAdapter {
		
		@Autowired
		private RedisTokenStore tokenStore;
		
		@Override
		public void configure(ResourceServerSecurityConfigurer resources) {
			resources.tokenStore(tokenStore).resourceId(RESOURCE_ID);
		}		

		@Override
		public void configure(HttpSecurity http) throws Exception {
			http
			/*    .sessionManagement()
	              .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
    	          .and()*/
				.cors()
					.and()
				.requestMatchers()
			    	.antMatchers("/api/**")
			    	.antMatchers("/img/**")
					.antMatchers("/location/**")
					.antMatchers("/status/**")
					.antMatchers("/led/**")
				.and()
				.authorizeRequests()
					.antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
					.antMatchers(HttpMethod.POST, "/api/person").permitAll()
					.antMatchers(HttpMethod.POST, "/api/picture").permitAll()
					.antMatchers(HttpMethod.GET, "/api/location").permitAll()
					.antMatchers(HttpMethod.PUT, "/api/sms/password").permitAll()
					.antMatchers(HttpMethod.PATCH, "/api/sms/password").permitAll()
					.antMatchers(HttpMethod.POST, "/api/sms/password").permitAll()
					.antMatchers(HttpMethod.PUT, "/api/sms/recoverpassword").permitAll()
					.antMatchers(HttpMethod.GET, "/api/person").hasAnyRole("ADMIN")
					.antMatchers(HttpMethod.DELETE, "/oauth/token").hasAnyRole("ADMIN","USER")
			    	.antMatchers("/api/geepy/**").hasAnyRole("ADMIN","USER")
			    	.antMatchers("/api/catalog/**").hasAnyRole("ADMIN","USER")
			    	.antMatchers("/api/picture/**").hasAnyRole("ADMIN","USER")
			    	.antMatchers("/api/bike/**").hasAnyRole("ADMIN","USER")
			    	.antMatchers("/img/**").hasAnyRole("ADMIN","USER")
			    	.antMatchers("/api/record/**").hasAnyRole("ADMIN","USER")
			    	.antMatchers("/api/track/**").hasAnyRole("ADMIN","GEEPY","USER")
			    	.antMatchers("/api/sms/**").hasAnyRole("ADMIN","USER")
				//	.antMatchers("/location/**").hasAnyRole("ADMIN")
	    	//.antMatchers("/api/person/**").hasAnyRole("ADMIN","USER")
					.antMatchers("/status/**").hasAnyRole("ADMIN")
					.antMatchers("/led/**").hasAnyRole("ADMIN");
		}
		
	}
	
	
	@Configuration
	@EnableAuthorizationServer
	protected static class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
		
		@Autowired
		private RedisTokenStore tokenStore;
		
		@Autowired
		@Qualifier("authenticationManager")
		private AuthenticationManager authenticationManager;
		   
	    
		@Override
		public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
			endpoints.authenticationManager(authenticationManager)
					 .tokenStore(tokenStore)
					 .approvalStoreDisabled()
					 .accessTokenConverter(accessTokenConverter())
					 .tokenEnhancer(tokenEnhancer());
		}

		@Override
		public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
			clients
				.inMemory()
					.withClient("admin-client")
						.resourceIds(RESOURCE_ID)
						.authorizedGrantTypes("password","refresh_token")
						.secret("lukas")
						.authorities("ROLE_ADMIN","ROLE_MONITOR")
						.scopes("read", "write")
						.accessTokenValiditySeconds(3200)
						.refreshTokenValiditySeconds(6500)
					.and()
					.withClient("mobile-client")
						.resourceIds(RESOURCE_ID)
						.authorizedGrantTypes("password","refresh_token")
						.secret("lukas")
						.authorities("ROLE_USER")
						.scopes("read", "write")
						.accessTokenValiditySeconds(3200)
						.refreshTokenValiditySeconds(6500)
					.and()
					.withClient("test-client")
						.resourceIds(RESOURCE_ID)
						.authorizedGrantTypes("password","refresh_token")
						.secret("lukas")
						.authorities("ROLE_USER")
						.scopes("read", "write")
						.accessTokenValiditySeconds(3200)
						.refreshTokenValiditySeconds(6500)
					.and()
					.withClient("geepy-client")
						.resourceIds(RESOURCE_ID)
						.authorizedGrantTypes("password","refresh_token")
						.secret("geepy")
						.authorities("ROLE_GEEPY")
						.scopes("read", "write")
						.accessTokenValiditySeconds(3200)
						.refreshTokenValiditySeconds(6500)
					.and();	
					
		}	
		
		
		//http://www.developersite.org/101-26267-spring
		@Override
		public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
		
			oauthServer.addTokenEndpointAuthenticationFilter(corsFilter()); //Important to avoid basic security intercepts first the request.
			oauthServer.realm("Geepy/client");
			oauthServer.tokenKeyAccess("permitAll()").checkTokenAccess("isAuthenticated()");
			oauthServer.allowFormAuthenticationForClients();
			
		}
		
	    @Bean
	    public TokenEnhancer tokenEnhancer() {
	        return new CustomTokenEnhacer();
	    }
	    
	    @Bean
	    public DefaultAccessTokenConverter accessTokenConverter() {
	        return new DefaultAccessTokenConverter();
	    }
		
	    @Bean
	    @Primary
	    public AuthorizationServerTokenServices tokenServices() {
	        DefaultTokenServices tokenServices = new DefaultTokenServices();
	        tokenServices.setTokenStore(tokenStore);
	        tokenServices.setSupportRefreshToken(true);
	        tokenServices.setTokenEnhancer(tokenEnhancer());
	        return tokenServices;
	    }
	    
	    
	    public static CorsFilter corsFilter() {

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

	}
		
	protected static class Stuff {
		
		@Autowired
		private JedisConnectionFactory jedisConnectionFactory;
		
		@Bean
		public  RedisTokenStore tokenStore() {
			RedisTokenStore redisTokenStore = new RedisTokenStore(jedisConnectionFactory);
			return redisTokenStore;
		}

	}
	
}
