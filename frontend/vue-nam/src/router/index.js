import Vue from 'vue'
import Router from 'vue-router'
import index from '@/components/index'


import kakaomap from '@/components/kakaomap'
import kakaomap2 from '@/components/kakaomap2'
import kakaomap3 from '@/components/kakaomap3'
import axios from 'axios';
import {store} from '../store/store'



Vue.use(Router)



const router = new Router({
 
//   mode: 'history'
//  ,
  routes: [
    {
      path: '/',
      name: 'index',
      component: index
    },
   
    {
      path: '/board/:placeId/:placeName',
      component: () => import('@/components/placeBoard.vue'),
      props: true
    },
    {
      path: '/map',
      component: kakaomap,
      props:true
    },
    {
      path: '/map2',
      component: kakaomap2
     
    }
    ,
    {
      path: '/map3',
      component: kakaomap3
      // meta: {authRequired:true}
     
    },
    {
      path: '/map3/:search',
      component: kakaomap3,
      props:true
    }
    ,
    {
      path: '/boardWrite/:placeId/:placeName',
      component: () => import('@/components/boardWrite.vue'),
      props: true,
      meta: {authRequired:true}
    },
    {
      path: '/allBoard',
      component: () => import('@/components/allBoard.vue')
    },
    {
      path: '/kakaomap4',
      component: () => import('@/components/kakaomap4')
    },
    {
      path: '/boardReview/:boardId',
      component: () => import('@/components/boardReview'),
      props: true
      
    },
    {
      path: '/boardUpdate/:boardId',
      component: () => import('@/components/boardUpdate'),
      props: true
    }
  ]
})



router.beforeEach((to,from,next) => {

  axios.defaults.headers.common['Authorization'] = `Bearer ${localStorage.getItem("token")}`;


  if(localStorage.getItem('token')){  //토큰 여부
      axios({
        method: 'post',
        url: '/auth'
      })
      .then(response=>{

        if(to.matched.some((routeInfo) => {
          return routeInfo.meta.authRequired;   //인증 필요 여부
        })){

              if(response.data.Auth !== 'FAIL') {   //인증 성공 여부

                store.commit('setIsLogin',true);
                next();

              } else {   //토큰 변조, 만료

                window.alert('로그인이 필요합니다.'); 
                store.commit('setIsLogin',false);  
                localStorage.clear();
                next('/');
              }

        }else{
              if(response.data.Auth !== 'FAIL') {  //인증 성공 여부 axios 
                store.commit('setIsLogin',true);        
              }
              else {  //토큰 변조, 만료
                store.commit('setIsLogin',false);  
                localStorage.clear();
              }
              next();
        }   

      })
      .catch(err=>{
        console.log(err);
      })

  }else{
      if(to.matched.some((routeInfo) => { 
        return routeInfo.meta.authRequired; //인증 필요 여부
      })){
        window.alert('로그인이 필요합니다.');
        
        next('/');

      } else {

        store.commit('setIsLogin',false);  
        next();
      }

  }
  
})

export default  router