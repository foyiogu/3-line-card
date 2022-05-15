package com.unionbankng.future.authorizationserver.pojos;

import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class ProfileSkillRequest {

    private Long userId;

    private List<String> skills;
}
