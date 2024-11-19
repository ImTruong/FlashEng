<script setup>
    import axios from 'axios';
    import { ref, watch, defineEmits, defineProps, computed, onMounted} from 'vue';
    import OverlayBackground from '../components/OverlayBackground.vue'
    import AddCardModal from '../components/AddCardModal.vue'
    import { useStore } from 'vuex';
    import ImageCard from './ImageCard.vue';
    const store = useStore();
    

    const emit = defineEmits(['close', 'save', 'update']);
    const props = defineProps(['isEditMode', 'existingSet', 'classId', 'className', 'inClass']);

    const visible = ref(true); 
    const setName = ref(props.isEditMode ? props.existingSet.name : '');
    const rows = ref(props.isEditMode ? props.existingSet.wordResponses: [{ id: '', word: '', ipa: '',audio: '', definition: '', example: '', image: '' }]);
    const selectedWords = ref([]); 
    const showSelectColumn = ref(false);
    const showOptions = ref(false)
    const selectedOption = ref(props.isEditMode ? props.existingSet.privacyStatus :(props.inClass ? 'CLASS' : 'PRIVATE'));
    // const selectedOption = ref(() => {
    // if (props.isEditMode) {
    //     return props.existingSet.privacyStatus;
    //     } else if (props.inClass) {
    //         return 'CLASS';
    //     } else {
    //         return 'PRIVATE';
    //     }
    // });
    const dropdownRef = ref(null)
    const isEditWord = ref(false)
    const showAddCardModal = ref(false);
        const classId = ref(
        (props.isEditMode && props.existingSet.privacyStatus === 'CLASS') || props.inClass
            ? props.classId 
            : ''
    );
    const isSearchVisible = ref(false);
    const searchTerm = ref('');
    const editWord = ref(null)

    const classSuggestions = ref([]);
    const myClasses = computed(() => store.getters.getClasses);
    // const searchClass = ref(props.isEditMode && props.existingSet.privacyStatus === 'CLASS' ? localStorage.getItem('className') : '');
    const searchClass = ref(
    props.isEditMode && props.existingSet.privacyStatus === 'CLASS'
        ? localStorage.getItem('className') 
        : (props.inClass 
            ? props.className 
            : ''
        )
    );
    const user =  JSON.parse(localStorage.getItem('user'));
    const showImg = ref(false);
    const image = ref("");

    onMounted(() => {
        Promise.all([
            store.dispatch('fetchClassData')
        ]).catch((error) => {
            console.error("Error fetching data:", error);
        });
        // console.log(props.existingSet);
        // ifconsole.log(props.existingSet.userDetailResponse);
    });


    const saveData  = async () => {
        showOptions.value = false;
        const token = localStorage.getItem('token');
        const payload = {
            setId: props.isEditMode ? props.existingSet.id : null,
            name: setName.value,
            description: "My set",
            privacyStatus: selectedOption.value,
            classId: classId.value || null // class_id có thể là null
        }
        try {
            const config = {
            headers: {
                Authorization: `Bearer ${token}` // Thêm token vào header
            }
            }
            if (props.isEditMode) {
                const response = await axios.put('/set', payload, { headers: config.headers });  // API cập nhật
                emit('update', response.data.data);
                if (response.data.message) {
                alert(response.data.message);
            }
                
            } else {
                const response = await axios.post('/set', payload, { headers: config.headers }); 
                emit('save', response.data.data); 
                if (response.data.message) {
                    alert(response.data.message);
                }
            }
            
        } catch (error) {
            alert("Sorry! You can't update this set!");
            selectedOption.value = props.existingSet.privacyStatus;
            // alert(`${error.message|| 'An error occurred'}`);
        }
    };

    const addNewWord = (newWord) => {
        if (rows.value[0].word === '') {
            rows.value[0] = newWord;
        } else {
            rows.value.push(newWord);
        }
    };
    
    const removeRow = async () => {
    if (selectedWords.value.length > 0) {
        console.log('Selected Words:', selectedWords.value);
        for (const wordId of selectedWords.value) {
            try {
                const token = localStorage.getItem('token');
                const config = {
                    headers: {
                        Authorization: `Bearer ${token}`
                    }
                };
                const word = rows.value.find(row => row.id === wordId);
                if (!word) {
                    alert('Not Found word with id: ' + wordId);                    
                    continue; // Skip nếu không tìm thấy từ
                }
                const response = await axios.delete(`/word/${wordId}`, config);
                rows.value = rows.value.filter(row => row.id !== wordId); // Xóa từ khỏi bảng
                if (response.data.message) {
                    alert(response.data.message);
                }
        } catch (error) {
            if (error.response) {
                alert(`${error.response.data.message || 'An error occurred'}`);
            } else {
                alert(`Network or Axios error: ${error.message}`);
            }
        }
        }
        selectedWords.value = []; // Reset selectedWords sau khi xóa
    }
    emit('update', rows.value)
};

    const closeForm = () => {
        emit('close');
        visible.value = false;
        window.location.reload();

    };

    const toggleSelectWord = (row) => {
        const index = selectedWords.value.indexOf(row.id); // Kiểm tra xem ID có trong selectedWords không
        if (index === -1) {
            selectedWords.value.push(row.id); // Thêm ID vào nếu chưa có
        } else {
            selectedWords.value.splice(index, 1); // Loại bỏ ID nếu đã có // Loại bỏ ID nếu đã có
        }
    };

    const toggleSelectColumn = () => {
        showOptions.value = false;
        showSelectColumn.value = !showSelectColumn.value;
    };

    const toggleOptions = () => {
        showOptions.value = !showOptions.value;
    };

    const selectOption = (option) => {
        selectedOption.value = option;
        // showOptions.value = false; 
    };
    const openAddCardModal = () => {
        showOptions.value = false;
        // chờ sửa be user respone có id khác null
        // if(props.isEditMode && props.existingSet.userDetailResponse.id != user.id){
        if(props.isEditMode && props.existingSet.userDetailResponse.username != user.username){
            alert("You aren't authorized to modify this set!");
            return;
        }
        // console.log
        if(!props.isEditMode && !setName.value){
            alert("Save set before add words.");
            return;
        }
        showAddCardModal.value = true;
        visible.value = false;
    };

    const closeAddCardModal = () => {
        isEditWord.value = false;
        visible.value = true;
        showAddCardModal.value = false;
    };
    
    const handleSaveData = () => {
        if (setName.value.trim()) {
            if (selectedOption.value === 'CLASS' && !classId) {
                console.log('Please enter classname');
                return; 
            }
            saveData();
        }else{
            alert("Please enter setname");
        }
    };
    const toggleSearch = () => {
        showOptions.value = false;
        isSearchVisible.value = !isSearchVisible.value;
    };
    const EditRow = (row) =>{
        isEditWord.value = true;
        editWord.value = row;
        openAddCardModal();
    }
    
    const filteredRows = computed(() => {
        if (!isSearchVisible.value || !searchTerm.value.trim()) {
            return rows.value;
        }
        return rows.value.filter(row => row.word.toLowerCase().includes(searchTerm.value.toLowerCase().trim()));
    });
    
    const updateWord = (updatedWord) => {
        const index = rows.value.findIndex(row => row.id === updatedWord.id); // Tìm chỉ mục của từ trong rows
        if (index !== -1) {
            rows.value[index] = updatedWord; // Cập nhật từ trong rows
        } else {
            console.error('Word not found in rows');
        }
        emit('update', rows.value); // Emit sự kiện 'update' để cập nhật lại mảng rows
    };

    watch(() => props.existingSet, (newExistingSet) => {
        console.log('New Existing Set:', newExistingSet); // Kiểm tra xem existingSet có giá trị đúng không
        if (newExistingSet && newExistingSet.words) {
            setName.value = newExistingSet.name; // Cập nhật tên set
            rows.value = newExistingSet.words; // Cập nhật các hàng
            selectedOption.value = newExistingSet.privacyStatus || '';
            classId.value = newExistingSet.privacyStatus === 'CLASS' ? props.classId : '';
        }
    }, { deep: true });
    
    watch(searchClass, () =>{
        classSuggestions.value = myClasses.value.filter(classItem => 
            classItem.className.toLowerCase().includes(searchClass.value.toLowerCase())
        );  
    })
    const selectClass = (classItem) => {
        searchClass.value = classItem.className;
        classId.value = classItem.classId;
        classSuggestions.value = [];
    };

    const openImage = (img) =>{
        showImg.value = true;
        image.value = img;
        visible.value = false;
    }

    const closeImage = () =>{
        showImg.value = false;
        visible.value = true;
    }
