<template>
  <header>
  <h1 style="cursor:pointer;margin:0;" @click="$router.push('/')">Brand</h1>

  
</header>
</template>

<script>
export default {

}
</script>

<style scoped>
header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  text-align: left;
  height: 70px; 
  padding: 1rem;
  color: white;
  background: teal;
  font-weight: bold;
  display: flex;
  justify-content: space-between;
  align-items: center;
  z-index: 9999;
  padding: 0 15px;
}
</style>