package com.edenhub.gateway.web.rest.vm;

import com.edenhub.gateway.service.dto.UserDTO;
import javax.validation.constraints.Size;

/**
 * View Model extending the UserDTO, which is meant to be used in the user management UI.
 */
public class ManagedUserVM extends UserDTO {

    public static final int PASSWORD_MIN_LENGTH = 4;

    public static final int PASSWORD_MAX_LENGTH = 100;


    public ManagedUserVM() {
        // Empty constructor needed for Jackson.
    }


    @Override
    public String toString() {
        return "ManagedUserVM{" + super.toString() + "} ";
    }
}
