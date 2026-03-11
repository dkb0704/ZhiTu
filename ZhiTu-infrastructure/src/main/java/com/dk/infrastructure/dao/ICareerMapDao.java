package com.dk.infrastructure.dao;

import com.dk.infrastructure.dao.po.CareerNodePO;
import com.dk.infrastructure.dao.po.CareerTrackPO;
import com.dk.infrastructure.dao.po.CareerTransferOptionPO;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * 岗位图谱 DAO。
 *
 * 对应 career_track / career_node / career_transfer_option 三张表，
 * 提供查询职业线路、节点以及转岗建议的基础数据访问能力。
 */
@Mapper
public interface ICareerMapDao {

    /** 查询所有职业线路（按排序值升序） */
    List<CareerTrackPO> selectAllTracks();

    /**
     * 按职业线路查询该线路下的全部节点（按排序值升序）。
     *
     * @param trackId 职业线路主键 ID（career_track.id）
     */
    List<CareerNodePO> selectNodesByTrackId(@Param("trackId") Long trackId);

    /**
     * 按节点查询该节点下配置的全部转岗建议（按排序值升序）。
     *
     * @param nodeId 节点主键 ID（career_node.id）
     */
    List<CareerTransferOptionPO> selectTransferOptionsByNodeId(@Param("nodeId") Long nodeId);
}
