package com.web.edutrade.repo;

import com.web.edutrade.model.Slide;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SlideRepo extends JpaRepository<Slide, Long> {
}