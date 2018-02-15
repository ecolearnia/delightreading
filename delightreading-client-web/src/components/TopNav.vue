<template>
  <nav class="navbar fixed-top navbar-light bg-light navbar-expand-md">
      <a class="navbar-brand" href="#"><img class="nav-pic" src="../assets/dr-logo1.png" ></a>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarCollapse" aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarCollapse">
        <ul class="navbar-nav mr-auto">
          <li class="nav-item">
            <a class="nav-link" href="#/home">Home</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#/goal">Explore</a>
          </li>
        </ul>
        <ul class="navbar-nav mt-2 mt-md-0">
          <li class="nav-item active">
            <a class="nav-link" href="#"><img class="nav-icon" src="../assets/coin-icon.png"></a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#/goal">My Readings</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#/userprofile"><img :src="myAccount.pictureUri" class="rounded-circle nav-pic" ></a>
          </li>
        </ul>

      </div>
    </nav>
</template>

<script>
import { mapGetters, mapState } from "vuex";

export default {
  name: "TopNav",
  data() {
    return {
      userAccount: {}
    };
  },
  mounted() {
  },
  computed: {
    ...mapGetters(["myAccount", "isAuthenticated", "isAccountLoaded"]),
    ...mapState({
      authLoading: state => state.auth.status === "loading"
    })
  },
  methods: {
    initSession: function(account) {},
    clearForm: function() {
      this.readGoal = "";
    },
    submitEntry: function() {
      // alert("Congratulations! for reading " + this.readLogEntry.title + " for " + this.readLogEntry.minsRead + " mins." )
    },

    addNote: function() {
      alert("Hey!!");
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
.nav-pic {
  height: 40px;
}
</style>
