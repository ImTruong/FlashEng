<script setup>
    import { ref, computed } from 'vue';
    import cardData from '../data/sets.json';
    import Header from "../components/Header.vue";
    import { useRoute } from 'vue-router';


    const route = useRoute();
    const selectedSet = ref(route.params.id);


    const currentCard = ref(0);
    const isFlipped = ref(false);
    const currentSet = ref(cardData.find(set => set.id == selectedSet.value)); 
    const totalCards = computed(() => currentSet.value ? currentSet.value.words.length : 0); 

    // Trạng thái của thẻ hiện tại
    const cardStatus = computed(() => `${currentCard.value + 1}/${totalCards.value}`); // Hiển thị trạng thái thẻ hiện tại


    const nextCard = () => {
        isFlipped.value = false
        currentCard.value = (currentCard.value + 1) % totalCards.value; // Chuyển sang thẻ tiếp theo
    };
    const toggleFlip = () => {
        isFlipped.value = !isFlipped.value;
    };
</script>

<template>
    <Header></Header>
    <div class="flashcard-container" v-if="currentSet"> 
        <div class="flashcard-header">
            <h2>{{ currentSet.name }}</h2>
            <span>{{ cardStatus }}</span>
        </div>

        <div class="flashcard-content" @click="toggleFlip">
            <div class="flashcard">
                <div v-if="!isFlipped" class="flashcard-front">
                    <img :src="currentSet.words[currentCard].image" alt="Flashcard Image" />
                    <p>{{ currentSet.words[currentCard].word }}</p>
                  </div>
                  <div v-else class="flashcard-back">
                    <p>{{ currentSet.words[currentCard].ipa }}</p>
                    <p>{{ currentSet.words[currentCard].definition }}</p>
                    <p>{{ currentSet.words[currentCard].example }}</p>
                  </div>
            </div>    
        </div>

        <div class="flashcard-rating">
            <button @click="nextCard" class="rating-btn">Very Difficult</button>
            <button @click="nextCard" class="rating-btn">Difficult</button>
            <button @click="nextCard" class="rating-btn">Easy</button>
            <button @click="nextCard" class="rating-btn">Very Easy</button>
        </div>
    </div>
    <div v-else>Loading...</div> 
</template>

<style scoped>
    .flashcard-container {
        max-width: 500px; 
        margin: 40px auto;
        padding: 20px;
        text-align: center;
    }

    .flashcard-header {
        display: flex;
        justify-content: center;
        flex-direction: column;
        align-items: center;
        font-size: 1.2em;
    }

    .flashcard-content {
        padding: 20px;
        border-radius: 10px;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.3); 
        background-color: #f9f9f9;
        border: none;
    }

    .flashcard-image {
        width: 60%;
        height: auto;
        border-radius: 8px;
    }

    .flashcard-word {
        font-size: 1.5em;
        margin-top: 10px;
    }

    .flashcard-rating {
        display: flex;
        justify-content: space-between;
        margin-top: 20px;
        gap: 10px;
    }

    .rating-btn {
        background-color: #DBF3F7;
        border: 1px solid #d3d3d3; 
        box-shadow: 0 2px 4px rgba(0, 0, 0, 0.2); 
        padding: 10px 0;
        border-radius: 5px;
        cursor: pointer;
        transition: background-color 0.3s;
        width: 100%; 
        text-align: center;
        font-size: 1em;
    }

    .rating-btn:hover {
        background-color: #b0b0b0;
    }
    .flashcard-content {
        perspective: 1000px; /* Tạo hiệu ứng 3D */
    }
    
    .flashcard-back{
        max-width: 500px; 
        margin: 40px auto;
        padding: 20px;
        text-align: center;
        font-size: 25px;
    }
    
</style>
