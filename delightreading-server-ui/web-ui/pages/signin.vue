<template>
  <div >
    <form class="form-signin">
      <h3 class="h3 mb-3 font-weight-normal">Sign in {{siteName}} with</h3>

      <a class="btn btn-lg btn-block btn-google" :href="serverBaseUrl + '/oauth2/authorization/google'">Continue with Google</a>
      <a class="btn btn-lg btn-block btn-facebook" href="/auth/facebook">Continue with Facebook</a>
      <hr />
      <!--
      Or with sign in with email
      <label for="inputEmail" class="sr-only">Email address</label>
      <input type="email" id="inputEmail" class="form-control" placeholder="Email address" required>
      <label for="inputPassword" class="sr-only">Password</label>
      <input type="password" id="inputPassword" class="form-control" placeholder="Password" required>
      <div class="checkbox mb-3">
        <label>
          <input type="checkbox" value="remember-me"> Remember me
        </label>
      </div>
      <button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
      -->
    </form>
  </div>
</template>

<script>
export default {
  name: "SignIn",
  data() {
    return {
      siteName: "DelightReading",
      serverBaseUrl: process.env.SERVER_BASE_URL
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
.btn-google {
  color: white !important;
  background-color: #4285F4;

}
.btn-facebook {
  color: white !important;
  background-color: #3b5998;
}

.form-signin {
  width: 100%;
  max-width: 330px;
  padding: 15px;
  margin: 0 auto;
}
.form-signin .checkbox {
  font-weight: 400;
}
.form-signin .form-control {
  position: relative;
  box-sizing: border-box;
  height: auto;
  padding: 10px;
  font-size: 16px;
}
.form-signin .form-control:focus {
  z-index: 2;
}
.form-signin input[type="email"] {
  margin-bottom: -1px;
  border-bottom-right-radius: 0;
  border-bottom-left-radius: 0;
}
.form-signin input[type="password"] {
  margin-bottom: 10px;
  border-top-left-radius: 0;
  border-top-right-radius: 0;
}
</style>
