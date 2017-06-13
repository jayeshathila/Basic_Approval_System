package pheasy.controller;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import pheasy.beans.SecuredUserDetails;

/**
 * Created by jayeshathila
 * on 13/06/17.
 */
public class AbstractController {

    public void authenticateForNonPatient() {
        SimpleGrantedAuthority authority = ((SecuredUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getAuthorities().get(0);
        if (!UserDataController.ROLES_WITH_PATIENT_ACCESS.contains(authority.getAuthority())) {
            throw new IllegalArgumentException("You do not have sufficient role to access patients");
        }
    }

}
