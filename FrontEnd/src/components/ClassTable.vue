<script setup>
    import { ref, watch, defineEmits, defineProps, onMounted} from 'vue';
    import OverlayBackground from '../components/OverlayBackground.vue'
    // import { debounce } from 'lodash'; // Nếu bạn sử dụng lodash
    import AddMember from './AddMember.vue';
    import axios from 'axios';

    const emit = defineEmits(['close', 'save']);

    const props = defineProps(['isEditMode']);

    const visible = ref(true); 
    const className = ref(props.isEditMode ? localStorage.getItem.className : '');
    const rows = ref([{ username: '', role: ''}]);
    const selectedUsers = ref([]); // Danh sách các từ được chọn
    const showSelectColumn = ref(false);
    const selectedOption = ref('')
    const dropdownRef = ref(null)
    const search = ref("")
    const showSearch = ref(false);
    const showAddMember = ref(false);
    const roleFilter = ref("Role");
    const classId = ref(null);
    const errorMessage = ref(null);
    const memberList = ref([]);

    const updateClassName = (newClassName) => {
        className.value = newClassName;
    };

    const saveData  = async () => {
        const token = localStorage.getItem('token');
        const payload = {
            classId: props.isEditMode ? localStorage.getItem('classId') : null,
            name: className.value,
        }
        try {
            const config = {
            headers: {
                Authorization: `Bearer ${token}` // Thêm token vào header
            }
            }
            if (props.isEditMode) {
                
                const response = await axios.put('/class/update/name', payload, { headers: config.headers });  // API cập nhật
                // emit('update', response.data.data); 
                alert(response.data.message);
                localStorage.setItem('classId', classId.value);
                localStorage.setItem('className', className.value);
            } else {
                const response = await axios.post('/class', payload, { headers: config.headers }); 
                console.log(response.data)
                classId.value = response.data.data.classId;
                console.log(classId.value);
                emit('save', response.data.data); 
                rows.value = response.data.data.memberList;
                memberList.value = response.data.data.memberList;
            }
            localStorage.setItem('classId', classId.value);
            localStorage.setItem('className', className.value);
            // alert(res)
        } catch (error) {
            console.log(error);
            if (error.response && error.response.data && error.response.data.message) {
                alert(error.response.data.message);
                className.value = localStorage.getItem('className');
                console.log(className.value);
            } else {
                alert("An error occurred. Please try again.");
            }
            // if (error.response) {
            //     console.error('API Error:', error.response.status, error.response.data);
            //     alert("You are not authorized to change this class's name.")
            //     className.value = localStorage.getItem('className');
            //     console.log(className.value);
            // } else {
            //     console.error('Network or Axios error:', error.message);
            // }
        }
    };

    const removeRow = async () => {
        if(selectedUsers.value.length > 0 && !selectedUsers.value[0]){
            return;
        }
        if (selectedUsers.value.length > 0) {
            console.log('Selected Users:', selectedUsers.value);
            const token = localStorage.getItem('token');
            const classId = localStorage.getItem('classId');
            for (const userId of selectedUsers.value) {
                try {
                    const config = {
                        headers: {
                            Authorization: `Bearer ${token}`
                        }
                    };
                    const user = rows.value.find(row => row.userId === userId);
                    if (!user) {
                        console.error('Không tìm thấy từ với ID:', userId);
                        continue; // Skip nếu không tìm thấy từ
                    }
                    console.log('Request URL:', `/class/member/delete?userId=${userId}&classId=${classId}`);
                    const response = await axios.delete(`/class/member/delete?userId=${userId}&classId=${classId}`, config);
                    console.log('User deleted:', response.message);
                    rows.value = rows.value.filter(row => row.userId !== userId); // Xóa từ khỏi bảng
                    memberList.value = rows.value;
                    if(memberList.value.length === 0){
                        window.location.reload();
                    }
                } catch (error) {
                    console.log(error);
                    if (error.response && error.response.data && error.response.data.message) {
                        alert(error.response.data.message);
                    } else {
                        alert("An error occurred. Please try again.");
                    }
                }
            }
            selectedUsers.value = []; // Reset selectedusers sau khi xóa
        }
        emit('update', rows.value)                                                                                          
    };

    const closeForm = () => {
        emit('close');
        visible.value = false;
    };

    const toggleSelectMember = (userId) => {
        const index = selectedUsers.value.indexOf(userId);
        if (index === -1) {
            // Thêm ID vào nếu chưa có
            selectedUsers.value.push(userId);
        } else {
            // Loại bỏ ID nếu đã có
            selectedUsers.value.splice(index, 1);
        }
    };

    const toggleSelectColumn = () => {
        showSelectColumn.value = !showSelectColumn.value;
    };


    watch([search, showSearch], () =>{
        console.log(showSearch.value && search.value);
        console.log(memberList.value);
        if(!showSearch.value || search.value == ""){
            memberList.value = rows.value;
            // memberList.value = rows.value.filter(member => member.userName.toLowerCase().includes(search.value.toLowerCase()))
        }
        else{
            memberList.value = rows.value.filter(member => member.userName.toLowerCase().includes(search.value.toLowerCase()))
        }
    })

    const toggleSearch = () =>{
        showSearch.value = !showSearch.value;
        search.value = "";
    }

    const openAddMember = () => {
        showAddMember.value = true;
        visible.value = false;
    };

    const closeAddMember = () => {
        showAddMember.value = false;
        visible.value = true
    };
    
    const handleSaveData = () => {
        if (className.value.trim()) {
            saveData();
        } 
        else {
            alert("Class name is empty!");
        }
    };

    const getMember = async () => {
        try {
            const token = localStorage.getItem('token') // Lấy token từ localStorage
            if (!token) {
            errorMessage.value = 'You must be logged in to view user information'
            return
            }
            const response = await axios.get(`class/member/list?classId=${classId.value}`, {
                headers: {
                    Authorization: `Bearer ${token}`, // Đảm bảo gửi token trong header
                },
            })
            rows.value = response.data.data.memberList;
            memberList.value = rows.value;
            console.log(rows.value);
        } catch (error) {
            errorMessage.value = error.response ? error.response.data : 'An error occurred'
            console.error('Error fetching user info:', error)
        }                                 
    };

    const updateRole = async (user) => {
        const token = localStorage.getItem('token'); // Lấy token từ localStorage
        const payload = {
            userId: user.userId,
            classId: classId.value,
            role: user.role,
        };

        try {
            const response = await axios.put('/class/member/role', payload, {
                headers: {
                    Authorization: `Bearer ${token}`, // Gửi token trong header
                },
            });
            console.log('Role updated:', response.data);
            alert(response.data.message);
        } catch (error) {
            if(user.role == "ADMIN") user.role = "MEMBER"
            else user.role = "ADMIN";
            console.error('Error updating role:', error);
            const errorMessage = error.response.data.message;
            console.error('Error response:', error.response.data);
            
            // Hiển thị thông báo lỗi nếu có message từ server
            if (errorMessage) {
                alert(errorMessage);
            } else {
                alert("An error occurred. Please try again.");
            }
        }
    };



    onMounted(() => {
        if(props.isEditMode){
            classId.value = localStorage.getItem('classId');
            className.value = localStorage.getItem('className');
            getMember();
        }
        else{
            memberList.value = rows.value;
        }
    });

    watch(roleFilter, () =>{
        if(roleFilter.value == "Role"){
            memberList.value = rows.value;
        }
        else{
            memberList.value = rows.value.filter(member => member.role === roleFilter.value);
        }
    })

