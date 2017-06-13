package pheasy;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import pheasy.beans.MedicineConsumptionTime;
import pheasy.beans.Prescription;
import pheasy.beans.SecuredUserDetails;
import pheasy.repositories.UserRepository;
import pheasy.services.PrescriptionService;
import pheasy.utils.TimeConstants;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;

@SpringBootApplication
public class Application implements CommandLineRunner {


    public static final String PATIENT_ROLE = "patient";
    public static final String DOCTOR_ROLE = "doctor";
    public static final String PHARMACIST_ROLE = "Pharmacist";

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PrescriptionService prescriptionService;

    public static void main(String[] args) throws Throwable {
        SpringApplication.run(Application.class, args);
    }


    /**
     * This method runs on every deployment so here it is used as temporary data generator and cleaner
     */
    @Override
    public void run(String... args) throws Exception {

        userRepository.deleteAll();
        prescriptionService.deleteAll();

        List<SimpleGrantedAuthority> authorities = new ArrayList<>(1);
        authorities.add(new SimpleGrantedAuthority(PATIENT_ROLE));

        SecuredUserDetails jayeshUser = userRepository.save(new SecuredUserDetails("jayesh", "jayesh", authorities));
        SecuredUserDetails vinayUser = userRepository.save(new SecuredUserDetails("vinay", "vinay", authorities));
        SecuredUserDetails paritaUser = userRepository.save(new SecuredUserDetails("parita", "parita", authorities));

        authorities = new ArrayList<>(1);
        authorities.add(new SimpleGrantedAuthority(DOCTOR_ROLE));
        userRepository.save(new SecuredUserDetails("doctor", "doctor", authorities));

        authorities = new ArrayList<>(1);
        authorities.add(new SimpleGrantedAuthority(PHARMACIST_ROLE));
        userRepository.save(new SecuredUserDetails("pharmacist", "pharmacist", authorities));

        List<Prescription> jayeshPrescriptions = new ArrayList<>();
        jayeshPrescriptions.add(createPrescription(jayeshUser.getUsername(), "JAYESH_PRESCRIPTION_1", "MED_1", "MED_2"));
        jayeshPrescriptions.add(createPrescription(jayeshUser.getUsername(), "JAYESH_PRESCRIPTION_2", "MED_3", "MED_4"));
        prescriptionService.saveAll(jayeshPrescriptions);

        List<Prescription> vinayPrescriptions = new ArrayList<>();
        vinayPrescriptions.add(createPrescription(vinayUser.getUsername(), "VINAY_PRESCRIPTION_1", "MED_1", "MED_2"));
        prescriptionService.saveAll(vinayPrescriptions);
    }

    private Prescription createPrescription(String associatedUsername, String title, String... medName) {
        Prescription prescription = new Prescription();
        prescription.setAssociatedUsername(associatedUsername);
        long currTime = new Date().getTime();
        prescription.setPrescriptionTitle(title);
        for (String s : medName) {
            prescription.addMedicineConsumptionTime(new MedicineConsumptionTime(s).addCosumption(currTime)
                    .addCosumption(Math.abs(currTime - new Random().nextInt(23) * TimeConstants.SECS_IN_HOUR)));
        }

        return prescription;
    }
}
