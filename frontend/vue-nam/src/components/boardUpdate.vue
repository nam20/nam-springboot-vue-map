<template>



    <body style="margin-top:130px;">
       
        <section>
         <header>
            <h1 style="cursor:pointer;margin:0;" @click="$router.push('/')">HeeJun</h1>

        
        </header>
            <div class="container">
                <strong>{{board.placeName}}</strong>
                
                    <span>
                        의 리뷰
                    </span>
                
            
            
             <div class="reviewContent">
                
                <ul>
                    <li>
                        <button :class="{active : grade == 'GOOD'}" @click="grade = 'GOOD'">맛있다!</button>
                    </li>
                    <li>
                        <button :class="{active : grade == 'SOSO'}" @click="grade = 'SOSO'">평범하다!</button>
                    </li>
                    <li>
                        <button :class="{active : grade == 'BAD'}" @click="grade = 'BAD'">별로!</button>
                    </li>
                </ul>

                 <textarea name="" id="" cols="30" rows="10" v-model="content" :placeholder="`${board.user.userName}님 솔직한 리뷰를 남겨주세요.`"></textarea>
             </div>

              <div class="fileSelect" >
                    <div class="uploadImage" v-for="(file, key) in boardImage" :key="key">
                        <div class="image-wrapper">
                            <div>
                                <img style="width:90px;height:90px;margin:0 5px 0 0;" :src="showImage[key]">
                            </div>
                            <div class="image-delete-box">
                                <i class="fas fa-times" v-on:click="removeFile( key )" style="background-color:black;color:white;"></i>
                            </div>
                            
                        </div>
                        
                       
                    </div>
                    <input v-on:change="fileSelect" id="files"  type="file" ref='boardImage' multiple="multiple">
                    <button class="addFile" @click="addFiles" style="font-size: 20px;"><i class="fas fa-plus" style="margin: 0 0 0 4.5px;"></i></button>

              </div>

            
    
                <div class="reviewSubmit">
                    <button @click="$router.go(-1)" style="margin:0 20px; 0 0;">돌아가기</button>
                    
                    <button v-bind:disabled="!content"  @click="boardUpdate">등록</button>
                
                        
                </div>

            </div>

        

        </section>
   </body>

</template>
 
<script>

//acceot
export default {
    
    props: ['boardId'],
    data(){
        return {
            content: '',
            
            grade : '',
            token : localStorage.getItem('token'),
            board: {
                user:{},
                files:[]
            },
            boardImage: [],
            showImage:[]

        }
    },
    mounted(){

       this.$axios({
           method: 'get',
           url: `/board/${this.boardId}`
       })     
       .then(response=>{
           console.log(response);
           
           if(response.data){
               console.log(response.data);
               
               this.board = response.data
               this.content = this.board.boardContent;
               this.grade = this.board.grade;

               this.board.files.forEach(fileName=> {
                    this.boardImage.push(fileName)
                    this.showImage.push('upload/' + fileName)
               })

                console.log(this.boardImage)

               //upload/    substring(9)
            
           }
       })

    },

    methods:{
        boardUpdate(){
            
            let frm = new FormData();

            frm.set('grade',this.grade)
            frm.set('content',this.content)
            frm.set('boardId',this.boardId)
            
            this.boardImage.forEach(file=>{
                
                if(typeof file === 'object') {
                    
                    frm.append('files',file)
                }
                else {
                    
                    frm.append('fileNames',file)
                }
            })


            this.$axios({
                method: 'patch',
                url: '/board',
                data: frm,
				headers:{
					Authorization : `Bearer ${localStorage.getItem('token')}`
				}

            })
            .then(res=>{
                if(res.data.Auth=='FAIL'){
                    window.alert('로그인이 필요합니다')
                    this.$router.push('/')
                }else{
                    window.alert('리뷰가 성공적으로 수정되었습니다.')
                    this.$router.push(`/board/${this.board.placeId}/${this.board.placeName}`)
                }
            })
            .catch(err=>{
                console.log(err);
                
            })
        },
        fileSelect(){
            let uploadedFiles = this.$refs.boardImage.files;

            for(var i = 0; i< uploadedFiles.length; i++){
                this.boardImage.push(uploadedFiles[i]);
                 
                var reader = new FileReader();
              
                reader.onload = (e) =>{
                    this.showImage.push(e.target.result);
                   
                    
                };
                reader.readAsDataURL(uploadedFiles[i]);
            }

           
        },
        removeFile(key){
            this.boardImage.splice(key,1);
            this.showImage.splice(key,1)
        },
        addFiles(){
            this.$refs.boardImage.click();
        }
    }
}
</script>

<style scoped>

@import url(../assets/main.css);

input[type="file"]{
    position: absolute;
    top: -500px;
  }

ul {

    display: table;
    list-style:none;
    margin:0;
    padding:0;
}

li {
    margin: 0 0 10px 0;
    padding: 0 0 0 0;
    border : 0;
    float: left;
}

.image-wrapper{
    position: relative;
}

.image-delete-box{
    position: absolute;
    right: 10px;
    top: 0;

}
/* 
html,body{
  height:100%;
}
body{
  text-align:center;
}
body:before{
  content:'';
  height:100%;
  display:inline-block;
  vertical-align:middle;
} */
/* button{
  background:#fff;
  color:#1AAB8A;
  
  border:none;
  position:relative;
  height:60px;
  font-size:1.6em;
  padding:0 2em;
  cursor:pointer;
  transition:800ms ease all;
  outline:none;
}


button.active{
  background:#1AAB8A;
  color:#fff;
  border:none;
  position:relative;
  height:60px;
  font-size:1.6em;
  padding:0 2em;
  cursor:pointer;
  transition:800ms ease all;
  outline:none;
} */

home-header > h1{
    display: inline;
}

button.active{
    color: rgb(113, 180, 165) !important;
	box-shadow: inset 0 0 0 2px rgb(113, 180, 165);
}

button:focus{
    outline: none;
}

textarea{
    resize: none;
    height: 220px;
    border: none;
    margin: 10px 0;
    padding: 0px 10px 15px 10px;
}

div.container{
    width:55%;
    margin: 0 auto;
    text-align: left;
}

div.reviewContent{
    height: 340px;
    border: 1px solid #e6e6e6ea;
   
    margin: 20px 0;
    padding: 13px;
}

div.reviewSubmit{
    margin-top: 50px;
    text-align: right;
}

i.fas.fa-plus::before{
    width:50px;
    height:50px;
}
button.addFile{
    margin:0;
    width:90px;
    height:90px;
    border:2px dashed #b3b3b39a;
    box-shadow: none;
    padding: 0;
}

button.addFile:hover{
    border-color:rgb(135, 194, 181);
    transition: border-color 0.2s ease-in-out,color 0.2s ease-in-out;
}

div.uploadImage{
    float: left;
}

</style>