<script setup>
    import { defineProps, defineEmits } from 'vue';
    import Card from './Set.vue';
    import setsData from '@/data/sets.json'
    import { ref } from 'vue';
    import OverlayBackground from './OverlayBackground.vue';
    

    const { classItem, Overlay_background } = defineProps(['classItem', 'Overlay_background']);

    const emit = defineEmits();

    function closeOverlay(){
        emit('close');
    }

    const join = ref("Join")

    const join_button = () =>{
        if(join.value == "Join") join.value = "Cancel"
        else join.value = "Join"
    }

</script>

<template>
    <OverlayBackground 
        :isVisible="Overlay_background" 
        @clickOverlay="closeOverlay" />
    <!-- <div class="overlay"  @click="Overlay_background = false" v-if= "Overlay_background == true"> -->
        <div class="class-container" @click.stop> 
            <img src="../assets/close.svg" alt="Icon" class="close-icon" @click="closeOverlay">
            <div class="class-info">
                <div class="class-header">
                    <img src="../assets/class.svg" alt="class-icon">
                    <h1>{{ classItem.name }}</h1>
                </div>
                <p class="class-details">{{ classItem.sets }} set | {{ classItem.members }} members</p>
                <div class="join-button" @click="join_button">
                    <p>{{ join }}</p>
                </div>
            </div>

            
        </div>
    <!-- </div> -->

</template>

<style scoped>
    .class-container{
        position: fixed;
        top: 25%;
        left: 35%;
        padding: 10px;
        z-index: 15;
        padding: 10;
        display: flex;
        justify-content: center;
        align-items: center;
        border-radius: 5%;
        width: 30%;
        height: 50%;
        background-color: white;
        overflow: hidden;
    }


    .class-container .close-icon{
        position: absolute;
        top: 10px;
        right: 10px;
        cursor: pointer;

    }

    
    .class-container .class-info{
        position: absolute;
        text-align: center;
        justify-content: center;
    }

    .class-info .class-header{
        position: relative;
        display: flex;
        justify-content: center;
        gap: 10px;
    }

    .class-info .class-details{
        margin-bottom: 20px;
    }


    .join-button {
        background-color: #BDEDF5;
        padding: 10px 20px;
        width: 100px;
        border-radius: 20px;
        cursor: pointer;
        display: inline-block;
        box-shadow: 2px;
        box-shadow: 2px 2px 10px rgba(0, 0, 0, 0.15); /* Tăng cường shadow khi hover */

    }

    .join-button p {
        margin: 0;
        font-size: 18px;
        color: #333;
    }

    .join-button:hover {
        background-color: #91e3df;
    }
</style>