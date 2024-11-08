<script setup>
import Header from "@/components/Header.vue" // Import your header component
  import {ref} from 'vue'

  const user = ref({
    name: '',
    email: '',
    birthday: '',
    country: ''
  });
  const errorMessage = ref(null);

// export default {
//   components: {
//     Header
//   },
//   data() {
//     return {
//       user: {
//         name: '',
//         email: '',
//         birthday: '',
//         country: '',
//       },
//     };
//   },
//   methods: {
//     updateProfile() {
//       // Logic to update profile
//     },
//     changePassword() {
//       // Logic to change password
//     },
//   },
// };
</script>


<template>
    <div class="user-profile-page">
      <!-- Header component -->
      <Header></Header>
  
      <div class="profile-container">
        
  
        <!-- Main profile content -->
        <main class="profile-content">
          <div class="profile-header">
            <div class="background"></div>
            <div class="avatar"></div>
            <h2>User</h2>
          </div>
          <form @submit.prevent="updateProfile">
            <div class="form-group">
              <label for="name">Name</label>
              <input v-model="user.name" id="name" type="text" />
            </div>
            <div class="form-group">
              <label for="email">Email</label>
              <input v-model="user.email" id="email" type="email" />
            </div>
            <div class="form-group">
              <label for="birthday"></label>
              <input v-model="user.birthday" id="birthday" type="date" />
            </div>
            <div class="form-group">
              <label for="country">Country</label>
              <input v-model="user.country" id="country" type="text" />
            </div>
            <div class="actions">
              <button type="button" @click="changePassword">Change password</button>
              <button type="submit">Edit profile</button>
            </div>
          </form>
        </main>
      </div>
    </div>
  </template>
  

  
  <style scoped>
  .user-profile-page {
    display: flex;
    flex-direction: column;
    padding: 0 20vw;
  }
  
  .profile-container {
    display: flex;
  }
  
  
  .profile-content {
    flex: 1;
    margin-top: 50.67px;
    /* max-width: ; */
  }
  
  .profile-header {
    width: 100%;
    /* display: flex; */
    align-items: center;
    margin-bottom: 20px;
    /* background-color: #007bff; */
    /* height: 150px; */
  }
  
  .background{
    background-color: #cacaca;
    height: 150px;
    width: 100%;
  }

  
  .avatar {
    margin: -50px 20px 0px 20px;
    width: 80px;
    height: 80px;
    border-radius: 50%;
    background-color: #d9ecf2;
  }

  .profile-header h2{
    /* margin-top: -80px; */
    margin: -65px 0 50px 105px ;
  }
  
  .form-group {
    margin-bottom: 15px;
  }
  
  .form-group label {
    display: block;
    margin-bottom: 5px;
  }
  
  .form-group input {
    width: 100%;
    padding: 8px;
    border: 1px solid #ccc;
    border-radius: 4px;
  }
  
  .actions {
    display: flex;
    justify-content: space-between;
  }
  
  .actions button {
    padding: 10px 15px;
    border: none;
    background-color: #007bff;
    color: white;
    border-radius: 4px;
    cursor: pointer;
  }
  
  .actions button:first-of-type {
    background-color: #6c757d;
  }
  </style>
  