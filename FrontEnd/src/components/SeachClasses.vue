<script setup>
    import { defineProps, defineEmits } from 'vue';
    import Card from './Set.vue';
    import setsData from '@/data/sets.json'
    import { ref } from 'vue';
    import LibrabyBody from './LibraryBody.vue';
    import OverlayBackground from './OverlayBackground.vue';
       
    const status = ref(true);

    const {searchQuery, sets, classes, Overlay_background } = defineProps(['searchQuery', 'sets', 'classes', 'Overlay_background']);


    const emit = defineEmits();

    function closeOverlay(){
        emit('close');
    }


</script>

<template>
    <OverlayBackground 
        :isVisible="Overlay_background" 
        @clickOverlay="closeOverlay" />
    <div class="classbox-container" v-if="Overlay_background">
        <div class="header">
            <h1>{{ searchQuery }}</h1>
            <img src="../assets/close.svg" alt="Icon" class="close-icon" @click="closeOverlay">
        </div> 
        <LibrabyBody
        class = "libraryBody"
        :sets="sets"
        :classes="classes"
        />
        
    </div>

</template>

<style scoped>

    .classbox-container{
        position: fixed;
        top: 15vh;
        left: 20vw;
        height: 75vh;
        width: 60vw;
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
        top: 10px;
        width: 100%;
    }


    .header .close-icon{
        position: absolute;
        right: 10px;
        cursor: pointer;

    }
    

    .header h1{
        position: absolute;
        top: 0px;
        left: 10%;
        bottom: 10px;
    }

    .classbox-container .libraryBody{
        position: absolute;
        top: 12%;
        height: 80%;
        padding: 30px;
        overflow-y: auto;
    }



</style>