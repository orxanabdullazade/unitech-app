package az.unibank.unitechapp.service.impl;

import az.unibank.unitechapp.exception.CustomErrorException;
import az.unibank.unitechapp.exception.ErrorCodeEnum;
import az.unibank.unitechapp.model.User;
import az.unibank.unitechapp.repository.UserRepository;
import az.unibank.unitechapp.request.AuthRequest;
import az.unibank.unitechapp.response.LoginSuccessResponse;
import az.unibank.unitechapp.response.RegisterSuccessResponse;
import az.unibank.unitechapp.service.AuthService;
import az.unibank.unitechapp.util.JwtTokenUtil;
import az.unibank.unitechapp.util.PasswordHasher;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.stereotype.Service;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final JwtTokenUtil jwtTokenUtil;

    @SneakyThrows
    @Override
    public RegisterSuccessResponse registerUser(AuthRequest authRequest) {

        Optional<User> registeredUser = userRepository.findByPin(authRequest.getPin());

        if (registeredUser.isPresent()) {
            throw new CustomErrorException(ErrorCodeEnum.ALREADY_REGISTERED);
        } else {
            String password=PasswordHasher.hashPassword(authRequest.getPassword());

            User user=new User();
            user.setPin(authRequest.getPin());
            user.setPassword(password);
            userRepository.save(user);
            return RegisterSuccessResponse
                    .builder()
                    .success("true")
                    .build();
        }

    }

    @SneakyThrows
    @Override
    public LoginSuccessResponse loginUser(AuthRequest authRequest) {
        User loginUser = userRepository.findByPin(authRequest.getPin())
                .orElseThrow(() -> new CustomErrorException(ErrorCodeEnum.WRONG_CREDENTIALS));

        if(PasswordHasher.verifyPassword(authRequest.getPassword(),loginUser.getPassword())) {
            String token = jwtTokenUtil.generateToken(String.valueOf(loginUser.getId()));

            return LoginSuccessResponse
                    .builder()
                    .success("true")
                    .token(token)
                    .build();
        }else{
            throw new CustomErrorException(ErrorCodeEnum.WRONG_CREDENTIALS);
        }
    }


}

