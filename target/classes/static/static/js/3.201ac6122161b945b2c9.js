webpackJsonp([3],{Ybdb:function(t,a){},s3jv:function(t,a,e){"use strict";Object.defineProperty(a,"__esModule",{value:!0});var s={components:{boardContent:e("as43").a},data:function(){return{boards:[],searchResult:"",page:0,totalPages:0,limit:9,hasNextPage:!0}},created:function(){this.loadBoards()},watch:{boards:function(){this.boards.length||(this.searchResult="리뷰가 없습니다")}},methods:{loadBoards:function(){var t=this;this.$axios({method:"get",url:"/board",params:{page:this.page,limit:this.limit}}).then(function(a){if(a.data){var e=a.data;t.boards=t.boards.concat(e.boardList),t.boards.forEach(function(t){switch(t.grade){case"GOOD":t.grade="맛있다!";break;case"SOSO":t.grade="평범하다";break;case"BAD":t.grade="맛없다!!!"}}),t.page+=1,t.totalPages=e.totalPages,t.hasNextPage=e.hasNextPage}}).catch(function(t){console.log(t)})},slice:function(){this.boards=this.boards.slice(0,this.limit),this.hasNextPage=!0,this.page=1}}},i={render:function(){var t=this,a=t.$createElement,e=t._self._c||a;return e("div",{staticStyle:{"margin-top":"200px"}},[e("div",{attrs:{id:"wrapper"}},[e("div",{attrs:{id:"main"}},[e("h1",[t._v("모든 리뷰")]),t._v(" "),e("div",{staticClass:"inner"},[e("section",{staticClass:"tiles"},t._l(t.boards,function(t){return e("board-content",{key:t,attrs:{board:t}})}),1),t._v(" "),e("br"),t._v(" "),e("section",[t.totalPages>1?e("div",[t.hasNextPage?e("button",{on:{click:t.loadBoards}},[t._v("더보기")]):e("button",{on:{click:t.slice}},[t._v("접기")])]):t._e(),t._v(" "),e("H1",{staticStyle:{margin:"1em 0 1em"}},[t._v(t._s(t.searchResult))])],1)])]),t._v(" "),e("footer",{attrs:{id:"footer"}})])])},staticRenderFns:[]};var o=e("VU/8")(s,i,!1,function(t){e("Ybdb")},"data-v-67d0fee0",null);a.default=o.exports}});
//# sourceMappingURL=3.201ac6122161b945b2c9.js.map