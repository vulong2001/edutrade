package com.web.edutrade.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity
@Data
public class PackageInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    /** Thời hạn hợp đồng */
    private String durationMonths;
    /** Vốn tối thiểu*/
    private String minCapitalMillion;
    /** Lợi nhuận theo chu kỳ */
    private String cycleProfitRange;
    /** Lợi nhuận KH hưởng*/
    private String customerProfitPercent;
    /** Rủi ro tối đa trên vốn */
    private String maxRiskPercent;

}
