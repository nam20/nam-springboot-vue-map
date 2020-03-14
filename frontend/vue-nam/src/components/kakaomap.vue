<template>
  <div>
     
    <div id="container">
        <div id="rvWrapper">
           
            <div id="roadview" style="width:100%;height:100%;"></div> <!-- 로드뷰를 표시할 div 입니다 -->
            
            <div id="close" title="로드뷰닫기" @click="closeRoadview"><span class="img"></span></div>
        </div>
        <div id="mapWrapper">
            <div class="map_wrap">
            <div id="map" style="width:100%;height:100%"></div> <!-- 지도를 표시할 div 입니다 -->
           
            <div id="menu_wrap" class="bg_white" v-if="menu_search">
                <div class="option">
                    <div>
                        <form @submit="searchPlaces(); return false;">
                            키워드 : <input type="text" v-model="keyword" id="keyword" size="15" autocomplete="off"> 
                            <button type="submit">검색하기</button> 
                        </form>
                    </div>
                </div>
                <hr>
                <ul id="placesList"></ul>
                <div id="pagination"></div>
            </div>

            <div id="roadviewControl" @click="setRoadviewRoad"></div>
            </div>
        </div>
    </div>

  
    
      <router-link to="/">돌아가기</router-link>
      
        <p>{{result}}</p>
  </div>    
</template>

