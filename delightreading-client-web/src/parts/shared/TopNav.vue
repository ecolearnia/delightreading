<template>
  <nav class="navbar fixed-top navbar-light bg-light navbar-expand-md">
      <a class="navbar-brand" href="/"><img class="nav-pic" src="../../assets/dr-logo.png" ></a>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarCollapse" aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarCollapse">
        <ul class="navbar-nav mr-auto">
          <li class="nav-item">
            <a class="nav-link" href="/#/home">Home</a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="/#/explore">Explore</a>
          </li>
        </ul>

        <UserNav />

      </div>
    </nav>
</template>

<script>
/**
 * This commponent is shared with server-side rendered in delightreadingb-backend/web-ui/components
 */
import UserNav from "../UserNav.vue";

export default {
  name: "TopNav",
  components: {
    UserNav
  },
  data() {
    return {
      serverBaseUrl: process.env.SERVER_BASE_URL,
      userAccount: {}
    };
  },
  mounted() {},

  methods: {
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
</style>
