package az.unibank.unitechapp.service.impl;

import az.unibank.unitechapp.exception.CustomErrorException;
import az.unibank.unitechapp.model.User;
import az.unibank.unitechapp.repository.UserRepository;
import az.unibank.unitechapp.request.AuthRequest;
import az.unibank.unitechapp.response.LoginSuccessResponse;
import az.unibank.unitechapp.response.RegisterSuccessResponse;
import az.unibank.unitechapp.util.JwtTokenUtil;
import az.unibank.unitechapp.util.PasswordHasher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.security.GeneralSecurityException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class AuthServiceImplTest {

    private String token="eyJhbGciOiJIUzUxMiJ9.eyJpYXQiOjE2Nj" +
            "EwODQ5MTEsImV4cCI6MTY2MTA4ODUxMSwic3ViIjoiMSJ9.ami9TOUpVJi5X" +
            "dvXrwFE5in_f3EF8Q9cxh-tVK1-6DiWfDHsCfDTDAZ4mY-TrQ49ilDv5e1uCcoxG-BzLH1LtA";

    private UserRepository userRepository;
    private JwtTokenUtil jwtTokenUtil;
    private AuthServiceImpl authService;

    @BeforeEach
    void setUp() throws Exception {
        userRepository = mock(UserRepository.class);
        jwtTokenUtil = mock(JwtTokenUtil.class);
        authService = new AuthServiceImpl(userRepository,jwtTokenUtil);
        when(jwtTokenUtil.validateToken(token)).thenReturn(true);
    }

    @Test
    public void testRegisterUser() throws GeneralSecurityException {
        AuthRequest authRequest=AuthRequest.builder()
                .pin("test1")
                .password("1234")
                .build();
       User user= User.builder()
                .id(3)
                .pin("test2")
                .password(PasswordHasher.hashPassword("123"))
                .build();

        when(userRepository.findByPin(authRequest.getPin())).thenReturn(Optional.empty());
        when(userRepository.save(user)).thenReturn(user);

        RegisterSuccessResponse registerSuccessResponse=authService.registerUser(authRequest);

        assertNotNull(registerSuccessResponse);
        assertEquals("true",registerSuccessResponse.getSuccess());

    }

    @Test
    public void testRegisterUserWithError(){
        Exception ex= assertThrows(CustomErrorException.class,()-> {
            AuthRequest authRequest=AuthRequest.builder()
                    .pin("test1")
                    .password("1234")
                    .build();
            User user= User.builder()
                    .id(3)
                    .pin("test2")
                    .password(PasswordHasher.hashPassword("123"))
                    .build();

            when(userRepository.findByPin(authRequest.getPin())).thenReturn(Optional.of(user));
            when(userRepository.save(user)).thenReturn(user);

            RegisterSuccessResponse registerSuccessResponse = authService.registerUser(authRequest);

            assertNotNull(registerSuccessResponse);
            assertEquals("true", registerSuccessResponse.getSuccess());
        });
        assertEquals("pin already registered",ex.getMessage());

    }

    @Test
    public void testLoginUserSuccess() throws GeneralSecurityException {
        AuthRequest authRequest=AuthRequest.builder()
                .pin("orxan")
                .password("123")
                .build();
        Optional<User> user= Optional.ofNullable(User.builder()
                .id(1)
                .pin("orxan")
                .password(PasswordHasher.hashPassword("123"))
                .build());
        when(userRepository.findByPin(authRequest.getPin())).thenReturn(user);
        when(jwtTokenUtil.generateToken(String.valueOf(user.get().getId()))).thenReturn(token);

        LoginSuccessResponse loginSuccessResponse=authService.loginUser(authRequest);

        assertNotNull(loginSuccessResponse);
        assertEquals("true",loginSuccessResponse.getSuccess());
        assertEquals(token,loginSuccessResponse.getToken());

    }

    @Test
    public void testLoginUserFailureWithWrongPin(){
        Exception ex= assertThrows(CustomErrorException.class,()->{
            AuthRequest authRequest=AuthRequest.builder()
                    .pin("orxan")
                    .password("123")
                    .build();
            Optional<User> user= Optional.ofNullable(User.builder()
                    .id(1)
                    .pin("orxan")
                    .password(PasswordHasher.hashPassword("123"))
                    .build());
            when(userRepository.findByPin(authRequest.getPin())).thenReturn(Optional.empty());
            when(jwtTokenUtil.generateToken(String.valueOf(user.get().getId()))).thenReturn(token);

            LoginSuccessResponse loginSuccessResponse=authService.loginUser(authRequest);

            assertNotNull(loginSuccessResponse);
            assertEquals("true",loginSuccessResponse.getSuccess());
            assertEquals(token,loginSuccessResponse.getToken());
        });
        assertEquals("pin or password is wrong",ex.getMessage());

    }

    @Test
    public void testLoginUserFailureWithWrongPassword(){
        Exception ex= assertThrows(CustomErrorException.class,()->{
            AuthRequest authRequest=AuthRequest.builder()
                    .pin("orxan")
                    .password("123")
                    .build();
            Optional<User> user= Optional.ofNullable(User.builder()
                    .id(1)
                    .pin("orxan")
                    .password(PasswordHasher.hashPassword("12345"))
                    .build());
            when(userRepository.findByPin(authRequest.getPin())).thenReturn(user);
            when(jwtTokenUtil.generateToken(String.valueOf(user.get().getId()))).thenReturn(token);

            LoginSuccessResponse loginSuccessResponse=authService.loginUser(authRequest);

            assertNotNull(loginSuccessResponse);
            assertEquals("true",loginSuccessResponse.getSuccess());
            assertEquals(token,loginSuccessResponse.getToken());
        });
        assertEquals("pin or password is wrong",ex.getMessage());

    }


}