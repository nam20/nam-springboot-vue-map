<template>
  <div id="app">
    <!-- <img src="./assets/logo.png"> -->
    
    
      <header>
        <h1 style="cursor:pointer;margin:0;" @click="$router.push('/')">HeeJun</h1>
      </header>
      <router-view/>
   
    
  </div>
</template>

<script>

export default {
  name: 'App'
}
</script>

<style scoped>

@import url('./assets/main.css');


#app {
  font-family: 'Avenir', Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
</style>
