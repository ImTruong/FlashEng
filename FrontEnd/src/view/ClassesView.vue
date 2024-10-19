<script setup>
    import q from "../data/classes.json"
    import Header from '@/components/Header.vue';
    import {ref} from "vue"

    const classes = ref(q);

    const activeTab = ref("Classes");

    const Overlay_background = ref("false");



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
  
        <div class="class-list" @click="Overlay_background = true" v-if="activeTab === 'Classes'">
          <div v-for="classItem in classes" :key="classItem.id" class="class-card">
            <img src="../assets/class.svg" alt="Icon" class="class-icon" @click="Overlay_background = false">
            <div class="overlay"  @click="Overlay_background = false" v-if= "Overlay_background">
                <div class="classbox-container" @click.stop> 
                    <div class="search-container">
                        <input type="text" placeholder="Search ..." class="search-bar"/>
                        <img src="../assets/search.svg" alt="Icon" class="search-icon" >
                    </div>
                    <img src="../assets/add_set.svg" alt="Icon" class="add-set-icon">
                    <img src="../assets/add_member.svg" alt="Icon" class="add-member-icon">
                    <img src="../assets/leave-group.svg" alt="Icon" class="leave-group-icon" @click="Overlay_background = false">
                    <h2>{{ classItem.name }}</h2>
                    <img src="../assets/close.svg" alt="Icon" class="close-icon" @click="Overlay_background = false">
                    <div class="line"></div>
                </div>
           </div>
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
        padding: 100px;
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
    
    .class-list .overlay{
        display: flex;
        justify-content: center;
        align-items: center;
        position: fixed;
        top: 50.67px;
        left: 0;
        width: 100%;
        height: calc(100% - 50.67px);
        background-color: rgba(0, 0, 0, 0.3);
    }

    .overlay .classbox-container{
        /* margin: auto; */
        z-index: 10;
        padding: 10;
        position: absolute;
        display: flex;
        justify-content: center;
        align-items: center;
        border-radius: 5%;
       
        width: 50%;
        height: 80%;
        background-color: white;
    }

    .overlay .classbox-container .close-icon{
        position: absolute;
        top: 10px;
        right: 10px;
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
    }

    .add-member-icon{
        position: absolute;
        top: 25px;
        left: 65%;
        width: 75px;
    }

    .leave-group-icon{
        position: absolute;
        top: 25px;
        left: 80%;
        width: 40px;
    }

    .overlay .classbox-container .line{
        position: absolute;
        top: 15%;
        width: 80%;
        left: 10%;
    }
    
    .class-icon {
        /* position: relative; */
        font-size: 30px;
        margin-right: 20px;
    }
    
    .class-info h3 {
        margin: 0;
        font-weight: bold;
        font-size: 18px;
    }
    
    .class-info p {
        margin: 0;
        font-size: 14px;
        color: gray;
    }
</style>

