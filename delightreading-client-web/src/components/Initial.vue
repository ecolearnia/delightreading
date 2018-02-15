<template>
  <main role="main">
    <div class="jumbotron">
      <div class="heading">
        <h1 class="heading-title">Delight Reading</h1>
      </div>
    </div>
  </main>
</template>

<script>
export default {
  name: "DelightReasing",
  data() {
    return {
      msg: "Welcome to delight reading"
    };
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.jumbotron {
  background-image: url("/static/front_image2.jpg") ;
  background-size: cover;
  height: 300px;
  padding-top: 2em;
}
.heading {
  width: 60%;
  margin: 2em auto;
}
.heading-title {
  color: white;
  text-shadow: 2px 2px #333;
}
</style>
