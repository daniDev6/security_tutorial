package comspring.security.tutorial.tutorialsecurity.models;

import comspring.security.tutorial.tutorialsecurity.dto.dtoentrada.DtoAuthenticationResponse;

public class AuthenticationResponse {
    private String jwt;

    public AuthenticationResponse(String jwt) {
        this.jwt = jwt;
    }
    public AuthenticationResponse(DtoAuthenticationResponse authenticationResponse) {
        this.jwt = authenticationResponse.token();
    }


    public String getJwt() {
        return jwt;
    }

    public void setJwt(String jwt) {
        this.jwt = jwt;
    }
}
