<template>
  <div >
    <h1>{{ pageTitle }}</h1>

    <div class="container">
      <div class="card-deck">
        <div class="card" >
          <img class="card-img-top" src="~/assets/team/seojinstella.jpg" alt="Card image cap">
          <div class="card-body">
            <h5 class="card-title">Seojin Stella Ahn Kim / Happy Everything-er</h5>
            <p class="card-text">Child, learner. Discoverer of fun side of things.</p>
          </div>
          <div class="card-body">
            <a href="#" class="card-link">Card link</a>
          </div>
        </div>

        <div class="card" >
          <img class="card-img-top" src="http://www.youngsukahn.com/getsimple/theme/ys-pfolio-a/images/ysahn-jump.jpg" alt="Card image cap">
          <div class="card-body">
            <h5 class="card-title">Young-Suk Ahn Park / Delight Architect</h5>
            <p class="card-text">Father, husband, software.  Curious about the world. 
              Special interest in growing and helping other grow.
            </p>
          </div>
          <div class="card-body">
            <a href="https://www.linkedin.com/in/ysahn/" class="card-link">Profile</a>
          </div>
        </div>

        <div class="card" >
          <img class="card-img-top" src="~/assets/team/hyeyeon.jpg" alt="Card image cap">
          <div class="card-body">
            <h5 class="card-title">Hyeyeon Kim / Sustainable Delight Seeker</h5>
            <p class="card-text">Looking for a sustainable learning and enjoying.</p>
          </div>
          <div class="card-body">
            <a href="#" class="card-link">Card link</a>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
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
/* To have the cards fixed width */
.card-deck .card {
  min-width: 240px;
  max-width: 240px;
}
</style>
