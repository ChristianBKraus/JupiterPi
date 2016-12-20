package jupiterpi.models;

import org.springframework.data.repository.CrudRepository;

import jupiterpi.models.User;

public interface UserRepository extends CrudRepository<User, String>{

}
