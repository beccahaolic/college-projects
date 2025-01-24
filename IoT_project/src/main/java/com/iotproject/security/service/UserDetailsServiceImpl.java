package com.iotproject.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.iotproject.model.User;
import com.iotproject.repository.UserRepository;
 
/**
 * Serviço de detalhes do usuário.
 * Implementa a interface UserDetailsService para carregar os detalhes do usuário durante a autenticação.
*/
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
  @Autowired
  UserRepository userRepository; // Repositório de usuários

   /**
   * Carrega um usuário pelo nome de usuário.
   * 
   * @param username Nome de usuário.
   * @return Detalhes do usuário.
   * @throws UsernameNotFoundException Se o usuário não for encontrado.
   */
  @Override
  @Transactional
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    User user = userRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("User Not Found with username: " + username));

    return UserDetailsImpl.build(user);
  }
}