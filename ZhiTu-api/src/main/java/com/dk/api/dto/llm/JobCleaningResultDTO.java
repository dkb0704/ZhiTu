package com.dk.api.dto.llm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 岗位清洗结果 DTO。
 *
 * 用于承接模型对单个岗位的清洗判断结果，核心包括：
 * 1. 该岗位是否属于计算机相关岗位
 * 2. 岗位大类属于什么方向
 * 3. 当前 JD 的完整度和可用性
 * 4. 模型给出的解释原因
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobCleaningResultDTO {

    /**
     * 是否为计算机/信息技术相关岗位。
     */
    private boolean csJob;

    /**
     * 岗位所属领域，例如“计算机/信息技术”“非计算机”等。
     */
    private String jobDomain;

    /**
     * 岗位类别，例如“后端开发”“测试”“运维”“实施/技术支持”等。
     */
    private String jobCategory;

    /**
     * JD 质量判断结果。
     */
    private QualityDTO quality;

    /**
     * 模型给出的判断理由，便于人工核验和答辩展示。
     */
    private String reason;

    /**
     * JD 质量明细 DTO。
     */
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QualityDTO {

        /**
         * JD 是否整体完整，是否具备后续抽取岗位画像的价值。
         */
        private boolean jdComplete;

        /**
         * 是否包含岗位职责、工作内容等信息。
         */
        private boolean hasResponsibilities;

        /**
         * 是否包含任职要求、技能要求、学历要求等信息。
         */
        private boolean hasRequirements;
    }
}
