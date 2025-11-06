package com.agroconecta.agroconecta.service;

import com.agroconecta.agroconecta.auth.JwtService;
import com.agroconecta.agroconecta.auth.UserDetailsImpl;
import com.agroconecta.agroconecta.dto.*;
import com.agroconecta.agroconecta.exception.EmailAlreadyExistsException;
import com.agroconecta.agroconecta.exception.InvalidCredentialsException;
import com.agroconecta.agroconecta.exception.TokenInvalidException;
import com.agroconecta.agroconecta.exception.UnauthorizedException;
import com.agroconecta.agroconecta.mapper.AuthMapper;
import com.agroconecta.agroconecta.model.Usuario;
import com.agroconecta.agroconecta.repository.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.security.core.AuthenticationException;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    private final AuthMapper authMapper;

    @Transactional
    public RegisterResponse register(RegisterRequest request) {
        if (usuarioRepository.existsByEmail(request.getEmail())) {
            throw new EmailAlreadyExistsException("El correo ya está registrado");
        }

        Usuario usuario = Usuario.builder()
                .nombre(request.getNombre())
                .apellido(request.getApellido())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .rol(request.getRol())
                .build();

        Usuario usuarioGuardado = usuarioRepository.save(usuario);
        return authMapper.toRegisterResponse(usuarioGuardado);
    }

    @Transactional
    public LoginResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getEmail(),
                            request.getPassword()
                    )
            );

            Usuario usuario = usuarioRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new InvalidCredentialsException("Credenciales inválidas"));
            UserDetailsImpl userDetails = UserDetailsImpl.build(usuario);

            String jwtToken = jwtService.generateToken(userDetails);

            return authMapper.toLoginResponse(usuario, jwtToken);
        } catch (AuthenticationException e) {
            throw new InvalidCredentialsException("Credenciales inválidas");
        }
    }

    @Transactional
    public LogoutResponse logout(LogoutRequest request) {
        String token = request.getToken();

        if (token == null || token.isBlank()) {
            throw new TokenInvalidException("Token inválido o ausente");
        }
        try {
            String email = jwtService.extractUsername(token);
            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new UnauthorizedException("No autorizado"));

            UserDetailsImpl userDetails = UserDetailsImpl.build(usuario);
            if (!jwtService.isTokenValid(token, userDetails)) {
                throw new UnauthorizedException("No autorizado");
            }

            return LogoutResponse.builder()
                    .mensaje("Sesión cerrada correctamente.")
                    .build();
        } catch (Exception e) {
            throw new UnauthorizedException("No autorizado");
        }
    }
}
