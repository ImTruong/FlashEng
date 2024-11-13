<script setup>
    import { ref, computed, onMounted } from "vue";
    import Header from "@/components/Header.vue";
    import Card from "@/components/Set.vue";
    import { useRouter } from "vue-router"; 
    import { useStore } from "vuex";
    import axios from "axios";
    
    const router = useRouter();
    const store = useStore();
    const set = computed(() => store.getters.getSets);
    const displayedSets = ref([]);
    const errorMessage = ref(null);

    onMounted(() => {
        store.dispatch('fetchLibrarySets').then(() => {
            // Lấy lại giá trị sets sau khi fetch xong
            displayedSets.value = set.value.slice(0, 3); // Cập nhật danh sách hiển thị
        });
    });   
    // Hàm hiển thị toàn bộ dữ liệu khi nhấn "More..."
    const showAllSetsRecent = () => {
        displayedSets.value = set;
    };

    // Điều hướng đến trang "/classes" khi nhấn "More..."
    const showAllSets = () => {
        router.push("/classes");
    };

    const fetchUserInfo = async () => {
        try {
            const token = localStorage.getItem('token') // Lấy token từ localStorage
            if (!token) {
            errorMessage.value = 'You must be logged in to view user information'
            return
            }
            const response = await axios.get('/user', {
            headers: {
                Authorization: `Bearer ${token}`, // Đảm bảo gửi token trong header
            },
            })
            localStorage.setItem('user', JSON.stringify(response.data.data));
            console.log(localStorage.getItem('user'));
        } catch (error) {
            errorMessage.value = error.response ? error.response.data : 'An error occurred'
            console.error('Error fetching user info:', error)
        }                                 
    };

    onMounted(() => {
        fetchUserInfo();
    });

</script>

<template>
    <Header />
    <div class="home">
        <!-- Section Recent -->
        <h1 class="section-header">
            <span class="section-title">Recent</span>
            <span v-if="set.length > 3" class="more-link" @click="showAllSetsRecent">More...</span>
        </h1>
        <div class="set-container">
            <Card 
                v-for="set in displayedSets" 
                :key="set.id" 
                :set="set" />
        </div>

        <h1 class="section-header">
            <span class="section-title">Your Library</span>
            <span v-if="set.length > 3" class="more-link" @click="showAllSets">More...</span>
        </h1>
        <div class="set-container">
            <Card 
                v-for="set in displayedSets" 
                :key="set.id" 
                :set="set" />
        </div>
    </div>
</template>

<style scoped>
    .home {
        padding: 50px;
        height: 100vh; 
        margin-left: 20px;
        margin-right: 30px;
    }

    .section-title {
        font-weight: bold;
        margin-bottom: 20px;
    }

    .section-header {
        font-weight: bold;
        margin-bottom: 20px;
        display: flex;
        justify-content:space-between;
        align-items: center;
    }

    .set-container {
        display: flex;
        flex-wrap: wrap;
        gap: 20px;
    }

    .set-container .Card{
        flex: 1 1 calc(20% - 20px);
        box-sizing: border-box;
        height: 350px;
    }

    .more-link {
        font-size: 0.9rem;
        color: gray;
        cursor: pointer;
        margin-left: 20px;
    }

</style>