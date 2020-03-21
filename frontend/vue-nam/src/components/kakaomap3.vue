<template>
<div>
    
     <div id="menu_wrap" class="bg_white">
            <div class="option">
                <div>
                    <!-- <form  style="margin: 12px 0 12px 0;"> -->
                        키워드 : <input type="text" v-model="keyword" id="keyword" size="15" autocomplete="off" class="searchInput" @keyup.enter="searchPlaces()"> 
                        <button @click="searchPlaces()" type="submit" style="outline: none;padding:0 2px;letter-spacing:1px;height:25px;line-height:2px;box-shadow:none;font-size:15px;"><i class="fas fa-search fa-lg" style="vertical-align:middle;margin-bottom: 5px;"></i></button>
                       
                </div>
            </div>
            <hr>
            <div style="height:60px;margin:10px 0;">
                <button class="button" @click="$router.push('/board')" style="height:inherit;font-size:1.3em;padding:0 15px;"> 모든 리뷰 보러가기 </button>
                <!-- <router-link to="/allBoard">모든 리뷰 보러가기</router-link> -->
            </div>
            <ul id="placesList"></ul>
            <div id="pagination"></div>
            
        </div>
    <div class="map_wrap">
    
    <!-- <home-header></home-header> -->
    
    <div id="map" style="width:100%;height:100%;position:relative;overflow:hidden;"></div>
           

       
    
</div>

</div>
</template>

<script>



//import router from '../router'



