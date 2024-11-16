<script setup>
    import OverlayBackground from './OverlayBackground.vue';
    import { defineEmits, defineProps } from 'vue';
    import {ref, watch} from 'vue';
    import axios from 'axios';

    const emit = defineEmits(['close', 'save']);
    const visible = ref(true);
    const className = localStorage.getItem('className');
    const rows = ref([{ username: '', role: ''}]);
    const inviteRovokeMode = ref('Invite')
    const errorMessage = ref(null);
    function closeOverlay(){
        emit('close');
    }
    const invitee = ref("");
    const classId = localStorage.getItem('classId');
    const invitationId = ref("");
    const token = localStorage.getItem('token');
    const getInvite = async () => {
        try {
            const response = await axios.get(`/class/invitation/existence`, {
                params: {
                    inviteeUsername: invitee.value,
                    classId: classId,
                },
                headers: {
                    Authorization: `Bearer ${token}`,
                },
            });

            // invitationId.value = response.data.data.substring(14);
            // Nếu chuyển thành Object.
            invitationId.value = response.data.data.classInvitationId;
            console.log(response.data);
            console.log(invitationId.value);
            if(response.status == 200 && response.data.data){
                inviteRovokeMode.value = "Revoke";
            }
        } catch (error) {
            if (error.response) {
                // Log thông báo lỗi từ server
                console.error('Error response:', error.response.data);
            } else {
                console.error('Error while calling API:', error);
            }
        }
    }
    const inviteUser = async () => {
        try {
            const response = await axios.post(`/class/invitation?classId=${classId}&inviteeUsername=${invitee.value}`, {
                classId: classId,
                inviteeUsername: invitee.value,
            }, 
            {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            if (response.status == 200) {
                const errorMessage = response.data.message;
                alert(errorMessage);
                inviteRovokeMode.value = "Revoke";
                getInvite();
            }
        } catch (error) {
            if (error.response) {
                // Log thông báo lỗi từ server
                console.error('Error response:', error.response.data);
                alert(`Error: ${error.response.data.message || 'Forbidden'}`);
            } else {
                console.error('Error while calling API:', error);
                alert('An error occurred. Please try again later.');
            }
        }
    };

    const revokeUser = async() =>{
        try {
            getInvite();
            if (!invitationId.value) {
                console.error('Invitation ID is missing');
                return;
            }
            const response = await axios.delete(`/class/invitation/revoke?invitationId=${invitationId.value}`,
            {
                headers: {
                    Authorization: `Bearer ${token}`
                }
            });

            if (response.status == 200) {
                const errorMessage = response.data.message;
                alert(errorMessage);
                inviteRovokeMode.value = "Invite";
            }
        } catch (error) {
            console.error('Error while calling API:', error);
        }
    }

    const inviteRevokeUser = () =>{
        if(inviteRovokeMode.value == "Invite"){
            inviteUser();
        }
        else {
            revokeUser();
        }
    }

    watch(invitee, (newValue, oldValue) => {
      if (newValue !== oldValue) {
        // Gọi getInvite mỗi khi invitee thay đổi
        getInvite();
        inviteRovokeMode.value = "Invite";
      }
    });

</script>

<template>
    <OverlayBackground :isVisible="visible" @clickOverlay="closeOverlay"> </OverlayBackground>
    <div class="container">
        <div class="header">
            <p>Class:</p>
            <div class="className">
                {{ className }}
            </div>

            <img src="../assets/close.svg" alt="Icon" class="close-icon" @click="closeOverlay">
        </div>
        <div class="invitee">
            <p>Username:</p>
            <input type="text" v-model="invitee" placeholder="Enter username">
        </div>
        <div class="button">
            <button class="invite" @click="inviteRevokeUser">{{inviteRovokeMode}}</button>
            <button class="cancel" @click="closeOverlay">Cancel</button>
        </div>
    </div>
</template>


<style scoped>
    .container{
        position: fixed;
        top: 25vh;
        left: 35vw;
        display: flex;
        flex-direction: column;
        height: 50vh;
        width: 30vw;
        display: flex;
        justify-content: center;
        align-items: center;
        border-radius: 8px;
        background-color: white;
        overflow: hidden;
        z-index: 11;
        
    }

    .header{
        position: absolute;
        height: 50px;
        background-color: #BDEDF5;
        width: 100%;
        top: 0;
        display: flex;
        align-items: center;
    }

    .header .className{
        /* flex: 1; */
        width: 50%;
        height: 30px;
        margin-left: 5px;
        border-radius: 5px;
        background-color: white;
        text-align: center;
        align-items: center;
    }
    .header p{
        margin-left: 17%;
    }

    .close-icon{
        position: absolute;
        top: 10px;
        right:10px;
    }

    .invitee{
        display: flex;
        margin-top: 30px;
    }
    
    .invitee input{
        width: 60%;
        height: 30px;
        margin-left: 10px;
    }

    button {
        height: 30px;
        width: 70px;
        border-radius: 5px;
        border: none;
        box-shadow: 2px 2px 2px #bcbcbc;
        margin: 50px 20px 20px 0;
    }

    .invite{
        background-color: #BDEDF5;
    }
    .invite:hover{
        background-color: #61e5fd;
    }
    .cancel{
        background-color: #FF9FA8;

    }
    .cancel:hover{
        background-color: #f37c88;
    }
</style>