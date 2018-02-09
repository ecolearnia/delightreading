<template>
  <nav class="navbar fixed-top navbar-light bg-light navbar-expand-md">
      <a class="navbar-brand" href="#"><img src="../assets/dr-logo1.png"></a>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarCollapse" aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarCollapse">
        <ul class="navbar-nav mr-auto">
          <li class="nav-item">
            <a class="nav-link" href="#/home">About</a>
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
            <a class="nav-link" href="#/user"><img :src="userAccount.pictureUri" class="rounded-circle" ></a>
          </li>
        </ul>

      </div>
    </nav>
</template>

<script>
import "bootstrap";
import * as cookieUtils from "../utils/cookie-utils";
import * as userClient from "../utils/user-client";

export default {
  name: "TopNav",
  data() {
    return {
      userAccount: {}
    };
  },
  created() {
    let accessToken = cookieUtils.getCookie("dr_token");
    if (accessToken) {
      userClient
        .getMyAccount()
        .then(response => {
          this.userAccount = response.data;
          // alert("Me: " + JSON.stringify(response, undefined, 2));
        })
        .catch(error => {
          alert("Error: " + error);
        });
      console.log("Created");
    } else {
      alert("No Cookie found");
    }
  },
  methods: {
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

</style>
