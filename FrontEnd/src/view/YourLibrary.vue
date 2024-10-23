<script setup>
    import classesData from "../data/classes.json"
    import Header from '@/components/Header.vue';
    import LibrabyBody from "@/components/LibrabyBody.vue";
    import setsData from '@/data/sets.json'
    import {ref} from 'vue'

    const classes = ref(classesData);
    const sets = ref(setsData)


</script>

<template>
    <div id="app">
    <Header></Header>
    
    <main>
        <h2>Your library</h2>
        <LibrabyBody
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

