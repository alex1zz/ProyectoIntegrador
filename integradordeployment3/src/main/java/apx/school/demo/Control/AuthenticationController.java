package apx.school.demo.Control;

import apx.school.demo.Servicio.AuthService;
import apx.school.demo.Dto.auth.AuthDto;
import apx.school.demo.Dto.auth.LoginDto;
import apx.school.demo.Dto.auth.RegisterDto;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {

    @Autowired
    private AuthService authService;

    @PostMapping(value = "/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginDto login) {
        try {
            AuthDto authDto = this.authService.login(login);
            return ResponseEntity.ok(authDto);
        } catch (UsernameNotFoundException | BadCredentialsException e) {
            // Manejo de excepciones específico
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(e.getMessage());
        } catch (Exception e) {
            // Manejo de excepciones generales
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error inesperado");
        }
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody RegisterDto dto) {
        try {
            AuthDto authDto = this.authService.register(dto);
            return ResponseEntity.ok(authDto);
        } catch (Exception e) {
            // Manejo de excepciones generales
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Ocurrió un error inesperado");
        }
    }
}
