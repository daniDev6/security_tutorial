package comspring.security.tutorial.tutorialsecurity;

import comspring.security.tutorial.tutorialsecurity.principal.CrearAdmin;
import comspring.security.tutorial.tutorialsecurity.repository.IUsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class TutorialsecurityApplication implements CommandLineRunner {
	IUsuarioRepository usuarioRepository;
	PasswordEncoder passwordEncoder;
	CrearAdmin crearAdmin;
	@Autowired
	public TutorialsecurityApplication(IUsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, CrearAdmin crearAdmin) {
		this.usuarioRepository = usuarioRepository;
		this.passwordEncoder = passwordEncoder;
		this.crearAdmin = crearAdmin;
	}

	public static void main(String[] args) {
		SpringApplication.run(TutorialsecurityApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		crearAdmin.crearAdmin();
		crearAdmin.crearUser();
	}
}
