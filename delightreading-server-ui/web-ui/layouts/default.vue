<template>
  <div>
    <TopNav/>
    <div class="container">
      <nuxt/>
    </div>
    <Footer/>
  </div>
</template>

<script>
import TopNav from "~/parts/shared/TopNav.vue";
import Footer from "~/parts/shared/Footer.vue";

export default {
  components: {
    TopNav, Footer
  },
  data: function() {
    return { 
      message: "Delight Reading" 
    };
  }
};
</script>

<style>
body {
  padding-top: 100px;
}

</style>
