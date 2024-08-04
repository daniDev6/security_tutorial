package comspring.security.tutorial.tutorialsecurity.controllers;

import comspring.security.tutorial.tutorialsecurity.dto.dtoentrada.DtoAuthenticationRequest;
import comspring.security.tutorial.tutorialsecurity.dto.dtoentrada.DtoAuthenticationResponse;
import comspring.security.tutorial.tutorialsecurity.dto.dtorespuestas.DtoUsuario;
import comspring.security.tutorial.tutorialsecurity.models.AuthenticationRequest;
import comspring.security.tutorial.tutorialsecurity.models.AuthenticationResponse;
import comspring.security.tutorial.tutorialsecurity.models.Usuario;
import comspring.security.tutorial.tutorialsecurity.repository.IUsuarioRepository;
import comspring.security.tutorial.tutorialsecurity.service.AuthenticationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("usuario")
public class UsuarioController {
    private IUsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder;
    private AuthenticationService authenticationService;
    @Autowired
    public UsuarioController(IUsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, AuthenticationService authenticationService) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationService = authenticationService;
    }
    @PostMapping("/login")
    public ResponseEntity<DtoAuthenticationResponse> login(@RequestBody DtoAuthenticationRequest authenticationRequest){
        System.out.println("Entradno");
        System.out.println("Entradno");
        return ResponseEntity.ok(authenticationService.login(authenticationRequest));
    }

    @GetMapping("/traer")
    public ResponseEntity<DtoUsuario> traerUsuarioPorUsername(){
        System.out.println("Entradno");
        System.out.println("Entradno");
        String username="cv32";
        Usuario usuario=usuarioRepository.findByUsername(username).get();
        System.out.println(usuario.getAuthorities());
        DtoUsuario dtoUsuario = new DtoUsuario(usuario.getUsername(),usuario.getRole());
        return ResponseEntity.ok(dtoUsuario);
    }

    @GetMapping("/hola")
    public String hola(){
        return "Hola mundo";
    }

















}
