<script setup>
    import q from "../data/classes.json"
    import Header from '@/components/Header.vue';
    import {ref} from "vue"

    const classes = ref(q);

    const activeTab = ref("Classes");




</script>

<template>
    <div id="app">
    <Header></Header>
    
    <main>
        <h2>Your library</h2>
        <div class="tabs">
          <button :class="{ active: activeTab === 'Flashcard sets' }" @click="activeTab ='Flashcard sets'">Flashcard sets</button>
          <button :class="{ active: activeTab === 'Classes' }" @click="activeTab = 'Classes'">Classes</button>
        </div>
        <div class="line"></div>
  
        <div class="class-list" v-if="activeTab === 'Classes'">
          <div v-for="classItem in classes" :key="classItem.id" class="class-card">
            <!-- <span class="class-icon">👥</span> -->
            <div class="class-info">
              <h3>{{ classItem.name }}</h3>
              <p>{{ classItem.sets }} set | {{ classItem.members }} members</p>
            </div>
          </div>
        </div>
      </main>
    </div>
</template>
  
    
<style scoped>    

    main {
        padding: 20px;
    }
    
    h2 {
        font-weight: bold;
    }
    
    .tabs {
        display: flex;
        gap: 20px;
        margin-bottom: 20 20 0 20;
    }
    
    .tabs button {
        background: none;
        border: none;
        padding: 10px;
        cursor: pointer;
        font-weight: bold;
        font-size: 16px;
        color: #333;
    }
    
    .tabs button.active {
        border-bottom: 2px solid black;
    }
    
    .line {
        /* position: absolute; */
        display: flex;
        margin: 10 10 20 10;
        bottom: 0;
        left: 0;
        right: 0;
        width: 100%;
        height: 1px;
        background-color: rgba(14, 14, 14, 0.1); 
        margin-bottom: 30px;
    
    }
    
    .class-list {
        display: flex;
        flex-direction: column;
        gap: 20px;
    }
    
    .class-card {
        display: flex;
        align-items: center;
        background-color: white;
        padding: 20px;
        border-radius: 10px;
        box-shadow: 0 2px 5px rgba(0, 0, 0, 0.1);
    }
    
    .class-icon {
        font-size: 30px;
        margin-right: 20px;
    }
    
    .class-info h3 {
        margin: 0;
        font-size: 18px;
    }
    
    .class-info p {
        margin: 0;
        font-size: 14px;
        color: gray;
    }
</style>