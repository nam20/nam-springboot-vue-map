<template>
  <!DOCTYPE HTML>
		
		
	<div style="margin-top:200px;">
		<!-- <header>
		<h1 style="cursor:pointer;margin:0;" @click="$router.push('/')">HeeJun</h1>

		
		</header> -->
		
			<div id="wrapper">  

			
					<div id="main">
						<h1>모든 리뷰</h1>
						<div class="inner">
							
							
							<section class="tiles">

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

					<footer id="footer">
						
					</footer>

			</div>
			


			
		
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
				pageSize : 9,
				hasNextPage: true
        }
    },
    created(){
		this.loadBoards();
		//this.allBoardCount();
		
	},
	watch:{
		boards(){
			if(!this.boards.length) this.searchResult = '리뷰가 없습니다'
			
			
		}
	},
    methods:{
        loadBoards(){
				
            this.$axios({
				method: 'get',
				url: `/board/all/${this.page}/${this.pageSize}`
			})
            .then(res =>{
				
				if(res.data){

					const boardPaging = res.data
					
					this.boards = this.boards.concat(boardPaging.boardList);
					
					this.boards.forEach(board=>{
						switch(board.grade){
							case 'GOOD': board.grade = '맛있다!'
								break;
							case 'SOSO': board.grade = '평범하다'
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
		slice(){
			this.boards = this.boards.slice(0,this.pageSize);
			this.hasNextPage = true;
			this.page = 1;
		},
		// allBoardCount(){
		// 	this.$axios({
		// 		method: 'get',
		// 		url: `/boardCount`
		// 	})
		// 	.then(response=>{
		// 		console.log(response.data);
		
				
		// 		this.boardCount = response.data;
				
				
		// 	})
		// 	.catch(err=>{
		// 		console.log(err);
				
		// 	})
		// }
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