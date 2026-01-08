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

        // Validación del token (genera error 400)
        if (token == null || token.isBlank() || token.trim().isEmpty()) {
            throw new TokenInvalidException("Token inválido o ausente");
        }

        try {
            // Intentar extraer el username del token
            String email = jwtService.extractUsername(token);

            if (email == null || email.isBlank()) {
                throw new TokenInvalidException("Token inválido o ausente");
            }

            // Buscar el usuario
            Usuario usuario = usuarioRepository.findByEmail(email)
                    .orElseThrow(() -> new UnauthorizedException("No autorizado"));

            UserDetailsImpl userDetails = UserDetailsImpl.build(usuario);

            // Validar el token
            if (!jwtService.isTokenValid(token, userDetails)) {
                throw new UnauthorizedException("No autorizado");
            }

            return LogoutResponse.builder()
                    .mensaje("Sesión cerrada correctamente.")
                    .build();

        } catch (TokenInvalidException e) {
            throw e;
        } catch (UnauthorizedException e) {
            throw e;
        } catch (io.jsonwebtoken.MalformedJwtException |
                 io.jsonwebtoken.security.SignatureException |
                 IllegalArgumentException e) {
            // Token malformado o con firma inválida
            throw new TokenInvalidException("Token inválido o ausente");
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            // Token expirado
            throw new UnauthorizedException("No autorizado");
        } catch (Exception e) {
            // Cualquier otro error
            throw new TokenInvalidException("Token inválido o ausente");
        }
    }
}
