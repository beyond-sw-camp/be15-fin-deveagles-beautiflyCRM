package com.deveagles.be15_deveagles_be.features.membership.command.application.service.impl;

import com.deveagles.be15_deveagles_be.common.exception.BusinessException;
import com.deveagles.be15_deveagles_be.common.exception.ErrorCode;
import com.deveagles.be15_deveagles_be.features.membership.command.application.dto.request.SessionPassRequest;
import com.deveagles.be15_deveagles_be.features.membership.command.application.service.SessionPassCommandService;
import com.deveagles.be15_deveagles_be.features.membership.command.domain.aggregate.SessionPass;
import com.deveagles.be15_deveagles_be.features.membership.command.domain.repository.SessionPassRepository;
import com.deveagles.be15_deveagles_be.features.shops.command.domain.aggregate.Shop;
import com.deveagles.be15_deveagles_be.features.shops.command.repository.ShopRepository;
import java.time.LocalDateTime;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class SessionPassCommandServiceImpl implements SessionPassCommandService {

  private final SessionPassRepository sessionPassRepository;
  private final ShopRepository shopRepository;

  @Override
  public void registSessionPass(SessionPassRequest request) {

    if (request.getShopId() == null) {
      throw new BusinessException(ErrorCode.ITEMS_SHOP_ID_REQUIRED);
    }
    if (request.getSessionPassName() == null || request.getSessionPassName().isBlank()) {
      throw new BusinessException(ErrorCode.MEMBERSHIP_NAME_REQUIRED);
    }
    if (request.getSessionPassPrice() == null || request.getSessionPassPrice() <= 0) {
      throw new BusinessException(ErrorCode.MEMBERSHIP_PRICE_REQUIRED);
    }
    if (request.getExpirationPeriod() == null || request.getExpirationPeriod() <= 0) {
      throw new BusinessException(ErrorCode.MEMBERSHIP_EXPIRATION_PERIOD_REQUIRED);
    }
    if (request.getSession() == null || request.getSession() <= 0) {
      throw new BusinessException(ErrorCode.MEMBERSHIP_SESSION_REQUIRED);
    }
    if (Objects.isNull(request.getExpirationPeriodType())) {
      throw new BusinessException(ErrorCode.MEMBERSHIP_EXPIRATION_PERIOD_TYPE_REQUIRED);
    }

    Shop shop =
        shopRepository
            .findById(request.getShopId())
            .orElseThrow(() -> new BusinessException(ErrorCode.ITEMS_SHOP_NOT_FOUND));

    SessionPass sessionPass =
        SessionPass.builder()
            .shopId(shop)
            .secondaryItemId(request.getSecondaryItemId())
            .sessionPassName(request.getSessionPassName())
            .sessionPassPrice(request.getSessionPassPrice())
            .session(request.getSession())
            .expirationPeriod(request.getExpirationPeriod())
            .expirationPeriodType(request.getExpirationPeriodType())
            .bonus(request.getBonus())
            .discountRate(request.getDiscountRate())
            .sessionPassMemo(request.getSessionPassMemo())
            .createdAt(LocalDateTime.now())
            .modifiedAt(LocalDateTime.now())
            .build();

    sessionPassRepository.save(sessionPass);
  }

  @Override
  public void updateSessionPass(Long sessionPassId, SessionPassRequest request) {

    if (request.getSessionPassName() == null || request.getSessionPassName().isBlank()) {
      throw new BusinessException(ErrorCode.MEMBERSHIP_NAME_REQUIRED);
    }
    if (request.getSessionPassPrice() == null || request.getSessionPassPrice() <= 0) {
      throw new BusinessException(ErrorCode.MEMBERSHIP_PRICE_REQUIRED);
    }
    if (request.getExpirationPeriod() == null || request.getExpirationPeriod() <= 0) {
      throw new BusinessException(ErrorCode.MEMBERSHIP_EXPIRATION_PERIOD_REQUIRED);
    }
    if (request.getSession() == null || request.getSession() <= 0) {
      throw new BusinessException(ErrorCode.MEMBERSHIP_SESSION_REQUIRED);
    }
    if (Objects.isNull(request.getExpirationPeriodType())) {
      throw new BusinessException(ErrorCode.MEMBERSHIP_EXPIRATION_PERIOD_TYPE_REQUIRED);
    }

    boolean shopExists = shopRepository.existsById(request.getShopId());
    if (!shopExists) {
      throw new BusinessException(ErrorCode.ITEMS_SHOP_NOT_FOUND);
    }
    SessionPass sessionPass =
        sessionPassRepository
            .findById(sessionPassId)
            .orElseThrow(() -> new BusinessException(ErrorCode.SESSIONPASS_NOT_FOUND));

    // 필드 수정
    sessionPass.updateSessionPass(
        request.getSessionPassName(),
        request.getSecondaryItemId(),
        request.getSessionPassPrice(),
        request.getSession(),
        request.getExpirationPeriod(),
        request.getExpirationPeriodType(),
        request.getBonus(),
        request.getDiscountRate(),
        request.getSessionPassMemo());

    sessionPassRepository.save(sessionPass);
  }

  @Override
  public void deleteSessionPass(Long id) {
    SessionPass sessionPass =
        sessionPassRepository
            .findById(id)
            .orElseThrow(() -> new BusinessException(ErrorCode.SESSIONPASS_NOT_FOUND));

    sessionPass.setDeletedAt();
    sessionPassRepository.save(sessionPass);
  }
}
