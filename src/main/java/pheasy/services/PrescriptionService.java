package pheasy.services;

import com.mongodb.MongoClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import pheasy.beans.ApprovalState;
import pheasy.beans.PrecriptionRights;
import pheasy.beans.Prescription;
import pheasy.beans.PrescriptionUserRightMapping;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by jayeshathila
 * on 12/06/17.
 */
@Service
public class PrescriptionService {

    // On live systems it is not advisable to hard code values in class. Instead we should use a db/property file
    // to store these.


    public static final String TEST_DATABSE = "test";

    private final MongoTemplate mongoTemplate;

    @Autowired
    public PrescriptionService(Environment env) {

        String HOST = env.getProperty("spring.data.mongodb.host");
        int PORT = Integer.valueOf(env.getProperty("spring.data.mongodb.port"));

        MongoClient client = new MongoClient(HOST, PORT);
        SimpleMongoDbFactory simpleMongoDbFactory = new SimpleMongoDbFactory(client, TEST_DATABSE);
        this.mongoTemplate = new MongoTemplate(simpleMongoDbFactory);
    }


    public List<Prescription> findPrescriptionsForUsername(String username) {
        Query query = new Query(Criteria.where(Prescription.PrescriptionMapping.ASSOCIATED_USERNAME).is(username));
        query.fields().include(Prescription.PrescriptionMapping.PRESCRIPTION_TITLE).include("_id");
        return mongoTemplate.find(query, Prescription.class);
    }

    public Prescription getDetailsForPrescriptionTitle(String prescriptionTitle, String loggedInUsername) throws IllegalAccessException {

        Query query = new Query(Criteria.where(Prescription.PrescriptionMapping.PRESCRIPTION_TITLE).is(prescriptionTitle));
        Prescription prescription = mongoTemplate.findOne(query, Prescription.class);

        if (!prescription.isUserAccessibleToDetails(loggedInUsername)) {
            throw new IllegalAccessException("Logged in user: " + loggedInUsername + ", do not have permission to access the prescription");
        }

        return prescription;
    }

    public List<Prescription> saveAll(List<Prescription> prescriptions) {
        if (CollectionUtils.isEmpty(prescriptions)) {
            return Collections.emptyList();
        }

        mongoTemplate.insertAll(prescriptions);
        return prescriptions;
    }

    public Prescription requestApproval(String loggedInUser, String prescriptionTitle) {
        Query query = new Query(Criteria.where(Prescription.PrescriptionMapping.PRESCRIPTION_TITLE).is(prescriptionTitle));
        Prescription prescription = mongoTemplate.findOne(query, Prescription.class);

        // User himself do require approval
        if (prescription.getAssociatedUsername().equals(loggedInUser)) {
            return prescription;
        }

        prescription.addToUsernameVsRights(loggedInUser,
                new PrescriptionUserRightMapping(loggedInUser, ApprovalState.PENDING, PrecriptionRights.ADMIN));

        mongoTemplate.save(prescription);
        return prescription;
    }

    public Prescription approvePrescription(String username, String loggedInUser, String prescriptionTitle) throws IllegalAccessException {

        Query query = new Query(Criteria.where(Prescription.PrescriptionMapping.PRESCRIPTION_TITLE).is(prescriptionTitle));
        Prescription prescription = mongoTemplate.findOne(query, Prescription.class);

        // Prescription can only be edited to whom the same belongs
        if (!prescription.getAssociatedUsername().equals(loggedInUser)) {
            throw new IllegalAccessException("Only user himself can approve of the prescription");
        }

        // For demo purpose I have given only admin rights to every one
        Map<String, PrescriptionUserRightMapping> usernameVsRights = prescription.getUsernameVsRights();
        if (CollectionUtils.isEmpty(usernameVsRights)) {
            return prescription;
        }

        PrescriptionUserRightMapping mapping = usernameVsRights.get(username);
        if (mapping != null) {
            mapping.setRights(PrecriptionRights.ADMIN);
            mapping.setState(ApprovalState.SUCCESSFUL);
        }

        mongoTemplate.save(prescription);
        return prescription;
    }

    public void deleteAll() {
        mongoTemplate.dropCollection(Prescription.class);
    }

}