export default {
    
    props:['search']
    ,
    data(){
        return{
            markers: [],
            keyword: '광명역',
            overlaymarker: false,
            map:'',
            ps:'',
            infowindow:'',
            overlay:''
        }
    },


    mounted(){

        if(this.search) this.keyword = this.search;


        var mapContainer = document.getElementById('map'), // 지도를 표시할 div 
            mapOption = {
                center: new kakao.maps.LatLng(37.566826, 126.9786567), // 지도의 중심좌표
                level: 3 // 지도의 확대 레벨
            };  

        // 지도를 생성합니다    
        this.map = new kakao.maps.Map(mapContainer, mapOption); 

        // 장소 검색 객체를 생성합니다
        this.ps = new kakao.maps.services.Places();  

        // 검색 결과 목록이나 마커를 클릭했을 때 장소명을 표출할 인포윈도우를 생성합니다
        this.infowindow = new kakao.maps.InfoWindow({zIndex:1});

        this.overlay = new kakao.maps.CustomOverlay();


        // 키워드로 장소를 검색합니다
        this.searchPlaces();
    },
    methods:{
       
        
        // 키워드 검색을 요청하는 함수입니다
         searchPlaces() {

            if (!this.keyword.replace(/^\s+|\s+$/g, '')) {
                alert('키워드를 입력해주세요!');
                return false;
            }

            // 장소검색 객체를 통해 키워드로 장소검색을 요청합니다
            this.ps.keywordSearch(this.keyword, this.placesSearchCB,{category_group_code:"FD6"}); 
        },


        // 장소검색이 완료됐을 때 호출되는 콜백함수 입니다
         placesSearchCB(data, status, pagination) {
            if (status === kakao.maps.services.Status.OK) {

                // 정상적으로 검색이 완료됐으면
                // 검색 목록과 마커를 표출합니다
                this.displayPlaces(data);

                // 페이지 번호를 표출합니다
                this.displayPagination(pagination);

            } else if (status === kakao.maps.services.Status.ZERO_RESULT) {

                alert('검색 결과가 존재하지 않습니다.');
                return;

            } else if (status === kakao.maps.services.Status.ERROR) {

                alert('검색 결과 중 오류가 발생했습니다.');
                return;

            }
        },


                // 검색 결과 목록과 마커를 표출하는 함수입니다
         displayPlaces(places) {
               
                
            var listEl = document.getElementById('placesList'), 
            menuEl = document.getElementById('menu_wrap'),
            fragment = document.createDocumentFragment(), 
            bounds = new kakao.maps.LatLngBounds(), 
            listStr = '';
            
            // 검색 결과 목록에 추가된 항목들을 제거합니다
            this.removeAllChildNods(listEl);

            // 지도에 표시되고 있는 마커를 제거합니다
            this.removeMarker();
            
            for ( let i=0; i<places.length; i++ ) {
               
                
                // 마커를 생성하고 지도에 표시합니다
                var placePosition = new kakao.maps.LatLng(places[i].y, places[i].x),
                    marker = this.addMarker(placePosition, i), 
                    itemEl = this.getListItem(i, places[i]); // 검색 결과 항목 Element를 생성합니다

                // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
                // LatLngBounds 객체에 좌표를 추가합니다
                bounds.extend(placePosition);


                
                
            
               

           

           // this.overlay.setMap(null);
                
            
                // 마커와 검색결과 항목에 mouseover 했을때
                // 해당 장소에 인포윈도우에 장소명을 표시합니다
                // mouseout 했을 때는 인포윈도우를 닫습니다
                ((marker, title) => {
                     var content = this.getContent(places[i]); 
                       
                    kakao.maps.event.addListener(marker, 'mouseover', () => {
                        this.displayInfowindow(marker, title);
                    });

                    kakao.maps.event.addListener(marker, 'mouseout', () => {
                        this.infowindow.close();
                    });

                    // 마커를 클릭했을 때 커스텀 오버레이를 표시합니다
                    kakao.maps.event.addListener(marker, 'click', ()=> {    
                        // console.log(content);
                        // console.log(content.childNodes);
                        // console.log(content.children);
                        
                        
                        //   console.log(content.getElementsByClassName('title'));
                          
                          
                         
                      // var content = this.getContent(places[i]); 
                  

                        if(!this.overlaymarker || this.overlay.getContent() !== content){
                            this.overlay.setContent(content);                        
                            this.overlay.setPosition(marker.getPosition());
                            this.overlay.setMap(this.map);
                            this.overlay.setZIndex(3);                     
                        if(!this.overlaymarker) this.overlaymarker = !this.overlaymarker;
                        }else{
                            this.overlay.setMap(null);
                            this.overlaymarker = !this.overlaymarker;
                        }
                        
                    });
                                   

                    content.getElementsByClassName('close').item(0).onclick = () =>{
                        this.overlay.setMap(null);
                        this.overlaymarker = false;
                    }

                    content.getElementsByClassName('review')[0].onclick = () =>{
                        this.$router.push(`/board/${places[i].id}/${places[i].place_name}`);
                    }
                    content.getElementsByClassName('review')[0].onmouseover = () =>{
                        content.getElementsByClassName('review')[0].style.color= "blue"
                    }
                    content.getElementsByClassName('review')[0].onmouseout = () =>{
                        content.getElementsByClassName('review')[0].style.color= "";
                    }
                    
                    // content.onmousedown = () =>{
                    //     this.$router.push(`/board/${places[i].address_name}/${places[i].id}`);
                    // }

                    itemEl.onmouseover =  () => {
                        this.displayInfowindow(marker, title);
                    };

                    itemEl.onmouseout =  () => {
                        this.infowindow.close();
                    };

                    itemEl.onclick = () => {
                        
                        this.$router.push(`/board/${places[i].id}/${places[i].place_name}`);
                        
                    }
                })(marker, places[i].place_name);



            

                fragment.appendChild(itemEl);
            }

            // 검색결과 항목들을 검색결과 목록 Elemnet에 추가합니다
            listEl.appendChild(fragment);
             
            menuEl.scrollTop = 0;

            // 검색된 장소 위치를 기준으로 지도 범위를 재설정합니다
            this.map.setBounds(bounds);
        },


        // 검색결과 항목을 Element로 반환하는 함수입니다
         getListItem(index, places) {

            var el = document.createElement('li'),
            itemStr = '<span class="markerbg marker_' + (index+1) + '" style="margin:5px 0 0 0;"></span>' +
                        '<div class="info">' +
                        '   <h4>' + places.place_name + '</h4>';

            if (places.road_address_name) {
                itemStr += '    <span>' + places.road_address_name + '</span>' +
                            '   <span class="jibun gray">' +  places.address_name  + '</span>';
            } else {
                itemStr += '    <span>' +  places.address_name  + '</span>'; 
            }
                        
            itemStr += '  <span class="tel">' + places.phone  + '</span>' +
                        '</div>';           

            el.innerHTML = itemStr;
            el.className = 'item';

            return el;
        },


        getContent(places){
        
           var el = document.createElement('div');
           el.className="wrap";
            var content = 
         
            '    <div class="info">' + 
            '        <div class="title">' + 
                        `${places.place_name}`  + 
             '            <div class="close" title="닫기"></div>' + 
            '        </div>' + 
            '        <div class="body">' + 
            '            <div class="img">' +
            '                <img src="http://cfile181.uf.daum.net/image/250649365602043421936D" width="73" height="70">' +
            '           </div>' + 
            '            <div class="desc">' + 
            '                <div class="ellipsis">'+`${places.address_name}`+'</div>' ;
            if(places.road_address_name){content +=  '<div class="jibun ellipsis">'+ `${places.road_address_name}` +'</div>'}
            
            
            content += '<div class="review"><span>리뷰 보러가기</span></div>'
            '            </div>' + 
            '        </div>' + 
            '    </div>' ;
           
               
                el.innerHTML = content;
                
                return el;
        },


        // 마커를 생성하고 지도 위에 마커를 표시하는 함수입니다
         addMarker(position, idx, title) {
            var imageSrc = 'http://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png', // 마커 이미지 url, 스프라이트 이미지를 씁니다
                imageSize = new kakao.maps.Size(36, 37),  // 마커 이미지의 크기
                imgOptions =  {
                    spriteSize : new kakao.maps.Size(36, 691), // 스프라이트 이미지의 크기
                    spriteOrigin : new kakao.maps.Point(0, (idx*46)+10), // 스프라이트 이미지 중 사용할 영역의 좌상단 좌표
                    offset: new kakao.maps.Point(13, 37) // 마커 좌표에 일치시킬 이미지 내에서의 좌표
                },
                markerImage = new kakao.maps.MarkerImage(imageSrc, imageSize, imgOptions),
                marker = new kakao.maps.Marker({
                    position: position, // 마커의 위치
                    image: markerImage 
                });

            marker.setMap(this.map); // 지도 위에 마커를 표출합니다
            this.markers.push(marker);  // 배열에 생성된 마커를 추가합니다

            return marker;
        },


        // 지도 위에 표시되고 있는 마커를 모두 제거합니다
        removeMarker() {
            for ( var i = 0; i < this.markers.length; i++ ) {
                this.markers[i].setMap(null);
            }   
            this.markers = [];
        },

        // 검색결과 목록 하단에 페이지번호를 표시는 함수입니다
         displayPagination(pagination) {
            var paginationEl = document.getElementById('pagination'),
                fragment = document.createDocumentFragment(),
                i; 

            // 기존에 추가된 페이지번호를 삭제합니다
            while (paginationEl.hasChildNodes()) {
                paginationEl.removeChild (paginationEl.lastChild);
            }

            for (i=1; i<=pagination.last; i++) {
                var el = document.createElement('a');
                el.href = "#";
                el.innerHTML = i;

                if (i===pagination.current) {
                    el.className = 'on';
                } else {
                    el.onclick = (function(i) {
                        return function() {
                            pagination.gotoPage(i);
                        }
                    })(i);
                }

                fragment.appendChild(el);
            }
            paginationEl.appendChild(fragment);
        },


         // 검색결과 목록 또는 마커를 클릭했을 때 호출되는 함수입니다
        // 인포윈도우에 장소명을 표시합니다
         displayInfowindow(marker, title) {
            var content = '<div style="padding:5px;z-index:1;">' + title + '</div>';

            this.infowindow.setContent(content);
            this.infowindow.open(this.map, marker);
        },
        fuckingInfowindow(marker,title){
            var content = ''
        },

        // 검색결과 목록의 자식 Element를 제거하는 함수입니다
        removeAllChildNods(el) {   
            while (el.hasChildNodes()) {
                el.removeChild (el.lastChild);
            }
        },

        closeOverlay(){
            this.overlay.setMap(null);
        }






    }
}


