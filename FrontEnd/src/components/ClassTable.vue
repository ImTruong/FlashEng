<script setup>
    import { ref, watch, defineEmits, defineProps} from 'vue';
    import OverlayBackground from '../components/OverlayBackground.vue'
    // import { debounce } from 'lodash'; // Nếu bạn sử dụng lodash
    import AddMember from './AddMember.vue';

    const emit = defineEmits(['close', 'save']);

    const props = defineProps(['isEditMode', 'existingClass']);

    const visible = ref(true); 
    const className = ref('');
    const rows = ref([{ username: '', role: ''}]);
    const selectedUsers = ref([]); // Danh sách các từ được chọn
    const showSelectColumn = ref(false);
    const selectedOption = ref('')
    const dropdownRef = ref(null)
    const search = ref("")
    const showSearch = ref(false);
    const membersData = ref([])
    const showAddMember = ref(false);

    
    // Hàm lưu vào database
    // const saveToDatabase = async () => {
    //     console.log('Saving to database...');
    //     console.log('Class Name:', className.value);
    //     console.log('Rows:', rows.value);
    //     console.log('Selected User:', selectedUsers.value); // In ra các từ được chọn
    //     // Logic để lưu vào database
    // };

    // // Hàm lưu vào database với debounce
    // const saveToDatabaseDebounced = debounce(saveToDatabase, 300); // Gọi hàm mỗi 300ms sau khi dừng lại

    // // Theo dõi thay đổi của className và rows
    // watch(className, saveToDatabaseDebounced);
    // watch(rows, (newRows) => {
    //     saveToDatabaseDebounced(); // Gọi hàm lưu dữ liệu
    // }, { deep: true });

    const addMember = (newMember) => {
        if(row.value[0].username === ''){
            row.value[0] = newMember;
        }
        else{
            rows.value.push(newMember);
        }
    };

    const removeRow = () => {
        if (selectedUsers.value.length > 0) {
            rows.value = rows.value.filter(row => !selectedUsers.value.includes(row.username));
            selectedUsers.value = []; 
        } else if (rows.value.length > 1) {
            rows.value.pop();
        }
    };

    const closeForm = () => {
        emit('close');
        visible.value = false;
    };

    const toggleSelectMember = (username) => {
        const index = selectedUsers.value.indexOf(username);
        if (index === -1) {
            selectedUsers.value.push(username);
        } else {
            selectedUsers.value.splice(index, 1);
        }
    };

    const toggleSelectColumn = () => {
        showSelectColumn.value = !showSelectColumn.value;
    };



    const saveData = () => {
        emit('save', { className: className.value, rows: rows.value, selectedUsers: selectedUsers.value });
    };

    const selectOption = (option) => {
        selectedOption.value = option;
        showOptions.value = false; // Ẩn dropdown khi đã chọn
    };

    watch(search, () =>{
        if(showSearch){
            membersData.value = rows.filter(member => member.username.toLowerCase().includes(search.value.toLowerCase()))
        }
        else{
            membersData.value = rows.value;
        }
    })

    const openAddMember = () => {
        showAddMember.value = true;
        visible.value = false;
    };

    const closeAddMember = () => {
        showAddMember.value = false;
        visible.value = true
    };

</script>

<template>
    <OverlayBackground :isVisible="visible" @clickOverlay="closeForm" />
    <div v-if="visible" class="class-window">
        <div class="class-header">
            <img src="../assets/search_icon.svg" alt="Status" @click="showSearch=!showSearch">
            <input v-model.trim = "search" v-if="showSearch" type="text" placeholder="Search for username" class="search-bar">
            <div class="class-name" v-if="!showSearch">
                <label for="class-name">Class:</label>
                <input id="class-name" v-model="className" placeholder="Enter class name" />
            </div>
            <button @click="closeForm" class="close-btn">✖</button>
        </div>


    <div class="table-container">
        <table class="class-table">
            <thead>
              <tr>
                <th v-if="showSelectColumn" class="select-column">Select</th>
                <th class="username-column">Username</th>
                <th>Role</th>
              </tr>
            </thead>
            <tbody>
                <!-- <tr >
                    <th v-if="showSelectColumn">
                    </th>
                    <th class="username-column">ptitstudent1</th>
                    <th class="role">Admin</th>
              </tr> -->
              <tr v-for="(row, index) in rows" :key="index">
                <td v-if="showSelectColumn">
                    <input type="checkbox" @change="toggleSelectMember(row.username)" :checked="selectedUsers.includes(row.username)" />
                </td>
                <td class="username-column"><input v-model="row.username" placeholder="Username" /></td>
                <td class="role"><p>Member</p></td>
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
        max-height: 300px; /* Chiều cao tối đa cho bảng */
        overflow-y: auto; /* Kích hoạt thanh cuộn dọc */
        margin-top: 20px; /* Khoảng cách giữa tiêu đề và bảng */
        flex-grow: 1; /* Cho phép phần này chiếm không gian còn lại */
        position: relative;
    }
    .class-table {
        width: 100%;
        margin-top: 20px;
        border-collapse: collapse; /* Bỏ khoảng cách giữa các cột */
        table-layout: fixed;
    }

    .class-table thead th {
        background-color: #A8D5E5; 
        border: 1px solid black;
    }
    .class-table th {
        padding: 5px;
        border: 1px solid #ccc;
        /* text-align: center;  */
    }


    .class-table td {
        padding: 5px;
        border: 1px solid #ccc;
        text-align: center; 
    }
    
  
    .class-table input {
        width: 100%;
        padding: 5px;
        border: 1px solid #ccc;
        border-radius: 4px;
    }

    .select-column {
        width: 50px; /* Chiều rộng cho cột Select */
    }
    
    /* Các cột còn lại có chiều rộng bằng nhau */
    /* .class-table th:not(.select-column) { */
        /* width: calc((100% - 10px) / ); Chiều rộng cho 5 cột còn lại */
    /* }   */
    .username-column{
        width: 70%;
    }
  
    .actions {
        display: flex;
        justify-content: space-around; /* Căn giữa các icon */
        margin: 5px;
        margin-bottom: 0px;
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
<!-- 
Khi tạo class thẻ thì sẽ tự động lưu trong database, các thao tác với set thẻ sẽ thao tác trực tiếp với dữ liệu trong database
Thiếu phần tìm kiếm
 -->