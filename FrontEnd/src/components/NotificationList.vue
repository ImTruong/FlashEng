<script setup>
  import { ref, onMounted } from 'vue';
  import axios from 'axios';
  import NotificationItem from './NotificationItem.vue';
  
  const notifications = ref([]);

  // Fetch notifications from the API
  const fetchNotifications = async () => {
    try {
      const token = localStorage.getItem("token");
      if (!token) {
        console.warn("Token không tồn tại");
        return;
      }
      const response = await axios.get("/notification/user", {
        headers: {
          Authorization: `Bearer ${token}`
        }
      }); // Replace with your actual API endpoint
      notifications.value = response.data.data; // Assuming response data is an array of notifications
    } catch (error) {
      console.error("Error fetching notifications:", error);
    }
  };
  onMounted(() => {
    fetchNotifications();
  });
</script>
<template>
    <div class="notification-list">
      <h3>Notifications</h3>
      <NotificationItem
        v-for="notification in notifications"
        :key="notification.id"
        :notification="notification"
      />
    </div>
  </template>
  
  
 <style scoped>
  .notification-list {
    width: 300px;
    padding: 16px;
    background-color: #fff;
    border-radius: 8px;
    box-shadow: 0px 4px 12px rgba(0, 0, 0, 0.1);
  }
  
  .notification-list h3 {
    font-size: 16px;
    margin-bottom: 12px;
  }
  </style>
  