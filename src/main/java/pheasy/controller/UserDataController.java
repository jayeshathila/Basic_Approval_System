package pheasy.controller;

import com.google.common.collect.Sets;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import pheasy.beans.SecuredUserDetails;
import pheasy.repositories.UserRepository;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * Created by jayeshathila
 * on 12/06/17.
 */
@RestController
@RequestMapping("user")
public class UserDataController extends AbstractController {

    public static final Set<String> ROLES_WITH_PATIENT_ACCESS = Sets.newHashSet("doctor", "pharmacist");

    private final UserRepository userRepository;

    @Autowired
    public UserDataController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }


    /**
     * Only Doctors and Pharmacists can access patient list
     */
    @RequestMapping(value = "/patients", method = RequestMethod.GET)
    @ResponseBody
    public List<String> getAllPatientNames() throws IOException {
        super.authenticateForNonPatient();

        List<SecuredUserDetails> all = this.userRepository.findAll();
        List<String> rv = new ArrayList<>();
        for (SecuredUserDetails securedUserDetails : all) {
            rv.add(securedUserDetails.getUsername());
        }

        return rv;
    }

}
