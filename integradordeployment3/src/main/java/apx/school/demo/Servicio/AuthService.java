package apx.school.demo.Servicio;


import apx.school.demo.Entity.UserEntity;
import apx.school.demo.Repository.MongoDBRepository;
import apx.school.demo.Config.JwtService;
import apx.school.demo.Util.Role;
import apx.school.demo.Dto.auth.AuthDto;
import apx.school.demo.Dto.auth.LoginDto;
import apx.school.demo.Dto.auth.RegisterDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class AuthService {

    private MongoDBRepository userRepository;

    private PasswordEncoder passwordEncoder;

    private JwtService jwtService;

    private AuthenticationManager authenticationManager;

    @Autowired
    public AuthService(MongoDBRepository userRepository,
                       PasswordEncoder passwordEncoder,
                       JwtService jwtService,
                       AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    public AuthDto login(final LoginDto request){
        try {
            // Intenta autenticar al usuario
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));

            // Si la autenticación es exitosa, busca al usuario en el repositorio
            UserDetails user = userRepository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado con el email: " + request.getEmail()));

            // Genera el token JWT para el usuario autenticado
            String token = jwtService.getToken(user);
            return new AuthDto(token);

        } catch (UsernameNotFoundException e) {
            // Maneja el caso donde el usuario no fue encontrado
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado", e);

        } catch (BadCredentialsException e) {
            // Maneja el caso donde las credenciales son incorrectas
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales incorrectas", e);

        } catch (Exception e) {
            // Maneja cualquier otra excepción inesperada
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Ocurrió un error inesperado durante el inicio de sesión", e);
        }
    }

    public AuthDto register(final RegisterDto request){
        UserEntity user = new UserEntity();
        user.setName(request.getName());
        user.setEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(Role.USER);

        userRepository.save(user);
        return new AuthDto(this.jwtService.getToken(user));
    }
}
