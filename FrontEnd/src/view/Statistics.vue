<script setup>
    import Header from '@/components/Header.vue';    // import statistic from '@/data/statistic.json'
    import { ref, onMounted } from 'vue';
    import axios from 'axios';

    const data = ref("");

    const fetchStatistic = async () => {
        try {
            const token = localStorage.getItem('token');
            if(!token){
                console.warn('Not found the token!');
                return;
            }
            const response = await axios.get('/study', {
                headers: {
                Authorization: `Bearer ${token}`
                }
            });
            data.value = response.data.data;
        }
        catch (error) {
            console.error("Error fetching statistics:", error);
        }
    };

    onMounted(() => {
        fetchStatistic();
    });
</script>

<template>
    <Header></Header>
    <div class="container">
        <div class="header">
            <h1>Statistic</h1>
        </div>
        <div v-for="d in data"  class="details">
            <!-- <div class="date"> -->
                <tr>
                    <td>
                        <div class="point"></div>
                    </td>
                    <td class="date">{{ d.date }}</td>
                    <td class="words">{{ d.numberOfWords }} words</td>
                    <td >
                        <div class="bar" :style="{width: d.numberOfWords * 15 + 'px'}" > </div>
                    </td>
                </tr>
                <!-- <p class="date">{{ d['Date'] }}</p>
                <p> {{ d['Number of words'] }}</p> -->
            <!-- </div> -->
        </div>

    </div>
</template>

<style scoped>
    .container {
        margin-top: 80px;
        margin-left: 100px;
    }
    .header{
        position: relative;
        margin-bottom: 30px;
    }

    .details{
        display: flex;
        margin-left: 30px;
    }

    tr{
        height: 50px;
    }

    .date{
        width: 150px;
        font-size: 20px;
        text-align: center;
    }
    .words{
        width: 150px;
        font-size: 20px;
    }
    .point{
        height: 10px;
        width: 10px;
        background-color: #BDEDF5;
        border-radius: 50%;
    }
    .bar{
        border-radius: 20px;
        height: 8px;
        width: 200px;
        background-color: #94e6f5;
    }
</style>