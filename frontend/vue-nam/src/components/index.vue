<template>
    
  <div class="hello">
    <!-- <h1>{{ msg }}</h1> -->

    <header>
      <h1 style="cursor:pointer;margin:0;">HeeJun</h1>

  
    </header>
    
    <!-- <input type="text" v-model="tester" v-on:keyup.enter="naverSearch" placeholder="네이버 사전 검색" autocomplete="off">
    <span v-on:click="naverSearch">클릭</span> -->
    <div style="margin-top:150px;">
      <h1>HEE JUN</h1>
      <h2>카카오 지도 api를 이용한 음식점 리뷰 사이트입니다.</h2>
      <div>
        <input type="text" v-model="kakaoMapSearch" @keyup.enter="kakaoMap" placeholder="지역 검색" autocomplete="off" style="display:inline;width:40%;padding:0 10px;height:60px;font-size:x-large;border-color: #d2f7ef;">
        <button @click="kakaoMap" style="box-shadow:none;height:60px;padding:0 10px;"><i class="fas fa-search fa-3x" style="vertical-align:middle;margin:0 0 5px 0;"></i></button>
        
      </div>
    
      <div >
          <div v-if="this.$store.state.isLogin">
              <button  type="button" @click="logOut">로그아웃</button>
              <button  @click="getUserProfile(),profileUpdateModal=true">유저 프로필 업데이트</button>
          </div>

          <div v-else>
            <button  @click="registerModalOpen()">회원가입 </button>
            <button  type="button" @click="loginModalOpen()">로그인</button>
          </div>
        
          
        </div>

    </div>
   

    
    <router-link to="/map3">지도</router-link><br>

    <!-- <li v-for="item in this.$store.state.items[0]" v-bind:key="item" >
      <p style="width:700px;margin:30px auto;">{{item.title}} <br>  <a style="max-width:700px;" :href="item.link">{{item.link}}</a> <br> {{item.description}}</p> 
     
    </li> -->
    
    <Modal v-if="registerModal || loginModal" @close="userClear()" >
      <div slot="header">
        <h3 class="modal-default-button"><i class="closeModalBtn fas fa-times fa-lg" @click="userClear()"></i></h3>
       </div>
        <div slot="body" class="loginForm">
          <input  id="userName" type="text" v-model="userName" name="userName" placeholder="id 입력!!!!" autocomplete="off" style="margin-top:50px;">
          
          <input  id="userPassword" type="text" v-model="userPassword" name="userPassword" placeholder="pw 입력!!!!" autocomplete="off">

          <input  v-if="!!registerModal" id="userPassword2" type="text" v-model="userPassword2" name="userPassword2" placeholder="pw 입력!!!!" autocomplete="off">
          <p v-show="!!registerModal && userPassword !== userPassword2">비번을 맞춰주십시오</p>
        </div>
        
        
        <div slot="footer" v-if="registerModal">
           
        <button :disabled="!userName || !userPassword || userPassword !== userPassword2" type="button" @click="register">회원 가입</button>
        </div>

        <div slot="footer" v-if="loginModal">
            
        <button :disabled="!userName || !userPassword"  type="button" @click="login">로그인</button>
        </div>

        
        <p slot="footer" style="margin-top:20px;"> {{failResult}}</p>
        
    </Modal>






    <Modal v-if="profileUpdateModal">
      <div slot="header">
        <h3 class="modal-default-button"><i class="closeModalBtn fas fa-times fa-lg" @click="removeFile(),profileUpdateModal=false,profileSubmit=false"></i></h3>
      </div>
      <div slot="body" style="margin-top:40px;">
        <input v-on:change="fileSelect()" ref="userProfile" type="file">
        <button @click="addFiles" style="margin-bottom:30px;">이미지 선택</button>
        <div v-if="profileImage" class="image-wrapper">
          
          <img :src="profileImage" style="width:300px;height:300px;border-radius:150px;">
          <div class="image-delete-box">
          <i class="closeModalBtn fas fa-times" @click="removeFile(),profileSubmit=true" style="margin:0 7px 0 0;"></i>
          </div>
      </div>
      </div>
      <div  slot="footer" style="margin-bottom:30px;"><button v-bind:disabled="!profileSubmit" @click="userProfileUpdate()">프로필 변경</button></div>
    </Modal>
    
    
  </div>
</template>

<script>

import Modal from './common/Modal.vue'
import router from '../router'



