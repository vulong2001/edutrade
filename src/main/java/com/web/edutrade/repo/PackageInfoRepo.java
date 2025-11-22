package com.web.edutrade.repo;

import com.web.edutrade.model.Customer;
import com.web.edutrade.model.PackageInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PackageInfoRepo extends JpaRepository<PackageInfo, Long> {
}
