import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios';
import router from '../router'



Vue.prototype.$axios = axios;


Vue.use(Vuex);





export const store = new Vuex.Store({
  
    state:{
        isLogin: false
    },
    mutations:{
          setIsLogin(state, payload){
              state.isLogin = payload;
          }
    },
    actions:{
        
    }
        
    
});