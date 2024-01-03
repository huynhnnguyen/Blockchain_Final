package com.edenhub.gateway.service.serviceitf;

import com.edenhub.gateway.domain.User;
import com.edenhub.gateway.service.dto.UserDTO;

public interface UserServiceInterface {
    User createUser(UserDTO userDTO);

    void deleteUser(String login);
}
