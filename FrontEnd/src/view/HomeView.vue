<script setup>
    import { ref, computed, onMounted } from "vue";
    import Header from "@/components/Header.vue";
    import Card from "@/components/Set.vue";
    import { useRouter } from "vue-router"; 
    import { useStore } from "vuex";
    
    const router = useRouter();
    const store = useStore();
    const sets = computed(() => store.getters.getSets);
    const displayedSets = ref([]);

    onMounted(() => {
        store.dispatch('fetchLibrarySets').then(() => {
            // Lấy lại giá trị sets sau khi fetch xong
            displayedSets.value = sets.value.slice(0, 3); // Cập nhật danh sách hiển thị
        });
    });   
    // Hàm hiển thị toàn bộ dữ liệu khi nhấn "More..."
    const showAllSetsRecent = () => {
        displayedSets.value = sets;
    };

    // Điều hướng đến trang "/classes" khi nhấn "More..."
    const showAllSets = () => {
        router.push("/classes");
    };

</script>

<template>
    <Header />
    <div class="home">
        <!-- Section Recent -->
        <h1 class="section-header">
            <span class="section-title">Recent</span>
            <span v-if="sets.length > 3" class="more-link" @click="showAllSetsRecent">More...</span>
        </h1>
        <div class="set-container">
            <Card 
                v-for="set in displayedSets" 
                :key="set.id" 
                :set="set" />
        </div>

        <h1 class="section-header">
            <span class="section-title">Your Library</span>
            <span v-if="sets.length > 3" class="more-link" @click="showAllSets">More...</span>
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