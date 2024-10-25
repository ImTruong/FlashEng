<script>
    import ClassTable from './ClassTable.vue';
    import { defineEmits, defineProps } from 'vue';
    // const {members} = defineProps(['members']);
    import {ref} from 'vue'

    const classTable = ref(true);
    const emit = defineEmits(['close', 'save']);

    
    const rows = ref([{ username: '', role: ''}]);

    const addRow = () => {
        rows.value.push({ username: '', role: ''});
    };

    const removeRow = () => {
        if (selectedUsers.value.length > 0) {
            rows.value = rows.value.filter(row => !selectedUsers.value.includes(row.username));
            selectedUsers.value = []; 
        } else if (rows.value.length > 1) {
            rows.value.pop();
        }
    };
</script>
<!-- <template>
    <ClassTable @close="classTable = false"></ClassTable>
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
</template>

<style scoped>
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
</style> -->