<script setup>
  import { ref } from 'vue';
  import { useRoute } from 'vue-router';
  import Header from "../components/Header.vue"; 
  import cardData from "../data/sets.json";

  const route = useRoute();
  const selectedSet = ref(route.params.id);
  
  // Tìm set tương ứng từ danh sách
  const currentSet = ref(cardData.find(set => set.id == selectedSet.value)); 
  
  // Lấy ngẫu nhiên các thẻ từ và định nghĩa
  const getRandomCards = (cards, count) => {
    const shuffled = [...cards].sort(() => 0.5 - Math.random());
    return shuffled.slice(0, count);
  };

  // Lấy 5 thẻ ngẫu nhiên từ currentSet.words
  const selectedCards = ref(getRandomCards(currentSet.value.words, 5));
  const englishWords = ref(selectedCards.value.map(card => card.word));
  const vietnameseWords = ref(selectedCards.value.map(card => card.definition));

  // Sắp xếp từ và định nghĩa
  englishWords.value = [...englishWords.value].sort(() => 0.5 - Math.random());
  vietnameseWords.value = [...vietnameseWords.value].sort(() => 0.5 - Math.random());

  // Trạng thái chọn từ
  const selectedWord = ref(null);
  const selectedDefinition = ref(null);
  const matchedPairs = ref([]);

  // Logic xử lý chọn từ
  function selectWord(index) {
    if (selectedWord.value === index) {
      selectedWord.value = null;
    } else {
      selectedWord.value = index;
      checkMatch();
    }
  }

  function selectDefinition(index) {
    if (selectedDefinition.value === index) {
      selectedDefinition.value = null;
    } else {
      selectedDefinition.value = index;
      checkMatch();
    }
  }

  // Kiểm tra sự khớp giữa từ và định nghĩa
  function checkMatch() {
    if (selectedWord.value !== null && selectedDefinition.value !== null) {
      // Lấy từ và định nghĩa đã chọn
      const selectedEnglishWord = englishWords.value[selectedWord.value];
      const selectedVietnameseDefinition = vietnameseWords.value[selectedDefinition.value];

      // Tìm trong selectedCards để xem cặp từ và định nghĩa có khớp không
      const isMatch = selectedCards.value.some(
        card => card.word === selectedEnglishWord && card.definition === selectedVietnameseDefinition
      );
      
      if (isMatch) {
        matchedPairs.value.push({ wordIndex: selectedWord.value, defIndex: selectedDefinition.value });
        console.log(matchedPairs.value.length);
      }

      // Đặt lại trạng thái chọn từ sau mỗi lần kiểm tra (chỉ giữ màu nếu chọn đúng)
      selectedWord.value = null;
      selectedDefinition.value = null;
    }
  }

  function isMatched(wordIndex, defIndex) {
  if (wordIndex !== null) {
    return matchedPairs.value.some(pair => pair.wordIndex === wordIndex);
  }
  if (defIndex !== null) {
    return matchedPairs.value.some(pair => pair.defIndex === defIndex);
  }
  return false;
}
</script>

<template>
  <Header />
  <div class="matching-game">
    <h2>Matching</h2>
    <div class="matching-container">
      <div class="words-list">
        <button
          v-for="(word, index) in englishWords"
          :key="'eng-' + index"
          :class="{'matched': isMatched(index, selectedWord)}"
          @click="selectWord(index)"
        >
          {{ word }}
        </button>
      </div>
      <div class="words-list">
        <button
          v-for="(word, index) in vietnameseWords"
          :key="'vie-' + index"
          :class="{'matched': isMatched(selectedWord, index)}"
          @click="selectDefinition(index)"
        >
          {{ word }}
        </button>
      </div>
    </div>
  </div>
</template>

<style scoped>
  .matching-game {
    text-align: center;
    padding: 30px;
    margin: 20px;
  }

  .matching-container {
    padding: 30px;
    display: flex;
    justify-content: center;
    gap: 50px;
  }

  .words-list {
    display: flex;
    flex-direction: column;
    gap: 10px;
  }

  .words-list button {
    background-color: white;
    padding: 10px 20px;
    font-size: 16px;
    border: 1px solid #ccc;
    border-radius: 4px;
    cursor: pointer;
    transition: background-color 0.3s;
    box-shadow: 0 8px 16px rgba(0, 0, 0, 0.1);
  }

  .words-list button.matched {
    background-color: #a8def0; 
    cursor: default;
  }

  .words-list button.selected {
    background-color: #a8def0;
  }
</style>
