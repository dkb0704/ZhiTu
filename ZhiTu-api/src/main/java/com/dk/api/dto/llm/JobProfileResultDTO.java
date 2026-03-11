package com.dk.api.dto.llm;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * 岗位画像抽取结果 DTO。
 *
 * 该结构用于保存模型从 JD 中抽取出的结构化岗位要求，
 * 后续可直接用于人岗匹配、推荐解释和岗位标签展示。
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobProfileResultDTO {

    /**
     * 岗位 ID，一般对应 job_position.id。
     */
    private String jobId;

    /**
     * 必须掌握的技能或技术栈。
     */
    private List<String> skillsMust;

    /**
     * 加分项技能，不满足也可能可以投递，但具备会更有优势。
     */
    private List<String> skillsNice;

    /**
     * 最低学历要求，例如“本科”“专科”。
     */
    private String degreeMin;

    /**
     * 优先专业列表，例如“计算机科学与技术”“软件工程”等。
     */
    private List<String> majorsPreferred;

    /**
     * 最低经验年限，校招或实习岗位可为 0。
     */
    private Integer expYearsMin;

    /**
     * 软技能要求，例如沟通能力、团队协作、学习能力等。
     */
    private List<String> softSkills;

    /**
     * 其他限制条件，例如出差、值班、地域限制等。
     */
    private List<String> otherConstraints;
}
