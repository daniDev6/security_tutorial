# Tutorial Spring Security Stateless
Este codigo puede ser utilizado para una base de api resfull sin estado y con jwt token

Para la configuracion se debe agregar las siguientes dependencias
## Dependencias en spring Inicializer [ir] (https://start.spring.io/)
- Spring Security
- Spring Web
- Spring Data JPA
- MySQL Driver(En casod e usar MySQL)
- Lombok(Opcional)
## Dependencias que debe agregar por su cuenta
- jjwt-api: [link](https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-api)
- jjwt-impl:  [link](https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-impl)
- jj2t-jackson: [link](https://mvnrepository.com/artifact/io.jsonwebtoken/jjwt-jackson)
## Usos y consejos

Si desea utilizar esta aplicacion como base para su api resfull debe tener encuenta que debe crear mas dto para non dejar expuesta 
sus entidades.
Lista de principales cambios:
- Borrar comentarios
- Crear un DTO para crear usuarios
- Agregar las correspondientes restricciones
- Agregar una clase para la conteccion de exceptions


## Explicacion del codigo
### Crear las entidades
Se deben crear las entidades segun su necesidad
Datos utilizados en esta api:
- id (sera generado automaticamente)
- username (debe ser unico)
- password (estara encriptado)
- role (solo tiene 2 disponibles: ADMIN, USER)


### Implementar User Detail
Se trata de implementar lo que spring security utilizara para poder authentificar

```
public class Usuario implements UserDetails {
```
Le perdira q implemente los metodos de user details

#### TENER EN CUENTA
Esta parte es vital para q spring security comprenda los roles y permisos
```
 @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        List<GrantedAuthority> listaAutorities = role.getPersmisosLista().stream().map(r->new SimpleGrantedAuthority(r.name())).collect(Collectors.toList());
        listaAutorities.add(new SimpleGrantedAuthority("ROLE_"+role.name()));
        return listaAutorities;
    }
```
### Arquitectura de Spring security
Para comprender bien el uso de spring security es vital saber como funciona internamente:

- Leer documentacion oficial   [ir ala documentacion](https://docs.spring.io/spring-security/reference/servlet/architecture.html)


### Configuracion Inicial 
#### HttpConfig
Para este paso debe crear una clase que se encargue de la configuracion de httpSecurity
- No olvidar las anotaciones para q spring comprenda que hace la clase
```
@Configuration
@EnableWebSecurity
public class HttpConfig {
```
- Inyectar las dependencias necesarias ( @Autowired usa spring para saber que se deben inyectar dependencias)
```
   private AuthenticationProvider authenticationProvider;
   private JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public HttpConfig(AuthenticationProvider authenticationProvider, JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.authenticationProvider = authenticationProvider;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

```
- Crear la configuracion necesaria para la api en funcion de la necesidad
```
@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .csrf(csrf->csrf.disable())//proteccion contra CROSS
                .sessionManagement(s->s.sessionCreationPolicy(SessionCreationPolicy.STATELESS))//Indicamos que tipo de aplicacion sera en este caso, sin estado
                .authenticationProvider(authenticationProvider)//funcion que voy a crear para delegar el tipo de authentificacion
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)//filtro que le voy a inyectar a cada request
                .authorizeHttpRequests(request->{
                    request.requestMatchers(HttpMethod.GET,"/usuario/hola").permitAll();//prueba para saber si no se necesita token
                    request.requestMatchers(HttpMethod.GET,"/usuario/traer").hasAuthority(Persmisos.TRAER_USUARIO.name());//requerira q tenga la autoridad de traer usuarios
                    request.requestMatchers(HttpMethod.POST,"/usuario/login").permitAll();
                    request.anyRequest().authenticated();
                });
        return httpSecurity.build();
    }
```

#### SecurityBeanInyector (Clase encargada de hacer la authentificacion)

- Inyectamos dependencias necesarias
```
private IUsuarioRepository usuarioRepository;
    @Autowired
    public SecurityBeanInjector(IUsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }
```
- Configuramos el authentication manager
```
 @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)throws Exception{
        return authenticationConfiguration.getAuthenticationManager();
    }

```
- Configuramos el tipo de provider
```
@Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService());
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }
```
- Indicamos a Spring el tipo de PasswordEncoder que debe usar para codificar
```
 @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

```
- Buscamos el usuario
```
    @Bean
    public UserDetailsService userDetailsService(){
        return username -> usuarioRepository.findByUsername(username)
        .orElseThrow(()->new RuntimeException("USer not found"));
    }//expresion lambda
    
```
#### Creamos un servicio para crear tokens
```
@Service
public class JwtService {
    @Value("${security.jwt.expiration-minutes}")//creamos una variable q este en las properties
    private long EXPIRATION_MINUTES;
    @Value("${security.jwt.secrete-key}")
    private String SECRET_KEY;
    public String generateToken(Usuario usuario, Map<String, Object> extraClaim){
            Date issuedAt = new Date(System.currentTimeMillis());
            Date expiration = new Date(issuedAt.getTime()+EXPIRATION_MINUTES*60*1000);
            return Jwts.builder()
                    .setClaims(extraClaim)
                    .setSubject(usuario.getUsername())
                    .setIssuedAt(issuedAt)
                    .setExpiration(expiration)
                    .setHeaderParam(Header.TYPE,Header.JWT_TYPE)
                    .signWith(generateKey(), SignatureAlgorithm.HS256)
                    .compact();
    }
    public Key generateKey(){
        byte[] secretByte = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(secretByte);
    }

    public String extractUsername(String jwt){

        return extractAllClaims(jwt).getSubject();
    }
    public Claims extractAllClaims(String jwt){
        return Jwts.parser().setSigningKey(generateKey()).build().parseClaimsJws(jwt).getBody();
    }
}

```
#### Crear la clase encargada de aplicar el filtro
Esta clase es importante debido a que sera la encargada de implementar los filtros

- Extendemos de la clase OncePerRequestFilter pra crear nuestro filtro personalizado
```
@Service
public class JwtAuthenticationFilter extends OncePerRequestFilter {
```
- Inyeccion de dependencias
```
    private JwtService jwtService;
    private IUsuarioRepository usuarioRepository;
    @Autowired
    public JwtAuthenticationFilter(JwtService jwtService, IUsuarioRepository usuarioRepository) {
        this.jwtService = jwtService;
        this.usuarioRepository = usuarioRepository;
    }

```
- Nos pedira que implementemos el metodo doFilterInternal
```
    @Override//indica que se va a sobreescribir un metodo
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");//es un estandar q cuando se envia un token se llamara Authorization
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


```
#### Crear un Servicio pra la Authentication

- Indicamos que sera un servicio
```
@Service
public class AuthenticationService {
```


- Inyeccion de dependencias
```
private IUsuarioRepository usuarioRepository;
    private JwtService jwtService;
    private PasswordEncoder passwordEncoder;
    private AuthenticationManager authenticationManager;
    @Autowired
    public AuthenticationService(IUsuarioRepository usuarioRepository, JwtService jwtService, PasswordEncoder passwordEncoder,AuthenticationManager authenticationManager) {
        this.usuarioRepository = usuarioRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager=authenticationManager;

    }
```

- creamos informacion extra para el token a la hora de verificar
```
    public Map<String,Object> generateExtraClaims(Usuario usuario){
        Map<String,Object> extraClaim=new HashMap<>();
        extraClaim.put("name",usuario.getUsername());
        extraClaim.put("role",usuario.getRole());
        extraClaim.put("permisos",usuario.getAuthorities());
        return extraClaim;
    }




```
- Creamos el metodo login para poder logearnos
```
  public DtoAuthenticationResponse login(DtoAuthenticationRequest authenticationRequest1){
        AuthenticationRequest authenticationRequest=new AuthenticationRequest(authenticationRequest1);
        UsernamePasswordAuthenticationToken  authenticationToken=new UsernamePasswordAuthenticationToken(
                authenticationRequest.getUsername(),authenticationRequest.getPassword()
        );
        authenticationManager.authenticate(authenticationToken);
        Usuario usuario = usuarioRepository.findByUsername(authenticationRequest.getUsername()).get();
        String jwt = jwtService.generateToken(usuario,generateExtraClaims(usuario));

        return new DtoAuthenticationResponse(jwt);
    }

```


### Importancias de los DTO
Los dto se crean para no dejar expuesta nuestras entidades, debido a esto para una implementacion base de este codigo
sugiero crear mas Dto ala hora de hacer login.





## Contacto
Si desea contactar por cualquier error no dude en mandarme correo electronico
danidev067@gmail.com <a href="mailto:danidev067@gmail.com">Enviarme un mensaje</a>






