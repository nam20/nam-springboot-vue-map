<template>


	

	
			<div style="background-color:#e6e6e6ea;">
				
						<!-- <header>
							<h1 style="cursor:pointer;margin:0;" @click="$router.push('/')">HeeJun</h1>
						</header> -->

	
					<div id="main" v-if="board">
						
							
						
						<div class="inner" style="text-align:left;padding:15px;">
								
									<div style="float:left;">
										<img v-if="!!board.user.userProfile" :src="'upload/'+ board.user.userProfile"  alt="" class="boardProfile">
										<img v-else src="upload/defaultProfile.png" alt="" class="boardProfile">
										<!-- <h2 style="display:inline;vertical-align:top;letter-spacing:2px;">{{board.user.userName}}</h2> -->
										<router-link :to="`/userBoard/${board.user.userName}`" style="vertical-align:top;letter-spacing:2px;font-size: x-large;">{{board.user.userName}}</router-link>
										<router-link :to="`/map/${board.placeName}`" style="display:block;font-weight:bold">{{board.placeName}}</router-link>
										<!-- <h6 style="margin-top:10px;letter-spacing:1px;">{{board.placeName}}</h6> -->
									</div>

									<div style="float:right;">
										<h2 style="display:inline;letter-spacing:1px;">{{board.grade}}</h2>
									</div>
								
								<div style="clear:both;">
									<p>{{board.boardContent}}</p>
									<p style="margin:0;">{{board.createdTime.substring(0,10)}}</p>							
								</div>

								<div v-if="board.files.length" class="boardImages" style="margin-top:15px;">
									<span v-for="boardImage in board.files" :key="boardImage" style="cursor:pointer;">
										<img  class="boardImage" @click="carouselModal= true" :src="'upload/'+boardImage" alt="">
									</span>
								</div>
							
						</div>
						
						<div class="inner">
							<section>

								<div v-if="comments" class="commentsList" >
									<div v-for="comment in comments" :key="comment" class="comment" >

										<div style="float:left;margin:10px 0 0 10px;">
											<img v-if="comment.user.userProfile" :src="'upload/' + comment.user.userProfile" class="commentImage" alt="" >
											<img v-else src="upload/defaultProfile.png" class="commentImage" alt="">
											<strong style="margin:0 0 0 10px;vertical-align:top;">{{comment.user.userName}}</strong>
										</div>

										<p style="float:right;margin:5px 10px 0 0;">{{comment.createdDate.substring(0,10)}}</p>
										
										<p class="event_desc" >{{comment.commentContent}}</p>
									
								    </div>

								</div>
								
								<!-- <textarea v-model="comment" v-on:keyup.enter="commentWrite" name="" id="" cols="30" rows="10" style="border-top-color:#E9E9E9;"></textarea> -->
								<input v-if="me" type="text" v-model="commentContent" v-on:keyup.enter="commentWrite" style="border-top-color:#E9E9E9;width: 50%;">
								<button @click="doSend(commentContent)">센드</button>
								<div style="margin-bottom:30px;">
									<button @click="$router.go(-1)" >돌아가기</button>
									<button v-bind:disabled="! me || !commentContent" @click="commentWrite">댓글등록</button>

									

									<div v-if="links.delete">
										<button @click="$router.push(`/boardUpdate/${boardId}`)">수정하기</button>
										<button @click="deleteBoard">삭제하기</button>
									</div>
								</div>
										
							
							</section>
					
							
						</div>
					</div>

					<footer id="footer" style="background-color:#e6e6e6ea;">


							<!-- <image-modal></image-modal> -->
							<div class="modal-mask" v-if="carouselModal">
								<div class="modal-wrapper">
									<div class="modal-container">
										<div>
											<i class="fas fa-times fa-lg" @click="carouselModal=false" style="cursor:pointer;float:right"></i>
										</div>
										<myCarousel :data="carouselArr" style="margin-top: 25px;" :autoplay="false"></myCarousel>
									</div>
								</div>
							</div>
					</footer>

				
					 <!-- <footer id="footer">
						<ul class="copyright">
								<li>&copy; Untitled. All rights reserved</li><li>Design: <a href="http://html5up.net">HTML5 UP</a></li>
							</ul>
					</footer> -->

		
			</div>

</template>

<script>

import VueCarousel from '@chenfengyuan/vue-carousel';

