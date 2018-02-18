<template>
  <div >
    <h1>{{ pageTitle }}</h1>

    <div class="container">
      <div class="row">
        <div class="col-sm">
          <div class="card" >
            <img class="card-img-top" src="https://www.pinterest.es/pin/571957221409574984/" alt="Card image cap">
            <div class="card-body">
              <h5 class="card-title">Seojin Stella Ahn Kim / Happy Everything-er</h5>
              <p class="card-text">.</p>
            </div>
            <div class="card-body">
              <a href="#" class="card-link">Card link</a>
            </div>
          </div>
        </div>
        <div class="col-sm">
          <div class="card" >
            <img class="card-img-top" src="http://www.youngsukahn.com/getsimple/theme/ys-pfolio-a/images/ysahn-jump.jpg" alt="Card image cap">
            <div class="card-body">
              <h5 class="card-title">Young-Suk Ahn Park / Delight Architect</h5>
              <p class="card-text">Father of an interesting child. </p>
            </div>
            <div class="card-body">
              <a href="#" class="card-link">Card link</a>
            </div>
          </div>
        </div>
        <div class="col-sm">
          <div class="card" >
            <img class="card-img-top" src="http://www.youngsukahn.com/getsimple/theme/ys-pfolio-a/images/ysahn-jump.jpg" alt="Card image cap">
            <div class="card-body">
              <h5 class="card-title">Hyeyeon Kim / Delight Architect</h5>
              <p class="card-text">Father of an interesting child. </p>
            </div>
            <div class="card-body">
              <a href="#" class="card-link">Card link</a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import "bootstrap";

export default {
  name: "TheTeam",
  data() {
    return {
      pageTitle: "The Team"
    };
  },
  methods: {
    clearForm: function() {
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
