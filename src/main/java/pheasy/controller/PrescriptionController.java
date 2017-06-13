package pheasy.controller;

import com.google.common.collect.Maps;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import pheasy.beans.ApprovalState;
import pheasy.beans.Prescription;
import pheasy.beans.SecuredUserDetails;
import pheasy.services.PrescriptionService;

import java.io.IOException;
import java.util.List;
import java.util.Map;

/**
 * Created by jayeshathila
 * on 13/06/17.
 */
@RestController
@RequestMapping("prescriptions")
public class PrescriptionController extends AbstractController {

    private final PrescriptionService prescriptionService;

    @Autowired
    public PrescriptionController(PrescriptionService prescriptionService) {
        this.prescriptionService = prescriptionService;
    }

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<Prescription> getUsersPrescriptions(@RequestParam("username") String username) throws IOException {
        if (StringUtils.isEmpty(username)) {
            throw new IllegalArgumentException("Username cannot be empty");
        }

        super.authenticateForNonPatient();

        return this.prescriptionService.findPrescriptionsForUsername(username);
    }

    @RequestMapping(value = "/requestApproval/{prescriptionTitle}", method = RequestMethod.POST)
    @ResponseBody
    public Map<String, String> requestApproval(@PathVariable("prescriptionTitle") String prescriptionTitle) throws IOException, IllegalAccessException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        SecuredUserDetails principal = (SecuredUserDetails) auth.getPrincipal();

        String uname = principal.getUsername();
        Prescription prescription = prescriptionService.requestApproval(uname, prescriptionTitle);
        ApprovalState state = prescription.getUsernameVsRights().get(uname).getState();
        Map<String, String> rv = Maps.newHashMap();
        rv.put("title", prescription.getPrescriptionTitle());
        rv.put("state", state.name());

        return rv;
    }

    @RequestMapping(value = "/approve/{prescriptionTitle}", method = RequestMethod.POST)
    @ResponseBody
    public Prescription approvePrescription(@RequestParam("usernameToApprove") String username, @PathVariable("prescriptionTitle") String prescriptionTitle) throws IOException, IllegalAccessException {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        SecuredUserDetails principal = (SecuredUserDetails) auth.getPrincipal();

        return prescriptionService.approvePrescription(username, principal.getUsername(), prescriptionTitle);
    }

    @RequestMapping(value = "/details", method = RequestMethod.GET)
    @ResponseBody
    public Prescription getUsersPrescriptionDetails(@RequestParam(value = "prescriptionTitle", required = true) String prescriptionTitle) throws IllegalAccessException {
        if (StringUtils.isEmpty(prescriptionTitle)) {
            throw new IllegalArgumentException("Prescription Id cannot be empty");
        }

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        SecuredUserDetails principal = (SecuredUserDetails) auth.getPrincipal();

        Prescription prescription = this.prescriptionService.getDetailsForPrescriptionTitle(prescriptionTitle, principal.getUsername());
        // Trimming some field which we do not want to expose to other roles
        if (!prescription.getAssociatedUsername().equals(principal.getUsername())) {
            prescription.setUserRightMappings(null);
        }

        return prescription;
    }
}
