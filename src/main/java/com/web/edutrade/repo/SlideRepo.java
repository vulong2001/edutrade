package com.web.edutrade.repo;

import com.web.edutrade.model.Slide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository


public interface SlideRepo extends JpaRepository<Slide, Long> {

    //  auto_update = true
    List<Slide> findByAutoUpdateTrue();

    // stock_symbol
    List<Slide> findByStockSymbolIsNotNull();

    //  stock symbol
    List<Slide> findByStockSymbol(String stockSymbol);

    // Custom query: Tìm slides cần update (chưa update > 5 phút)
    @Query("SELECT s FROM Slide s WHERE s.autoUpdate = true AND " +
            "(s.lastUpdated IS NULL OR s.lastUpdated < CURRENT_TIMESTAMP - 300000)")
    List<Slide> findSlidesNeedingUpdate();


}

