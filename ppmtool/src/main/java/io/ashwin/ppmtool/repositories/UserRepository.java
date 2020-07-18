package io.ashwin.ppmtool.repositories;

import io.ashwin.ppmtool.domain.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends CrudRepository<User,Long> {

    User findByUsername(String username);

    User getById(Long id); // get instead of find to avoid returning optional


}
