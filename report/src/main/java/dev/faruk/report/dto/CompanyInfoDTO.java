package dev.faruk.report.dto;

import lombok.*;

/**
 * Data Transfer Object for company info. Used for creating reports.
 */
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class CompanyInfoDTO {
    private String name;
    private String address;
    private String phoneNumber;
    private String province;

    public static CompanyInfoDTO initialCompanyInfo = new CompanyInfoDTO(
            "Örnek Şirket",
            " 1234 Sok. 12345",
            "+90 555 555 55 55",
            "Istanbul");
}
