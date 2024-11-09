<script setup>
    import axios from 'axios';
    import { ref, watch, defineEmits, defineProps} from 'vue';
    import OverlayBackground from '../components/OverlayBackground.vue'
    import AddCardModal from '../components/AddCardModal.vue'
    // import axios from "@/axios"

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
    const showAddCardModal = ref(false);
    const classId = ref('')

    const saveData  = async () => {
        const token = localStorage.getItem('token');
        const payload = {
            name: setName.value,
            description: "Created set", // description có thể là null
            privacyStatus: selectedOption.value,
            classId: null, // class_id có thể là null
        }
        // if (selectedOption.value === 'CLASS' && classId.value.trim()) {
        //     payload.class_id = classId.value.trim();
        // }
        try {
            const config = {
            headers: {
                Authorization: `Bearer ${token}` // Thêm token vào header
            }
            }
            if (props.isEditMode) {
                // const response = await axios.put(`/sets/${props.existingSet.id}`, payload, { headers: config.headers });  // API cập nhật
                // console.log('Set updated:', response.data);
                // emit('update', response.data); 
            } else {
                console.log(payload.classId)
                const response = await axios.post('/set', payload, { headers: config.headers }); 
                console.log('Set created:', response.data.data);
                console.log(response.data.data)
                emit('save', response.data.data); // Gửi sự kiện save với dữ liệu từ API
            }
            
        } catch (error) {
            if (error.response) {
                console.error('API Error:', error.response.status, error.response.data);
            } else {
                console.error('Network or Axios error:', error.message);
            }
        }
    };

    const addNewWord = (newWord) => {
        if (rows.value[0].word === '') {
        // Nếu dòng đầu tiênc có giá trị null hoặc rỗng, thay thế nó
            rows.value[0] = newWord;
        } else {
            // Nếu không, thêm từ mới vào cuối mảng
            rows.value.push(newWord);
        }
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

    const toggleOptions = () => {
    showOptions.value = !showOptions.value;
};

    const selectOption = (option) => {
        selectedOption.value = option;
        showOptions.value = false; 
    };
    const openAddCardModal = () => {
        showAddCardModal.value = true;
        visible.value = false;
    };

    const closeAddCardModal = () => {
        showAddCardModal.value = false;
        visible.value = true
    };
    const handleSaveData = () => {
        if (setName.value.trim()) {
            if (selectedOption.value.trim()) {
                if (selectedOption.value === 'CLASS' && !classId.value.trim()) {
                    console.warn('Vui lòng nhập ID lớp khi chọn Group.');
                    return; 
                }
                saveData();
        } else {
            console.warn('Vui lòng chọn Privacy Status.');
            }
        }
    };

    // watch(rows, (newRows) => {
    //     saveData(); 
    // }, { deep: true });
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
        <button @click.stop="selectOption('PUBLIC')" class="option-button">
            <img src="../assets/globe.svg" alt="Public" class="option-icon" />
            <span class="option-text">Public</span>
            <span v-if="selectedOption === 'PUBLIC'" class="checkmark">✔</span>
        </button>
        <button @click.stop="selectOption('PRIVATE')" class="option-button">
            <img src="../assets/lock.svg" alt="Private" class="option-icon" />
            <span class="option-text">Private</span>
            <span v-if="selectedOption === 'PRIVATE'" class="checkmark">✔</span>
        </button>
        <div class="option-container">
            <button @click.stop="selectOption('CLASS')" class="option-button">
                <img src="../assets/lock.svg" alt="Group" class="option-icon" />
                <span class="option-text">Group</span>
                <span v-if="selectedOption === 'CLASS'" class="checkmark">✔</span>
            </button>
            <input
                v-if="selectedOption === 'CLASS'"
                v-model="classId"
                type="text"
                placeholder="Enter class ID"
                class="class-input"
            />
        </div>
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
        <button @click="openAddCardModal" class="icon-button">
            <img src="../assets/add-word.svg" alt="" class="icon">
        </button>
        <button @click="handleSaveData" class="icon-button">
            <img src="../assets/add-word.svg" alt="" class="icon">
        </button>
      </div>
    </div>
    <AddCardModal :setName="setName" v-if="showAddCardModal" @close="closeAddCardModal" @save="addNewWord"></AddCardModal>
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
        z-index: 11;
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
        width: 100%;
    }

    .option-container {
        display: flex;
        align-items: center;
    }
    
    .option-button {
        display: flex;
        align-items: center;
        margin-right: 8px; /* Khoảng cách giữa button và input */
    }
    
    .class-input {
        padding: 4px;
        font-size: 14px;
        width: 100px;
        margin-right: 5px;
    }    

    .set-name input {
        margin-left: 10px;
        padding: 5px;
        border: 1px solid black;
        border-radius: 4px;
        text-align: center; 
        width: 50%;
        min-width: 150px; 
        max-width: 300px;
    }
  
    .table-container {
        max-height: 300px; 
        overflow-y: auto; 
        margin-top: 20px; 
        flex-grow: 1; 
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