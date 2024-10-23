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
</script>

<template>
 
    <div class="overlay"  @click="Overlay_background = false" v-if= "Overlay_background == true">
        <div class="classbox-container" @click.stop> 
            <div class="search-container">
                <input type="text" placeholder="Search ..." class="search-bar"/>
                <img src="../assets/search.svg" alt="Icon" class="search-icon" >
            </div>
            <img src="../assets/add_set.svg" alt="Icon" class="add-set-icon">
            <img src="../assets/add_member.svg" alt="Icon" class="add-member-icon">
            <img src="../assets/leave-group.svg" alt="Icon" class="leave-group-icon" @click="closeOverlay">
            <h2>{{ classItem.name }}</h2>
            <img src="../assets/close.svg" alt="Icon" class="close-icon" @click="closeOverlay">
            <div class="line"></div>
            
            <div class="card-container">
                <Card 
                    v-for="set in sets" 
                    :key="set.id" 
                    :set="set" />

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

    .overlay .classbox-container .close-icon{
        position: absolute;
        top: 10px;
        right: 10px;
        cursor: pointer;

    }

    .overlay .classbox-container h2{
        position: absolute;
        top: 20px;
        left: 10%;
    }

    .overlay .search-container{
        position: absolute; 
        text-align: center;
        justify-content: center;
        width: 25%;
        top: 25px;
        left: 20%;
    }

    .overlay .search-container .search-icon{
        position: absolute;
        left: 10px;
        top: 7px;
        
    }

    .overlay .search-container .search-bar{
        width: 100%;
        padding: 8px 16px 8px 40px;
        border-radius: 5px;
    }

    .add-set-icon{
        position: absolute;
        top: 25px;
        left: 50%;
        width: 75px;
        cursor: pointer;

    }

    .add-member-icon{
        position: absolute;
        top: 25px;
        left: 65%;
        width: 75px;
        cursor: pointer;

    }

    .leave-group-icon{
        position: absolute;
        top: 25px;
        left: 80%;
        width: 40px;
        cursor: pointer;

    }

    .overlay .classbox-container .line{
        position: absolute;
        top: 15%;
        width: 80%;
        height: 1px;
        left: 10%;
        z-index: 100;
        background-color: rgba(71, 59, 59, 0.8)
    }

    .card-container {
        position: absolute;
        width: 100%;
        height: 80%;
        top:15%;
        padding: 20px;
        display: flex;
        flex-wrap: wrap;
        gap: 10px;
        overflow: hidden;
    }




</style>