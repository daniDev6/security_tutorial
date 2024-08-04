package comspring.security.tutorial.tutorialsecurity.repository;

import comspring.security.tutorial.tutorialsecurity.models.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface IUsuarioRepository extends JpaRepository<Usuario,Long> {
    public Optional<Usuario> findByUsername(String username);
}