</script>

<template>
    <OverlayBackground :isVisible="visible" @clickOverlay="closeForm" />
    <div v-if="visible" class="class-window">
        <div class="class-header">
            <img src="../assets/search_icon.svg" alt="Status" @click="toggleSearch" class="search-icon">
            <input v-model.trim = "search" v-if="showSearch" type="text" placeholder="Search for username" class="search-bar">
            <div class="class-name" v-if="!showSearch">
                <label for="class-name">Class:</label>
                <input id="class-name" v-model="className" placeholder="Enter class name" />
            </div>
            <button @click="closeForm" class="close-btn">✖</button>
        </div>


    <div class="table-container">
        <!-- {{ memberList }} -->
        <table class="class-table">
            <thead>
              <tr>
                <th v-if="showSelectColumn" class="select-column">Select</th>
                <th class="username-column">Username</th>
                <th>
                    <select class="role-option" v-model="roleFilter">
                        <option value="Role">Role</option>
                        <option value="ADMIN">ADMIN</option>
                        <option value="MEMBER" selected>MEMBER</option>
                    </select>
                </th>
              </tr>
            </thead>
            <tbody>
                <tr v-for="(row, index) in memberList" :key="index">
                    <td v-if="showSelectColumn">
                        <input type="checkbox" @change="toggleSelectMember(row.userId)" :checked="selectedUsers.includes(row.userId)" />
                    </td>
                    <!-- <td> {{ row.userId }}</td> -->
                    <td class="username-column"><p>{{row.userName}}</p></td>
                    <td class="role">
                        <!-- Thay input bằng select -->
                        <select v-if="row.role" class="role-option" v-model="row.role" @change="updateRole(row)">
                            <option value="ADMIN">ADMIN</option>
                            <option value="MEMBER" selected>MEMBER</option>
                        </select>
                    </td>              
                </tr>
            </tbody>
          </table>
    </div>
  
    <div class="actions">
        <button @click="toggleSelectColumn" class="icon-button">
            <img src="../assets/select.svg" alt="" class="icon">
        </button>
        <button @click="removeRow" class="icon-button">
            <img src="../assets/delete-word.svg" alt="" class="icon">
        </button>
        <button @click="openAddMember" class="icon-button">
            <img src="../assets/add-word.svg" alt="" class="icon">
        </button>
        <button @click="handleSaveData" class="icon-button">
            <img src="../assets/save.svg" alt="" class="icon">
        </button>
    </div>
    </div>
    
    <AddMember :setName="setName" v-if="showAddMember" @close = "closeAddMember" @save= "addMember"></AddMember>
