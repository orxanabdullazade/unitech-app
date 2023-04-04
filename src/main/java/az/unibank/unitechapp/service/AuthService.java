package az.unibank.unitechapp.service;

import az.unibank.unitechapp.request.AuthRequest;
import az.unibank.unitechapp.response.LoginSuccessResponse;
import az.unibank.unitechapp.response.RegisterSuccessResponse;
import org.springframework.stereotype.Service;


@Service
public interface AuthService {

    RegisterSuccessResponse registerUser(AuthRequest authRequest);

    LoginSuccessResponse loginUser(AuthRequest authRequest);
}
