package com.web.edutrade.service;

import com.web.edutrade.repo.MarketCategory;
import com.web.edutrade.model.MarketCategoryEntity;
import com.web.edutrade.repo.MarketCategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class MarketCategoryServiceImpl implements MarketCategoryService {

    private final MarketCategoryRepository repository;

    public MarketCategoryServiceImpl(MarketCategoryRepository repository) {
        this.repository = repository;
    }

    @Override
    public List<MarketCategory> getAll() {
        return repository.findAll()
                .stream()
                .map(entity -> (MarketCategory) entity)
                .collect(Collectors.toList());
    }

    @Override
    public MarketCategory getById(Long id) {
        MarketCategoryEntity entity = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));
        return entity;
    }

    @Override
    public MarketCategory create(MarketCategoryEntity category) {
        if (category.getId() != null) {
            throw new IllegalArgumentException("ID must be null when creating new category");
        }

        if (repository.existsByName(category.getName())) {
            throw new IllegalArgumentException("Category with name '" + category.getName() + "' already exists");
        }

        return repository.save(category);
    }

    @Override
    public MarketCategory update(Long id, MarketCategoryEntity category) {
        MarketCategoryEntity existing = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found with id: " + id));

        if (category.getName() != null && !category.getName().isEmpty()) {
            repository.findByName(category.getName()).ifPresent(found -> {
                if (!found.getId().equals(id)) {
                    throw new IllegalArgumentException("Category name already exists");
                }
            });
            existing.setName(category.getName());
        }

        if (category.getDescription() != null) {
            existing.setDescription(category.getDescription());
        }

        if (category.getActive() != null) {
            existing.setActive(category.getActive());
        }

        return repository.save(existing);
    }

    @Override
    public void delete(Long id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Cannot delete. Category not found with id: " + id);
        }
        repository.deleteById(id);
    }

    @Override
    public boolean existsById(Long id) {
        return repository.existsById(id);
    }

    @Override
    public List<MarketCategory> getAllActive() {
        return repository.findByActiveTrue()
                .stream()
                .map(entity -> (MarketCategory) entity)
                .collect(Collectors.toList());
    }

    @Override
    public List<MarketCategory> searchByName(String name) {
        return repository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(entity -> (MarketCategory) entity)
                .collect(Collectors.toList());
    }
}