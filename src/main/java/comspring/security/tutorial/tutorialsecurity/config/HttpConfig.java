package comspring.security.tutorial.tutorialsecurity.config;

import comspring.security.tutorial.tutorialsecurity.config.filter.JwtAuthenticationFilter;
import comspring.security.tutorial.tutorialsecurity.enums.Persmisos;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
@Configuration
@EnableWebSecurity
public class HttpConfig {
    private AuthenticationProvider authenticationProvider;
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public HttpConfig(AuthenticationProvider authenticationProvider, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }




    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .csrf(csrf->csrf.disable())
                .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//Indicamos que tipo de aplicacion sera en este caso, sin estado
                .authenticationProvider(authenticationProvider)//funcion que voy a crear para delegar el tipo de authentificacion
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .authorizeHttpRequests(request->{
                    request.requestMatchers(HttpMethod.GET,"/usuario/hola").permitAll();//prueba para saber si no se necesita token
                    request.requestMatchers(HttpMethod.GET,"/usuario/traer").hasAuthority(Persmisos.TRAER_USUARIO.name());//requerira q tenga la autoridad de traer usuarios
                    request.requestMatchers(HttpMethod.POST,"/usuario/crear").hasAuthority(Persmisos.GUARDAR_USUARIO.name());
                    request.requestMatchers(HttpMethod.POST,"/usuario/login").permitAll();
                    request.anyRequest().authenticated();
                });
        return httpSecurity.build();


    }
}
