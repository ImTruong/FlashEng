<script setup>
    import { ref, watch, defineEmits, defineProps, onMounted} from 'vue';
    import setsData from '../data/sets.json'; 
    import OverlayBackground from '../components/OverlayBackground.vue'

    const emit = defineEmits(['close', 'save', 'update']);

    const props = defineProps(['isEditMode', 'existingSet']);

    const visible = ref(true); 
    const setName = ref(props.isEditMode ? props.existingSet.name : '');
    const rows = ref(props.isEditMode ? props.existingSet.words: [{ word: '', ipa: '', definition: '', example: '', image: '' }]);
    const selectedWords = ref([]); 
    const showSelectColumn = ref(false);
    const showOptions = ref(false)
    const selectedOption = ref('')
    const dropdownRef = ref(null)

    const saveData = () => {
        const payload = {
            id: props.isEditMode ? props.existingSet.id : null, // Lấy ID từ existingSet nếu đang ở chế độ chỉnh sửa
            setName: setName.value,
            rows: rows.value,
            selectedWords: selectedWords.value
        };
        if (props.isEditMode) {
            emit('update', payload); // Gửi sự kiện update nếu ở chế độ chỉnh sửa
        } else {
            emit('save', payload); // Gửi sự kiện save nếu không phải ở chế độ chỉnh sửa
        }
    };

    const addRow = () => {
        rows.value.push({ word: '', ipa: '', definition: '', example: '', image: '' });
        saveData();
    };

    const removeRow = () => {
        if (selectedWords.value.length > 0) {
            rows.value = rows.value.filter(row => !selectedWords.value.includes(row.word));
            selectedWords.value = []; 
        } else if (rows.value.length > 1) {
            rows.value.pop();
        }
    };

    const closeForm = () => {
        emit('close');
        visible.value = false;
    };

    const toggleSelectWord = (word) => {
        const index = selectedWords.value.indexOf(word);
        if (index === -1) {
            selectedWords.value.push(word);
        } else {
            selectedWords.value.splice(index, 1);
        }
    };

    const toggleSelectColumn = () => {
        showSelectColumn.value = !showSelectColumn.value;
    };

    const toggleOptions = (option) => {
        showOptions.value = option; 
        showOptions = false;
    };

    const selectOption = (option) => {
        selectedOption.value = option;
        showOptions.value = false; 
    };

    watch(setName, saveData);
    watch(rows, (newRows) => {
        saveData(); 
    }, { deep: true });
    watch(() => props.existingSet, (newExistingSet) => {
        if (newExistingSet && newExistingSet.words) {
            setName.value = newExistingSet.name; // Cập nhật tên set
            rows.value = newExistingSet.words; // Cập nhật các hàng
        }
    }, { deep: true });

    console.log('Existing Set:', props.existingSet);
    console.log('Rows:', rows.value);

</script>

<template>
    <OverlayBackground :isVisible="visible" @clickOverlay="closeForm" />
    <div v-if="visible" class="set-window">
        <div class="set-header">
            <img src="../assets/lock.svg" alt="Status" @click.stop="toggleOptions" class="option-icon">
            <img src="../assets/search_icon.svg" alt="Status">
            <div class="set-name">
            <label for="set-name">Set:</label>
            <input id="set-name" v-model="setName" placeholder="Enter set name" />
            </div>
            <button @click="closeForm" class="close-btn">✖</button>
        </div>
    <div v-show="showOptions" class="options-dropdown" ref="dropdownRef">
        <button @click.stop="selectOption('Public')" class="option-button">
            <img src="../assets/globe.svg" alt="Public" class="option-icon" />
            <span class="option-text">Public</span>
            <span v-if="selectedOption === 'Public'" class="checkmark">✔</span>
        </button>
        <button @click.stop="selectOption('Private')" class="option-button">
            <img src="../assets/lock.svg" alt="Private" class="option-icon" />
            <span class="option-text">Private</span>
            <span v-if="selectedOption === 'Private'" class="checkmark">✔</span>
        </button>
        <button @click.stop="selectOption('Group')" class="option-button">
            <img src="../assets/lock.svg" alt="Group" class="option-icon" />
            <span class="option-text">Group</span>
            <span v-if="selectedOption === 'Group'" class="checkmark">✔</span>
        </button>
    </div>

    <div class="table-container">
        <table class="set-table">
            <thead>
              <tr>
                <th v-if="showSelectColumn" class="select-column">Select</th>
                <th>Word</th>
                <th>IPA</th>
                <th>Definition</th>
                <th>Example</th>
                <th>Image</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(row, index) in rows" :key="index">
                <td v-if="showSelectColumn">
                    <input type="checkbox" @change="toggleSelectWord(row.word)" :checked="selectedWords.includes(row.word)" />
                </td>
                <td><input v-model="row.word" placeholder="Word" /></td>
                <td><input v-model="row.ipa" placeholder="IPA" /></td>
                <td><input v-model="row.definition" placeholder="Definition" /></td>
                <td><input v-model="row.example" placeholder="Example" /></td>
                <td><input v-model="row.image" placeholder="Image URL" /></td>
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
        <button @click="addRow" class="icon-button">
            <img src="../assets/add-word.svg" alt="" class="icon">
        </button>
      </div>
    </div>
</template>  
  
<style scoped>
    .set-window {
        position: fixed;
        top: 100px;
        left: 50%;
        transform: translateX(-50%);
        background-color: white;
        border-radius: 8px;
        box-shadow: 0 4px 10px rgba(0, 0, 0, 0.2);
        padding: 20px;
        width: 60%;
        z-index: 1000;
    }
  
    .set-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
    }
  
    .set-header img{
        margin-left: 10px;
    }

    .set-name {
        display: flex;
        align-items: center;
        justify-content: center;
        flex-grow: 1;
    }

    .set-name input {
        margin-left: 10px;
        padding: 5px;
        border: 1px solid black;
        border-radius: 4px;
        text-align: center; 
        width: 300px;
    }
  
    .table-container {
        max-height: 300px; /* Chiều cao tối đa cho bảng */
        overflow-y: auto; /* Kích hoạt thanh cuộn dọc */
        margin-top: 20px; /* Khoảng cách giữa tiêu đề và bảng */
        flex-grow: 1; /* Cho phép phần này chiếm không gian còn lại */
        position: relative;
    }
    .set-table {
        width: 100%;
        margin-top: 20px;
        border-collapse: collapse; /* Bỏ khoảng cách giữa các cột */
        table-layout: fixed;
    }

    .set-table th {
        background-color: #A8D5E5; 
        border: 1px solid black;
    }

    .set-table td {
        padding: 5px;
        border: 1px solid #ccc;
        text-align: center; 
    }
  
    .set-table input {
        width: 100%;
        padding: 5px;
        border: 1px solid #ccc;
        border-radius: 4px;
    }

    .select-column {
        width: 50px; /* Chiều rộng cho cột Select */
    }
    
    /* Các cột còn lại có chiều rộng bằng nhau */
    .set-table th:not(.select-column) {
        width: calc((100% - 10px) / 5); /* Chiều rộng cho 5 cột còn lại */
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
Khi tạo set thẻ thì sẽ tự động lưu trong database, các thao tác với set thẻ sẽ thao tác trực tiếp với dữ liệu trong database
Thiếu phần tìm kiếm
 -->