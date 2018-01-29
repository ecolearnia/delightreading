<template>
  <div class="hello">
    <h1>{{ msg }}</h1>
    <h2>by Seojin Stella and Young-Suk</h2>
    <a type="button" class="btn btn-outline-primary btn-lg" href="#/home">home</a>
  </div>
</template>

<script>
export default {
  name: "HelloWorld",
  data() {
    return {
      msg: "Welcome to delight reading"
    };
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
