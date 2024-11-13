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
  const openModal = () => {
    if (props.notification.type === 'CLASS_JOIN_REQUEST') {
      isModalVisible.value = true;
      console.log("Modal should be visible now");
    } else {
      router.push('/review');
    }
  };
  const closeModal = () => {
    isModalVisible.value = false;
  };

  const handleModalAction = async (action) => {
    if (props.notification.type != 'CLASS_JOIN_REQUEST'){
      return;
    }
    modalAction.value = action;
    try {
      const token = localStorage.getItem('token')
      const response = await axios.get(`/class/request/join/${action}?requestId=${props.notification.id}`, {
        headers: {
          Authorization: `Bearer ${token}` 
        }
      });
      console.log(response.data); 
    } catch (error) {
      console.error('API error:', error);
    }
    closeModal();
  };
</script>
<template>
  <div class="notification-item" :class="{ 'read': notification.isRead }"  @click="openModal">
    <div class="icon">
      <img :src="getIconSrc(notification.type)" alt="icon" />
    </div>
    <div class="content">
      <p class="message">{{ notification.message }}</p>
      <span class="metadata">{{ formattedDate }}</span>
    </div>
    <div v-if="isModalVisible" class="modal-overlay" @click="closeModal">
      <div class="modal-content" @click.stop>
        <h3>{{ notification.message }}</h3>
        <p>Bạn muốn tham gia lớp học này?</p>
        <button @click="handleModalAction('accept')">Chấp nhận</button>
        <button @click="handleModalAction('reject')">Từ chối</button>
      </div>
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
    opacity: 0.5;
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
  .modal-overlay {
    position: fixed;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background-color: rgba(0, 0, 0, 0.6); /* Màu nền tối hơn */
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 14;
  }
  
  .modal-content {
    background-color: #ffffff;
    padding: 20px;
    border-radius: 8px;
    box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
    text-align: center;
    width: 300px;
  }
  
  .modal-content h3 {
    font-size: 18px;
    margin-bottom: 12px;
    color: #495057; /* Màu chữ modal */
  }
  
  button {
    background-color: #007bff;
    color: white;
    border: none;
    padding: 10px 15px;
    margin: 5px;
    border-radius: 4px;
    cursor: pointer;
    font-size: 14px;
  }
  
  button:hover {
    background-color: #0056b3;
  }
  
  button:focus {
    outline: none;
  }
  
  button:active {
    background-color: #003d80;
  }
</style>
  