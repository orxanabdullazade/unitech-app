package az.unibank.unitechapp.controller;

import az.unibank.unitechapp.request.AuthRequest;
import az.unibank.unitechapp.response.LoginSuccessResponse;
import az.unibank.unitechapp.response.RegisterSuccessResponse;
import az.unibank.unitechapp.service.AuthService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
@ApiOperation("Auth Service")
public class AuthController {

    public final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<RegisterSuccessResponse> registerUser(@RequestBody AuthRequest authRequest){
        return ResponseEntity.ok(authService.registerUser(authRequest));
    }

    @PostMapping("/login")
    public ResponseEntity<LoginSuccessResponse>  loginUser(@RequestBody AuthRequest authRequest){
        return ResponseEntity.ok(authService.loginUser(authRequest));
    }
    

}
