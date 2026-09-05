package com.sih.advisor.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * DTO for SWOT (Strengths, Weaknesses, Opportunities, Threats) analysis.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SwotAnalysisDTO {

    private List<String> strengths;
    private List<String> weaknesses;
    private List<String> opportunities;
    private List<String> threats;
}
