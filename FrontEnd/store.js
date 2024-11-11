// store.js
import { createStore } from 'vuex';
import axios from 'axios';

const store = createStore({
  state: {
    sets: [],
    displayedSets: []
  },
  mutations: {
    setSets(state, newSets) {
      state.sets = newSets;
    },
    setDisplayedSets(state, displayedSets) {
        state.displayedSets = displayedSets;
    }
  },
  actions: {
    async fetchLibrarySets({ commit }) {
      try {
        const token = localStorage.getItem("token");
        if (!token) {
          console.warn("Token không tồn tại");
          return;
        }
        const response = await axios.get("/set", {
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        commit('setSets', response.data.data);
      } catch (error) {
        console.error("Error fetching library sets:", error);
      }
    }
  },
  getters: {
    getSets: (state) => state.sets,
    getDisplayedSets: (state) => state.displayedSets
  }
});

export default store;