export default {
  name: 'HelloWorld',
  data () {
    return {
      msg: '포트폴리오',
      
      
     
      userName: '',
      userPassword: '',
      userPassword2:'',
      registerModal: false,
      loginModal: false,
      failResult: '',
      kakaoMapSearch:'',



      userProfile:'',
      profileUpdateModal:false,
      profileImage:'',
      profileSubmit:false
      
      
      
    }
  },
  created(){
      //this.getUserProfile();
  },
  methods:{
 
    
    naverSearch(){
      this.$store.commit('naverSearch',this.tester);
    }
    ,
   
    register(){
     
        var frm = new FormData();
        frm.set('userName',this.userName)
        frm.set('userPassword',this.userPassword)

     
        this.$axios({
        method: 'post',
        url: `/register`,
        data: frm
      })
      .then((response)=>{
       
        
        
        if(response.data == '중복된 아이디입니다.'){

          this.failResult = response.data;
          this.userName = ''
         
        } else {
          
          this.userClear();
          localStorage.setItem("token",response.data);
          this.$store.commit('setIsLogin',true);
          //this.$store.state.isLogin = true;
            
        }
        
        
      })
      .catch(function(error){
        console.log(error);
        
        
      });
    },
    registerModalOpen(){
      this.registerModal = true;
    },
    loginModalOpen(){
      this.loginModal = true;
    },
    
    login(){
     
        var frm = new FormData();
        frm.set('userName',this.userName)
        frm.set('userPassword',this.userPassword)
      
        this.$axios({
        method: 'post',
        url: `/login`,
        data: frm
      })
      .then((response)=>{
        
        
          if(response.data == '아이디 틀림' || response.data == '비번 틀림'){

             this.failResult = response.data;
         
          }
          else {
              this.userClear();
              console.log(response.data);

              localStorage.setItem("token",response.data);
              
              this.$store.commit('setIsLogin',true);
              //this.$store.state.isLogin = true;

            //router.push(`/hello/${this.userName}`);
            
          }
         
        
      })
      .catch(function(error){
          console.log(error);
            
      });
    }
    ,
    logOut(){
      localStorage.clear();
      this.$store.commit('setIsLogin',false);
      //this.$store.state.isLogin = false;
    }
    ,
    
    userClear(){
      this.userName = '';
      this.userPassword = '';
      this.userPassword2 = '';
      
      this.registerModal = false;
      this.loginModal = false;
      this.failResult = '';
    },
    routerTest(){
      router.push(`/board/1`);
    },
    kakaoMap(){
      this.$router.push(`map3/${this.kakaoMapSearch}`)
    },





    fileSelect(){
      this.profileSubmit = true;

      console.log(this.$refs)
      this.userProfile = this.$refs.userProfile.files[0];

      var reader = new FileReader();

      reader.onload = e =>{
        this.profileImage = e.target.result;
      }

      console.log(this.userProfile)

      reader.readAsDataURL(this.userProfile);
    },


    removeFile(){
      this.userProfile = ''
      this.profileImage = ''
    },



    userProfileUpdate(){

      var frm = new FormData();
      frm.set('token',localStorage.getItem('token'))


      if(this.userProfile){
        frm.set('profile',this.userProfile)
      }else if(this.profileImage){
        frm.set('profileName',this.profileImage)
      }
      this.$axios({
        method:'patch',
        url:'/user/profile',
        data: frm,
        headers:{
            'Content-Type': 'multipart/form-data',
             Authorization : `Bearer ${localStorage.getItem("token")}`
        }
        
      })
      .then(response=>{
        this.removeFile();
        this.profileUpdateModal = false;
        this.profileSubmit = false;
        console.log(response.data)
        if(response.data.Auth=='FAIL'){
          window.alert('로그인 해주십쇼')
          this.$store.commit('setIsLogin',false)
        }else{
          window.alert('프로필이 성공적으로 변경되었습니다.')
          //this.getUserProfile();
        }
      })
      .catch(err=>{
        console.log(err)
      })
    },



    getUserProfile(){
      var frm = new FormData();
      frm.set('token',localStorage.getItem('token'))
      this.$axios({
        method:'post',
        url: '/user/profileName',
        data: frm
      })
      .then(response=>{
        console.log(response)
        if(response.data != ""){
          this.profileImage = 'upload/' + response.data
        }
      })
      .catch(err=>{
        console.log(err)
      })
    },


    addFiles(){
      this.$refs.userProfile.click();
    }
  },
  components:{
    Modal
  }
 
  
}


</script>
<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>



@import url('../assets/main.css');

input[type="file"]{
    position: absolute;
    top: -500px;
  }

h1, h2 {
  font-weight: normal;
}
ul {
  list-style-type: none;
  padding: 0;
}
li {
  display: inline-block;
  margin: 0 10px;
}
a {
  color: #42b983;
}

button{
  margin:5px 5px;
}

header > h1{
  font-weight: bold;
  
}

div.loginForm > input{
  padding:0 0 0 5px;
  width:90%;
}

.modal-default-button {
  float: right;
  margin: 0;
}

i.fas{
  cursor: pointer;
}


.image-wrapper{
    position: relative;
    text-align:center;
}

.image-delete-box{
    position: absolute;
    right: 10px;
    top: 0;
}

</style>
