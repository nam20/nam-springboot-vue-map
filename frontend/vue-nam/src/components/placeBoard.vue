<template>
  <!DOCTYPE HTML>
<!--
	Phantom by HTML5 UP
	html5up.net | @ajlkn
	Free for personal and commercial use under the CCA 3.0 license (html5up.net/license)
-->

	
<body class="is-preload" style="margin-top:200px;">
	

		<!-- <home-header></home-header> -->

		<!-- Wrapper -->
			<div id="wrapper">
					<header>
  		<h1 style="cursor:pointer;margin:0;" @click="$router.push('/')">HeeJun</h1>

  
   </header>
				<!-- Header -->
					

				<!-- Menu -->
					

				<!-- Main -->
					<div id="main">
						<h1>{{this.placeName}} <h2 style="color:rgb(78, 187, 163);display:inline;letter-spacing:1px;">{{gradeAvg}}</h2></h1>
						
							<button @click="$router.push(`/boardWrite/${placeId}/${placeName}`)" style="margin-bottom:20px;">
						
								리뷰 작성하기
							
							</button>
							
						<div class="inner">
							
								
								
								
							
							<section class="tiles" style="margin-top:20px;">



								<article class="style3" v-for="board in arr" :key="board">
									<span class="image" >
										<img v-if="board.files.length" :src="'upload/'+ board.files[0]" alt="" />
										<img v-else src="upload/defaultImage.jpg" alt="" > 
									</span>
									<router-link :to="`/boardReview/${board.id}`" style="padding:16px 16px 16px 0;">
										<p style="margin:0 0 15px 0;text-shadow: #333333 1px 0 3px;font-size:x-large;font-weight:bold;">{{board.grade}}</p>
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
					
					</footer>

			</div>
			

		<!-- Scripts -->
			
</body>

</template>

<script>


export default {
		
		 data(){
        return{
			arr : [],
			searchResult : '',
			gradeAvg: 0,
			boardCount : 0,
			page: 0,
			paging: true,
			pageSize: 9
        }
    },
    mounted(){
		this.boardArr();
		this.boardGrade();
		
		// else{
		// 			this.searchResult = `리뷰가 없습니다.`
		// 		}
	},
	watch:{
		arr(){
			
			if(!this.arr.length) this.searchResult = `${this.placeName}에 대한 리뷰가 하나도 없습니다`
			console.log(this.arr.length); 
		}
	},
    methods:{
        boardArr(){
				
            this.$axios({
				method: 'get',
				url: `/boards/${this.placeId}/${this.page}/${this.pageSize}`
			})
            .then(response =>{
				console.log(response.data);
				console.log(response);
				
				
				if(response.data){
					// response.data.forEach(element => {
					// 	this.arr.push(element);
					// })
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
					this.page += 1;
				}

				console.log(parseInt(this.arr.length / this.pageSize) > 0);
			
				


            })
            .catch(error=>{
                console.log(error);
                
            })

		},
		boardGrade(){
			this.$axios({
				method: 'get',
				url: `/boardCount/gradeAvg/${this.placeId}`
			})
			.then(response=>{
				console.log(response.data);
				
				if(response.data.avg){
					this.gradeAvg = response.data.avg.toFixed(1);
					this.boardCount = response.data.count;
				}

				if(this.boardCount <= this.pageSize)  this.paging = false;
				
			})
			.catch(err=>{
				console.log(err);
				
			})
		},
		slice(){
			this.arr = this.arr.slice(0,this.pageSize);
			this.page = 1;
		}
	},
	props: ['placeId','placeName']
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

span.image > img{
	width: 360px;
	height: 400px;
}
button:active{
	outline: none;
}


</style>