<script>
export default {

      data(){
          return{
            result:'',
            markers:[],
            keyword:'광명역',
            menu_search: true
          }
      }
    
     ,mounted(){
    
      this.overlayOn = false; // 지도 위에 로드뷰 오버레이가 추가된 상태를 가지고 있을 변수
       var container = document.getElementById('container'), // 지도와 로드뷰를 감싸고 있는 div 입니다
        mapWrapper = document.getElementById('mapWrapper'), // 지도를 감싸고 있는 div 입니다
        mapContainer = document.getElementById('map'), // 지도를 표시할 div 입니다 
        rvContainer = document.getElementById('roadview'); //로드뷰를 표시할 div 입니다
       

      var options = { //지도를 생성할 때 필요한 기본 옵션
        center: new kakao.maps.LatLng(33.450701, 126.570667), //지도의 중심좌표.
        level: 3 //지도의 레벨(확대, 축소 정도)
      };
       
        
        this.map = new kakao.maps.Map(mapContainer, options); //지도 생성 및 객체 리턴
       
        // 로드뷰 객체를 생성합니다 
        this.rv = new kakao.maps.Roadview(rvContainer); 

    
        // 좌표로부터 로드뷰 파노라마 ID를 가져올 로드뷰 클라이언트 객체를 생성합니다 
        this.rvClient = new kakao.maps.RoadviewClient(); 






        // // 장소 검색 객체를 생성합니다
        this.ps = new kakao.maps.services.Places();  
      

        // // 검색 결과 목록이나 마커를 클릭했을 때 장소명을 표출할 인포윈도우를 생성합니다
        this.infowindow = new kakao.maps.InfoWindow({zIndex:1});



        // 키워드로 장소를 검색합니다
         this.searchPlaces();

        


        

        // 지도를 클릭한 위치에 표출할 마커입니다
        this.marker = new kakao.maps.Marker({          
            // 지도 중심좌표에 마커를 생성합니다 
            position: this.map.getCenter() 
        }); 

        // 지도에 마커를 표시합니다
        //this.marker.setMap(this.map);

          // 로드뷰에 좌표가 바뀌었을 때 발생하는 이벤트를 등록합니다 
        kakao.maps.event.addListener(this.rv, 'position_changed', function() {

            // 현재 로드뷰의 위치 좌표를 얻어옵니다 
            var rvPosition = this.rv.getPosition();

            // 지도의 중심을 현재 로드뷰의 위치로 설정합니다
            this.map.setCenter(rvPosition);

            // 지도 위에 로드뷰 도로 오버레이가 추가된 상태이면
             if(this.overlayOn) {
                // 마커의 위치를 현재 로드뷰의 위치로 설정합니다
                this.marker.setPosition(rvPosition);
             }
        });

           
            
        // 지도를 클릭하면 마지막 파라미터로 넘어온 함수를 호출합니다
        kakao.maps.event.addListener(this.map, 'click', mouseEvent => {        
            
              // 클릭한 위도, 경도 정보를 가져옵니다 
            var latlng = mouseEvent.latLng;
            
            this.result =  latlng.getLat() +',';
            this.result += latlng.getLng() + '';

            // 지도 위에 로드뷰 도로 오버레이가 추가된 상태가 아니면 클릭이벤트를 무시합니다 
            if(!this.overlayOn) {
                return;
            }

             // 마커 위치를 클릭한 위치로 옮깁니다
            this.marker.setPosition(latlng);
            
            
            // 클락한 위치를 기준으로 로드뷰를 설정합니다
            this.toggleRoadview(latlng);

          
           

            
        });
        
        
  },
    methods:{
       

        // panTo() {
        // // 이동할 위도 경도 위치를 생성합니다 
        // var moveLatLon = new kakao.maps.LatLng(33.450701, 126.570667);
        
        // // 지도 중심을 부드럽게 이동시킵니다
        // // 만약 이동할 거리가 지도 화면보다 크면 부드러운 효과 없이 이동합니다
        // this.map.panTo(moveLatLon);            
        // },
       
        // // 전달받은 좌표(position)에 가까운 로드뷰의 파노라마 ID를 추출하여
        // // 로드뷰를 설정하는 함수입니다
        // toggleRoadview(position){
        //     this.rvClient.getNearestPanoId(position, 50, panoId => {
        //         // 파노라마 ID가 null 이면 로드뷰를 숨깁니다
        //         if (panoId === null) {
        //             this.toggleMapWrapper(true, position);
        //         } else {
        //             this.toggleMapWrapper(false, position);

        //             // panoId로 로드뷰를 설정합니다
        //             this.rv.setPanoId(panoId, position);
        //         }
        //     });
        // },
        // // 지도를 감싸고 있는 div의 크기를 조정하는 함수입니다
        // toggleMapWrapper(active, position) {
        //     var container = document.getElementById('container') // 지도와 로드뷰를 감싸고 있는 div 입니다
        //     if (active) {

        //         // 지도를 감싸고 있는 div의 너비가 100%가 되도록 class를 변경합니다 
        //         container.className = '';

        //         // 지도의 크기가 변경되었기 때문에 relayout 함수를 호출합니다
        //         this.map.relayout();
                
        //         // 지도의 너비가 변경될 때 지도중심을 입력받은 위치(position)로 설정합니다
        //         this.map.setCenter(position);
        //     } else {

        //         // 지도만 보여지고 있는 상태이면 지도의 너비가 50%가 되도록 class를 변경하여
        //         // 로드뷰가 함께 표시되게 합니다
        //         if (container.className.indexOf('view_roadview') === -1) {
        //             container.className = 'view_roadview';

        //             // 지도의 크기가 변경되었기 때문에 relayout 함수를 호출합니다
        //             this.map.relayout();
                    

        //             // 지도의 너비가 변경될 때 지도중심을 입력받은 위치(position)로 설정합니다
        //             this.map.setCenter(position);
        //         }
        //     }
        // },
        //         // 지도 위의 로드뷰 도로 오버레이를 추가,제거하는 함수입니다
        // toggleOverlay(active) {
        //     if (active) {
        //         this.overlayOn = true;

        //         // 지도 위에 로드뷰 도로 오버레이를 추가합니다
        //         this.map.addOverlayMapTypeId(kakao.maps.MapTypeId.ROADVIEW);

        //         // 지도 위에 마커를 표시합니다
        //         this.marker.setMap(this.map);

        //         // 마커의 위치를 지도 중심으로 설정합니다 
        //         //this.marker.setPosition(this.map.getCenter());

        //         // 로드뷰의 위치를 지도 중심으로 설정합니다
        //         this.toggleRoadview(this.map.getCenter());
        //     } else {
        //         this.overlayOn = false;

        //         // 지도 위의 로드뷰 도로 오버레이를 제거합니다
        //         this.map.removeOverlayMapTypeId(kakao.maps.MapTypeId.ROADVIEW);

        //         // 지도 위의 마커를 제거합니다
        //         this.marker.setMap(null);
        //     }
        // },
        // // 지도 위의 로드뷰 버튼을 눌렀을 때 호출되는 함수입니다
        // setRoadviewRoad() {
        //     var control = document.getElementById('roadviewControl');
        
        //     // 버튼이 눌린 상태가 아니면
        //     if (control.className.indexOf('active') === -1) {
        //         control.className = 'active';
        //         control.style.left = '5px';
        //         this.menu_search = false;
        //         this.keyword = '';
        //         // 로드뷰 도로 오버레이가 보이게 합니다
        //         this.toggleOverlay(true);
        //     } else {
        //         control.className = '';
        //         control.style.left = '275px';
        //         this.menu_search = true;
        //         // 로드뷰 도로 오버레이를 제거합니다
        //         this.toggleOverlay(false);
        //     }
        // },
        // // 로드뷰에서 X버튼을 눌렀을 때 로드뷰를 지도 뒤로 숨기는 함수입니다
        // closeRoadview() {
        //     var position = this.marker.getPosition();
        //     this.toggleMapWrapper(true, position);
        // },









        //검색


        // 키워드 검색을 요청하는 함수입니다
         searchPlaces() {

            if (!this.keyword.replace(/^\s+|\s+$/g, '')) {
                alert('키워드를 입력해주세요!');
                return false;
            }

            // 장소검색 객체를 통해 키워드로 장소검색을 요청합니다
            this.ps.keywordSearch(this.keyword, this.placesSearchCB); 
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
            
            for ( var i=0; i<places.length; i++ ) {

                // 마커를 생성하고 지도에 표시합니다
                var placePosition = new kakao.maps.LatLng(places[i].y, places[i].x),
                    marker = this.addMarker(placePosition, i), 
                    itemEl = this.getListItem(i, places[i]); // 검색 결과 항목 Element를 생성합니다

                // 검색된 장소 위치를 기준으로 지도 범위를 재설정하기위해
                // LatLngBounds 객체에 좌표를 추가합니다
                bounds.extend(placePosition);
                //console.log(places[i]);
                
                // 마커와 검색결과 항목에 mouseover 했을때
                // 해당 장소에 인포윈도우에 장소명을 표시합니다
                // mouseout 했을 때는 인포윈도우를 닫습니다
                ((marker, title) => {
                    kakao.maps.event.addListener(marker, 'mouseover', () => {
                        this.displayInfowindow(marker, title);
                    });

                    kakao.maps.event.addListener(marker, 'mouseout', () => {
                        this.infowindow.close();
                    });

                    itemEl.onmouseover =  () => {
                        this.displayInfowindow(marker, title);
                    };

                    itemEl.onmouseout =  () => {
                        this.infowindow.close();
                    };
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
            itemStr = '<span class="markerbg marker_' + (index+1) + '"></span>' +
                        '<div class="info" >' +
                        '   <h5>' + places.place_name + '</h5>';

            if (places.road_address_name) {
                itemStr += '    <span>' + places.road_address_name + '</span>' +
                            '   <span class="jibun gray" >' +  places.address_name  + '</span>';
            } else {
                itemStr += '    <span>' +  places.address_name  + '</span>'; 
            }
                        
            itemStr += '  <span class="tel">' + places.phone  + '</span>' +
                        '</div>';           

            el.innerHTML = itemStr;
            el.className = 'item';

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

        // 검색결과 목록의 자식 Element를 제거하는 함수입니다
        removeAllChildNods(el) {   
            while (el.hasChildNodes()) {
                el.removeChild (el.lastChild);
            }
        }










       
    }
}
</script>

<style scoped>
/* #container {overflow:hidden;height:600px;position:relative;}
#mapWrapper {width:100%;height:600px;z-index:1;}
#rvWrapper {width:50%;height:600px;top:0;right:0;position:absolute;z-index:0;}
#container.view_roadview #mapWrapper {width: 50%;}
#roadviewControl {position:absolute;top:5px;left:280px;width:42px;height:42px;z-index: 1;cursor: pointer; background: url(http://t1.daumcdn.net/localimg/localimages/07/2018/pc/common/img_search.png) 0 -450px no-repeat;}
#roadviewControl.active {background-position:0 -350px;}
#close {position: absolute;padding: 4px;top: 5px;left: 5px;cursor: pointer;background: #fff;border-radius: 4px;border: 1px solid #c8c8c8;box-shadow: 0px 1px #888;}
#close .img {display: block;background: url(http://t1.daumcdn.net/localimg/localimages/07/mapapidoc/rv_close.png) no-repeat;width: 14px;height: 14px;} */


.map_wrap, .map_wrap * {margin:0;padding:0;font-family:'Malgun Gothic',dotum,'돋움',sans-serif;font-size:12px;}
.map_wrap a, .map_wrap a:hover, .map_wrap a:active{color:#000;text-decoration: none;}
.map_wrap {position:relative;width:100%;height:800px;}
#menu_wrap {position:absolute;top:0;left:0;bottom:0;width:250px;margin:10px 0 30px 10px;padding:5px;overflow-y:auto;background:rgba(255, 255, 255, 0.7);z-index: 2;font-size:12px;border-radius: 10px;}
.bg_white {background:#fff;}
#menu_wrap hr {display: block; height: 1px;border: 0; border-top: 2px solid #5F5F5F;margin:3px 0;}
#menu_wrap .option{text-align: center;}
#menu_wrap .option p {margin:10px 0;}  
#menu_wrap .option button {margin-left:5px;}
#placesList li {list-style: none;}
#placesList .item {position:relative;border-bottom:1px solid #888;overflow: hidden;cursor: pointer;min-height: 65px;}
#placesList .item span {display: block;margin-top:4px;}
#placesList .item h4, #placesList .item .info {text-overflow: ellipsis;overflow: hidden;white-space: nowrap;}
#placesList .item .info{padding:10px 0 10px 15px;}
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