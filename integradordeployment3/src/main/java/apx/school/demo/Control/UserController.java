package apx.school.demo.Control;

import apx.school.demo.Dto.UserDto;
import apx.school.demo.Entity.UserEntity;
import apx.school.demo.Servicio.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public ResponseEntity<List<UserDto>> findAll(){
        List<UserDto> lista = this.userService.getAll();
        return ResponseEntity.ok(lista);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> findById(@PathVariable("id") String id){
        UserDto usuario = this.userService.getById(id);
        return ResponseEntity.ok(usuario);
    }

    @PostMapping("/")
    public ResponseEntity<UserDto> save(@RequestBody UserDto userDto){
        UserDto saved = this.userService.save(userDto);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> update(@RequestBody UserDto userDto, @PathVariable("id") String id){
        UserDto updated = this.userService.update(userDto, id);
        return ResponseEntity.ok(updated);

    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") String id){
        this.userService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
