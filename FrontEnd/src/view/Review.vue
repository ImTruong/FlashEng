<script setup>
    import { ref, computed, onMounted } from 'vue';
    import { useRoute } from 'vue-router';
    import axios from 'axios';
    import Header from "../components/Header.vue";

    const route = useRoute();
    const currentCard = ref(0);
    const isFlipped = ref(false);
    const totalCards = ref([]);


    const fetchReviewCard = async () => {
        try {
            const token = localStorage.getItem('token');
            const config = {
                headers: {
                    Authorization: `Bearer ${token}` // Thêm token vào header
                }
            };

            // Giả sử API có endpoint là "/review-card"
            const response = await axios.get('/word/userCurrent', config);
            console.log('Review Card:', response.data.data);
            totalCards.value = response.data.data;
        } catch (error) {
            if (error.response) {
                console.error('API Error:', error.response.status, error.response.data);
            } else {
                console.error('Network or Axios error:', error.message);
            }
            throw error; // Ném lỗi để xử lý ở nơi khác nếu cần
        }
    };

    const cardStatus = computed(() => `${currentCard.value + 1}/${totalCards.value.length}`);

    const nextCard = () => {
        isFlipped.value = false;
        currentCard.value = (currentCard.value + 1) % totalCards.value.length; // Chuyển sang thẻ tiếp theo
    };

    const toggleFlip = () => {
        isFlipped.value = !isFlipped.value;
    }

    const submitRating = async (rating) => {
        try {
            const currentWord = totalCards[currentCard].id;
            const studySessionData = {
                wordId: currentWord.id,
                difficulty: rating
            };
            console.log(studySessionData);
            const token = localStorage.getItem('token'); 
            const config = {
                headers: {
                    Authorization: `Bearer ${token}` // Thêm token vào header
                }
            }
            // Make API request to log study session
            await axios.post('/study', studySessionData, config); // Replace with your actual API endpoint
            console.log('Study session created:', studySessionData);
            nextCard();
        } catch (error) {
            console.error('Error submitting rating:', error);
        }
    };

    onMounted(() => {
        fetchReviewCard();
    });
</script>

<template>
    <Header></Header>
    <div class="flashcard-container" v-if="currentSet"> 
        <div class="flashcard-header">
            <h2>Review Flashcards</h2>
            <span>{{ cardStatus }}</span>
        </div>

        <div class="flashcard-content" @click="toggleFlip">
            <div class="flashcard">
                <<div v-if="!isFlipped" class="flashcard-front">
                    <img :src="totalCards[currentCard].image" alt="Flashcard Image" />
                    <p>{{ totalCards[currentCard].word }}</p>
                </div>
                <div v-else class="flashcard-back">
                    <p>{{ totalCards[currentCard].ipa }}</p>
                    <p>{{ totalCards[currentCard].definition }}</p>
                    <p>{{ totalCards[currentCard].example }}</p>
                </div>
            </div>    
        </div>

        <div class="flashcard-rating">
            <button @click="submitRating('Very Difficult')" class="rating-btn">Very Difficult</button>
            <button @click="submitRating('Difficult')" class="rating-btn">Difficult</button>
            <button @click="submitRating('Easy')" class="rating-btn">Easy</button>
            <button @click="submitRating('Very Easy')" class="rating-btn">Very Easy</button>
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

    .flashcard {
        width: 100%;
        max-width: 300px; 
        margin: 0 auto;
        text-align: center;
    }
    
    .flashcard img {
        max-width: 100%; /* Ảnh sẽ không vượt quá chiều rộng của flashcard */
        height: auto; 
        object-fit: cover; 
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
