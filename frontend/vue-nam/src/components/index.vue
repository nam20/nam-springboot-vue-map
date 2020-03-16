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
    
      <div>
          <div v-if="this.$store.state.isLogin">
              <button  @click="logOut()">로그아웃</button>
              <button  @click="loadProfileImage(),profileUpdateModal=true">유저 프로필 업데이트</button>
          </div>

          <div v-else>
            <button  @click="registerModal = true">회원가입 </button>
            <button  @click="loginModal = true">로그인</button>
          </div>
          <button @click="$router.push('/allBoard')">모든 리뷰 보러가기</button>
        
          
        </div>

    </div>
   

    
    <router-link to="/map">지도</router-link><br>

    <!-- <li v-for="item in this.$store.state.items[0]" v-bind:key="item" >
      <p style="width:700px;margin:30px auto;">{{item.title}} <br>  <a style="max-width:700px;" :href="item.link">{{item.link}}</a> <br> {{item.description}}</p> 
     
    </li> -->
    
    <Modal v-if="registerModal || loginModal" @close="userClear()" >
      <div slot="header">
        <h3 class="modal-default-button"><i class="closeModalBtn fas fa-times fa-lg" @click="userClear()"></i></h3>
       </div>
        <div slot="body" class="loginForm">
          <input  id="userName" type="text" v-model="userName" name="userName" placeholder="아이디" autocomplete="off" style="margin-top:50px;">
          
          <input  id="userPassword" type="text" v-model="userPassword" name="userPassword" placeholder="비밀번호" autocomplete="off">

          <input  v-if="!!registerModal" id="userPasswordCheck" type="text" v-model="userPasswordCheck" name="userPasswordCheck" placeholder="비밀번호 확인" autocomplete="off">
          <p v-show="!!registerModal && userPassword !== userPasswordCheck">비밀번호를 맞춰주십시오</p>
        </div>
        
        

        <div slot="footer" v-if="registerModal">

          <button :disabled="!userName || !userPassword || userPassword !== userPasswordCheck" type="button" @click="register">회원 가입</button>

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
        <input v-on:change="fileSelect()" ref="newUserProfile" type="file">
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
      
      
      
     
      userName: '',
      userPassword: '',
      userPasswordCheck:'',
      registerModal: false,
      loginModal: false,
      failResult: '',
      kakaoMapSearch:'',



      newUserProfile:'',
      profileUpdateModal:false,
      profileImage:'',
      profileSubmit:false
      
      
      
    }
  },
  created(){
      //this.getnewUserProfile();
  },
  methods:{
 
    
    naverSearch(){
      this.$store.commit('naverSearch',this.tester);
    }
    ,
   
    register(){
     
       
     
       this.$axios.post('/register',{
          userName:this.userName,
          userPassword:this.userPassword
        })
        .then((response)=>{
        
          
          
          if(response.data == '중복된 아이디입니다.'){

            this.failResult = response.data;
            this.userName = ''
          
          } else {
            
            this.userClear();
            localStorage.setItem("token",response.data);
            this.$store.commit('setIsLogin',true);
            window.alert('회원가입되었습니다.')
            //this.$store.state.isLogin = true;
              
          }
          
          
        })
        .catch(function(error){
          console.log(error);
          
        });
    },
    // registerModalOpen(){
    //   this.registerModal = true;
    // },
    // loginModalOpen(){
    //   this.loginModal = true;
    // },
    
    login(){
     
        this.$axios.post('/login',{
          userName:this.userName,
          userPassword:this.userPassword
        })
        .then((res)=>{
          
          
            if(res.data == '없는 아이디입니다.' || res.data == '비밀번호가 틀렸습니다.'){

              this.failResult = res.data;
          
            }
            else {
                this.userClear();
                console.log(res.data);

                localStorage.setItem("token",res.data);
                
                this.$store.commit('setIsLogin',true);
                window.alert('로그인되었습니다.')

              
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
      this.userPasswordCheck = '';
      
      this.registerModal = false;
      this.loginModal = false;
      this.failResult = '';
    },
   
    kakaoMap(){
      this.$router.push(`map/${this.kakaoMapSearch}`)
    },





    fileSelect(){
      this.profileSubmit = true;

      this.newUserProfile = this.$refs.newUserProfile.files[0];

      var reader = new FileReader();

      reader.onload = e =>{
        this.profileImage = e.target.result;
      }

     

      reader.readAsDataURL(this.newUserProfile);
    },


    removeFile(){
      this.newUserProfile = ''
      this.profileImage = ''
    },



    userProfileUpdate(){

      var frm = new FormData();
      frm.set('token',localStorage.getItem('token'))


      if(this.newUserProfile){
        frm.set('profile',this.newUserProfile)   //파일
      }
      // else if(this.profileImage){
      //   frm.set('profileName',this.profileImage) //삭제 여부 결정
      // }
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

        
        if(response.data.Auth=='FAIL'){
          window.alert('로그인이 필요합니다.')
          this.$store.commit('setIsLogin',false)
        }else{
          window.alert('프로필이 성공적으로 변경되었습니다.')
          
        }
      })
      .catch(err=>{
        console.log(err)
      })
    },



    loadProfileImage(){
     
      this.$axios.post('/user/profile',{
        token:localStorage.getItem('token')
      })
      .then(response=>{
        console.log(response)
        if(response.data){
          this.profileImage = 'upload/' + response.data
        }
      })
      .catch(err=>{
        console.log(err)
      })
    },


    addFiles(){
      this.$refs.newUserProfile.click();
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
