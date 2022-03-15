package com.company.topaloq.service;

import com.company.topaloq.dto.auth.AuthDTO;
import com.company.topaloq.dto.auth.RegistrationDTO;
import com.company.topaloq.dto.UserDTO;

public interface AuthService {

    UserDTO registration(RegistrationDTO dto);

    UserDTO login(AuthDTO dto);

    void verify(String jwt);

}
