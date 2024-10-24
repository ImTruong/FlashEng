<script setup>
    import { defineProps, defineEmits } from 'vue';
    import Card from './Set.vue';
    import OverlayBackground from './OverlayBackground.vue';
    import setsData from '@/data/sets.json'
    import { ref, watch } from 'vue';


    const { classItem, Overlay_background } = defineProps(['classItem', 'Overlay_background']);

    const emit = defineEmits();

    function closeOverlay(){
        emit('close');
    }

    const sets = ref(setsData)
    const icon = ref(false)
    const search = ref("")

    watch(search, () => {
        sets.value = setsData.filter(set => set.name.toLowerCase().includes(search.value.toLowerCase()))
    })

    
</script>

<template>
    
    <OverlayBackground 
        :isVisible="Overlay_background" 
        @clickOverlay="closeOverlay" />
    <div class="classbox-container" v-if="Overlay_background"> 
        <div class="search-container">
            <input v-model.trim="search" type="text" placeholder="Search ..." class="search-bar"/>
            <img src="../assets/search.svg" alt="Icon" class="search-icon" >
        </div>
        <div v-if="icon" class="icon">
            <img src="../assets/add_set.svg" alt="Icon" class="add-set-icon">
            <img src="../assets/add_member.svg" alt="Icon" class="add-member-icon">
            <img src="../assets/leave-group.svg" alt="Icon" class="leave-group-icon" @click="closeOverlay">
        </div>
        <h2 @click="icon = !icon">{{ classItem.name }}</h2>
        <img src="../assets/close.svg" alt="Icon" class="close-icon" @click="closeOverlay">
        <div class="line"></div>
        
        <div class="card-container">
            <Card 
                v-for="set in sets" 
                :key="set.id" 
                :set="set" />

        </div>
    </div>
</template>

<style scoped>

    .classbox-container{
        position: fixed;
        top: 15vh;
        left: 25%;
        height: 70vh;
        width: 50vw;
        display: flex;
        justify-content: center;
        align-items: center;
        border-radius: 5%;
        background-color: white;
        overflow: hidden;
        z-index: 11;
    }

    .classbox-container .close-icon{
        position: absolute;
        top: 10px;
        right: 10px;
        cursor: pointer;

    }

    .classbox-container h2{
        position: absolute;
        top: 25px;
        left: 10%;
        cursor: pointer;
    }

    .search-container{
        position: absolute; 
        text-align: center;
        justify-content: center;
        width: 30%;
        top: 25px;
        left: 60%;
    }

    .search-container .search-icon{
        position: absolute;
        left: 10px;
        top: 7px;
    }

    .search-container .search-bar{
        width: 100%;
        padding: 8px 16px 8px 30px;
        border-radius: 5px;
        display:inline-block;
    }

    .icon{
        position: absolute;
        top: 15%;
        left: 15%;
        display: flex;
        justify-content: center;
        align-items: center;
        width: 200px;
        height: 60px;
        background-color: rgb(251, 251, 251);
        z-index: 12;
        box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.5);

    }
    


    .add-set-icon{
        cursor: pointer;
        height: 40px;
    } 
    .add-set-icon:hover{
        transform: scale(1.05);
    }

    .add-member-icon{
        cursor: pointer;
        height: 40px;
    } 

    .add-member-icon:hover{
        transform: scale(1.05);
    }
    .leave-group-icon{
        cursor: pointer;
        height: 35px;

    } 
    .leave-group-icon:hover{
        transform: scale(1.05);
    }
    .classbox-container .line{
        position: absolute;
        top: 15%;
        width: 80%;
        height: 1px;
        left: 10%;
        z-index: 100;
        background-color: rgba(71, 59, 59, 0.5)
    }

    .card-container {
        position: absolute;
        width: 100%;
        height: 80%;
        top:15%;
        padding: 10px;
        display: flex;
        flex-wrap: wrap;
        gap: 10px;
        overflow: auto;
    }


</style>