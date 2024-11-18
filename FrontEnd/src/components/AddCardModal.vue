<script setup>
  import { ref, defineEmits, defineProps, watch } from 'vue';
  import OverlayBackground from '../components/OverlayBackground.vue';
  import axios from 'axios';
  import ImageCard from './ImageCard.vue';

  const emit = defineEmits(['close', 'save', 'update']);
  const visible = ref(true);
  const definitions = ref([]);
  const isDropdownOpen= ref(false);
  const showImg = ref(false);
  const props = defineProps({
    setName: {
      type: String,
      default: ''
    },
    setId: {
      type: Number,
      default: null
    }, 
    word:{
      type: Object,
      default: null
    }
  });

  const newWord = ref({
    id: '',
    word: '',
    ipa: '',
    audio: '',
    definition: '',
    example: '',
    image: ''
  });

  const audioCache = ref({});

  const closeForm = () => {
    emit('close');
    visible.value = false;
  };
  
  const handleImageUpload = (event) => {
    if (event.target.files && event.target.files[0]) {
      newWord.value.image = event.target.files[0];
    } else {
      alert('No file selected');
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
      console.log(props.word)
      const data = await response.json();
      if (data && data[0] && data[0].phonetics) {
        const phonetic = data[0].phonetics.find(p => p.audio);
        const audioUrl = phonetic ? phonetic.audio : null;
        newWord.value.ipa = phonetic ? phonetic.text : null;
        definitions.value = data[0].meanings?.flatMap(meaning =>
          meaning.definitions?.map(def => def.definition) || []
        )
        console.log(definitions.value);
        audioCache.value[word] = audioUrl;
        return audioUrl;
      }

      return null; // Return null if no audio is available
    } catch (error) {
      console.error('Error fetching meaning:', error);
      return null;
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
    console.log(newWord.value)
    formData.append('setId', props.setId)
    if(newWord.value.id) formData.append('id', newWord.value.id)
    formData.append('word', newWord.value.word)
    formData.append('ipa', newWord.value.ipa)
    if(newWord.value.audio)formData.append('audio', newWord.value.audio)
    formData.append('definition', newWord.value.definition)
    formData.append('example', newWord.value.example)
    if(newWord.value.image)formData.append('image', newWord.value.image);
    console.log(formData);
    try {
      const config = {
        headers: {
            Authorization: `Bearer ${token}` // Thêm token vào header
        }
      }
      for (let [key, value] of formData.entries()) {
        console.log(`${key}:`, value);
      }
      console.log(newWord.value);
      console.log(newWord.value.image);
      if(props.word){
        const response = await axios.put('/word', formData, config);
        if(props.word.image && !newWord.value.image)
        newWord.value.image = props.word.image;
        emit('update', newWord.value);
        alert(response.data.message);
      }
      else {
        const response = await axios.post('/word', formData, config);    
        emit('save', response.data.data);
        alert(response.data.message);
      }
      closeForm()
    } catch (error) {
      console.log(error);
      if (error.response && error.response.data && error.response.data.message) {
          alert(error.response.data.message);
      } else {
          alert("An error occurred. Please try again.");
      }
    }
  };

  const updateSetName = (event) => {
    emit('update:setName', event.target.value);
  };

  const toggleDropdown = () =>{
      isDropdownOpen.value = !isDropdownOpen.value;
  };
  const selectDefinition = (definition) =>{
      newWord.value.definition = definition;
      isDropdownOpen.value = false;
  }

  const openImage = (img) => {
    if (img instanceof File) {
        const imageUrl = URL.createObjectURL(img);
        newWord.value.image = imageUrl;
    } else {
        newWord.value.image = img;
    }
    showImg.value = true;
    visible.value = false;
  };

    const closeImage = () =>{
        showImg.value = false;
        visible.value = true;
        console.log(visible.value);
    }
    watch(() => props.word, (newValue) => {
    if (newValue) {
      newWord.value = { ...newValue };
      newWord.value.image = null;
    }
  }, { immediate: true });

</script>

<template>
  <OverlayBackground :isVisible="visible" @clickOverlay="closeForm" />
  <div class="container" v-if="visible">
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
          <label for="audio">Audio:</label>
          <div class="audio-container">
            <button class="audio-button" type="button" @click="handlePlayAudio">
              <img class="audio-icon" src="../assets/speaker-icon.svg" alt="Speaker Icon" />
            </button>
            <audio id="audio" style="display: none;" controls></audio> <!-- Âm thanh sẽ được phát khi bấm loa -->
          </div>
        </div>
  
        <div class="form-group">
          <label for="ipa">IPA:</label>
          <input type="text" v-model="newWord.ipa" placeholder="Auto fill" />
        </div>
  
        <!-- <div class="form-group">
          <label for="definition">Definition:</label>
          <input class="" type="text" v-model="newWord.definition" placeholder="Enter or select definition" />
          <select v-model="newWord.definition" class="select-definition">
            <option value="" disabled selected>Choose a definition</option>
            <option v-for="(definition, index) in definitions" :key="index" :value="definition">
              {{ definition }}
            </option>
          </select>      
        </div> -->
  
        <div class="form-group">
          <label for="definition">Definition:</label>
          <input class="definition-input" type="text" v-model="newWord.definition" placeholder="Enter or select definition" />
          
          <div class="custom-select">
            <!-- <div class="selected-option" @click="toggleDropdown">
                {{ newWord.definition }}
            </div> -->
            <img class="dropdown-icon" src="../assets/dropdown.svg" alt="Dropdown Icon"  @click="toggleDropdown"/>
            <ul v-show="isDropdownOpen" class="options">
                <li 
                    v-for="(definition, index) in definitions" 
                    :key="index" 
                    class="option" 
                    @click="selectDefinition(definition)">
                    {{ definition }}
                </li>
            </ul>
          </div>
      </div>
  
        <div class="form-group">
          <label for="example">Example:</label>
          <input type="text" v-model="newWord.example" placeholder="Enter example" />
        </div>
  
        <div class="form-group">
          <label for="image">Image:</label>
          <div class="img-container">
            <input type="file" @change="handleImageUpload" ref="fileInput" style="display: none;" />
            <img src="../assets/add_img.svg" alt="Upload Icon" class="icon-upload" @click="$refs.fileInput.click()" />
            <p v-if="newWord.image" class="new-image" @click="openImage(newWord.image)">Image</p>
          </div>
        </div>
  
        <div class="modal-actions">
          <button type="submit" class="add-btn">Save</button>
          <button type="button" @click="closeForm" class="cancel-btn">Cancel</button>
        </div>
      </form>
    </div>

  </div>
  <ImageCard :Overlay_background ="showImg" :image="newWord.image" v-if="showImg" @close="closeImage"></ImageCard>
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

  /* .select-definition{
    width: 15px;
    height: 25px;
    border: none;
    position: fixed;
    top: 47%;
    left: 86%;
  } */

  /* .select-definition option{
    width: 100px;
    padding: 10px;
  } */

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

  .audio-container {
    display: flex;
    align-items: center; /* Căn giữa theo chiều dọc */
    /* gap: 10px;           Khoảng cách giữa label và icon */
    width: 100%;         /* Đảm bảo phần tử chiếm hết chiều rộng */
    justify-content: center; /* Căn trái mặc định */
  }

  .audio-button{
    background-color: rgb(255, 255, 255);
    border: none;
    width: 30px;
  }

  audio {
    width: 40%; 
    margin-top: 10px; 
    height: 20px;
    margin-left: 10px;
  }

  .audio-icon{
    width: 20px;
    cursor: pointer;
  }

  .audio-icon:hover{
    transform: scale(1.05);
  }

  .definition-input{
    padding-right: 20px !important;
  }

  .custom-select {
    /* width: 200px; */
    position: relative;
    border: 1px solid #ccc;
    background-color: white;
  }

  .selected-option {
      padding: 10px;
      cursor: pointer;
      background-color: #f1f1f1;
      border-bottom: 1px solid #ccc;
  }

  .dropdown-icon{
    border: none;
    position: absolute;
    top: -12px;
    left: -25px;
  }

  .options {
      /* display: none; */
      position: absolute;
      top: 23px;
      left: -280px;
      width: 300px;
      max-height: 150px;
      overflow-y: auto;
      background-color: rgb(255, 255, 255);
      border: 1px solid #ffffff;
      list-style-type: none;
  }


  .option {
      display: flex;
      padding: 5px;
      cursor: pointer;
      width: 100%;
      white-space: normal; /* Cho phép nội dung xuống dòng */
      word-wrap: break-word; /* Cho phép từ dài xuống dòng */
      
  }

  .option:hover {
      background-color: #ddd;
  }

  .img-container{
    margin-right: 25px;
    display: flex;
    align-items: center; /* Căn giữa theo chiều dọc */
    /* gap: 10px;           Khoảng cách giữa label và icon */
    width: 100%;         /* Đảm bảo phần tử chiếm hết chiều rộng */
    justify-content: center; /* Căn trái mặc định */
  }

  .new-image:hover{
    color: blue;
    font-weight: 500;
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

  