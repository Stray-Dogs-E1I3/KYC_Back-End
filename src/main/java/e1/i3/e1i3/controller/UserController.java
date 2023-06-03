package e1.i3.e1i3.controller;

import e1.i3.e1i3.domain.user.User;
import e1.i3.e1i3.dto.user.UserLoginReqDTO;
import e1.i3.e1i3.service.user.UserService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@CrossOrigin(origins = {"http://localhost:3000"})
public class UserController {

    @Value("${SecretKey}")
    String secretKey;
    private final UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    //유저가 지갑로그인을 하면 JWT토큰을 반환합니다.
    @PostMapping(value = "/login")
    public ResponseEntity<String> login(@RequestBody UserLoginReqDTO userLoginReqDTO){
        String address=userLoginReqDTO.getUserAddress();

        //db에 저장
        userService.login(address);

        String token = generateToken(userLoginReqDTO.getUserAddress());


        return ResponseEntity.ok().body(token);
    }



    private String generateToken(String address) {
        String token = Jwts.builder()
                .setSubject(address)
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
        return token;
    }


}
