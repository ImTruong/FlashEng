// store.js
import { createStore } from 'vuex';
import axios from 'axios';

const store = createStore({
  state: {
    sets: [],
    displayedSets: [],
    classes: []
  },
  mutations: {
    setSets(state, newSets) {
      state.sets = newSets;
    },
    setDisplayedSets(state, displayedSets) {
      state.displayedSets = displayedSets;
    },
    setClassData(state, classes){
      state.classes = classes;
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
    ,
    async fetchClassData({ commit }) { 
      try {
        const token = localStorage.getItem("token");
        if (!token) {
          console.warn("Token không tồn tại");
          return;
        }
        const response = await axios.get("/class", { 
          headers: {
            Authorization: `Bearer ${token}`
          }
        });
        commit('setClassData', response.data.data); 
      } catch (error) {
          console.error("Error fetching class data:", error);
        }
     }
  },
  getters: {
    getSets: (state) => state.sets,
    getDisplayedSets: (state) => state.displayedSets,
    getClasses: (state) => state.classes 
  }
});

export default store;
