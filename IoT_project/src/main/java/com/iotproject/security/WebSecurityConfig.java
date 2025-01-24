package com.iotproject.security; 

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.iotproject.security.jwt.AuthEntryPointJwt;
import com.iotproject.security.jwt.AuthTokenFilter;
import com.iotproject.security.service.UserDetailsServiceImpl;

/**
 * Classe de configuração de segurança da web.
 * Configura a segurança HTTP, autenticação e autorização.
*/
@Configuration
@EnableMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig {

  @Autowired
  UserDetailsServiceImpl userDetailsService; // Serviço de detalhes do usuário

  @Autowired
  private AuthEntryPointJwt unauthorizedHandler; // Manipulador de entrada não autorizada

  @Bean
  public AuthTokenFilter authenticationJwtTokenFilter() {
    return new AuthTokenFilter(); // Filtro de autenticação JWT
  }

  @Bean
  public DaoAuthenticationProvider authenticationProvider() {
      DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
      authProvider.setUserDetailsService(userDetailsService); // Configura o serviço de detalhes do usuário
      authProvider.setPasswordEncoder(passwordEncoder()); // Configura o codificador de senhas
      return authProvider;
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration authConfig) throws Exception {
    return authConfig.getAuthenticationManager(); // Gerenciador de autenticação
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder(); // Codificador de senhas BCrypt
  }

  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
      http.cors().and().csrf().disable()
          .exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
          .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
          .authorizeHttpRequests()
          .requestMatchers("/api/auth/**").permitAll() // Endpoints de autenticação são públicos
          .requestMatchers("/devices/**").hasAnyRole("USER", "ADMIN")// Proteção para DeviceController
          .requestMatchers("/metrics/**").hasAnyRole("USER", "ADMIN") // Proteção para MetricsController
          .anyRequest().authenticated(); // Outros endpoints requerem autenticação
  
      http.authenticationProvider(authenticationProvider());
      http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
  
      return http.build();
  }
  
}