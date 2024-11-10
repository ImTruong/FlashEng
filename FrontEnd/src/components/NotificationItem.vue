<script setup>
  import { computed } from 'vue';
  import { formatDistanceToNow } from 'date-fns/formatDistanceToNow';

  
  const props = defineProps({
    notification: {
      type: Object,
      required: true
    }
  });
  
  
  const iconSrc = computed(() => {
    switch (props.notification.type) {
      case 'inviter':
        return 'path_to_inviter_icon';
      case 'reminder':
        return 'path_to_reminder_icon';
      case 'join':
        return 'path_to_join_icon';
      default:
        return 'default_icon_path';
    }
  });
  
  const formattedDate = computed(() => {
    return formatDistanceToNow(new Date(props.notification.created_at), { addSuffix: true });
  });
</script>
<template>
    <div class="notification-item" :class="{ 'read': notification.is_read }">
      <div class="icon">
        <img :src="iconSrc" alt="icon" />
      </div>
      <div class="content">
        <p class="message">{{ notification.message }}</p>
        <span class="metadata">{{ formattedDate }}</span>
      </div>
    </div>
</template>
  
  
  
<style scoped>
  .notification-item {
    display: flex;
    align-items: center;
    padding: 8px;
    border-radius: 8px;
    background-color: #f8f9fa;
    margin-bottom: 10px;
  }
  
  .notification-item.read {
    opacity: 0.5;
  }
  
  .icon img {
    width: 24px;
    height: 24px;
    margin-right: 10px;
  }
  
  .content {
    display: flex;
    flex-direction: column;
  }
  
  .message {
    font-size: 14px;
    font-weight: bold;
  }
  
  .metadata {
    font-size: 12px;
    color: #6c757d;
  }
</style>
  