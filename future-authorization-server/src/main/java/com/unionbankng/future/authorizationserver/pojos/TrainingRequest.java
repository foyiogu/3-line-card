package com.unionbankng.future.authorizationserver.pojos;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class TrainingRequest {

    private Long trainingId;

    private Long profileId;

    private Long userId;

    private String title;

    private String organization;

    private String yearAwarded;


    private String linkOrId;

    private String description;

}
