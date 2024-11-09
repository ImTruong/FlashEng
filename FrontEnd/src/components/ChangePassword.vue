<script setup>
    import OverlayBackground from './OverlayBackground.vue';
    import { defineEmits, defineProps } from 'vue';
    import {ref} from 'vue';

    const emit = defineEmits(['close', 'save']);
    const visible = ref(true);

</script>

<template>
    <OverlayBackground :isVisible="visible" @clickOverlay="closeForm"> </OverlayBackground>
    <div class="container">
        <label for="">Current password</label>
        <input type="text">
        <label for="">New password</label>
        <input type="text">
        

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

</style>