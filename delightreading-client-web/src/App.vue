<template>
  <div id="app">
    <TopNav />
    <div class="container">
      <router-view/>
    </div>
    <Footer />
  </div>
</template>

<script>
import TopNav from "./parts/shared/TopNav.vue";
import Footer from "./parts/shared/Footer.vue";

export default {
  name: "App",
  components: {
    TopNav, Footer
  },
  created: function() {
  }
};
</script>

<style>
body {
  padding-top: 100px;
}

</style>
