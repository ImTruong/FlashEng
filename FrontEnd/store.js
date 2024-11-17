// store.js
import { createStore } from 'vuex';
import axios from 'axios';
import createPersistedState from "vuex-persistedstate";

const store = createStore({
  state: {
    sets: [],
    displayedSets: [],
    classes: [],
    currentSet: null
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
    },
    setCurrentSet(state, set) {
      state.currentSet = set;
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
        const response = await axios.get("/class/user", { 
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
  },
  plugins: [createPersistedState()],
});

export default store;
