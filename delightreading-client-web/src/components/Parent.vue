<template>
  <div>
    <h2>{{ pageTitle }}</h2>
    <div class="row">
      Manage your children
    </div>

    <div class="card-deck">
      <div class="card" v-for="member in members"  v-bind:key="member.sid" >
        <img class="card-img-top" :src="member.account.pictureUri" alt="Card image cap">
        <div class="card-body">
          <p class="card-text" v-html="member.account.username"></p>
        </div>
      </div>
      <div class="card" >
        <div class="card-body">
          <p class="card-text centered">
            <button class="btn btn-primary"  v-on:click="childAccountForm">Add child</button>
          </p>
        </div>
      </div>
    </div>

    <!-- Modal New Child {{ -->
    <div class="modal fade" id="childFormModal" tabindex="-1" role="dialog" aria-labelledby="addChild" aria-hidden="true">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="exampleModalLabel">Create you child account</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">
            <form>
              <div class="form-group row">
                <label for="username" class="col-sm-4 col-form-label">Child's username</label>
                <div class="col-sm-8">
                  <input ref="username" v-model="newMemberAccount.username" type="text" class="form-control" id="username" placeholder="username">
                </div>
              </div>
              <div class="form-group row">
                <label for="password"  class="col-sm-4 col-form-label">Child's password</label>
                <div class="col-sm-8">
                  <input ref="password" v-model="newMemberAccount.password" type="password" class="form-control" id="password" >
                </div>
              </div>
              <div class="form-group row">
                <label for="inputGrade" class="col-sm-4 col-form-label">Grade</label>
                  <div class="col-sm-8">
                  <select id="inputGrade" v-model="school.grade" class="form-control">
                    <option value="pre-k">Pre-K</option>
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
              <div class="form-group row">
                <label for="inputGender"  class="col-sm-4 col-form-label">Gender</label>
                <div class="col-sm-8">
                  <select id="inputGender" v-model="newMemberAccount.profile.gender" class="form-control">
                    <option value="">N/A</option>
                    <option value="m">Boy</option>
                    <option value="f">Girl</option>
                    <option value="m">Man</option>
                    <option value="f">Woman</option>
                  </select>
                </div>
              </div>
            </form>
          </div>
          <div class="modal-footer">
            <button type="button"  v-on:click="addChild()" class="btn btn-primary">Add</button>
          </div>
        </div>
      </div>
    </div>
    <!-- }} Modal -->
  </div>
</template>

<script>
import "bootstrap";
import { mapGetters, mapState } from "vuex";

export default {
  name: "Parent",
  data() {
    return {
      pageTitle: "Your children",
      newMemberAccount: {
        username: undefined,
        password: undefined,
        profile: {
          gender: undefined
        }
      },
      school: {
        name: undefined,
        grade: undefined
      },
      members: [
        {
          sid: 1,
          status: "active",
          account: {
            username: "mountainbook",
            nickname: "SL",
            givenName: "Stela Luna",
            pictureUri: "https://lh4.googleusercontent.com/-ujilfL1okNw/AAAAAAAAAAI/AAAAAAAAFf8/wH26BvJ409I/photo.jpg?sz=50"
          }
        },
        {
          sid: 2,
          status: "active",
          account: {
            username: "mountainbook",
            nickname: "SL",
            givenName: "Stela Luna",
            pictureUri: "https://lh4.googleusercontent.com/-ujilfL1okNw/AAAAAAAAAAI/AAAAAAAAFf8/wH26BvJ409I/photo.jpg?sz=50"
          }
        }
      ]
    };
  },
  created() {
    // this.account = userClient.getSessionUser();
  },
  mounted() {
    this.account = Object.assign({}, this.myAccount);
  },
  computed: {
    ...mapGetters(["myAccount", "isAuthenticated", "isAccountLoaded"]),
    ...mapState({
      authLoading: state => state.auth.status === "loading"
    })
  },
  methods: {
    childAccountForm: function() {
      $("#childFormModal").modal();
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
/* To have the cards fixed width */
.card-deck .card {
  min-width: 180px;
  max-width: 180px;
}
.centered {
  text-align: center;
}
</style>
