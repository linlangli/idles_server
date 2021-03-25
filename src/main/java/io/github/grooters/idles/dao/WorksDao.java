package io.github.grooters.idles.dao;

import io.github.grooters.idles.bean.Works;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface WorksDao extends CrudRepository<Works, Integer> {

    List<Works> findAll();

    Works findById(long id);

    Works findByWorksNumber(String worksNumber);

}
