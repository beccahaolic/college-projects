package com.iotproject.controller;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.iotproject.model.ERole;
import com.iotproject.model.Role;
import com.iotproject.model.User;
import com.iotproject.payload.request.LoginRequest;
import com.iotproject.payload.request.SignupRequest;
import com.iotproject.payload.response.JwtResponse;
import com.iotproject.payload.response.MessageResponse;
import com.iotproject.repository.RoleRepository;
import com.iotproject.repository.UserRepository;
import com.iotproject.security.jwt.JwtUtils;
import com.iotproject.security.service.UserDetailsImpl;

import jakarta.validation.Valid;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {

  @Autowired
  AuthenticationManager authenticationManager;

  @Autowired
  UserRepository userRepository;

  @Autowired
  RoleRepository roleRepository;

  @Autowired
  PasswordEncoder encoder;

  @Autowired
  JwtUtils jwtUtils;

  /**
   * Método para autenticar um utilizador.
   * 
   * @param loginRequest dados de login fornecidos pelo utilizador.
   * @return ResponseEntity com o JWT gerado e informações do utilizador autenticado.
   */
  @PostMapping("/signin")
  public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
    // Autentica o utilizador usando o AuthenticationManager
    Authentication authentication = authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

    // Armazena a autenticação no contexto de segurança
    SecurityContextHolder.getContext().setAuthentication(authentication);
    
    // Gera o JWT
    String jwt = jwtUtils.generateJwtToken(authentication);
    
    // Obtém os detalhes do utilizador autenticado
    UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
    
    // Extrai os papéis do utilizador
    List<String> roles = userDetails.getAuthorities().stream()
        .map(item -> item.getAuthority())
        .collect(Collectors.toList());

    // Retorna o JWT e informações do utilizador na resposta
    return ResponseEntity.ok(new JwtResponse(jwt, 
                         userDetails.getId(), 
                         userDetails.getUsername(), 
                         userDetails.getEmail(), 
                         roles));
  }

  /**
   * Método para registar um novo utilizador.
   * 
   * @param signUpRequest dados de registo fornecidos pelo utilizador.
   * @return ResponseEntity com uma mensagem de sucesso ou erro.
   */
  @PostMapping("/signup")
  public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
    // Verifica se o nome de utilizador já existe
    if (userRepository.existsByUsername(signUpRequest.getUsername())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Erro: Nome de utilizador já está em uso!"));
    }

    // Verifica se o email já está em uso
    if (userRepository.existsByEmail(signUpRequest.getEmail())) {
      return ResponseEntity
          .badRequest()
          .body(new MessageResponse("Erro: Email já está em uso!"));
    }

    // Cria uma nova conta de utilizador com os dados fornecidos
    User user = new User(signUpRequest.getUsername(), 
                         signUpRequest.getEmail(),
                         encoder.encode(signUpRequest.getPassword()));

    // Define os papéis do utilizador
    Set<String> strRoles = signUpRequest.getRole();
    Set<Role> roles = new HashSet<>();

    if (strRoles == null) {
      // Se não forem fornecidos papéis, atribui o papel padrão 'ROLE_USER'
      Role userRole = roleRepository.findByName(ERole.ROLE_USER)
          .orElseThrow(() -> new RuntimeException("Erro: Papel não encontrado."));
      roles.add(userRole);
    } else {
      // Atribui papéis específicos se fornecidos
      strRoles.forEach(role -> {
        switch (role) {
          case "admin" -> {
            Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                .orElseThrow(() -> new RuntimeException("Erro: Papel não encontrado."));
            roles.add(adminRole);
          }
          default -> {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Erro: Papel não encontrado."));
            roles.add(userRole);
          }
        }
      });
    }

    // Atribui os papéis ao utilizador
    user.setRoles(roles);
    
    // Guarda o novo utilizador na base de dados
    userRepository.save(user);

    // Retorna uma resposta de sucesso
    return ResponseEntity.ok(new MessageResponse("Utilizador registado com sucesso!"));
  }
}
