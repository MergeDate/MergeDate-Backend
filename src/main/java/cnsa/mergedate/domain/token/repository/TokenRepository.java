package cnsa.mergedate.domain.token.repository;

import cnsa.mergedate.domain.token.entity.Token;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TokenRepository extends CrudRepository<Token, String> {

}
