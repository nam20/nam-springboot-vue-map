import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios';
import router from '../router'



Vue.prototype.$axios = axios;


Vue.use(Vuex);





export const store = new Vuex.Store({
  
    state:{
        host: 'http://localhost:18080',
       
        items: [],

        isLogin: false
    },
    mutations:{
        naverSearch(state,tester){        
            axios.get(`${state.host}/naverSearch/${tester}`)
            .then((response) => {
               state.items = []; 
               state.items.push(response.data.items);      
               console.log(state.items);
            })
            .catch((error) => {
              console.log(error);
            });
            tester = ''; 
          },
          setIsLogin(state, payload){
              state.isLogin = payload;
          }
    },
    actions:{
        
    }
        
    
});