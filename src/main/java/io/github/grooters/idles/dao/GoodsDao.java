package io.github.grooters.idles.dao;

import io.github.grooters.idles.bean.Goods;
import org.springframework.data.repository.CrudRepository;
import java.util.List;

public interface GoodsDao extends CrudRepository<Goods, Integer> {

    List<Goods> findBySellerNumber(String number);

    Goods findById(int id);

    Goods findByGoodsNumber(String goodsNumber);

    List<Goods> findAll();

}
