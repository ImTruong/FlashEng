<script setup>
    import { defineProps, defineEmits, onMounted, watch } from 'vue';
    import { ref } from 'vue';
    import LibraryBody from './LibraryBody.vue';
    import OverlayBackground from './OverlayBackground.vue';
    import { useStore } from 'vuex';
    import axios from 'axios';
    
    const status = ref(true);

    const {searchQuery,  Overlay_background } = defineProps(['searchQuery', 'Overlay_background']);
    const store = useStore();
    const sets = ref([]);


    const emit = defineEmits();

    function closeOverlay(){
        emit('close');
    }

    const classes = ref([]);

    const getClasses = async() =>{
        try{
            const token = localStorage.getItem('token');
            const response = await axios.get(`class?name=${searchQuery}`, {
                headers: {
                    Authorization: `Bearer ${token}`, // Đảm bảo gửi token trong header
                },
            })
            classes.value = response.data.data;
            console.log(classes);
        } catch (error) {
            console.error('Error fetching user info:', error)
        }       
    }

    const getSets = async() =>{
        try{
            const token = localStorage.getItem('token');
            const response = await axios.get(`/set/search?name=${searchQuery}`, {
                headers: {
                    Authorization: `Bearer ${token}`, // Đảm bảo gửi token trong header
                },
            })
            sets.value = response.data.data;
            console.log(classes);
        } catch (error) {
            console.error('Error fetching user info:', error)
        } 
    }

    watch(
        () => searchQuery,
        (newQuery) => {
            console.log('Search query changed:', newQuery);
            getClasses(); // Gọi lại getClasses khi searchQuery thay đổi
            getSets(); // Gọi lại getSets khi searchQuery thay đổi
        },
        { immediate: true } // Gọi ngay lập tức khi component được mount lần đầu tiên
    );
    onMounted(() => {
        getClasses();
        getSets();
    });

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
        <LibraryBody
        class="libraryBody"
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