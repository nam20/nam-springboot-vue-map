webpackJsonp([5],{XyHx:function(t,e){},jtAw:function(t,e,a){"use strict";Object.defineProperty(e,"__esModule",{value:!0});var i={props:["placeId","placeName"],data:function(){return{content:"",grade:"GOOD",boardImage:[],image:[]}},computed:{me:function(){return this.$store.state.me},links:function(){return this.$store.state.links}},created:function(){this.$store.dispatch("loadUser")},methods:{boardWrtie:function(){var t=this,e=new FormData;e.set("grade",this.grade),e.set("content",this.content),e.set("placeId",this.placeId),e.set("placeName",this.placeName),e.set("token",localStorage.getItem("token")),this.boardImage.forEach(function(t){return e.append("files",t)}),this.$axios({method:"post",url:"/board",data:e,headers:{"Content-Type":"multipart/form-data",Authorization:"Bearer "+localStorage.getItem("token")}}).then(function(e){"FAIL"==e.data.Auth?(window.alert("로그인이 필요합니다"),t.$router.push("/")):(window.alert("리뷰가 성공적으로 작성되었습니다."),t.$router.push("/board/"+t.placeId+"/"+t.placeName))}).catch(function(t){console.log(t)})},fileSelect:function(){for(var t=this,e=this.$refs.boardImage.files,a=0;a<e.length;a++){this.boardImage.push(e[a]);var i=new FileReader;i.onload=function(e){t.image.push(e.target.result)},i.readAsDataURL(e[a])}var n=new DataTransfer;this.$refs.boardImage.files=n.files},removeFile:function(t){this.boardImage.splice(t,1),this.image.splice(t,1)},addFiles:function(){this.$refs.boardImage.click()},createImage:function(t){new Image;var e=new FileReader,a=this;e.onload=function(t){a.image=t.target.result},e.readAsDataURL(t)}}},n={render:function(){var t=this,e=t.$createElement,a=t._self._c||e;return a("div",{staticStyle:{"margin-top":"130px"}},[a("section",[a("div",{staticClass:"container"},[a("strong",[t._v(t._s(t.placeName))]),t._v(" "),a("span",[t._v("\n                     의 리뷰\n                 ")]),t._v(" "),a("div",{staticClass:"reviewContent"},[a("ul",[a("li",[a("button",{class:{active:"GOOD"===t.grade},on:{click:function(e){t.grade="GOOD"}}},[t._v("맛있다!")])]),t._v(" "),a("li",[a("button",{class:{active:"SOSO"===t.grade},on:{click:function(e){t.grade="SOSO"}}},[t._v("평범하다!")])]),t._v(" "),a("li",[a("button",{class:{active:"BAD"===t.grade},on:{click:function(e){t.grade="BAD"}}},[t._v("맛없다!!!")])])]),t._v(" "),a("textarea",{directives:[{name:"model",rawName:"v-model",value:t.content,expression:"content"}],attrs:{name:"",id:"",cols:"30",rows:"10",placeholder:t.me.userName+"님 솔직한 리뷰를 남겨주세요."},domProps:{value:t.content},on:{input:function(e){e.target.composing||(t.content=e.target.value)}}})]),t._v(" "),a("div",{staticClass:"fileSelect"},[t._l(t.boardImage,function(e,i){return a("div",{key:i,staticClass:"uploadImage"},[a("div",{staticClass:"image-wrapper"},[a("div",[a("img",{staticStyle:{width:"90px",height:"90px",margin:"0 5px 0 0"},attrs:{src:t.image[i]}})]),t._v(" "),a("div",{staticClass:"image-delete-box"},[a("i",{staticClass:"fas fa-times",staticStyle:{"background-color":"black",color:"white"},on:{click:function(e){return t.removeFile(i)}}})])])])}),t._v(" "),a("input",{ref:"boardImage",attrs:{id:"files",type:"file",multiple:"multiple"},on:{change:t.fileSelect}}),t._v(" "),a("button",{staticClass:"addFile",staticStyle:{"font-size":"20px"},on:{click:t.addFiles}},[a("i",{staticClass:"fas fa-plus",staticStyle:{margin:"0 0 0 4.5px"}})])],2),t._v(" "),a("div",{staticClass:"reviewSubmit"},[a("button",{staticStyle:{margin:"0 20px"},on:{click:function(e){return t.$router.go(-1)}}},[t._v("돌아가기")]),t._v(" "),a("button",{attrs:{disabled:!t.content},on:{click:t.boardWrtie}},[t._v("등록")])])])])])},staticRenderFns:[]};var s=a("VU/8")(i,n,!1,function(t){a("XyHx")},"data-v-5498ef5f",null);e.default=s.exports}});
//# sourceMappingURL=5.469ac1851954c11d988e.js.map