<template>
  <div>
    <h2>{{ pageTitle }}</h2>
    <div class="container">
      <form>
        <div class="form-row">
          <div class="form-group col-md-6">
            <label for="inputGivenName">First Name</label>
            <input type="text" v-model="account.givenName" class="form-control" id="inputGivenName" placeholder="First name">
          </div>
          <div class="form-group col-md-6">
            <label for="inputFamilyName">Last Name</label>
            <input type="text" v-model="account.familyName" class="form-control" id="inputFamilyName" placeholder="Last Name">
          </div>
        </div>
        <div class="form-row">
          <div class="form-group col-md-6">
            <label for="inputBirthdate">Birth date</label>
            <input type="text" class="form-control" id="inputBirthdate" placeholder="Birth date">
          </div>
          <div class="form-group col-md-6">
            <label for="inputGender">Gender</label>
            <select id="inputGender" v-model="profile.gender" class="form-control">
              <option>Choose...</option>
              <option value="">N/A</option>
              <option value="F">Girl</option>
              <option value="M">Boy</option>
            </select>
          </div>
        </div>
        <div class="form-row">
          <div class="form-group col-md-8">
            <label for="inputSchool">School</label>
            <input type="text" v-model="school.name" class="form-control" id="inputSchool">
          </div>
          <div class="form-group col-md-4">
            <label for="inputGrade">Grade</label>
            <select id="inputGrade" v-model="school.grade" class="form-control">
              <option>Choose...</option>
              <option value="K">K</option>
              <option value="1">1 Grade</option>
              <option value="2">2 Grade</option>
              <option value="3">3 Grade</option>
              <option value="4">4 Grade</option>
              <option value="5">5 Grade</option>
              <option value="6">6 Grade</option>
              <option value="7">7 Grade</option>
              <option value="8">8 Grade</option>
              <option value="9">9 Grade</option>
              <option value="10">10 Grade</option>
              <option value="11">11 Grade</option>
              <option value="12">12 Grade</option>
              <option value="College">College</option>
              <option value="Adult">Adult</option>
            </select>
          </div>
        </div>
        <button type="submit" class="btn btn-primary" v-on:click="submitEntry">Save</button>
      </form>
    </div>
  </div>
</template>

<script>
import "bootstrap";
import "bootstrap-datepicker";
import * as userClient from "../utils/user-client";

export default {
  name: "UserProfile",
  data() {
    return {
      pageTitle: "My Profile",
      account: {
        givenName: undefined,
        familyName: undefined,
        dateOfBirth: undefined
      },
      profile: {
        emails: undefined,
        synopsys: undefined,
        hometown: undefined,
        education: undefined,
        expertise: undefined,
        experiences: undefined,
        accomplishments: undefined,
        style: undefined,
        interests: undefined,
        languages: undefined,
        gender: undefined,
        websites: undefined
      },
      school: {
        name: undefined,
        grade: undefined
      }
    };
  },
  created() {
    // this.account = userClient.getSessionUser();
    userClient.getMyProfile().then(response => {
      this.profile = response.data;
      if (this.profile.education) {
        this.school = {
          name: this.profile.education[0].institution,
          grade: this.profile.education[0].title
        };
      }
    });
  },
  methods: {
    clearForm: function() {},
    submitEntry: function() {
      // alert("Congratulations! for reading " + this.readLogEntry.title + " for " + this.readLogEntry.minsRead + " mins." )
      this.profile.education = [
        {
          institution: this.school.name,
          title: this.school.grade
        }
      ];
      userClient
        .saveMyProfile(this.account, this.profile)
        .then(saved => {})
        .catch(error => {
          alert("Error:" + error);
        });
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
