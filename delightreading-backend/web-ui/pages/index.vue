<template>
  <div>
    <div class="jumbotron jumbo-heading">
      <div class="container">
        <h1 class="display-3 ">{{title}} </h1>
        <p>Be delighted reading and sharing.</p>
        <p><a class="btn btn-primary btn-lg" href="/signup" role="button">Sign up &raquo;</a></p>
      </div>
    </div>

  </div>
</template>

<script>
export default {
  name: "index",
  data() {
    return {
        title: "Experience the Happiness of Reading",
        siteName: "Delight Reading"
    }
  }
};
</script>


<style scoped>
.jumbo-heading {
  background-size: cover;
  background-image: url(~/assets/front-girlbook.jpg);
}

.jumbo-heading .container {
  color: white;
}
</style>
