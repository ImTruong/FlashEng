<script setup>
    import axios from "axios";
    import classesData from "../data/classes.json"
    import Header from '@/components/Header.vue';
    import LibraryBody from "@/components/LibraryBody.vue";
    import { ref, computed, onMounted } from "vue";
    import { useStore } from 'vuex';
    const store = useStore();
    
    const classes = ref(classesData)
    const sets = computed(() => store.getters.getSets);

    const fetchLibraryClasses = async () => {
    try {
        const token = localStorage.getItem('token') // Lấy token từ localStorage
        if (!token) {
        errorMessage.value = 'You must be logged in to view user information'
        return
        }
        const response = await axios.get('/class/user', {
        headers: {
            Authorization: `Bearer ${token}`, // Đảm bảo gửi token trong header
        },
        })
        classes.value = response.data.data;
    } catch (error) {
        errorMessage.value = error.response ? error.response.data : 'An error occurred'
        console.error('Error fetching user info:', error)
    }
    };

    onMounted(() => {
        fetchLibraryClasses();
        store.dispatch('fetchLibrarySets');  
    })

    
   
</script>

<template>
    <div id="app">
    <Header></Header>
    <main>
        <h2>Your library</h2>
        <LibraryBody
            :sets="sets"
            :classes="classes"
        />
      </main>
    </div>
</template>
  
    
<style scoped>    

    main {
        padding: 100px;
    }
    
    h2 {
        font-weight: bold;
    }
    
    
</style>