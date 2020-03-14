<template>
  <!DOCTYPE HTML>
		
		
	<body class="is-preload" style="margin-top:200px;">
		<header>
		<h1 style="cursor:pointer;margin:0;" @click="$router.push('/')">HeeJun</h1>

		
		</header>
			<!-- <home-header></home-header> -->
		<!-- Wrapper -->
			<div id="wrapper">  

				<!-- Header -->
					<!-- <header id="header">
						<div class="inner">

							
								<a href="/" class="logo">
									<span class="symbol"><img src="images/logo.svg" alt="" /></span><span class="title">Phantom</span>
								</a>

						
								<nav>
									<ul>
										<li><a href="#menu">Menu</a></li>
									</ul>
								</nav>

						</div>
					</header> -->
					

				<!-- Menu -->
					<!-- <nav id="menu">
						<h2>Menu</h2>
						<ul>
							<li><a href="index.html">Home</a></li>
							<li><a href="generic.html">Ipsum veroeros</a></li>
							<li><a href="generic.html">Tempus etiam</a></li>
							<li><a href="generic.html">Consequat dolor</a></li>
							<li><a href="elements.html">Elements</a></li>
						</ul>
					</nav> -->

				<!-- Main -->
					<div id="main">
						<h1>모든 리뷰</h1>
						<div class="inner">
							
							
							<section class="tiles">



								<article class="style3" v-for="board in arr" :key="board">
									<span class="image">
										<img v-if="board.files.length" :src="'upload/'+ board.files[0]" alt=""/>
										<img v-else src="upload/defaultImage.jpg" alt="" />
									</span>
									<router-link :to="`/boardReview/${board.id}`"  style="padding:16px 16px 16px 0;">
										<p class="board_info">{{board.placeName}}</p>
										<p class="board_info">{{board.grade}}</p>
										<p class="text_overflow" >{{board.boardContent}}</p>

										
										<!-- <div class="content">
											<p class="text_overflow" >{{board.boardContent}}</p>
											<h2>{{board.grade}}</h2>
										</div> -->
									</router-link>
								</article>
								

								


							</section>
							<br>
							<section>
								<div v-if="paging">
										<button v-if="arr.length < boardCount" @click="boardArr">더보기</button>
										<button v-else @click="slice">접기</button>
								</div>
									<H1 style="margin:1em 0 1em;">{{searchResult}}</H1>
							</section>

							
							
						</div>
					</div>

				<!-- Footer -->
					<footer id="footer">
						<!-- <div class="inner">
							<section>
								<h2>Get in touch</h2>
								<form method="post" action="#">
									<div class="fields">
										<div class="field half">
											<input type="text" name="name" id="name" placeholder="Name" />
										</div>
										<div class="field half">
											<input type="email" name="email" id="email" placeholder="Email" />
										</div>
										<div class="field">
											<textarea name="message" id="message" placeholder="Message"></textarea>
										</div>
									</div>
									<ul class="actions">
										<li><input type="submit" value="Send" class="primary" /></li>
									</ul>
								</form>
							</section>
							<section>
								<h2>Follow</h2>
								<ul class="icons">
									<li><a href="#" class="icon brands style2 fa-twitter"><span class="label">Twitter</span></a></li>
									<li><a href="#" class="icon brands style2 fa-facebook-f"><span class="label">Facebook</span></a></li>
									<li><a href="#" class="icon brands style2 fa-instagram"><span class="label">Instagram</span></a></li>
									<li><a href="#" class="icon brands style2 fa-dribbble"><span class="label">Dribbble</span></a></li>
									<li><a href="#" class="icon brands style2 fa-github"><span class="label">GitHub</span></a></li>
									<li><a href="#" class="icon brands style2 fa-500px"><span class="label">500px</span></a></li>
									<li><a href="#" class="icon solid style2 fa-phone"><span class="label">Phone</span></a></li>
									<li><a href="#" class="icon solid style2 fa-envelope"><span class="label">Email</span></a></li>
								</ul>
							</section>
							<ul class="copyright">
								<li>&copy; Untitled. All rights reserved</li><li>Design: <a href="http://html5up.net">HTML5 UP</a></li>
							</ul>
						</div> -->
					</footer>

			</div>
			

		<!-- Scripts -->
			
		
	</body>
	
</template>

<script>


export default {
		
		 data(){
			return{
				arr :  [],
				searchResult : '',
				page: 0,
				paging: true,
				pageSize : 9,
				boardCount: 0
        }
    },
    mounted(){
		this.boardArr();
		this.allBoardCount();
		
	},
	watch:{
		arr(){
			if(!this.arr.length) this.searchResult = '리뷰가 하나도 없습니다'
			console.log(this.arr.length);
			
		}
	},
    methods:{
        boardArr(){
				
            this.$axios({
				method: 'get',
				url: `/allBoard/${this.page}/${this.pageSize}`
			})
            .then(response =>{
				console.log(response.data);
				console.log(response);
				
				
				if(response.data){
					
					this.arr = this.arr.concat(response.data);
					
					this.arr.forEach(board=>{
						switch(board.grade){
							case 'GOOD': board.grade = '맛있다!'
								break;
							case 'SOSO': board.grade = '평범하다'
								break;
							case 'BAD': board.grade = '별로...'
								break;
							default:
								break;
						}
					})
					console.log(this.page);
					
					this.page += 1;
					
				}
				
				
                

            })
            .catch(error=>{
                console.log(error);
                
            })

		},
		slice(){
			this.arr = this.arr.slice(0,this.pageSize);
			this.page = 1;
		},
		allBoardCount(){
			this.$axios({
				method: 'get',
				url: `/boardCount`
			})
			.then(response=>{
				console.log(response.data);
		
				
				this.boardCount = response.data;
				if(this.boardCount <= this.pageSize)  this.paging = false;
				
			})
			.catch(err=>{
				console.log(err);
				
			})
		}
	}
}
</script>

<style scoped>
@import url(../assets/main.css);

p.text_overflow{
	
	overflow: hidden;
    text-overflow: ellipsis;
	display: -webkit-box;
	width: 80%;
    -webkit-line-clamp: 3;
    -webkit-box-orient: vertical;
    word-wrap:break-word; 
    line-height: 1.2em;
	height: 3.5em; /* line-height 가 1.2em 이고 3라인을 자르기 때문에 height는 1.2em * 3 = 3.6em */
	/* text-shadow: #000 1px 0px 10px; */
	opacity: 0;

	-moz-transition: max-height 0.5s ease, opacity 0.5s ease;
	-webkit-transition: max-height 0.5s ease, opacity 0.5s ease;
	-ms-transition: max-height 0.5s ease, opacity 0.5s ease;
	transition: max-height 0.5s ease, opacity 0.5s ease;
					
	max-height: 0;
				
	margin-top: 0.35em;
	



}

body:not(.is-touch) .tiles article:hover  p.text_overflow{
	max-height: 15em;
	opacity: 1;
}

p.board_info{
	margin:0 0 15px 0;
	text-shadow: #333333 1px 0 3px;
	font-size:x-large;
	font-weight:bold;
}

span.image > img{
	width:360px;height:400px;
}

button:active{
	outline: none;
}



</style>