<script setup>
    import { defineProps, defineEmits } from 'vue';
    import Card from './Set.vue';
    import setsData from '@/data/sets.json'
    import { ref } from 'vue';


    const { classItem, Overlay_background } = defineProps(['classItem', 'Overlay_background']);

    const emit = defineEmits();

    function closeOverlay(){
        emit('close');
    }

    const sets = ref(setsData)
    const displayedSets = ref([])
    const showAllSets = () => {
        displayedSets.value = sets.value; // Hiển thị toàn bộ khi nhấn "More..."
    };

    const join = ref("Join")

    const join_button = () =>{
        if(join.value == "Join") join.value = "Cancel"
        else join.value = "Join"
    }
</script>

<template>
 
    <div class="overlay"  @click="Overlay_background = false" v-if= "Overlay_background == true">
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
    </div>

</template>

<style scoped>
    .overlay{
        position: fixed;
        display: flex;
        justify-content: center;
        align-items: center;
        top: 50.67px;
        left: 0;
        width: 100%;
        height: calc(100% - 50.67px);
        background-color: rgba(0, 0, 0, 0.3);
    }

    .overlay .class-container{
        padding: 10px;
        /* margin: auto; */
        z-index: 10;
        padding: 10;
        position: absolute;
        display: flex;
        justify-content: center;
        align-items: center;
        border-radius: 5%;

        width: 30%;
        height: 50%;
        background-color: white;
        overflow: hidden;
    }

    .overlay .class-container .close-icon{
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