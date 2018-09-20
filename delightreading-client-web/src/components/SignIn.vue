<template>
  <div >
    <form class="form-signin">
      <h3 class="h3 mb-3 font-weight-normal">Sign in with</h3>

      <a class="btn btn-lg btn-block btn-google" :href="serverBaseUrl + '/oauth2/authorization/google'">Continue with Google</a>
      <a class="btn btn-lg btn-block btn-facebook" href="/auth/facebook">Continue with Facebook</a>
      <hr />
      Or with sign in with email
      <label for="inputUsername" class="sr-only">Username</label>
      <input type="text" id="inputName"  v-model="credentials.username" class="form-control" placeholder="Username" required autofocus>
      <label for="inputPassword" class="sr-only">Password</label>
      <input type="password" id="inputPassword" v-model="credentials.password" class="form-control" placeholder="Password" required>
      <div class="checkbox mb-3">
        <label>
          <input type="checkbox" value="remember-me"> Remember me
        </label>
      </div>
      <button class="btn btn-lg btn-primary btn-block" v-on:click="submitEntry" type="submit">Sign In</button>
    </form>
  </div>
</template>

<script>
import "bootstrap";

import * as userClient from "../utils/user-client";

const CREDENTIALS_NEW = {
  username: "",
  email: "",
  password: ""
}
export default {
  name: "SignIn",
  components: {
  },
  data() {
    return {
      pageTitle: "SignIn",
      credentials: Object.assign({}, CREDENTIALS_NEW)
    };
  },
  methods: {
    clearForm: function() {
    },
    submitEntry: function() {
      userClient.signIn(this.credentials).then(response => {
        if (response.data) {
          let token = response.data.token;
          alert("Login success: " + token);
          // TODO: set cooke
          document.cookie = "dr_token=" + token + "; path=/";
        }
      }).catch(error => {
        alert("Error: " + error);
      });
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
