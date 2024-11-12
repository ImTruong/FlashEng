<script setup>
    import { ref, computed, onMounted } from 'vue';
    import axios from 'axios';
    import Header from "../components/Header.vue";
    import { useRouter } from 'vue-router'; // Import Vue Router

    const currentCard = ref(0);
    const isFlipped = ref(false);
    const totalCards = ref([]);
    const isCompleted = ref(false)
    const router = useRouter()


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
            totalCards.value = response.data.data;
            console.log('Review Card:', totalCards.value);

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
        if (currentCard.value + 1 >= totalCards.value.length) {
        isCompleted.value = true; // Đánh dấu là đã hoàn thành
        setTimeout(() => {
            router.push('/'); // Chuyển hướng về trang home sau khi thông báo hoàn thành
        }, 10000); // Chờ 2 giây để người dùng có thể nhìn thấy thông báo
        } else {
        isFlipped.value = false;
        currentCard.value += 1;
        }
    };

    const toggleFlip = () => {
        isFlipped.value = !isFlipped.value;
    }

    const submitRating = async (rating) => {
        try {
            const currentWord = totalCards.value[currentCard.value];
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
    const handleComplete = () => {
        router.push('/');
    };

    onMounted(() => {
        fetchReviewCard();
    });
</script>

<template>
    <Header></Header>
    <div class="flashcard-container" v-if="totalCards.length > 0"> 
        <div class="flashcard-header">
            <h2>Review</h2>
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
        <div class="alert completed" v-if="isCompleted">
            <div class="message">
                <p>Completed!</p>
                <button @click="handleComplete">Go to Home</button>
            </div>
        </div>
    </div>
    <div v-else>
        <div class="alert no-cards" v-if="totalCards.length === 0">
            <div class="message">
                <p>No cards available</p>
                <button @click="handleComplete">Go to Home</button>
            </div>
        </div>
    </div></template>

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
    
    .alert {
        position: fixed;
        top: 0;
        left: 0;
        right: 0;
        bottom: 0;
        display: flex;
        justify-content: center;
        align-items: center;
        background-color: rgba(0, 0, 0, 0.5);
        z-index: 1000;
    }
    
    .alert .message {
        background-color: white;
        padding: 40px 50px;
        border-radius: 12px;
        text-align: center;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.3);
    }
    
    .alert .message p {
        font-size: 32px;
        font-weight: bold;
        margin-bottom: 20px;
    }
    
    .alert .message button {
        padding: 15px 30px;
        background-color: #3498db;
        color: white;
        border: none;
        border-radius: 5px;
        cursor: pointer;
        font-size: 18px;
    }
    
    .alert .message button:hover {
        background-color: #2980b9;
    }
    
    /* Tùy chỉnh cho thông báo "No cards available" */
    .no-cards .message p {
        color: #e66355; /* Màu đỏ */
    }
    
    /* Tùy chỉnh cho thông báo "Completed" */
    .completed .message p {
        color: #69cee0; /* Màu xanh */
    }
</style>
