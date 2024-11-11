<script setup>
  import { ref } from 'vue';
  import NotificationItem from './NotificationItem.vue';
  
  const notifications = ref([
    {
      id: 1,
      message: "Now is the moment to pick up new words.",
      type: "reminder",
      is_read: false,
      created_at: "2024-11-08T10:00:00Z",
    },
    {
      id: 2,
      message: "ptitstudent1 invited you to D22",
      type: "inviter",
      is_read: true,
      created_at: "2024-11-07T14:30:00Z",
    },
    {
      id: 3,
      message: "You became member of D22",// admin duyệt vào nhóm 
      type: "join",
      is_read: false,
      created_at: "2024-11-06T09:00:00Z",
    }
  ]);
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
  