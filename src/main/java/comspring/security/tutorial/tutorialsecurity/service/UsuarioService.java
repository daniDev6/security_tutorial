package comspring.security.tutorial.tutorialsecurity.service;

import comspring.security.tutorial.tutorialsecurity.dto.dtoentrada.DtoUsuarioCrear;
import comspring.security.tutorial.tutorialsecurity.dto.dtorespuestas.DtoUsuarioRespuesta;
import comspring.security.tutorial.tutorialsecurity.models.Usuario;
import comspring.security.tutorial.tutorialsecurity.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
@Service
public class UsuarioService {
    private IUsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder;
    @Autowired
    public UsuarioService(IUsuarioRepository usuarioRepository,PasswordEncoder passwordEncoder) {
        this.passwordEncoder=passwordEncoder;
        this.usuarioRepository = usuarioRepository;
    }

    public DtoUsuarioRespuesta crearUsuario(DtoUsuarioCrear dtoUsuario) {
            Usuario usuario = new Usuario(dtoUsuario);
        try{
            usuario.setPassword(dtoUsuario.contrasena());
            usuarioRepository.save(usuario);
        }catch (Exception e){
            new RuntimeException(e);
        }
        return new DtoUsuarioRespuesta(usuario.getUsername(),usuario.getRole());


    }
    public DtoUsuarioRespuesta traerPoId(Long id){
        Usuario usuario=usuarioRepository.findById(id).get();
        DtoUsuarioRespuesta dtoUsuarioRespuesta = new DtoUsuarioRespuesta(usuario.getUsername(),usuario.getRole());
        return dtoUsuarioRespuesta;
    }
    public List<DtoUsuarioRespuesta> traerUsuarios(){
        List<Usuario> usuarios = usuarioRepository.findAll();

        List<DtoUsuarioRespuesta> usuarioRespuestas = new ArrayList<DtoUsuarioRespuesta>();
        usuarios.stream().forEach(u->{
            usuarioRespuestas.add(new DtoUsuarioRespuesta(u.getUsername(),u.getRole()));
        });
        return usuarioRespuestas;
    }


    public String borrarPorId(Long id) {
        try{
            usuarioRepository.deleteById(id);
            return "Se borro con exito";
        }catch (Exception e){
            new RuntimeException(e);
            return "No se pudo borrar";
        }
    }
}
