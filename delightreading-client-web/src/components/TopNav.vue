<template>
  <nav class="navbar fixed-top navbar-light bg-light navbar-expand-md">
      <a class="navbar-brand" href="/"><img class="nav-pic" src="../assets/dr-logo2.png" ></a>
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
        <ul class="navbar-nav mt-2 mt-md-0"  v-if="!isAuthenticated">
          <li class="nav-item">
            <a class="nav-link" href="http://localhost:9090/auth/google">Sign in</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#">Sign up</a>
          </li>
        </ul>

        <ul class="navbar-nav mt-2 mt-md-0"  v-if="isAuthenticated">
          <li class="nav-item">
            <a class="nav-link" href="#"><img class="nav-icon" src="../assets/coin-icon.png"></a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#/goal">My Readings</a>
          </li>
          <li class="nav-item">
            <div class="dropdown">
              <a class="nav-link dropdown-toggle"  data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><img :src="myAccount.pictureUri" class="rounded-circle nav-pic" ></a>
              <div class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownMenuButton">
                <a class="dropdown-item" href="#/userprofile">Profile</a>
                <a class="dropdown-item" href="#">Friends</a>
                <div class="dropdown-divider"></div>
                <a class="dropdown-item" href="#"  v-on:click="doLogout">Sign out</a>
              </div>
            </div>
          </li>
        </ul>

      </div>
    </nav>
</template>

<script>
import { mapGetters, mapState } from "vuex";
import { AUTH_LOGOUT } from "../store/actions/auth";

export default {
  name: "TopNav",
  data() {
    return {
      userAccount: {}
    };
  },
  mounted() {},
  computed: {
    ...mapGetters(["myAccount", "isAuthenticated", "isAccountLoaded"]),
    ...mapState({
      authLoading: state => state.auth.status === "loading"
    })
  },
  methods: {
    doLogout: function() {
      let theRouter = this.$router;
      this.$store.dispatch(AUTH_LOGOUT).then(() => {
        theRouter.push("/");
      });
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
