package com.web.edutrade.service;

import com.web.edutrade.model.PackageInfo;
import com.web.edutrade.repo.PackageInfoRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PackageInfoService {

    private final PackageInfoRepo packageInfoRepo;

    /**
     * Lấy tất cả thông tin gói
     */
    public List<PackageInfo> getAllPackages() {
        return packageInfoRepo.findAll();
    }

    /**
     * Lấy thông tin gói theo ID
     */
    public PackageInfo getPackageById(Long id) {
        return packageInfoRepo.findById(id)
                .orElseThrow(() -> new RuntimeException("Không tìm thấy gói với ID: " + id));
    }

    /**
     * Tạo hoặc cập nhật gói
     */
    public PackageInfo savePackage(PackageInfo packageInfo) {
        return packageInfoRepo.save(packageInfo);
    }

    /**
     * Xóa gói
     */
    public void deletePackage(Long id) {
        packageInfoRepo.deleteById(id);
    }
}