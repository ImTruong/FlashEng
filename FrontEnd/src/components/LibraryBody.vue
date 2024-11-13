<script setup>
    import Header from '@/components/Header.vue';
    import ClassBox from '@/components/ClassBox.vue'
    import InvitationBox from './InvitationBox.vue';
    import Card from "../components/Set.vue"
    import {ref} from "vue"
    import { defineProps} from 'vue';
    import ClassTable from './ClassTable.vue';


    const classBoxMode = ref(false);
    const classTableMode = ref(false);

    const {sets, classes} = defineProps(['sets','classes']);

    const activeTab = ref("Flashcard sets");
    const selectedClassItem = ref("");

    const selectClass = (classItem) => {
        selectedClassItem.value = classItem;
        console.log(selectedClassItem.value);
        localStorage.setItem('classId', selectedClassItem.value.classId);
        localStorage.setItem('className', selectedClassItem.value.className);
    }

    const showClassTable = (classItem) => {
        classTableMode.value = true;
        selectClass(classItem);
    }

    const showClassBox = (classItem) => {
        classBoxMode.value = true;
        selectClass(classItem);
    }

    function closeOverlay() {
        emit('close');
    }

</script>

<template>
    <main>
        <div class="tabs">
          <button :class="{ active: activeTab === 'Flashcard sets' }" @click="activeTab ='Flashcard sets'">Flashcard sets</button>
          <button :class="{ active: activeTab === 'Classes' }" @click="activeTab = 'Classes'">Classes</button>
        </div>
        <div class="line"></div>
        <div class="sets-list"  v-if="activeTab === 'Flashcard sets'">
            <div class="sets-container">
                <div v-for="set in sets" :key="set.id" class="card-wrapper">
                    <Card :set="set" @click="" />
                </div>
            </div>
        </div>
        <div class="class-list"  v-if="activeTab === 'Classes'">
            <div v-for="classItem in classes" :key="classItem.id" class="class-card" @click="showClassBox(classItem)">
                <img src="../assets/class.svg" alt="Icon" class="class-icon">
                <div class="class-info">
                    <h3>{{ classItem.className }}</h3>
                    <div class="class-detail" @click.stop="">
                        <p @click="showClassBox(classItem)">{{ classItem.numberOfSets }} {{ classItem.numberOfMembers <= 1 ? 'set' : 'sets' }} |</p>
                        <p @click="showClassTable(classItem)"> {{ classItem.numberOfMembers }} {{ classItem.numberOfMembers <= 1 ? 'member' : 'members' }}</p>
                    </div>
                </div>
            </div>
            
        </div>
        <ClassBox 
        v-if="classBoxMode" 
        :Overlay_background ="classBoxMode" 
        @close ="classBoxMode = false"
        />
        <ClassTable 
            v-if="classTableMode" 
            :isEditMode=true  
            @close="classTableMode = false" 
            @update=""
        />
        <!-- <InvitationBox
        :classItem ="selectedClassItem" 
        :Overlay_background ="Overlay_background" 
        @close ="Overlay_background = false"
        ></InvitationBox> -->
    </main>
</template>
  
    
<style scoped>    
    main {
        position: absolute;
        top: 40px;
        left: 0px;
        width: 100%;
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

    .class-card:hover{
        transform: scale(1.05);
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

    .class-list .class-detail{
        display: flex;
    }
    
    .class-info p {
        margin: 0;
        font-size: 14px;
        color: gray;
        margin-left: 5px;
        cursor: pointer;
    }

    .class-info p:hover{
        transform: scale(1.1);
    }

    .sets-container {
        display: flex;
        flex-wrap: wrap; 
        gap: 20px;
    }
    
    .card-wrapper {
        flex: 1 1 calc(33.33% - 20px);
        min-width: 250px;
    }
</style>

