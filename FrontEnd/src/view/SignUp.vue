<script setup>
  import router from '@/router';
  import axios from 'axios';
  import { ref } from 'vue';

  const fullname = ref('');
  const username = ref('');
  const password = ref('');
  const email = ref('');
  const country = ref('');
  const message = ref('');

  // Hàm đăng ký người dùng
  const registerUser = async () => {
    if (!fullname.value || !username.value || !password.value || !email.value || !country.value) {
      message.value = 'Please fill in all the fields.';
      alert(message.value);
      return
    }
    try {
      // Gửi yêu cầu POST đến API
      const response = await axios.post('/user/register', {
        fullName: fullname.value,
        username: username.value,
        password: password.value,
        email: email.value,
        country: country.value
      });
      message.value = "Registration successful";
      alert(message.value);
      router.push('/login')
      fullname.value = '';
      username.value = '';
      password.value = '';
      email.value = '';
      country.value = '';

    } catch (error) {
      console.log(error);
      if (error.response && error.response.data && error.response.data.message) {
          alert(error.response.data.message);
      } else {
          alert("An error occurred. Please try again.");
      }
    }
  };
</script>

<template>
    <div class="signup-container">
      <div class="signup-left"></div>
      <div class="signup-right">
        <h2>Register</h2>
        <form @submit.prevent="registerUser">
          <div class="input-group">
            <label for="fullname">Fullname</label>
            <input type="text" id="fullname" v-model="fullname" placeholder="Enter your name without accents" required />
          </div>

          <div class="input-group">
            <label for="username">Username</label>
            <input type="text" id="username" v-model="username" placeholder="Choose a username" required />
          </div>

          <div class="input-group">
            <label for="password">Password</label>
            <input type="password" id="password" v-model="password" placeholder="Enter your password" required />
          </div>

          <div class="input-group">
            <label for="email">Email</label>
            <input type="email" id="email" v-model="email" placeholder="Enter your email address" required />
          </div>

          <div class="input-group">
            <label for="country">Country</label>
            <input type="text" id="country" v-model="country" placeholder="Enter your country" required />
          </div>

          <button type="submit" class="signup-button">Sign in</button>
        </form>
        <div class="login" @click="">
          <span>Already have an account? <a href="/login">Login</a></span>
        </div>
      </div>
    </div>
  </template>
  
  
  
  <style scoped>
  .signup-container {
    display: flex;
    height: 100vh;
    width: 100%;
  }
  
  .signup-left {
    flex: 1;
    background-color: #d9f1f4;
  }
  
  .signup-right {
    flex: 1;
    display: flex;
    flex-direction: column;
    justify-content: center;
    /* align-items: center; */
    padding: 10vw;
  }

  h2 {
    font-weight: 400;
    font-size: 40px;
    margin-bottom: 20px;
  }
  
  .input-group {
    width: 100%;
    margin-bottom: 1rem;
  }
  
  .input-group label {
    display: block;
    font-size: 0.9rem;
    margin-bottom: 0.3rem;
  }
  
  .input-group input {
    width: 100%;
    padding: 0.8rem;
    border: 1px solid #ccc;
    border-radius: 4px;
    background-color: #eaf6f9;
  }
  
  .signup-button {
    width: 100%;
    padding: 0.8rem;
    font-size: 1rem;
    color: #fff;
    background-color: #7fdff9;
    border: none;
    border-radius: 4px;
    cursor: pointer;
  }
  
  .signup-button:hover {
    background-color: #6fd0e9;
  }
  
  .login {
    margin-top: 1rem;
    font-size: 0.9rem;
  }
  
  .login a {
    color: #007bff;
    text-decoration: none;
  }
  </style>
  