</script>

<style >
@import url(../assets/main.css);
/* .button{
    -moz-appearance: none;
		-webkit-appearance: none;
		-ms-appearance: none;
		appearance: none;
		-moz-transition: background-color 0.2s ease-in-out, color 0.2s ease-in-out, box-shadow 0.2s ease-in-out;
		-webkit-transition: background-color 0.2s ease-in-out, color 0.2s ease-in-out, box-shadow 0.2s ease-in-out;
		-ms-transition: background-color 0.2s ease-in-out, color 0.2s ease-in-out, box-shadow 0.2s ease-in-out;
		transition: background-color 0.2s ease-in-out, color 0.2s ease-in-out, box-shadow 0.2s ease-in-out;
		background-color: transparent;
		border-radius: 4px;
		border: 0;
		box-shadow: inset 0 0 0 2px #585858;
		color: #585858 !important;
		cursor: pointer;
		display: inline-block;
		font-size: 0.4em;
		font-weight: 900;
		height: 3.5em;
		letter-spacing: 0.1em;
		
		overflow: hidden;
		padding: 0 5px;
		text-align: center;
		text-decoration: none;
		text-overflow: ellipsis;
		text-transform: uppercase;
		white-space: nowrap;
        margin: 5px 0;
        vertical-align: middle;
}

.button:hover{
    color: rgb(113, 180, 165) !important;
	box-shadow: inset 0 0 0 2px rgb(113, 180, 165);
} */

