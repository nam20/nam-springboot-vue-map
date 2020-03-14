<template>
  <div>
      <h1>게시판 {{ address_name }}</h1>
        <router-link to="/">돌아가기</router-link>
        <router-link to="/boardWrtie">리뷰 쓰기</router-link>
        <table style="width:1000px; margin:10px auto;text-align:center; border:1px solid #dddddd">
          
            <thead>
               <tr>
                   <th colspan="5">{{this.searchtitle}}에 대한 리뷰 모음!{{this.$route.params}}</th>
               </tr>
               <tr >
                   <th>번호</th>
                   <th>제목</th>
                   <th>작성자</th>
                   <th>날짜</th>
                   <th>조회수</th>
               </tr>
            </thead>
               
            <tbody>
                <tr v-for="(board,index) in arr" :key="board">
                    <td>번호!{{board.userID}} - {{index}}</td>
                    <td>제목!{{board.boardID}}</td>
                    <td>작성자!{{board.boardContent}}</td>
                    <td>날짜!{{board.boardTitle}}</td>
                    <td>조회수!{{board.boardDate}}</td>
                </tr>
            </tbody>
           

        </table>
     
  </div>
</template>

<script>
//import axios from 'axios';
export default {
   data(){
      return{
        arr: []
           
      }
   },
   created: function(){
       console.log('created');
       
   },
   mounted : function(){
       this.boardArr();
       console.log('mounted');
       
   }
   ,
   methods:{
       boardArr(){
           

          
           this.$axios.get(`/boardView/${this.id}`)
          .then(response =>{
                console.log(response.data);
                response.data.forEach(element => {
                    this.arr.push(element);
                });
                

            })
           .catch(error => {
               console.log(error);
             
           })         
           

       }
   },
   props: ['id','searchtitle']
}
</script>

<style>

</style>