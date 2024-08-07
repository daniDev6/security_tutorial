package comspring.security.tutorial.tutorialsecurity.controllers;

import comspring.security.tutorial.tutorialsecurity.dto.dtoentrada.DtoAuthenticationRequest;
import comspring.security.tutorial.tutorialsecurity.dto.dtoentrada.DtoAuthenticationResponse;
import comspring.security.tutorial.tutorialsecurity.dto.dtoentrada.DtoUsuarioCrear;
import comspring.security.tutorial.tutorialsecurity.dto.dtorespuestas.DtoUsuarioRespuesta;
import comspring.security.tutorial.tutorialsecurity.models.Usuario;
import comspring.security.tutorial.tutorialsecurity.repository.IUsuarioRepository;
import comspring.security.tutorial.tutorialsecurity.service.AuthenticationService;
import comspring.security.tutorial.tutorialsecurity.service.UsuarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("usuario")
public class UsuarioController {
    private IUsuarioRepository usuarioRepository;
    private UsuarioService usuarioService;
    private AuthenticationService authenticationService;
    @Autowired
    public UsuarioController(IUsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, AuthenticationService authenticationService, UsuarioService usuarioService) {
        this.usuarioRepository = usuarioRepository;
        this.usuarioService=usuarioService;
        this.authenticationService = authenticationService;
    }
    @PostMapping("/login")
    public ResponseEntity<DtoAuthenticationResponse> login(@RequestBody DtoAuthenticationRequest authenticationRequest){
        return ResponseEntity.ok(authenticationService.login(authenticationRequest));
    }
    @GetMapping("/traer/{id}")
    public ResponseEntity<DtoUsuarioRespuesta> traerUsuarioPorUsername(@PathVariable Long id){
        return ResponseEntity.ok(usuarioService.traerPoId(id));
    }
    @GetMapping("/traer")
    public ResponseEntity<List<DtoUsuarioRespuesta>> traerUsuarios(){
        return ResponseEntity.ok(usuarioService.traerUsuarios());
    }
    @PostMapping("/crear")
    public ResponseEntity<DtoUsuarioRespuesta> crearUsuario(@RequestBody DtoUsuarioCrear dtoUsuario){
        return ResponseEntity.ok(usuarioService.crearUsuario(dtoUsuario));
    }
    @DeleteMapping("/borrar/{id}")
    public ResponseEntity<String> borrarUsuario(@PathVariable Long id){
        return ResponseEntity.ok(usuarioService.borrarPorId(id));
    }


    @GetMapping("/hola")
    public String hola(){
        return "Hola mundo";
    }

















}
