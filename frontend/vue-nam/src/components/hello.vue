<template>
  <div>
      <h2>
          로그인 성공
      </h2>
         <my-carousel :data="data"></my-carousel>

         
      <!-- <imageModal></imageModal> -->
  </div>
</template>

<script>
import imageModal from './common/imageModal'
import VueCarousel from '@chenfengyuan/vue-carousel';

export default {
    data() {
      return {
        data: [
            '<div class="example-slide"><img src="upload/8bce8e67eea2t345t34tferdsft.gif" alt=""></div>',
          '<div class="example-slide">Slide 1</div>',
          '<div class="example-slide">Slide 2</div>',
          '<div class="example-slide">Slide 3</div>',
        ],
      };
    },
    components:{
        imageModal,
        myCarousel: {VueCarousel,props:['carouselArr']}
    }
}
</script>

<style>
.example-slide {
    align-items: center;
    background-color: #666;
    color: #999;
    display: flex;
    font-size: 1.5rem;
    justify-content: center;
    min-height: 10rem;
  }
</style>