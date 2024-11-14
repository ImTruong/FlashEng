<script setup>
  import { ref, computed } from 'vue';
  import { formatDistanceToNow } from 'date-fns';
  import { useRouter } from 'vue-router'; 

  const router = useRouter();
  const props = defineProps({
    notification: {
      type: Object,
      required: true
    }
  });
  const emit = defineEmits(['notifyParent']);
  const isModalVisible = ref(false); 
  const modalAction = ref('');
  const getIconSrc = (type) => {
    switch (type) {
      case 'inviter':
        return 'path_to_inviter_icon';
      case 'REMINDER_STUDY_SESSION':
        return 'path_to_reminder_icon';
      case 'CLASS_JOIN_REQUEST':
        return 'path_to_join_icon';
      default:
        return 'default_icon_path';
    }
  };
  
  const formattedDate = computed(() => {
    return formatDistanceToNow(new Date(props.notification.createdAt), { addSuffix: true });
  });
 
</script>
<template>
  <div class="notification-item" :class="{ 'read': notification.isRead }" >
    <div class="icon">
      <img :src="getIconSrc(notification.type)" alt="icon" />
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
    transition: opacity 0.3s ease, background-color 0.3s ease;
    box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1); /* Thêm bóng đổ cho notification */ 
    cursor: pointer; /* Con trỏ khi di chuột vào */
  }

  .notification-item.read {
    /* opacity: 0.5; */
    background-color: #f8f9fa;
  }

  .notification-item:hover {
    background-color: #f1f1f1; /* Màu nền khi hover */
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
    margin: 0;
  }

  .metadata {
    font-size: 12px;
    color: #6c757d;
  }
  
</style>