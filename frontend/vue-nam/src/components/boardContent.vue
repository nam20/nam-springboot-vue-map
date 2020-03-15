<template>
    <article >
        <span class="image">
            <img v-if="board.files.length" :src="`upload/${board.files[0]}`" alt=""/>
            <img v-else src="upload/defaultImage.jpg" alt="" />
        </span>
        <router-link :to="`/boardReview/${board.id}`"  style="padding:16px 16px 16px 0;">
            <p class="board_info">{{board.placeName}}</p>
            <p class="board_info">{{board.grade}}</p>
            <p class="text_overflow" >{{board.boardContent}}</p>

        
            
        </router-link>
    </article>
</template>

<script>
export default {
    props:{
        board:{
            type:Object,
            required:true
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

article:hover  p.text_overflow{
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
</style>