<script setup>
  import {defineProps} from "vue"
  import {useRouter} from "vue-router"
  
  const router = useRouter()
  const {set} = defineProps(['set'])

  const navigateToSet = () => {
    router.push(`/set/${set.id}`);
  }
</script>

<template>
    <div class="card" @click="navigateToSet">
      <div class="card-text">
        <h2>{{ set.name }}</h2>
        <p class="number-terms">{{ set.terms }} terms</p>
        <p class="owner">{{ set.owner }}</p>
      </div>
    </div>
</template>
  

<style scoped>
  .card {
    margin-left: 10px;
    width: 30%;
    min-width: 250px;
    height: 160px;
    overflow: hidden;
    border-radius: 4%;
    box-shadow: 1px 1px 10px rgba(0, 0, 0, 0.1);
    margin-bottom: 35px;
    cursor: pointer;
  }
  
  .card:hover {
    transform: scale(1.05);
    }

    .card-text {
        position: relative;
        /* margin-top: 10px; */
        padding: 15px;
        width: 100%;
        height: 100%;
    }

    .card-text h2 {
        font-weight: 500;

    }

    .card-text p {
        color: #777;
        margin-top: 5px;
        /* margin-bottom: 40px; */
    }

    .owner{
      position: absolute;
      top:70%;
    }
    

    .number-terms{
        position: relative;
        background-color: rgba(105, 105, 148, 0.2);
        border-radius: 5px;
        width: 70px;
        height: 25px;
        display: flex;
        text-align: center;
        justify-content: center;
        /* top: 30% */
    }

</style>