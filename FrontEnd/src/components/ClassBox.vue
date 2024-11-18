<script setup>
    import { onMounted, computed, defineProps, defineEmits } from 'vue';
    import Card from './Set.vue';
    import OverlayBackground from './OverlayBackground.vue';
    import ClassTable from './ClassTable.vue';
    import { ref, watch } from 'vue';
    import { useStore } from 'vuex';
    import axios from 'axios';
    import SetTable from '../components/SetTable.vue'

    const store = useStore();
    const classTable = ref(false);
    // const sets = computed(() => store.getters.getSets);
    const sets = ref([])
    const filteredSets = ref([]);
    const visible = ref(true);
    const isEditMode = ref(false);
    const existingClass = ref({});
    const className = localStorage.getItem('className');
    const classId = localStorage.getItem('classId');
    const setTable = ref(false)
    const existingSet = ref(null);
    const { Overlay_background } = defineProps(['Overlay_background']);
    const emit = defineEmits();
    const inClass = ref(true)
    const icon = ref(false);
    const search = ref("");

    function closeOverlay() {
        emit('close');
    }

    const showClassTable = (classItem) => {
        console.log(isEditMode);
        classTable.value = true;
        visible.value = false;
        icon.value = false;
        existingClass.value = classItem;
    };

    const closeClassTable = () => {
        classTable.value = false;
        visible.value = true;
    };

    // const handleUpdate = (updatedRows) => {
    //     existingClass.value.className;
    // };

    // Theo dõi thay đổi trong ô tìm kiếm và cập nhật danh sách `filteredSets`
    watch(search, () => {
        filteredSets.value = sets.value.filter(set =>
            set.name.toLowerCase().includes(search.value.toLowerCase())
        );
    });

    // đã test chưa cập nhâpj lại lớp 
    const leaveClass = async () => {
        try {
            const token = localStorage.getItem('token');
            console.log(token);

            const response = await axios.delete(`/class/member/leave`, {
                params: {
                    classId: classId, // Sử dụng giá trị của classId
                },
                headers: {
                    Authorization: `Bearer ${token}`, // Đính kèm token vào headers
                },
            });

            console.log(response);
            alert(response.data.message);
            window.location.reload();
        } catch (error) {
            console.log(error);
            if (error.response && error.response.data && error.response.data.message) {
                alert(error.response.data.message);
            } else {
                alert("An error occurred. Please try again.");
            }
        }
    };

    const getSetByClassId = async() => {
        try{
            const token = localStorage.getItem('token');
            console.log(classId)
            const response = await axios.get(`/set/class/${classId}`, {
                headers: {
                    Authorization: `Bearer ${token}`, // Attach the token in the request headers
                },
            });
            console.log(response.data.data);
            sets.value = response.data.data
            console.log(sets.value)
        }catch(error){
            console.log(error);
        }
    };

    const handleSet = (data) => {
        existingSet.value = data; // Gán dữ liệu cho set hiện tại nếu cần
    }

    const showSetTable = (editMode = false) => {
        isEditMode.value = editMode;
        setTable.value = true; // Hiển thị SetTable
        // visible.value = false;
        icon.value = false;
        existingClass.value = classItem;  
    };
    
    onMounted(() => {
        getSetByClassId();
    });
</script>

<template>
    <OverlayBackground 
        :isVisible="OverlayBackground" 
        @clickOverlay="closeOverlay" />
    <div class="classbox-container" v-if="visible"> 
        <div class="search-container">
            
            <input v-model.trim="search" type="text" placeholder="Search ..." class="search-bar"/>
            <img src="../assets/search.svg" alt="Icon" class="search-icon">
        </div>
        <div v-if="icon" class="icon">
            <img src="../assets/add_set.svg" alt="Icon" class="add-set-icon" @click="showSetTable(false)">
            <img src="../assets/add_member.svg" alt="Icon" class="add-member-icon" @click="showClassTable">
            <img src="../assets/leave-group.svg" alt="Icon" class="leave-group-icon" @click="leaveClass">
        </div>
        <h2 @click="icon = !icon">
            {{ className }}
            <img src="../assets/dropdown.svg" alt="Icon" class="dropdown-icon" >
        </h2>
        <img src="../assets/close.svg" alt="Icon" class="close-icon" @click="closeOverlay">
        <div class="line"></div>
        
        <div class="card-container">
            <Card 
                v-for="set in sets" 
                :key="set.id" 
                :set="set" 
                :classId="classId"/>
        </div>
        <SetTable 
            v-if="setTable" 
            :classId="classId"
            :className="className"
            :inClass="inClass = true"
            :isEditMode="isEditMode"
            :existingSet="existingSet"
            @close="setTable = false" 
            @save="handleSet"
            />
    </div>
    
    <ClassTable 
        v-if="classTable" 
        :isEditMode=true  
        @close="closeClassTable" 
        
    />   
</template>


<style scoped>

    .classbox-container{
        position: fixed;
        top: 15vh;
        left: 20vw;
        height: 75vh;
        width: 60vw;
        display: flex;
        justify-content: center;
        align-items: center;
        border-radius: 8px;
        background-color: white;
        overflow: hidden;
        z-index: 11;
    }

    .dropdown-icon{
        width: 30px;
    }

    .dropdown-icon:hover{
        transform: scale(1.1);
    }

    .classbox-container .close-icon{
        position: absolute;
        top: 10px;
        right: 10px;
        cursor: pointer;

    }

    .classbox-container h2{
        position: absolute;
        top: 25px;
        left: 10%;
        cursor: pointer;
        display: flex;
        align-content: center;
    }

    .classbox-container h2:hover{
        transform: scale(1.1);
    }

    .search-container{
        position: absolute; 
        text-align: center;
        justify-content: center;        
        width: 30%;
        top: 25px;
        left: 60%;
    }

    .search-container .search-icon{
        position: absolute;
        left: 10px;
        top: 7px;
    }

    .search-container .search-bar{
        width: 100%;
        padding: 8px 16px 8px 30px;
        border-radius: 5px;
        display:inline-block;
        cursor: pointer;
    }

    .icon{
        position: absolute;
        top: 15%;
        left: 15%;
        display: flex;
        justify-content: center;
        align-items: center;
        width: 200px;
        height: 60px;
        background-color: rgb(251, 251, 251);
        z-index: 12;
        box-shadow: 0px 4px 10px rgba(0, 0, 0, 0.5);

    }
    
    .add-set-icon{
        cursor: pointer;
        height: 40px;
    } 
    .add-set-icon:hover{
        transform: scale(1.05);
    }

    .add-member-icon{
        cursor: pointer;
        height: 40px;
    } 

    .add-member-icon:hover{
        transform: scale(1.05);
    }
    .leave-group-icon{
        cursor: pointer;
        height: 35px;

    } 
    .leave-group-icon:hover{
        transform: scale(1.05);
    }
    .classbox-container .line{
        position: absolute;
        top: 15%;
        width: 80%;
        height: 1px;
        left: 10%;
        z-index: 10;
        background-color: rgba(71, 59, 59, 0.5)
    }

    .card-container {
        position: absolute;
        width: 100%;
        height: 80%;
        top:17%;
        padding: 10px;
        display: flex;
        justify-content: center;
        flex-wrap: wrap;
        gap: 10px;
        overflow: auto;
    }


</style>