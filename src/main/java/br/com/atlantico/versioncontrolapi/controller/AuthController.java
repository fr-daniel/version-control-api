package br.com.atlantico.versioncontrolapi.controller;

import br.com.atlantico.versioncontrolapi.config.JwtTokenProvider;
import br.com.atlantico.versioncontrolapi.model.Permissao;
import br.com.atlantico.versioncontrolapi.repository.UsuarioRepository;
import br.com.atlantico.versioncontrolapi.util.AuthenticationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping
public class AuthController {

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtTokenProvider jwtTokenProvider;

    @Autowired
    UsuarioRepository usuarioRepository;

    @PostMapping("/login")
    public ResponseEntity signin(@RequestBody AuthenticationRequest data) {

        try {
            String username = data.getEmail();
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, data.getPassword()));
            List<String> roles = new ArrayList<>();
            for (Permissao papel : this.usuarioRepository.findByEmail(username).getPermissoes()) {
                roles.add(papel.getDescricao());
            }
            String token = jwtTokenProvider.createToken(username, new ArrayList());

            Map<Object, Object> model = new HashMap<>();
            model.put("username", username);
            model.put("papeis", roles);
            model.put("token", "Bearer " + token);

            return ok(model);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }

}
