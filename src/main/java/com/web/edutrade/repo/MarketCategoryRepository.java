package com.web.edutrade.repo;

import com.web.edutrade.model.MarketCategoryEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MarketCategoryRepository extends JpaRepository<MarketCategoryEntity, Long> {

    Optional<MarketCategoryEntity> findByName(String name);

    List<MarketCategoryEntity> findByNameContainingIgnoreCase(String keyword);

    boolean existsByName(String name);

    List<MarketCategoryEntity> findByActiveTrue();

    List<MarketCategoryEntity> findByActive(Boolean active);
}
