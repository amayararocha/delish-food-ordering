package com.delish.controller;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.delish.config.JwtProvider;
import com.delish.model.Cart;
import com.delish.model.USER_ROLE;
import com.delish.model.User;
import com.delish.repository.CartRepository;
import com.delish.repository.UserRepository;
import com.delish.request.LoginRequest;
import com.delish.response.AuthResponse;
import com.delish.service.CustomerUserDetailsService;

@RestController
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private PasswordEncoder passwordEnconder;

	@Autowired
	private JwtProvider jwtProvider;

	@Autowired
	private CustomerUserDetailsService customerUserDatailsService;

	@Autowired
	private CartRepository cartRepository;

	@PostMapping("/signup") // cria o usuario
	public ResponseEntity<AuthResponse> createdUserHendler(@RequestBody User user) throws Exception {

		User isEmailExist = userRepository.findByEmail(user.getEmail());

		if (isEmailExist != null) {
			throw new Exception("Email is already used with another account");
		}

		User createdUser = new User();
		createdUser.setEmail(user.getEmail());
		createdUser.setFullName(user.getFullName());
		createdUser.setRole(user.getRole());
		createdUser.setPassword(passwordEnconder.encode(user.getPassword()));

		User savedUser = userRepository.save(createdUser);

		Cart cart = new Cart();
		cart.setCustumer(savedUser);
		cartRepository.save(cart);

		Authentication authentication = new UsernamePasswordAuthenticationToken(user.getEmail(), user.getPassword());
		SecurityContextHolder.getContext().setAuthentication(authentication);		

		String jwt = jwtProvider.generateToken(authentication);

		AuthResponse authResponse = new AuthResponse();
		authResponse.setJwt(jwt);
		authResponse.setMessage("Register sucess");
		authResponse.setRole(savedUser.getRole());

		return new ResponseEntity<>(authResponse, HttpStatus.CREATED);
	}

	@PostMapping("/signin") // faz o login se já possuir uma conta
	public ResponseEntity<AuthResponse> signin(@RequestBody LoginRequest req){
		
		String username = req.getEmail();
		
		String password = req.getPassword();
		
		Authentication authentication = authenticate(username, password);
		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		String role = authorities.isEmpty()?null:authorities.iterator().next().getAuthority();
		String jwt = jwtProvider.generateToken(authentication);

		AuthResponse authResponse = new AuthResponse();
		authResponse.setJwt(jwt);
		authResponse.setMessage("Login sucess");
		authResponse.setRole(USER_ROLE.valueOf(role));

		return new ResponseEntity<>(authResponse, HttpStatus.OK);
		
	}
	//metodo de autenticação
	private Authentication authenticate(String username, String password) {
		
		UserDetails userDetails = customerUserDatailsService.loadUserByUsername(username);
		
		if (userDetails == null) {
			throw new BadCredentialsException("Invalid username...");
		}
		
		if (!passwordEnconder.matches(password, userDetails.getPassword())) {
			throw new BadCredentialsException("Invalid password...");
		}
		
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}
}
