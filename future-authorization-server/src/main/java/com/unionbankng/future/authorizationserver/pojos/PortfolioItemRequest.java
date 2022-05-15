package com.unionbankng.future.authorizationserver.pojos;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import java.util.List;

@Data
public class PortfolioItemRequest {

    private Long portfolioItemId;

    private Long profileId;

    private Long userId;

    private String title;

    private String description;

    private String link;
}
