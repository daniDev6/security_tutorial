package comspring.security.tutorial.tutorialsecurity.principal;

import comspring.security.tutorial.tutorialsecurity.enums.Role;
import comspring.security.tutorial.tutorialsecurity.models.Usuario;
import comspring.security.tutorial.tutorialsecurity.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class CrearAdmin {

    private IUsuarioRepository usuarioRepository;
    private PasswordEncoder passwordEncoder;
    @Autowired
    public CrearAdmin(IUsuarioRepository usuarioRepository,PasswordEncoder passwordEncoder) {
        this.usuarioRepository=usuarioRepository;
        this.passwordEncoder=passwordEncoder;
    }

    public CrearAdmin() {
    }

    public void crearAdmin(){
        Role roleAdmin = Role.ADMIN;

        Usuario admin = new Usuario("admin","admin",roleAdmin);

        if(usuarioRepository.findByUsername(admin.getUsername()).isPresent()){
            System.out.println("Admin ya existe en la base de datos");
            return;
        }
        admin.setPassword(passwordEncoder.encode(admin.getPassword()));
        usuarioRepository.save(admin);

    }
    public void crearUser(){
        Role roleUser = Role.USER;
        Usuario user = new Usuario("user","user",roleUser);
        if(usuarioRepository.findByUsername(user.getUsername()).isPresent()){
            System.out.println("Usuario ya existe en la base de datos");
            return;
        }
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        usuarioRepository.save(user);



    }






}
