package comspring.security.tutorial.tutorialsecurity.dto.dtorespuestas;

import comspring.security.tutorial.tutorialsecurity.enums.Role;

public record DtoUsuarioRespuesta(
        String nombre,
        Role role
){
}