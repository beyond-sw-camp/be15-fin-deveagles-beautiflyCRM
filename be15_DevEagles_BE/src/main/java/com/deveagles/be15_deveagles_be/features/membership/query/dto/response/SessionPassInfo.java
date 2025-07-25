package com.deveagles.be15_deveagles_be.features.membership.query.dto.response;

import java.util.Date;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SessionPassInfo {
  private String sessionPassName;
  private String secondaryItemName;
  private Integer remainingCount;
  private Date expirationDate;
}
