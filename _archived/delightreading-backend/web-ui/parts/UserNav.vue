<template>
  <div>
    <ul class="navbar-nav mt-2 mt-md-0" >
      <li class="nav-item">
        <a class="nav-link" href="/signin">Sign in</a>
      </li>
      <li class="nav-item">
        <a class="nav-link" href="/signup">Sign up</a>
      </li>
    </ul>

  </div>
</template>

<script>
export default {
  name: "UserNav",
  data() {
    return {
      serverBaseUrl: process.env.SERVER_BASE_URL,
      siteName: "DelightReading"
    };
  },
  methods: {
    showSigninForm: function() {
      $("#signinModal").modal();
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
