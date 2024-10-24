<script setup>
  import {ref, defineProps} from "vue"
  import {useRouter} from "vue-router"
  
  const hover  = ref(false)
  const router = useRouter()
  const {set} = defineProps(['set'])

  const navigateToSet = () => {
    router.push(`/set/${set.id}`);
  }

  const editSet = () => {
    console.log(`Editing set ${set.id}`)
  }

  const gameSet = () => {
    console.log(`Editing set ${set.id}`)
  }

  const studySet = () => {
    console.log(`Viewing set ${set.id}`)
  }

  const deleteSet = () => {
    console.log(`Deleting set ${set.id}`)
  }
</script>

<template>
    <div class="card" @mouseover="hover = true" @mouseleave="hover = false" >
      <div class="card-text" @click="navigateToSet">
        <h2>{{ set.name }}</h2>
        <p class="number-terms">{{ set.terms }} terms</p>
        <p>{{ set.owner }}</p>
      </div>
      <div class="set-option">
        <button class="icon-container" @click.stop="studySet" >
          <img src="../assets/study.svg" alt="Study">
        </button>
        <button class="icon-container" @click.stop="gameSet" >
          <img src="../assets/game.svg" alt="Game">
        </button>
        <button class="icon-container" @click.stop="editSet" >
          <img src="../assets/edit.svg" alt="Edit">
        </button>
        <button class="icon-container" @click.stop="deleteSet" >
          <img src="../assets/delete.svg" alt="Delete">
        </button>
      </div>
    </div>
</template>
  

<style scoped>
  .card {
    margin-left: 10px;
    width: 28%;
    min-width: 280px;
    height: 160px;
    overflow: hidden;
    border-radius: 4%;
    box-shadow: 1px 1px 10px rgba(0, 0, 0, 0.1);
    margin-bottom: 35px;
    cursor: pointer;
    position: relative;
  }
  
  .card:hover {
    transform: scale(1.05);
    background-color:#d9d7d7ac;
  }

  .card-text {
      position: absolute;
      padding: 10px;
  }

  .card-text h2 {
      font-weight: 500;

  }

  .card-text p {
      color: #777;
      margin-top: 5px;
      margin-bottom: 40px;
  }

  .number-terms{
      background-color: rgba(105, 105, 148, 0.2);
      border-radius: 5px;
      width: 60px;
      height: 25px;
      text-align: center;
  }

  .set-option {
    position: absolute;
    display: flex;
    gap: 0px;
    visibility: hidden;
    transition: visibility 0.3s ease;
    justify-content: center;
    align-items: center;
    flex-wrap: wrap;
  }

  .card:hover .set-option {
    visibility: visible;
  }

  .set-option button {
    background-color: transparent;
    border: none;
    cursor: pointer;
    padding: 0px;
  }

  .set-option button:hover {
    transform: scale(1.1);
  }

  .option-icon img {
    width: 16px; /* Đặt kích thước hợp lý cho các biểu tượng */
    height: 16px;
  }

</style>