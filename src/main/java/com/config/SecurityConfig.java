package com.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import com.filter.TokenFilter;

@Configuration
public class SecurityConfig {
	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http, TokenFilter tokenFilter) throws Exception {
		return http.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(
						auth -> auth.requestMatchers(
                                "/v3/api-docs/**",         
                                "/swagger-ui/**",          
                                "/swagger-ui.html",        
                                "/webjars/**"              
                        ).permitAll()
						.requestMatchers("/api/public/**").permitAll().requestMatchers("/api/private/**")
								.hasAnyAuthority("admin").anyRequest().authenticated())
				.addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class).build();
	}
	
	@Bean
    public TokenFilter tokenFilter() {
        return new TokenFilter();
    }
}
