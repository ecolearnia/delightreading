<template>
  <div>
    <ul class="navbar-nav mt-2 mt-md-0"  v-if="!isAuthenticated">
      <li class="nav-item">
        <a class="nav-link" :href="serverBaseUrl + '/app/#/signin'">Sign in</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="/app/#/signup">Sign up</a>
      </li>
    </ul>

    <ul class="navbar-nav mt-2 mt-md-0"  v-if="isAuthenticated">
      <li class="nav-item" v-if="false">
        <a class="nav-link" href="#"><img class="nav-icon" src="../assets/coin-icon.png"></a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="/app/#/goal">My Goal</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="/app/#/readings">My Readings</a>
      </li>
      <li class="nav-item">
        <div class="dropdown">
          <a class="nav-link dropdown-toggle"  data-toggle="dropdown" aria-haspopup="true" aria-expanded="false"><img :src="myAccount.pictureUri" class="rounded-circle nav-pic" ></a>
          <div class="dropdown-menu dropdown-menu-right" aria-labelledby="dropdownMenuButton">
            <span class="dropdown-item dr-item-imp" >{{myAccount.givenName}} {{myAccount.familyName}}</span>
            <a class="dropdown-item" href="#/userprofile">Profile</a>
            <a class="dropdown-item" href="#/parent">Add Children</a>
            <a class="dropdown-item" href="#/friends">Friends</a>
            <div class="dropdown-divider"></div>
            <a class="dropdown-item" href="/"  v-on:click="doLogout">Sign out</a>
          </div>
        </div>
      </li>
    </ul>
  </div>
</template>

<script>
import { mapGetters, mapState } from "vuex";
import { AUTH_LOGOUT } from "../store/actions/auth";

export default {
  name: "UserNav",
  data() {
    return {
      serverBaseUrl: process.env.SERVER_BASE_URL,
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
      // let theRouter = this.$router;
      this.$store.dispatch(AUTH_LOGOUT).then(() => {
        // theRouter.push("/");
        // window.location.replace("/");
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

.nav-icon {
  height: 30px;
}
.dr-item-imp {
  text-transform: uppercase;
  font-size: 80%;
  font-weight: bold
}
</style>
