<template>
  
<!--
	Phantom by HTML5 UP
	html5up.net | @ajlkn
	Free for personal and commercial use under the CCA 3.0 license (html5up.net/license)
-->

	
	<div  style="margin-top:200px;">
	

		<!-- <home-header></home-header> -->

		<!-- Wrapper -->
			<div id="wrapper">
				
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



								<board-content v-for="board in boards" :key="board" :board="board">

								
								
									


							</section>
							<br>
							<section>
								<div v-if="totalPages > 1">
										<button v-if="hasNextPage" @click="loadBoards">더보기</button>
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
			
	</div>

</template>

<script>
import boardContent from './boardContent'

export default {
	components:{
		boardContent
	},
	data(){
        return{
			boards :  [],
			searchResult : '',
			page: 0,
			totalPages: 0,
			limit : 9,
			hasNextPage: true
        }
    },
    created(){
		this.loadBoards();
		this.boardGrade();
		

	},
	watch:{
		boards(){
			
			if(!this.boards.length) this.searchResult = `${this.placeName}에 대한 리뷰가 없습니다`
			
		}
	},
    methods:{
        loadBoards(){
				
            this.$axios({
				method: 'get',
				url: `/board/place/${this.placeId}`,
				params:{
					page:this.page,
					limit:this.limit
				}
			})
            .then(res =>{
				
				
				
				
				if(res.data){
					// res.data.forEach(element => {
					// 	this.boards.push(element);
					// })

					const boardPaging = res.data

					this.boards = this.boards.concat(boardPaging.boardList);
					this.boards.forEach(board=>{
						switch(board.grade){
							case 'GOOD': board.grade = '맛있다!'
								break;
							case 'SOSO': board.grade = '평범하다..'
								break;
							case 'BAD': board.grade = '맛없다!!!'
								break;
							default:
								break;
						}
					})

					this.page += 1;

					this.totalPages = boardPaging.totalPages

					this.hasNextPage = boardPaging.hasNextPage

				}

				
			
				


            })
            .catch(error=>{
                console.log(error);
                
            })

		},
		boardGrade(){
			this.$axios({
				method: 'get',
				url: `/board/gradeAvg/${this.placeId}`
			})
			.then(response=>{
				
				
				if(response.data.avg){
					this.gradeAvg = response.data.avg.toFixed(1);
					
				}

				//if(this.boardCount <= this.pageSize)  this.paging = false;
				
			})
			.catch(err=>{
				console.log(err);
				
			})
		},
		slice(){
			this.boards = this.boards.slice(0,this.limit);
			this.page = 1;
			this.hasNextPage = true;
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