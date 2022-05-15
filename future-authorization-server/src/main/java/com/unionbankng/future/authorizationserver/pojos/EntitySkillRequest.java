package com.unionbankng.future.authorizationserver.pojos;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class EntitySkillRequest {
    @NotNull
    private Long entityId;

    @NotNull
    private List<Long> skillIds;
}
