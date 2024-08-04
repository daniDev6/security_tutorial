package comspring.security.tutorial.tutorialsecurity.models;

import comspring.security.tutorial.tutorialsecurity.dto.dtoentrada.DtoAuthenticationRequest;

public class AuthenticationRequest {
    private String username;
    private String password;

    public AuthenticationRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public AuthenticationRequest(DtoAuthenticationRequest authenticationRequest) {
        this.username=authenticationRequest.nombre();
        this.password=authenticationRequest.contrasena();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
