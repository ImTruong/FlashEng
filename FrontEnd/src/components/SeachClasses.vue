<script setup>
    import { defineProps, defineEmits } from 'vue';
    import Card from './Set.vue';
    import setsData from '@/data/sets.json'
    import { ref } from 'vue';
    import LibrabyBody from './LibrabyBody.vue';


    const {searchQuery, sets, classes, Overlay_background } = defineProps(['searchQuery', 'sets', 'classes', 'Overlay_background']);


    const emit = defineEmits();

    function closeOverlay(){
        emit('close');
    }

</script>

<template>
    <div class="overlay"  @click="Overlay_background = false" v-if= "Overlay_background == true">
        <div class="classbox-container" @click.stop> 
            <LibrabyBody
            class = "libraryBody"
            :sets="sets"
            :classes="classes"
            />
            <h1>{{ searchQuery }}</h1>
            <img src="../assets/close.svg" alt="Icon" class="close-icon" @click="closeOverlay">
        </div>
    </div>

</template>

<style scoped>
    .overlay{
        position: fixed;
        display: flex;
        justify-content: center;
        /* align-items: center; */
        top: 50.67px;
        left: 0;
        width: 100%;
        height: calc(100% - 50.67px);
        background-color: rgba(0, 0, 0, 0.3);
    }

    .overlay .classbox-container{
        padding: 10px;
        /* margin: auto; */
        z-index: 10;
        position: absolute;
        display: flex;
        justify-content: center;
        align-items: center;
        border-radius: 5%;

        width: 50%;
        height: 80%;
        background-color: white;
        overflow: hidden;
    }


    .classbox-container .close-icon{
        position: absolute;
        top: 10px;
        right: 10px;
        cursor: pointer;

    }

    .classbox-container h1{
        position: absolute;
        top: 10px;
        left: 10%;
        bottom: 10px;
    }

    .classbox-container .libraryBody{
        position: absolute;
        padding: 20px;
    }



</style>