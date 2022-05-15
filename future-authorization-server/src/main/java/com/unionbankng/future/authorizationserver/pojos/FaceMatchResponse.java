package com.unionbankng.future.authorizationserver.pojos;

import lombok.Data;

@Data

public class FaceMatchResponse {
    private String score;
    private String verdict;
    private String message;
}
