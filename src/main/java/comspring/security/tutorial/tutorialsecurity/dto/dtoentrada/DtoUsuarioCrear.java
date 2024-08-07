package comspring.security.tutorial.tutorialsecurity.dto.dtoentrada;

import comspring.security.tutorial.tutorialsecurity.enums.Role;

public record DtoUsuarioCrear(
        String nombre,
        String contrasena,
        Role role
) {
}
