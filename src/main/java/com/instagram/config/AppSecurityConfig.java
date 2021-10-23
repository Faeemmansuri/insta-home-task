package com.instagram.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.instagram.filter.AppAuthenticationFilter;
import com.instagram.service.UserService;
import com.instagram.service.UserServiceImpl;




@Configurable
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class AppSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired private UserService userService;
	@Autowired private AppAuthenticationFilter appAuthenticationFilter; 
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
    @Bean
    @Override
    public AuthenticationManager authenticationManager() throws Exception {
        return super.authenticationManager();
    }
    
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
    	return new AppEntryPoint();
    }
    
    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
        authenticationManagerBuilder
            .userDetailsService(userService)
            .passwordEncoder(passwordEncoder());
    }

	@Override
	protected void configure(HttpSecurity httpSecurity) throws Exception {
		httpSecurity.cors().disable()
			.csrf().disable().authorizeRequests()
			.antMatchers("/login", "/register").permitAll()
			.anyRequest().authenticated()
			.and().exceptionHandling().authenticationEntryPoint(authenticationEntryPoint())
			.and().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS);
		
		 httpSecurity.addFilterBefore(appAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
	}

	@Override
	public void configure(WebSecurity webSecurity) throws Exception {
		webSecurity.ignoring()
			.antMatchers("/login", "/register");
	}

}