/* 
header{
	position: fixed;
  top: 0;
  left: 0;
  right: 0;
  
  height: 70px; 
  padding:0 10px;
  color: white;
  background: teal;
  font-weight: bold;
  display: flex;
  justify-content: space-between;
  align-items: center;
  z-index: 9999;
}

header > h1{
    letter-spacing: -.5px;
	cursor:pointer;
    margin:0;
    font-size: 44px;
} */


.wrap {position: absolute;left: -144px;bottom: 40px;width: 288px;height: 132px;margin-left: -144px;text-align: left;overflow: hidden;font-size: 12px;font-family: 'Malgun Gothic', dotum, '돋움', sans-serif;line-height: 1.5;}
    .wrap * {padding: 0;margin: 0;}
    .wrap .info {width: 286px;height: 120px;border-radius: 5px;border-bottom: 2px solid #ccc;border-right: 1px solid #ccc;overflow: hidden;background: #fff;}
    .wrap .info:nth-child(1) {border: 0;box-shadow: 0px 1px 2px #888;}
    .info .title {padding: 5px 0 0 10px;height: 30px;background: #eee;border-bottom: 1px solid #ddd;font-size: 18px;font-weight: bold;}
    .info .close {position: absolute;top: 10px;right: 10px;color: #888;width: 17px;height: 17px;background: url('http://t1.daumcdn.net/localimg/localimages/07/mapapidoc/overlay_close.png');}
    .info .close:hover {cursor: pointer;}
    .info .body {position: relative;overflow: hidden;}
    .info .desc {position: relative;margin: 13px 0 0 90px;height: 75px;}
    .desc .ellipsis {overflow: hidden;text-overflow: ellipsis;white-space: nowrap;}
    .desc .jibun {font-size: 11px;color: #888;margin-top: -2px;}
    .info .img {position: absolute;top: 6px;left: 5px;width: 73px;height: 71px;border: 1px solid #ddd;color: #888;overflow: hidden;}
    .info:after {content: '';position: absolute;margin-left: -12px;left: 50%;bottom: 0;width: 22px;height: 12px;background: url('http://t1.daumcdn.net/localimg/localimages/07/mapapidoc/vertex_white.png')}
    .info .link {color: #5085BB;}


input.searchInput{
    margin:0;
    
    width:135px;
    border-color:#c9c9c9;
    background-color:white;
    height:32px;
    padding-left:5px;
    font-size:15px;
    display: inline-block;
}

input.searchInput:focus{
    border-color: rgb(210, 247, 239);
}



.map_wrap, .map_wrap * {margin:0;padding:0;font-family:'Malgun Gothic',dotum,'돋움',sans-serif;font-size:12px;}
.map_wrap a, .map_wrap a:hover, .map_wrap a:active{color:#000;text-decoration: none;}
.map_wrap {position:relative;width:100%;height:800px;}
#menu_wrap {position:absolute;top:0;left:0;bottom:0;width:250px;margin:70px 0 30px 10px;padding:5px;overflow-y:auto;background:rgba(255, 255, 255, 0.7);z-index: 2;font-size:12px;border-radius: 10px;}
.bg_white {background:#fff;}
#menu_wrap hr {display: block; height: 1px;border: 0; border-top: 2px solid #5F5F5F;margin:3px 0;}
#menu_wrap .option{text-align: center;}
#menu_wrap .option p {margin:10px 0;}  
#menu_wrap .option button {margin-left:5px;}
#placesList li {list-style: none;}
#placesList .item {position:relative;border-bottom:1px solid #888;overflow: hidden;cursor: pointer;min-height: 65px;}
#placesList .item span {display: block;margin-top:4px;}
#placesList .item h4, #placesList .item .info {text-overflow: ellipsis;overflow: hidden;white-space: nowrap;font-size:12px;}
#placesList .item h4{margin-bottom: 12px;padding-left: 50px;text-align: left;}
#placesList .item .info{padding:10px 0 10px 0px;}
#placesList .info .gray {color:#8a8a8a;}
#placesList .info .jibun {padding-left:26px;background:url(http://t1.daumcdn.net/localimg/localimages/07/mapapidoc/places_jibun.png) no-repeat;}
#placesList .info .tel {color:#009900;}
#placesList .item .markerbg {float:left;position:absolute;width:36px; height:37px;margin:10px 0 0 10px;background:url(http://t1.daumcdn.net/localimg/localimages/07/mapapidoc/marker_number_blue.png) no-repeat;}
#placesList .item .marker_1 {background-position: 0 -10px;}
#placesList .item .marker_2 {background-position: 0 -56px;}
#placesList .item .marker_3 {background-position: 0 -102px}
#placesList .item .marker_4 {background-position: 0 -148px;}
#placesList .item .marker_5 {background-position: 0 -194px;}
#placesList .item .marker_6 {background-position: 0 -240px;}
#placesList .item .marker_7 {background-position: 0 -286px;}
#placesList .item .marker_8 {background-position: 0 -332px;}
#placesList .item .marker_9 {background-position: 0 -378px;}
#placesList .item .marker_10 {background-position: 0 -423px;}
#placesList .item .marker_11 {background-position: 0 -470px;}
#placesList .item .marker_12 {background-position: 0 -516px;}
#placesList .item .marker_13 {background-position: 0 -562px;}
#placesList .item .marker_14 {background-position: 0 -608px;}
#placesList .item .marker_15 {background-position: 0 -654px;}
#pagination {margin:10px auto;text-align: center;}
#pagination a {display:inline-block;margin-right:10px;}
#pagination .on {font-weight: bold; cursor: default;color:#777;}
</style>