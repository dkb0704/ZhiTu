package com.dk.domain.llm.model.valobj;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 岗位清洗结果值对象。
 *
 * 用于保存模型或规则对岗位的清洗判断结果。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobCleaningResultVO {

    /**
     * 是否为计算机相关岗位。
     */
    private boolean csJob;

    /**
     * 岗位所属领域。
     */
    private String jobDomain;

    /**
     * 岗位类别。
     */
    private String jobCategory;

    /**
     * JD 质量信息。
     */
    private QualityVO quality;

    /**
     * 判断原因。
     */
    private String reason;

    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class QualityVO {
        /**
         * JD 是否完整。
         */
        private boolean jdComplete;

        /**
         * 是否包含岗位职责。
         */
        private boolean hasResponsibilities;

        /**
         * 是否包含任职要求。
         */
        private boolean hasRequirements;
    }
}
