package io.github.grooters.idles.dao;

import io.github.grooters.idles.bean.User;
import io.github.grooters.idles.bean.Works;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Transactional
@Repository
public interface UserDao extends CrudRepository<User, Integer> {

    User findByUserNumber(String userNumber);

    User findByEmail(String email);

    List<User> findAll();

    User findByTokenNumber(String token);

}
