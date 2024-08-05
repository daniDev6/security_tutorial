package comspring.security.tutorial.tutorialsecurity.service;

import comspring.security.tutorial.tutorialsecurity.dto.dtoentrada.DtoAuthenticationRequest;
import comspring.security.tutorial.tutorialsecurity.dto.dtoentrada.DtoAuthenticationResponse;
import comspring.security.tutorial.tutorialsecurity.models.AuthenticationRequest;
import comspring.security.tutorial.tutorialsecurity.models.AuthenticationResponse;
import comspring.security.tutorial.tutorialsecurity.models.Usuario;
import comspring.security.tutorial.tutorialsecurity.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
@Service
public class AuthenticationService {
    private IUsuarioRepository usuarioRepository;
    private JwtService jwtService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    @Autowired
    public AuthenticationService(IUsuarioRepository usuarioRepository, JwtService jwtService, PasswordEncoder passwordEncoder,AuthenticationManager authenticationManager) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager=authenticationManager;

    }

    public DtoAuthenticationResponse login(DtoAuthenticationRequest authenticationRequest1){
        AuthenticationRequest authenticationRequest=new AuthenticationRequest(authenticationRequest1);
        UsernamePasswordAuthenticationToken  authenticationToken=new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(),authenticationRequest.getPassword()
        );
        authenticationManager.authenticate(authenticationToken);
        Usuario usuario = usuarioRepository.findByUsername(authenticationRequest.getUsername()).get();
        String jwt = jwtService.generateToken(usuario,generateExtraClaims(usuario));

        return new DtoAuthenticationResponse(jwt);
    }


    public Map<String,Object> generateExtraClaims(Usuario usuario){
        Map<String,Object> extraClaim=new HashMap<>();
        extraClaim.put("name",usuario.getUsername());
        extraClaim.put("role",usuario.getRole());
        extraClaim.put("permisos",usuario.getAuthorities());
        return extraClaim;
    }














}
