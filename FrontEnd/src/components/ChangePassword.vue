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
        errorMessage.value = error.response?.data?.message || 'Error connecting to the server!';
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
            <input type="text" v-model="currentPassword">
        </div>
        <div class="password">
            <label>New password</label>
            <input type="text" v-model="newPassword" >
        </div>
        <div class="password">
            <label>Confirm password</label>
            <input type="text" v-model="confirmPassword" >
        </div>

        <!-- Hiển thị thông báo lỗi và thành công -->
        <p v-if="errorMessage" class="error">{{ errorMessage }}</p>

        <!-- Nút hành động -->
        <div class="button-container">
            <button @click="closeForm">Cancel</button>
            <button @click="saveData">Save</button>
        </div>
    </div>
</template>

<style scoped>
.container {
    position: fixed;
    top: 25vh;
    left: 35vw;
    display: flex;
    flex-direction: column;
    height: 50vh;
    width: 40vw;
    justify-content: center;
    align-items: center;
    border-radius: 8px;
    background-color: white;
    overflow: hidden;
    z-index: 11;
    padding: 20px;
}

    h1{
        /* height: 30px; */
        margin-bottom: 30px;
        text-align: center;
    }
    .close-icon{
        position: absolute;
        top: 10px;
        right:10px;
    }
    
    

input {
    width: 150px;
    margin-bottom: 10px;
    padding: 8px;
    border-radius: 4px;
    border: 1px solid #ccc;
    margin-left: 10px;
}

.button-container {
    display: flex;
    justify-content: space-between;
    width: 80%;
    margin-top: 15px;

}

button {
    padding: 10px 20px;
    border: none;
    border-radius: 4px;
    cursor: pointer;
    background-color: #BDEDF5;
}

button:hover {
    background-color: #77effc;
}

.error {
    color: red;
    margin-bottom: 10px;
}

</style>