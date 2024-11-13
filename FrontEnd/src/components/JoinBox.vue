<script setup>
// chưa chạy được
    import { defineProps, defineEmits } from 'vue';
    import Card from './Set.vue';
    import setsData from '@/data/sets.json'
    import { ref } from 'vue';
    import OverlayBackground from './OverlayBackground.vue';
    

    const { classItem, Overlay_background } = defineProps(['classItem', 'Overlay_background']);

    const emit = defineEmits();

    function closeOverlay(){
        emit('close');
    }

    const join = ref("Join")

    const requestId = ref(21);  

    const join_button = async () => {
  // Nếu trạng thái hiện tại là "Join", gửi yêu cầu API để tham gia lớp học
        if (join.value === "Join") {
            try {
                const token = localStorage.getItem(token);
                const response = await axios.post(`/class/request/join/accept?requestId=${requestId.value}`, // URL API của bạn
                {
                headers: {
                    Authorization: `Bearer ${token}`, // Thêm token vào header để xác thực
                }
                }
            );
            if (response.status === 200) {
                // Nếu yêu cầu thành công, thay đổi trạng thái
                join.value = "Cancel";
            }
            } catch (error) {
            console.error('Error while joining the class:', error);
            // Xử lý lỗi nếu cần
            }
        } else {
            // Nếu trạng thái là "Cancel", bạn có thể gửi yêu cầu hủy tham gia (nếu có API hủy)
            join.value = "Join";
            try {
                const token = localStorage.getItem(token);
                const response = await axios.post(`/class/request/join/reject?requestId=${requestId.value}`, // URL API của bạn
                {
                headers: {
                    Authorization: `Bearer ${token}`, // Thêm token vào header để xác thực
                }
                }
            );
            // if (response.status === 200) {
            //     // Nếu yêu cầu thành công, thay đổi trạng thái
            //     join.value = "Cancel";
            // }
            } catch (error) {
            console.error('Error while joining the class:', error);
            // Xử lý lỗi nếu cần
            }
        }
    }

</script>

<template>
    <OverlayBackground 
        :isVisible="Overlay_background" 
        @clickOverlay="closeOverlay" />
    <!-- <div class="overlay"  @click="Overlay_background = false" v-if= "Overlay_background == true"> -->
        <div class="class-container" @click.stop> 
            <img src="../assets/close.svg" alt="Icon" class="close-icon" @click="closeOverlay">
            <div class="class-info">
                <div class="class-header">
                    <img src="../assets/class.svg" alt="class-icon">
                    <h1>{{ classItem.className }}</h1>
                </div>
                <p class="class-details">{{ classItem.numberOfSets }} {{ classItem.numberOfMembers === 1 ? 'set' : 'sets' }} | {{ classItem.numberOfMembers }} {{ classItem.numberOfMembers === 1 ? 'member' : 'members' }}</p>
                <div class="join-button" @click="join_button">
                    <p>{{ join }}</p>
                </div>
            </div>

            
        </div>
    <!-- </div> -->

</template>

<style scoped>
    .class-container{
        position: fixed;
        top: 25%;
        left: 35%;
        padding: 10px;
        z-index: 15;
        padding: 10;
        display: flex;
        justify-content: center;
        align-items: center;
        border-radius: 5%;
        width: 30%;
        height: 50%;
        background-color: white;
        overflow: hidden;
    }


    .class-container .close-icon{
        position: absolute;
        top: 10px;
        right: 10px;
        cursor: pointer;

    }

    
    .class-container .class-info{
        position: absolute;
        text-align: center;
        justify-content: center;
    }

    .class-info .class-header{
        position: relative;
        display: flex;
        justify-content: center;
        gap: 10px;
    }

    .class-info .class-details{
        margin-bottom: 20px;
    }


    .join-button {
        background-color: #BDEDF5;
        padding: 10px 20px;
        width: 100px;
        border-radius: 20px;
        cursor: pointer;
        display: inline-block;
        box-shadow: 2px;
        box-shadow: 2px 2px 10px rgba(0, 0, 0, 0.15); /* Tăng cường shadow khi hover */

    }

    .join-button p {
        margin: 0;
        font-size: 18px;
        color: #333;
    }

    .join-button:hover {
        background-color: #91e3df;
    }
</style>