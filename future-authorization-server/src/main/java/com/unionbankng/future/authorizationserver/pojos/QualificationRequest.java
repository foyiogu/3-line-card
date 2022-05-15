package com.unionbankng.future.authorizationserver.pojos;

import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class QualificationRequest {
    private Long qualificationId;

    private Long userId;

    private Long profileId;

    private String school;

    private String country;
    @NotNull
    private String degree;
    private String fieldOfStudy;
    @NotNull//2012
    private String startYear;

    private String endYear;
    private String grade;
    private String description;
    private String activities;
    private String media;
}
