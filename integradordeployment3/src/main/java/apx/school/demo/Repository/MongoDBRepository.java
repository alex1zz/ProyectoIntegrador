package apx.school.demo.Repository;

import apx.school.demo.Entity.UserEntity;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MongoDBRepository extends MongoRepository<UserEntity, String> {

    Optional<UserEntity> findByEmail(String email);

}
