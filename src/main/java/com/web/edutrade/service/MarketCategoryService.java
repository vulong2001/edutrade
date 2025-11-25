package com.web.edutrade.service;

import com.web.edutrade.repo.MarketCategory;
import com.web.edutrade.model.MarketCategoryEntity;
import java.util.List;

public interface MarketCategoryService {

    List<MarketCategory> getAll();

    MarketCategory getById(Long id);

    MarketCategory create(MarketCategoryEntity category);

    MarketCategory update(Long id, MarketCategoryEntity category);

    void delete(Long id);

    boolean existsById(Long id);

    List<MarketCategory> getAllActive();

    List<MarketCategory> searchByName(String name);
}
