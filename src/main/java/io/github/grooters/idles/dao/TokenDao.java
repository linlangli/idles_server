package io.github.grooters.idles.dao;

import io.github.grooters.idles.bean.Token;
import org.springframework.data.repository.CrudRepository;

public interface TokenDao extends CrudRepository<Token, Integer> {

    Token findByTokenNumber(String tokenNumber);

}
