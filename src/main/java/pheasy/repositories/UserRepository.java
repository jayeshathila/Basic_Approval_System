package pheasy.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import pheasy.beans.SecuredUserDetails;

import java.util.List;

/**
 * Created by jayeshathila
 * on 12/06/17.
 */
public interface UserRepository extends MongoRepository<SecuredUserDetails, String> {

    SecuredUserDetails findByUsername(String firstName);

    @Query(value = "{'authorities.role':'patient'}", fields = "{ 'username' : 1}")
    List<SecuredUserDetails> findAll();
}
