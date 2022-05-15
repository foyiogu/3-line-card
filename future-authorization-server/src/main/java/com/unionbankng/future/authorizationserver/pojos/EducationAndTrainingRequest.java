package com.unionbankng.future.authorizationserver.pojos;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Data
public class EducationAndTrainingRequest {

    private Long qualificationId;

    private Long profileId;

    private Long userId;

    private String school;

    private String country;

    private String degree;

    private String fieldOfStudy;

    private String startYear;

    private String endYear;

    private String grade;

    private String description;

    private String activities;

    private String media;

    private Long trainingId;

    private String trainingTitle;

    private String trainingOrganization;

    private String trainingYearAwarded;

    private String trainingLinkOrId;

    private String trainingDescription;
}