</script>

<template>
    <OverlayBackground :isVisible="visible" @clickOverlay="closeForm" />
    <div v-if="visible" class="set-window">
        <div class="set-header">
            <img src="../assets/lock.svg" alt="Status" @click.stop="toggleOptions" class="option-icon">
            <!-- <img src="../assets/search_icon.svg" alt="Status"> -->
            <img src="../assets/search_icon.svg" alt="Search" @click.stop="toggleSearch" class="option-icon">
            <div v-show="isSearchVisible">
                <input v-model="searchTerm" placeholder="Search for a word" class="common-input" />
            </div>
            <div class="set-name">
                <label for="set-name">Set:</label>
                <input id="set-name" v-model="setName" placeholder="Enter set name" class="common-input"/>
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
                <img src="../assets/lock.svg" alt="class" class="option-icon" />
                <span class="option-text">Class</span>
                <span v-if="selectedOption === 'CLASS'" class="checkmark">✔</span>
            </button>
            <!-- {{ classSuggestions }} -->
            <input
                v-if="selectedOption === 'CLASS'"
                v-model="searchClass"
                @input="fetchClassList"
                type="text"
                placeholder="Enter class name"
                class="class-input"
            />
            <ul v-if="classSuggestions.length > 0 && selectedOption === 'CLASS'" class="dropdown-list">
                <li
                    v-for="(classItem, index) in classSuggestions"
                    :key="index"
                    @click="selectClass(classItem)"
                    class="dropdown-item"
                >
                    {{ classItem.className }}
                </li>
            </ul>
        </div>
        
    </div>

    <div class="table-container">
        <!-- {{ myClasses }} -->
        <table class="set-table">
            <thead>
              <tr>
                <th v-if="showSelectColumn" class="select-column">Select</th>
                <th>Word</th>
                <th>IPA</th>
                <th>Definition</th>
                <th>Example</th>
                <th class="image">Image</th>
                <th class="edit">Edit</th>
              </tr>
            </thead>
            <tbody>
              <tr v-for="(row, index) in  filteredRows" :key="index">
                <td v-if="showSelectColumn">
                    <input type="checkbox" @change="toggleSelectWord(row)" :checked="selectedWords.includes(row.id)" />
                </td>
                
                <td ><p>{{ row.word }}</p></td>
                <td><p>{{ row.ipa }}</p></td>
                <td><p>{{ row.definition }}</p></td>
                <td><p>{{ row.example }}</p></td>
                <td class="image">
                    <img v-if="row.image" src="../assets/image.svg" alt="class" class="image-icon" @click="openImage(row.image)" />
                </td>
                <td>
                    <img v-if="row.word" src="../assets/edit-02.svg" alt="" @click="EditRow(row)" class="edit-icon">
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
        <button @click="openAddCardModal" class="icon-button">
            <img src="../assets/add-word.svg" alt="" class="icon">
        </button>
        <button @click="handleSaveData" class="icon-button">
            <img src="../assets/save.svg" alt="" class="icon">
        </button>
      </div>
    </div>
    <AddCardModal 
        :setName="setName"         
        :setId="props.existingSet.id" 
        :word="isEditWord ? editWord : null"
        v-if="showAddCardModal" 
        @update="updateWord"
        @close="closeAddCardModal" 
        @save="addNewWord">
    </AddCardModal>
    <ImageCard :Overlay_background ="showImg" :image="image" v-if="showImg" @close="closeImage"></ImageCard>
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
        margin: 4px;
        width: 100%;
        padding: 10px;
        border: 1px solid #ccc;
        border-radius: 4px;
    }

    .dropdown-list {
        position: absolute;
        top: 100%;
        left: 50%;
        width: 50%;
        /* margin-left: 50%; */
        max-height: 200px;
        overflow-y: auto;
        margin: 0;
        padding: 0;
        list-style-type: none;
        border: 1px solid #ccc;
        border-radius: 4px;
        background-color: #fff;
        z-index: 10;
        box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    }

    .dropdown-item {
        padding: 10px;
        cursor: pointer;
        transition: background-color 0.2s;
    }

    .dropdown-item:hover {
        background-color: #f0f0f0;
    }

    .common-input {
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
        min-height: 60px;
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

    .select-column {
        width: 50px; /* Chiều rộng cho cột Select */
    }

    .image{
        width: 50px !important; 
    }

    .image-icon:hover{
        transform: scale(1.05);
    }

    
    .edit {
        width: 50px; /* Chiều rộng cố định nhỏ gọn */
    }

    .edit-icon:hover{
        transform: scale(1.05);
    }
    
    /* Styling cho hình ảnh trong cột Edit */
    .set-table td img {
        width: 20px; /* Kích thước nhỏ hơn cho icon Edit */
        height: auto;
        cursor: pointer; /* Hiển thị con trỏ khi hover vào */
    }
    
    .set-table th:not(.select-column, .edit) {
        width: calc((100% - 20px) / 5); /* Chiều rộng cho 5 cột còn lại */
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

    .icon:hover{
        transform: scale(1.05);
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