<script setup>
  import { ref, defineEmits, defineProps } from 'vue';
  import OverlayBackground from '../components/OverlayBackground.vue';
  import axios from 'axios';

  const emit = defineEmits(['close', 'save']);
  const visible = ref(true);
  const props = defineProps({
    setName: {
      type: String,
      default: ''
    },
    setId: {
      type: Number,
      default: null
    }
  });

  const newWord = ref({
    word: '',
    ipa: '',
    audio: '',
    definition: '',
    example: '',
    image: null
  });

  // Lưu cache âm thanh cho các từ đã tìm
  const audioCache = ref({});

  const closeForm = () => {
    emit('close');
    visible.value = false;
  };

  const updateSetName = (event) => {
    emit('update:setName', event.target.value);
  };
  
  const handleImageUpload = (event) => {
    if (event.target.files && event.target.files[0]) {
      newWord.value.image = event.target.files[0];
    } else {
      console.log('No file selected');
    }
  };
  // Fetch âm thanh và lưu vào cache
  const fetchMeaning = async (word) => {
    if (audioCache.value[word]) {
      return audioCache.value[word]; // Trả lại kết quả từ cache nếu đã có
    }

    try {
      const response = await fetch(`/api/meaning/${word}`);
      if (!response.ok) {
        console.error('API error:', response.statusText);
        return null; // Return null if the API call fails
      }

      const data = await response.json();
      if (data && data[0] && data[0].phonetics) {
        const phonetic = data[0].phonetics.find(p => p.audio);
        const audioUrl = phonetic ? phonetic.audio : null;
        newWord.value.ipa = phonetic ? phonetic.text : null;

        // Lưu vào cache
        audioCache.value[word] = audioUrl;
        return audioUrl;
      }

      return null; // Return null if no audio is available
    } catch (error) {
      console.error('Error fetching meaning:', error);
      return null;
    }
  };

  // Hàm phát âm thanh
  const playAudio = () => {
    const audioElement = document.getElementById('audio');
    if (audioElement) {
      audioElement.play(); // Play the audio
    }
  };

  // Hàm xử lý khi ấn vào loa để phát âm
  const handlePlayAudio = async () => {
    const word = newWord.value.word.trim();
    if (word) {
      const phonetic = await fetchMeaning(word);
      if (phonetic) {
        newWord.value.audio = phonetic; // Cập nhật âm thanh vào đối tượng
        // Cập nhật nguồn và phát âm thanh
        const audioElement = document.getElementById('audio');
        audioElement.src = phonetic;
        audioElement.style.display = "block"; // Hiển thị audio
        audioElement.play(); // Phát âm thanh
      } else {
        // Ẩn loa nếu không có âm thanh
        const audioElement = document.getElementById('audio');
        audioElement.style.display = "none";
      }
    }
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
        alert('An error occurred while saving the word');
    }
  };

  const updateSetName = (event) => {
    emit('update:setName', event.target.value);
  };

</script>

<template>
  <OverlayBackground :isVisible="visible" @clickOverlay="closeForm" />
  <div class="modal-content" @click.stop>
    <div class="modal-header">
      <div class="set-name">
        <label for="set-name">Set:</label>
        <input id="set-name" :value="props.setName" @input="updateSetName" placeholder="Enter set name" />
      </div>
      <button class="close-btn" @click="closeForm">×</button>
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
        <label for="example">Example:</label>
        <input type="text" v-model="newWord.example" placeholder="Enter example" />
      </div>

      <div class="form-group">
        <label for="image">Image:</label>
        <input type="file" @change="handleImageUpload" ref="fileInput" style="display: none;" />
        <img src="../assets/add_img.svg" alt="Upload Icon" class="icon-upload" @click="$refs.fileInput.click()" />
      </div>

      <div class="form-group">
        <label for="audio">Audio:</label>
        <button class="audio-button" type="button" @click="handlePlayAudio">
          <img src="../assets/speaker-icon.svg" alt="Speaker Icon" />
        </button>
        <audio id="audio" style="display: none;" controls></audio> <!-- Âm thanh sẽ được phát khi bấm loa -->
      </div>

      <div class="modal-actions">
        <button type="submit" class="add-btn">Save</button>
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
    height: 500px;
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

  .audio-button{
    background-color: rgb(255, 255, 255);
    border: none;
    width: 50px;
    height: 50px;
  }


</style>

  