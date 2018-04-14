<template>
  <div >
    <h1>{{ pageTitle }}</h1>
    <form>
      <div class="form-row">
        <div class="form-group col-md-6">
          <label for="inputCategory">Category</label>
          <select id="inputCategory" v-model="ticketEntry.category" class="form-control">
            <option value="comment">Comment</option>
            <option value="question">Question</option>
            <option value="feature">Improvement</option>
            <option value="defect">Defect</option>
          </select>
        </div>
        <div class="form-group col-md-6">
          <label for="inputVisibility">Who can see</label>
          <select id="inputVisibility" v-model="ticketEntry.visibility" class="form-control">
            <option value="public">Everyone</option>
            <option value="private">Only Admin</option>
          </select>
        </div>
      </div>
      <div class="form-row">
        <div class="form-group col-md-12">
          <label for="inputNickname">Description</label>
          <textarea  v-model="ticketEntry.description" class="form-control" aria-label="With textarea" placeholder="Description"></textarea>
        </div>
      </div>
      <div class="form-row">
        <div class="form-group col-md-12">
          <button type="button" class="btn btn-primary" v-on:click="submitEntry" >Add</button>
        </div>
      </div>
    </form>

    <table class="table">
      <thead>
        <tr>
          <th>Category</th>
          <th>Date</th>
          <th>Description</th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="ticket in tickets" v-bind:key="ticket.sid">
          <td>{{ ticket.category}}</td>
          <td>{{ ticket.createdAt | formatDate}}</td>
          <td>{{ ticket.description}}</td>
          <td>
            <button v-if="isDeletable(ticket)" type="button" class="btn btn-danger" v-on:click="deleteEntry(ticket.sid)" >X</button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Modal {{ -->
    <div class="modal fade" id="feedbackModal" tabindex="-1" role="dialog" aria-labelledby="reviewModalLabel" aria-hidden="true">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="reviewModalLabel">{{ selectedEntry.name }}</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">
            <div class="form-group">
              <label for="description">Description</label>
              <textarea v-model="selectedEntry.descriptionn" class="form-control" rows="3"></textarea>
            </div>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            <button type="button"  v-on:click="updateLogWithFeedback()" class="btn btn-primary">Save</button>
          </div>
        </div>
      </div>
    </div>
    <!-- }} Modal -->
  </div>
</template>

<script>
import { mapGetters, mapState } from "vuex";
import * as ticketClient from "../utils/ticket-client";

const TICKET_ENTRY_NEW = {
  sid: null,
  category: "comment",
  name: null,
  description: null,
  priority: null,
  tags: null,
  visibility: "public",
  closeDate: null
}

export default {
  name: "Tickets",
  components: {
  },
  data() {
    return {
      pageTitle: "Tickets",
      ticketEntry: Object.assign({}, TICKET_ENTRY_NEW),
      selectedEntry: Object.assign({}, TICKET_ENTRY_NEW),
      tickets: []
    };
  },
  created() {
    this.loadTickets();
  },
  computed: {
    ...mapGetters(["myAccount", "isAuthenticated", "isAccountLoaded"]),
    ...mapState({
      authLoading: state => state.auth.status === "loading"
    })
  },
  methods: {
    clearForm: function() {
      this.ticketEntry = Object.assign({}, TICKET_ENTRY_NEW);
    },
    loadTickets: function() {
      ticketClient
        .listTickets()
        .then(response => {
          this.tickets = response.data;
        })
        .catch(error => {
          alert(error);
        });
    },
    submitEntry: function() {
      ticketClient
        .addTicket(this.ticketEntry)
        .then(response => {
          this.loadTickets();
          this.clearForm();
        })
        .catch(error => {
          alert("Error: " + error);
        });
    },
    isDeletable: function(ticket) {
      // debugger;
      // TODO: this.myAccount is undefined, fix it.
      if (this.myAccount) {
        return (parseInt(ticket.createdBy) === this.myAccount.sid) ||
          (this.myAccount.role && this.myAccount.role.includes("host"))
      }
      return false;
    },
    deleteEntry: function(sid) {
      ticketClient.deleteTicket(sid).then(() => {
        this.loadLog();
      });
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
