<script setup>
    import axios from "axios";
    import classesData from "../data/classes.json"
    import Header from '@/components/Header.vue';
    import LibraryBody from "@/components/LibraryBody.vue";
    import setsData from '@/data/sets.json'
    import {ref, defineProps, onMounted} from 'vue'

    // defineProps(['sets']);
    const classes = ref(classesData);
    const sets = ref([])
    const fetchLibrarySets = async () => {
        try {
            const token = localStorage.getItem("token");
            if (!token) {
                console.warn("Token không tồn tại");
                return;
            }
            const response = await axios.get("/set/private", {
                headers: {
                    Authorization: `Bearer ${token}`  
                }
            });
            // Lưu danh sách các set từ API
            sets.value = response.data.data;
            displayedSets.value = sets.value.slice(0, 3); 
        } catch (error) {
            console.error("Error fetching library sets:", error);
        }
    };
    onMounted(() => {
        fetchLibrarySets();
    });
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