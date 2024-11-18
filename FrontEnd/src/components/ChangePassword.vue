<script setup>
import OverlayBackground from './OverlayBackground.vue';
import { defineEmits, ref } from 'vue';
import axios from 'axios';

const emit = defineEmits(['close', 'save']);
const visible = ref(true);

// Các giá trị nhập liệu
const currentPassword = ref('');
const newPassword = ref('');
const confirmPassword = ref('');
const errorMessage = ref('');
const successMessage = ref('');

// Phương thức đóng form
const closeForm = () => {
    emit('close');
    visible.value = false;
};


// Phương thức kiểm tra và gửi dữ liệu
const saveData = async () => {

    // Dữ liệu gửi lên backend
    const requestData = {
        oldPassword: currentPassword.value,
        newPassword: newPassword.value,
        confirmPassword: confirmPassword.value
    };

    try {
        // Get the token from localStorage
        const token = localStorage.getItem('token');
        if (!token) {
            errorMessage.value = 'You must be logged in to view user information';
            return;
        }
        const response = await axios.put('/user/password', requestData, {
            headers: {
            Authorization: `Bearer ${token}`, // Attach the token in the request headers
            },
        });

        // Handle successful response (uncomment if necessary)
        if (response.data.success) {
            emit('save', { newPassword: newPassword.value });
            closeForm();
        } else {
            errorMessage.value = 'An error occurred while changing the password!';
        }
        } catch (error) {
        // Handle errors (e.g., server issues, invalid response)
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
    <OverlayBackground :isVisible="visible" @clickOverlay="closeForm"></OverlayBackground>
    <div class="container">
        <div class="header">
            <h1>Change password</h1>
        </div>
        <img src="../assets/close.svg" alt="Icon" class="close-icon" @click="closeForm">
        <div class="password">
            <label>Current password</label>
            <input type="password" v-model="currentPassword">
        </div>
        <div class="password">
            <label>New password</label>
            <input type="password" v-model="newPassword" >
        </div>
        <div class="password">
            <label>Confirm password</label>
            <input type="password" v-model="confirmPassword" >
        </div>

        <!-- Hiển thị thông báo lỗi và thành công -->
        <p v-if="errorMessage" class="error">{{ errorMessage }}</p>

        <!-- Nút hành động -->
        <div class="button-container">
            <button @click="saveData">Save</button>
            <button @click="closeForm">Cancel</button>
        </div>
    </div>
</template>

<style scoped>
/* Đặt chung cho toàn bộ container */
    .container {
        top:-400px;
        width: 400px;
        margin: auto;
        padding: 20px;
        background-color: #f9f9f9;
        border-radius: 10px;
        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.1);
        position: relative;
        z-index: 20;
    }

    /* Header */
    .header {
        text-align: center;
        margin-bottom: 20px;
    }

    .close-icon {
        width: 24px;
        height: 24px;
        position: absolute;
        top: 20px;
        right: 20px;
        cursor: pointer;
    }

    /* Căn chỉnh các trường input */
    .password {
        display: flex;
        flex-direction: column;
        margin-bottom: 15px;
    }

    .password label {
        margin-bottom: 5px;
        font-weight: 600;
    }

    .password input {
        width: 100%;
        padding: 10px;
        border: 1px solid #ccc;
        border-radius: 5px;
        box-sizing: border-box;
    }

    /* Hiển thị thông báo lỗi */
    .error {
        color: #ff4d4d;
        font-weight: 600;
        text-align: center;
        margin-top: 10px;
    }

    /* Button container */
    .button-container {
        display: flex;
        justify-content: space-between;
        margin-top: 20px;
    }

    .button-container button {
        width: 48%;
        padding: 10px;
        border: none;
        border-radius: 5px;
        cursor: pointer;
        box-shadow: 2px 2px 2px rgba(0, 0, 0, 0.6);
    }


    .button-container button:last-child {
        background-color: #ffc1c1;
    }

    .button-container button:last-child:hover {
        background-color: #ff9191;
    }

    .button-container button:first-child {
        background-color: #9adfeb;
    }

    .button-container button:first-child:hover {
        background-color: #93fffa;
    }


</style>