package learn.petnote.controllers;


import io.jsonwebtoken.Jwts;
import learn.petnote.domain.Result;
import learn.petnote.domain.ResultType;
import learn.petnote.domain.UserService;
import learn.petnote.models.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/user")
@CrossOrigin(origins = {"http://localhost:5173"})
public class UserController {

    UserService service;

    SecretSigningKey secretSigningKey;

    public UserController(UserService service, SecretSigningKey secretSigningKey) {
        this.service = service;
        this.secretSigningKey = secretSigningKey;
    }

    @PostMapping
    public ResponseEntity<Object> createUser(@RequestBody User user) {
        Result<User> result = service.createUser(user);
        if (result.isSuccess()) {
            return new ResponseEntity<>(result.getpayload(), HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>(result.getErrorMessages(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/authenticate")
    public ResponseEntity<Object> authenticate(@RequestBody User user) {
        Result<User> result = service.authenticate(user.getUsername(), user.getPassword());
        if (result.isSuccess()) {
            String jwt = Jwts.builder()
                    .claim("username", result.getpayload().getUsername())
                    .claim("id", result.getpayload().getId())
                    .signWith(secretSigningKey.getSigningKey())
                    .compact();
            Map<String, String> jwtMap = new HashMap<>();
            jwtMap.put("jwt", jwt);
            return new ResponseEntity<>(jwtMap, HttpStatus.OK);
        } else if (result.getResultType() == ResultType.NOT_FOUND) {
            return new ResponseEntity<>(result.getErrorMessages(), HttpStatus.NOT_FOUND);
        } else {
            return new ResponseEntity<>(result.getErrorMessages(), HttpStatus.UNAUTHORIZED);
        }
    }

    @GetMapping("/verify")
    public ResponseEntity<String> verifyUser(@RequestParam("token") String token) {
        System.out.println("Verify called with token: " + token);
        boolean verified = service.verifyUser(token);

        System.out.println("Verification result: " + verified);

        if (verified) {
            return ResponseEntity.ok("Your email has been verified. You may now log in.");
        } else {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body("Invalid or expired verification link.");
        }
    }



}
