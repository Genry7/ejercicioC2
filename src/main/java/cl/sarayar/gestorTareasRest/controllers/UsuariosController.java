package cl.sarayar.gestorTareasRest.controllers;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import cl.sarayar.gestorTareasRest.config.auth.UserDetailsImpl;
import cl.sarayar.gestorTareasRest.config.auth.dto.JwtResponse;
import cl.sarayar.gestorTareasRest.config.auth.dto.MessageResponse;
import cl.sarayar.gestorTareasRest.entities.Usuario;
import cl.sarayar.gestorTareasRest.services.UsuariosService;
import cl.sarayar.gestorTareasRest.utils.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/usuarios")
public class UsuariosController {

	@Autowired
	UsuariosService usService;
	@Autowired
	JwtUtils jwtUtils;

	@PostMapping("/login")
	public ResponseEntity<?> authenticateUser(@RequestBody Usuario usuario) {


		return ResponseEntity.ok(usuario);
	}

	@PostMapping("/registrar")
	public ResponseEntity<?> registerUser( @RequestBody Usuario usuario) {
		if (this.usService.existsByCorreo(usuario.getCorreo())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Usuario ya existe!"));
		}

		usuario.setEstado(1);
		
		return ResponseEntity.ok(usService.save(usuario));
	}

	@PostMapping("/actualizar")
    public ResponseEntity<?> actualizarUsuario(@RequestBody Usuario usuario) {
		Usuario usuarioOriginal = usService.findById(usuario.getId());
		Usuario usuarioConCorreo = this.usService.findByCorreo(usuario.getCorreo());
		if (usuarioConCorreo!= null && !usuarioConCorreo.getId().equals(usuario.getId())) {
			return ResponseEntity
					.badRequest()
					.body(new MessageResponse("Error: Correo se encuentra utilizado!"));
		}
		usuarioOriginal.setNombre(usuario.getNombre());
		usuarioOriginal.setCorreo(usuario.getCorreo());
		usuarioOriginal.setEstado(usuario.getEstado());
		return ResponseEntity.ok(usService.save(usuarioOriginal));
		
	}

	@GetMapping("/get")
	public List<Usuario> getAll(){
		return this.usService.getAll();
	}	
	
}
