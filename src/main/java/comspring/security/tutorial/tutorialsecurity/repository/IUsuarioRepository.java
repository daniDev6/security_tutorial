package comspring.security.tutorial.tutorialsecurity.repository;

import comspring.security.tutorial.tutorialsecurity.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IUsuarioRepository extends JpaRepository<Usuario,Long> {
}
