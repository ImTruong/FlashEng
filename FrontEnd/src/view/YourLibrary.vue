<script setup>
    import axios from "axios";
    import Header from '@/components/Header.vue';
    import LibraryBody from "@/components/LibraryBody.vue";
    import { computed, onMounted } from "vue";
    import { useStore } from 'vuex';
    const store = useStore();
    
    const classes = computed(() => (store.getters.getClasses).reverse())
    const sets = computed(() => store.getters.getSets);

    onMounted(() => {
        Promise.all([
            store.dispatch('fetchLibrarySets'),
            store.dispatch('fetchClassData')
        ]).catch((error) => {
            console.error("Error fetching data:", error);
        });
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