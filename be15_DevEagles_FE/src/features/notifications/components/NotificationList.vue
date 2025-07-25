<!-- src/features/notifications/components/NotificationList.vue -->
<script setup>
  // ✨ [수정] 자체적인 로직(ref, onMounted, watch 등)을 모두 제거합니다.
  import BasePopover from '@/components/common/BasePopover.vue';
  import { useNotifications } from '@/features/notifications/composables/useNotifications.js';

  defineProps({
    modelValue: Boolean,
    triggerElement: Object,
  });
  const emit = defineEmits(['update:modelValue']);

  // ✨ [수정] 컴포넌트는 더 이상 자체적으로 상태를 관리하거나 API를 호출하지 않습니다.
  // 중앙 관제실에서 최종 가공된 데이터와 필요한 기능만 가져옵니다.
  const { allNotifications, isLoading, handleMarkAsRead } = useNotifications();

  // 헬퍼 함수들은 그대로 유지합니다.
  const getNotificationDetails = type => {
    switch (type) {
      case 'RESERVATION':
        return { icon: '📅', sender: '예약 시스템', color: '#3b82f6' };
      case 'ANALYSIS':
        return { icon: '📈', sender: '데이터 분석', color: '#10b981' };
      case 'NOTICE':
        return { icon: '📢', sender: '공지사항', color: '#f97316' };
      default:
        return { icon: '🔔', sender: '시스템', color: '#6b7280' };
    }
  };
  const formatDate = dateString => {
    if (!dateString) return '';
    const date = new Date(dateString);
    return `${date.getMonth() + 1}월 ${date.getDate()}일`;
  };
</script>

<template>
  <BasePopover
    :model-value="modelValue"
    :trigger-element="triggerElement"
    placement="bottom"
    size="md"
    :show-actions="false"
    :mask-closable="true"
    class="z-50"
    @update:model-value="emit('update:modelValue', $event)"
  >
    <template #default>
      <div class="notification-wrapper">
        <div class="notification-header">
          <span class="icon">🔔</span>
          <h3 class="title">새 알림</h3>
        </div>

        <div class="notification-scroll custom-scrollbar">
          <!-- ✨ [수정] 중앙 관리되는 isLoading 상태를 사용합니다. -->
          <div v-if="isLoading" class="empty-state">
            <p class="empty-text">알림을 불러오는 중입니다...</p>
          </div>

          <!-- ✨ [수정] 중앙 관리되는 최종 알림 목록(allNotifications)을 사용합니다. -->
          <ul v-else-if="allNotifications.length > 0" class="notification-list">
            <li
              v-for="item in allNotifications"
              :key="item.notificationId"
              :class="['notification-item', { 'is-read': item.read }]"
              @click="handleMarkAsRead(item)"
            >
              <div class="item-icon">
                {{ getNotificationDetails(item.type).icon }}
              </div>
              <div class="item-content">
                <div class="item-meta">
                  <span class="sender" :style="{ color: getNotificationDetails(item.type).color }">
                    {{ getNotificationDetails(item.type).sender }}
                  </span>
                  <span class="date">{{ formatDate(item.createdAt) }}</span>
                </div>
                <p class="message">{{ item.content }}</p>
              </div>
            </li>
          </ul>

          <div v-else class="empty-state empty-state--enhanced">
            <div class="empty-icon">📭</div>
            <p class="empty-text">알림이 없습니다.</p>
          </div>
        </div>
      </div>
    </template>
  </BasePopover>
</template>

<!-- 스타일 코드는 변경 없이 그대로 유지됩니다. -->
<style scoped>
  /* 이전과 동일한 스타일 코드 */
  .notification-wrapper {
    width: 100%;
    max-width: 340px;
    background-color: #fff;
    border-radius: 12px;
    box-sizing: border-box;
    overflow: hidden;
    padding: 0;
  }

  .notification-header {
    display: flex;
    align-items: center;
    gap: 8px;
    padding: 16px;
    border-bottom: 1px solid #e5e7eb;
  }

  .title {
    font-size: 14px;
    font-weight: 600;
    color: #111827;
  }

  .notification-scroll {
    max-height: 340px;
    overflow-y: auto;
  }

  .notification-list {
    display: flex;
    flex-direction: column;
    gap: 8px;
    padding: 8px 12px 16px;
  }

  .notification-item {
    display: flex;
    gap: 12px;
    padding: 14px 16px;
    border-radius: 12px;
    background-color: #fefefe;
    border: 1px solid #e0e0e0;
    box-shadow: 0 1px 2px rgba(0, 0, 0, 0.04);
    transition:
      background-color 0.2s,
      opacity 0.2s;
    word-break: break-word;
    cursor: pointer;
  }

  .notification-item:hover {
    background-color: #f9fafb;
  }

  .notification-item.is-read {
    opacity: 0.65;
  }

  .item-icon {
    font-size: 20px;
    line-height: 1;
    width: 32px;
    text-align: center;
    margin-top: 4px;
  }

  .item-content {
    flex: 1;
    min-width: 0;
  }

  .item-meta {
    display: flex;
    justify-content: space-between;
    font-size: 12px;
    margin-bottom: 6px;
  }

  .sender {
    font-weight: 600;
  }

  .date {
    color: #9ca3af;
  }

  .message {
    font-size: 13.5px;
    color: #374151;
    line-height: 1.5;
    white-space: pre-line;
  }

  .empty-state {
    padding: 40px 20px;
    text-align: center;
    color: #6b7280;
  }

  .empty-state--enhanced .empty-icon {
    font-size: 2rem;
    margin-bottom: 8px;
  }

  .custom-scrollbar {
    scrollbar-width: thin;
    scrollbar-color: rgba(0, 0, 0, 0.2) transparent;
  }

  .custom-scrollbar::-webkit-scrollbar {
    width: 5px;
  }

  .custom-scrollbar::-webkit-scrollbar-thumb {
    background-color: rgba(0, 0, 0, 0.3);
    border-radius: 6px;
  }

  .custom-scrollbar::-webkit-scrollbar-thumb:hover {
    background-color: rgba(0, 0, 0, 0.5);
  }
</style>
