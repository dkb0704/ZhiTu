package com.dk.infrastructure.adapter.repository;

import com.dk.domain.job.adapter.repository.ICareerMapRepository;
import com.dk.domain.job.model.entity.CareerNodeEntity;
import com.dk.domain.job.model.entity.CareerTrackEntity;
import com.dk.domain.job.model.entity.TransferOptionEntity;
import com.dk.infrastructure.dao.ICareerMapDao;
import com.dk.infrastructure.dao.po.CareerNodePO;
import com.dk.infrastructure.dao.po.CareerTransferOptionPO;
import com.dk.infrastructure.dao.po.CareerTrackPO;
import org.springframework.stereotype.Repository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 岗位图谱仓储实现：从 career_track / career_node / career_transfer_option 组装为前端结构。
 */
@Repository
public class CareerMapRepositoryImpl implements ICareerMapRepository {

    @Resource
    private ICareerMapDao careerMapDao;

    @Override
    public List<CareerTrackEntity> getFullMap() {
        List<CareerTrackPO> tracks = careerMapDao.selectAllTracks();
        if (tracks == null || tracks.isEmpty()) {
            return new ArrayList<>();
        }
        List<CareerTrackEntity> result = new ArrayList<>();
        for (CareerTrackPO t : tracks) {
            List<CareerNodePO> nodePOs = careerMapDao.selectNodesByTrackId(t.getId());
            List<CareerNodeEntity> nodes = new ArrayList<>();
            for (CareerNodePO n : nodePOs) {
                List<CareerTransferOptionPO> optPOs = careerMapDao.selectTransferOptionsByNodeId(n.getId());
                List<TransferOptionEntity> opts = optPOs == null ? new ArrayList<>() : optPOs.stream()
                        .map(o -> TransferOptionEntity.builder()
                                .title(o.getTargetTitle())
                                .desc(o.getTargetDesc() != null ? o.getTargetDesc() : "")
                                .build())
                        .collect(Collectors.toList());
                nodes.add(CareerNodeEntity.builder()
                        .id(n.getCode())
                        .title(n.getTitle())
                        .hasTransfer(n.getHasTransfer() != null && n.getHasTransfer() == 1)
                        .transferOptions(opts.isEmpty() ? null : opts)
                        .build());
            }
            result.add(CareerTrackEntity.builder()
                    .id(t.getCode())
                    .name(t.getName())
                    .nodes(nodes)
                    .build());
        }
        return result;
    }
}
