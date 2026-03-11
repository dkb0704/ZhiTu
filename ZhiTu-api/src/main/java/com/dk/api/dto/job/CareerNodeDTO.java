package com.dk.api.dto.job;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 岗位图谱节点 DTO。
 *
 * 表示岗位图谱中的单个节点，一般对应 career_node 表中的一行记录，
 * 并附带从 career_transfer_option 表聚合出的转岗建议。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CareerNodeDTO {
    /** 节点编码，例如 p1/p2/t3，用于前后端识别和定位 */
    private String id;
    /** 节点展示名称，即岗位名称，如「产品经理」「高级开发」 */
    private String title;
    /** 是否存在可转岗选项，用于前端展示「可转岗」浮层入口 */
    private Boolean hasTransfer;
    /** 可转岗目标岗位列表 */
    private List<TransferOptionDTO> transferOptions;
}