</template>  
  
<style scoped>
    .class-window {
        position: fixed;
        top: 100px;
        left: 50%;
        transform: translateX(-50%);
        background-color: white;
        border-radius: 8px;
        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
        padding: 20px;
        width: 40%;
        z-index: 1000;
    }
  
    .class-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .search-icon{
        height: 20px;
        cursor: pointer;
    }

    .search-icon:hover{
        transform: scale(1.1);
    }
  
    .class-header img{
        margin-left: 10px;
    }
    
    .class-header .search-bar{
        margin-left: 10px;
        padding: 5px;
        border: 1px solid black;
        border-radius: 4px;
        text-align: center; 
        width: 50%;
    }

    

    .class-name {
        display: flex;
        align-items: center;
        justify-content: center;
        flex-grow: 1;
    }



    .class-name input {
        margin-left: 10px;
        padding: 5px;
        border: 1px solid black;
        border-radius: 4px;
        text-align: center; 
        width: 50%;
    }
  
    .table-container {
        max-height: 300px; 
        overflow-y: auto; 
        margin-top: 20px; 
        flex-grow: 1; 
        position: relative;
    }
    .class-table {
        min-height: 70px;
        width: 100%;
        margin-top: 20px;
        border-collapse: collapse; /* Bỏ khoảng cách giữa các cột */
        table-layout: fixed;
    }

    .class-table thead th {
        background-color: #A8D5E5; 
        border: 1px solid black;
        position: sticky;
        top: 0; /* Cố định hàng tiêu đề khi cuộn */
        z-index: 1; /* Đảm bảo hàng tiêu đề luôn nằm trên cùng */
    }
    .class-table th {
        padding: 5px;
        border: 1px solid #ccc;
        text-align: center; 
    }

    .class-table th select{
        background-color: #A8D5E5;
        width: 80px;
        border: none;

    }
    
    .class-table th option{
        height: 25px;
        background-color: #ffffff;
    }


    .class-table td {
        padding: 5px;
        border: 1px solid #ccc;
        text-align: center; 
    }

    .role-option {
        width: 80px;
        height: 25px;
        border: none;
        cursor: pointer;
    }


    .select-column {
        width: 50px; /* Chiều rộng cho cột Select */
    }
    
    .username-column{
        width: 300px
    }

    .username-column p{
        text-align: left;
        margin-left: 10px;
    }
  
    .actions {
        display: flex;
        justify-content: space-around; /* Căn giữa các icon */
        margin: 5px;
        margin-top: 15px;
    }

    .icon-button {
        cursor: pointer; 
        width: 80px; 
        height: 40px; 
        display: flex;
        align-items: center; 
        justify-content: center; 
        border: none; 
        background: none; 
        transition: background-color 0.3s; 
    }

    .icon-button:hover {
        transform: scale(1.05);
    }

    .icon {
        width: 100%; 
        height: auto; 
        cursor: pointer;
    }

    .close-btn {
        background: none;
        border: none;
        font-size: 20px;
        cursor: pointer;
    }


    .options-dropdown {
        position: absolute; 
        background-color: white;
        border: 1px solid #ccc;
        border-radius: 4px;
        box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
        margin-top: 5px; 
        z-index: 1001; 
    }
    
    .option-button {
        display: flex; 
        align-items: center; 
        padding: 10px;
        border: none;
        background: none;
        width: 100%; 
        text-align: left; 
        cursor: pointer;
    }
    
    .option-button:hover {
        background-color: #f0f0f0;
    }
    
    .option-icon {
        width: 20px; 
        height: 20px; 
        margin-right: 15px; 
        cursor: pointer;
    }
    
    .checkmark {
    color:rgb(218, 87, 87); 
        margin-left: 15px; 
    }
</style>
