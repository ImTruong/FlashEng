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

    onMounted(() => {
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