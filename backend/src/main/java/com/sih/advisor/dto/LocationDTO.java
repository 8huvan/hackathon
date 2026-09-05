package com.sih.advisor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO for location details.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LocationDTO {

    private String village;
    private String block;
    private String district;
    private String state;
}