export default {
	components:{
		
		myCarousel: VueCarousel
		
	},
	computed:{
		me(){
			return this.$store.state.me
		}
	},
	data(){
		return {
			board: '',

			comments:[],

			commentContent: '',
			commentInterval:'',
			// isWriter: false,
			carouselArr:[],
			carouselModal: false,
			links:{},


			webSocket:''

		}
	}
	,
	props: ['boardId'],
	
	created(){
		
		this.$store.dispatch('loadUser')
		this.loadBoard();
	

		this.sendMessage()



		this.loadComments();	

		this.commentInterval = setInterval(()=>{
			this.loadComments();
				
		 }, 3000)
		
	},
	beforeDestroy(){
		clearInterval(this.commentInterval);
		this.webSocket.close();
	},
	methods:{
		loadBoard(){
			this.$axios.get(`/board/${this.boardId}`,{
				headers:{
					Authorization : `Bearer ${localStorage.getItem("token")}`
				}
			})
			.then(response=>{
				
				
				this.links = response.data._links
				
				
				this.board = response.data;

				
					switch(this.board.grade){
						case 'GOOD': this.board.grade = '맛있다!'
							break;
						case 'SOSO': this.board.grade = '평범하다'
							break;
						case 'BAD': this.board.grade = '맛없다!!!'
							break;
						default:
							break;
					}
					
							
				this.board.files.forEach(file=>{
					this.carouselArr.push(`<div class="example-slide"><img src="upload/${file}" style="width:600px;height:600px;" alt=""></div>`)
				})
				
				
			})
			.catch(err=>{
				console.log(err);
			})
		},
		loadComments(){
			this.$axios({
					method:'get',
					url: `/comments/${this.boardId}`
				})
				.then(response=>{
				
					
					this.comments = response.data;
					
				})
				.catch(err=>{
					console.error(err)
				})
		},
		commentWrite(){


			// var frm = new FormData();
			// frm.set('comment',this.comment);
			// frm.set('token',localStorage.getItem('token'));
			// frm.set('boardId',this.boardId);

			

			this.$axios({
				method: 'post',
				url: `/comment`,
				data: {
					comment:this.commentContent,
					token:localStorage.getItem('token'),
					boardId:this.boardId
				},
				headers:{
					Authorization : `Bearer ${localStorage.getItem('token')}`
				}
			})
			.then(response=>{
				if(response.data.Auth === 'FAIL'){
					window.alert('로그인이 필요합니다')
				}

				this.commentContent = ''
			})
			.catch(err=>{
				console.log(err);
				
			})
		},
		deleteBoard(){
			// this.$axios({
			// 	method: 'patch',
			// 	url: `/board/${this.boardId}`,
			// 	headers:{
			// 		Authorization : `Bearer ${localStorage.getItem('token')}`
			// 	}
			// })
			this.$axios.delete(`${this.links.delete.href}`,{
				headers:{
					Authorization : `Bearer ${localStorage.getItem('token')}`
				}
			})
			.then(response=>{


				if(response.data.Auth=='FAIL'){
                    window.alert('로그인이 필요합니다')
                    this.$router.push('/')
				}else{
					window.alert('성공적으로 삭제되었습니다.')
					this.$router.push(`/board/${this.board.placeId}/${this.board.placeName}`)
				}
			})
		},




		sendMessage(){
			this.webSocket = new WebSocket(`ws://localhost:18080/websocket/${this.boardId}`)
			
			this.webSocket.onopen = (evt) =>{
				this.onOpen(evt)
			}

			this.webSocket.onmessage  = (evt) =>{
				this.onMessage(evt)
			}

			this.webSocket.onerror  = (evt) =>{
				this.onError(evt)
			}
		
		},
		onOpen(evt){
			console.log(evt)
		
		},
		onMessage(evt){
			console.log(evt)
		},
		onError(evt){
			console.log(evt)
		},
		doSend(message){
			
			this.webSocket.send(message)
		},


	}
}
</script>

<style scoped>

@import url(../assets/main.css);


body{
	background-color: rgb(224, 224, 224)
}


div#main{
	
	width: 700px;
	margin: 20px auto;
	padding: 40px 15px;
	background-color: white;
	background-clip: content-box;
}

textarea{
	resize: none;
	margin-top:20px;
	width:60%;
	height:140px;
}
p.event_desc {
  
  line-height: 14px;
  height:70px;
  margin: 0 0 0 20px;
  display: table-cell;
  vertical-align: middle;
  padding: 10px;
  
}

.boardProfile{
	width:90px;
	height:90px;
	border-radius:90px;
	margin:0 10px 0 0;
}

.boardImages{
	overflow-x:scroll;
	max-height: 200px;
	white-space:nowrap;
	overflow-y:hidden;
}

.boardImage{
	margin:0 6px 0 0;
	width:200px;
	height:200px;
}

.commentsList{
	border-top: 1px solid #e6e6e6ea;
	max-height:500px;
	overflow-y:scroll;
	

}

.comment{
	/* border: 1px solid #e6e6e6ea; */
	margin:10px auto;
	/* height:80px; */
	max-height: 300px;
	overflow-y: hidden;
	/* border: 1px solid #e6e6e6ea; */
	border-bottom: 1px solid #e6e6e6ea;
}

.commentImage{
	width:50px;
	height:50px;
	/* display:inline; */
	border-radius:90px;
}




   .modal-mask {
  position: fixed;
  z-index: 9998;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, .5);
  display: table;
  transition: opacity .3s ease;
}

.modal-wrapper {
  display: table-cell;
  vertical-align: middle;
}

.modal-container {
  width: 80%;
  height: 80%;
  margin: 0px auto;
  padding: 20px 30px;
  color: #fff;
  background-color: #333;
  border-radius: 2px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, .33);
  transition: all .3s ease;
  font-family: Helvetica, Arial, sans-serif;
}


.example-slide {
    align-items: center;
    background-color: #333;
    color: #999;
    display: flex;
    font-size: 1.5rem;
    justify-content: center;
    min-height: 10rem;
  }
</style>