package io.github.grooters.idles.dao;

import io.github.grooters.idles.bean.Verification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@Repository
public interface VerificationDao extends CrudRepository<Verification, Integer> {

    Verification findByEmail(String email);

}
