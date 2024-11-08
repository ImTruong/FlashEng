<script setup>
    import OverlayBackground from './OverlayBackground.vue';
    import { defineEmits, defineProps } from 'vue';
    import {ref} from 'vue';

    const emit = defineEmits(['close', 'save']);
    const visible = ref(true);
    const className = ref('');
    const rows = ref([{ username: '', role: ''}]);
    function closeOverlay(){
        emit('close');
    }
    const invitee = ref("");


</script>

<template>
    <OverlayBackground :isVisible="visible" @clickOverlay="closeForm"> </OverlayBackground>
    <div class="container">
        <div class="header">
            <p>Class:</p>
            <input class="classname" :value="className">
            <img src="../assets/close.svg" alt="Icon" class="close-icon" @click="closeOverlay">
        </div>
        <div class="invitee">
            <p>Username:</p>
            <input type="text" v-model="invitee">
        </div>
        <div class="button">
            <button class="invite">Invite</button>
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

    .header input{
        /* flex: 1; */
        width: 50%;
        height: 30px;
        margin-left: 5px;
        border-radius: 5px;
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