package stream.okchun.dashboard.database.repos.auth;

import lombok.NonNull;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import stream.okchun.dashboard.database.entity.auth.User;

@Repository
public interface UserRepository extends CrudRepository<@NonNull User, @NonNull Long>, UserRepositoryCustom {
	User findByEmail(String email);
}
