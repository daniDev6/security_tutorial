package comspring.security.tutorial.tutorialsecurity;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
public class TutorialsecurityApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(TutorialsecurityApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		PasswordEncoder passwordEncoder= new BCryptPasswordEncoder();
		String contra = "daniel123";

		String pass = passwordEncoder.encode(contra);
		System.out.println(contra);
		System.out.println(pass);
		System.out.println(passwordEncoder.matches(contra,pass));
	}
}
