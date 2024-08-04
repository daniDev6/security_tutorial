package comspring.security.tutorial.tutorialsecurity.config.filter;

import comspring.security.tutorial.tutorialsecurity.models.Usuario;
import comspring.security.tutorial.tutorialsecurity.repository.IUsuarioRepository;
import comspring.security.tutorial.tutorialsecurity.service.JwtService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
@Service
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private JwtService jwtService;
    private IUsuarioRepository usuarioRepository;
    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService, IUsuarioRepository usuarioRepository) {
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {

            filterChain.doFilter(request, response);
            return;
        }
        String jwt = authHeader.split(" ")[1];
        String username = jwtService.extractUsername(jwt);
        Usuario usuario1 = usuarioRepository.findByUsername(username).get();
        UsernamePasswordAuthenticationToken authenticationToken=new UsernamePasswordAuthenticationToken(
                username,null,usuario1.getAuthorities()
        );
        SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        filterChain.doFilter(request,response);
    }
}
