import Vue from 'vue'
import Vuex from 'vuex'
import axios from 'axios';
import router from '../router'



Vue.prototype.$axios = axios;


Vue.use(Vuex);





export const store = new Vuex.Store({
  
    state:{
        isLogin: false,
        me:''
    },
    mutations:{
          setIsLogin(state, payload){
              state.isLogin = payload;
          },
          setMe(state,payload){
              state.me = payload;
          }
    },
    actions:{
        loadUser({commit}){
            return axios.get(`/user/${localStorage.getItem('token')}`)
            .then(res=>{
                commit('setMe',res.data)
            })
            .catch(err=>{
                console.error(err)
            })
        }
        
    }
        
    
});