<script setup>
  import axios from 'axios';
  import { ref, defineEmits, defineProps, watch } from 'vue';
  import OverlayBackground from '../components/OverlayBackground.vue';

  const emit = defineEmits(['close', 'save']);
  const visible = ref(true);
  const props = defineProps({
    setName: {
      type: String,
      default: ""
    },
    setId: {
      type: Number,
      default: null
    }
  });

  const newWord = ref({
    word: '',
    ipa: '',
    audio: 'temporary',
    definition: '',
    example: '',
    image: null,
  });

  const handleImageUpload = (event) => {
    if (event.target.files && event.target.files[0]) {
      newWord.value.image = event.target.files[0];
    } else {
      console.log('No file selected');
    }
  };
  const closeForm = () => {
    emit('close');
    visible.value = false;
  };

  const saveData = async () => {
    const token = localStorage.getItem('token');
    const formData = new FormData(); // Tạo đối tượng FormData
    formData.append('setId', props.setId)
    formData.append('word', newWord.value.word)
    formData.append('ipa', newWord.value.ipa)
    formData.append('audio', newWord.value.audio)
    formData.append('definition', newWord.value.definition)
    formData.append('example', newWord.value.example)
    if (newWord.value.image) {
      formData.append('image', newWord.value.image)
    }
    try {
      const config = {
        headers: {
            Authorization: `Bearer ${token}` // Thêm token vào header
        }
      }
      const response = await axios.post('/word', formData, config);
      if (response.status === 201) { 
        emit('save', response.data.data);
        closeForm()
        console.log('Word saved successfully!');
      } else {
        console.log('Failed to save word');
      }

    } catch (error) {
        console.error('Error saving word:', error);
    }
  };

  const updateSetName = (event) => {
    emit('update:setName', event.target.value);
  };

</script>
<template>
    <OverlayBackground :isVisible="visible" @clickOverlay="closeForm"> </OverlayBackground>
    <div class="modal-content" @click.stop>
      <div class="modal-header">
        <div class="set-name">
          <label for="set-name">Set:</label>
          <input id="set-name" :value="props.setName" @input="updateSetName" placeholder="Enter set name" />
        </div>
        <button class="close-btn" @click="closeForm">×</button> <!-- Nút đóng góc phải -->
      </div>
      <form @submit.prevent="saveData">
        <div class="form-group">
          <label for="word">Word:</label>
          <input type="text" v-model="newWord.word" placeholder="Enter word" />
        </div>
        <div class="form-group">
          <label for="ipa">IPA:</label>
          <input type="text" v-model="newWord.ipa" placeholder="Enter IPA" />
        </div>
        <div class="form-group">
          <label for="definition">Definition:</label>
          <input type="text" v-model="newWord.definition" placeholder="Enter definition" />
        </div>
        <div class="form-group">
          <label for="example">Example:</label> <!-- Thêm trường example -->
          <input type="text" v-model="newWord.example" placeholder="Enter example" />
        </div>
        <div class="form-group">
          <label for="image">Image:</label>
          <input type="file" @change="handleImageUpload" ref="fileInput" style="display: none;" />
          <img src="../assets/add_img.svg" alt="Upload Icon" class="icon-upload" @click="$refs.fileInput.click()" />
        </div>
        <div class="modal-actions">
          <button type="submit"  class="add-btn" >Save</button>
          <button type="button" @click="closeForm" class="cancel-btn">Cancel</button>
        </div>
      </form>
    </div>
</template>

<style scoped>
  .modal-content {
    display: flex;
    flex-direction: column;
    align-items: center; 
    justify-content: flex-start; 
    background: white;
    padding: 0px;
    border-radius: 12px;
    width: 400px;
    height: 450px;
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
    z-index: 13;
    position: fixed; 
    top: 10%; 
    left: 50%; 
    transform: translateX(-50%); 
    overflow-y: auto; 
  }

  .modal-header {
    display: flex;
    justify-content: space-between;
    align-items: center;
    background-color: #e0f7fa; /* Màu xanh nhạt */
    width: 100%;
    padding: 10px;
    border-radius: 8px 8px 0 0;
    position: relative;
    margin: 0;
    margin-bottom: 8px;;
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
    width: 200px;
  }

  .close-btn {
    background: none;
    border: none;
    font-size: 30px;
    cursor: pointer;
    margin-left: auto;
    margin-top: 0;
  }
  
  .form-group {
    margin-bottom: 20px;
    display: flex;
    align-items: center;
  }

  .form-group label {
    width: 130px; /* Thiết lập chiều rộng cố định cho label */
    margin-right: 10px; /* Khoảng cách giữa label và input */
  }

  .modal-actions {
    display: flex;
    justify-content: space-between;
  }

  input[type="text"] {
    width: 100%; /* Đảm bảo input chiếm đầy chiều rộng */
    padding: 10px; /* Thêm padding cho input */
    border: 1px solid #ccc; /* Thêm border */
    border-radius: 4px; /* Làm bo tròn các góc input */
    box-sizing: border-box;
    
  }
  
  input[type="file"] {
    margin-top: 10px; /* Tạo khoảng cách giữa label và input file */
  }

  .add-btn {
    background-color: #a8def0; /* Màu xanh nhạt */
    color: black;
    border: none;
    padding: 10px 20px;
    border-radius: 4px;
    cursor: pointer;
    font-weight: bold;
    margin-left: 50px;
    width: 100px;
    flex-grow: 1;
  }
  
  .cancel-btn {
    background-color: #ffccd5;
    color: black;
    border: none;
    padding: 10px 20px;
    border-radius: 4px;
    cursor: pointer;
    font-weight: bold;
    margin-right: 20px;
    width: 100px;
  }

  .icon-upload {
    cursor: pointer;
    margin-left: 40px;
    width: 60px; /* Đặt kích thước cố định theo yêu cầu */
    height: 60px;
    vertical-align: middle;
  }
</style>

  