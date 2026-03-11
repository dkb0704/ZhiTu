package com.dk.api.dto.job;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 职业线 DTO。
 *
 * 用于 /job/career-map 接口中，表示一条完整的职业发展线路，
 * 一般对应 career_track 表中的一条记录以及该线路下的所有节点。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CareerTrackDTO {
    /** 线路编码，例如 product / tech / operation */
    private String id;
    /** 线路名称，例如「产品线」「技术线」 */
    private String name;
    /** 按晋升顺序排列的节点列表，描绘从入门到高阶的成长路径 */
    private List<CareerNodeDTO> nodes;
}
