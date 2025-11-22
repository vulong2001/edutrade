package com.web.edutrade.service;

import com.web.edutrade.model.Slide;
import com.web.edutrade.repo.SlideRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SlideService {

    @Autowired
    private SlideRepo slideRepository;

    public List<Slide> getAllSlides() {
        return slideRepository.findAll();
    }
}