<template>
  <div class="hello">
    <h1>{{ pageTitle }}</h1>
    <div class="container">
      <form>
        <div class="form-row">
          <div class="form-group col-md-6">
            <label for="bookTitle">Book Title</label>
            <input v-model="readLogEntry.referenceTitle" type="text" class="form-control" id="bookTitle" placeholder="The book you read">
          </div>
          <div class="form-group col-md-3">
            <label for="date">Date</label>
            <div class="input-group date " data-provide="datepicker">
              <input v-model="readLogEntry.logTimestamp" id="date" type="text" class="form-control">
              <div class="input-group-addon">
            </div>
            </div>
          </div>
          <div class="form-group col-md-2">
            <label for="minutesRead">Mins. Read</label>
            <input v-model="readLogEntry.quantity" type="number" class="form-control" id="minutesRead" placeholder="Mins. you read">
          </div>
          <div class="form-group col-md-1">
            <button type="button" class="btn btn-outline-primary" v-on:click="submitEntry" >OK</button>
            <!-- <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#noteModal">Done</button> -->
          </div>
        </div>
      </form>
      <table class="table">
        <thead>
          <tr>
            <th>Title</th>
            <th>Date</th>
            <th>Mins</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="readLogItem in readLog" v-bind:key="readLogItem.sid">
            <td>{{ readLogItem.referenceTitle }}</td>
            <td>{{ readLogItem.date }}</td>
            <td>{{ readLogItem.quantity }}</td>
            <td>
              <button type="button" class="btn btn-danger" v-on:click="deleteEntry(readLogItem.sid)" >X</button>
            </td>
          </tr>
        </tbody>
      </table>
    </div>

    <!-- Modal -->
    <div class="modal fade" id="noteModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="exampleModalLabel">Reading Log of {{ readLogEntry.title }}</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">
            <form>
              <div class="form-group">
              <label for="feed">One question I had was</label>
              <textarea v-model="readLogEntry.note" class="form-control" id="note" rows="3"></textarea>
            </div>
          </form>
          </div>
          <div class="modal-footer">
            <button type="button" class="btn btn-secondary" data-dismiss="modal">Close</button>
            <button type="button" class="btn btn-primary">Save changes</button>
          </div>
        </div>
      </div>
    </div>

  </div>
</template>

<script>
import "bootstrap";
import "bootstrap/js/dist/util";
import "bootstrap/js/dist/modal";
import * as activityClient from "../utils/activity-client";

export default {
  name: "Home",
  data() {
    return {
      pageTitle: "Log your reading",
      readLogEntry: {
        goalSid: 1,
        activity: "read",
        logTimestamp: null,
        quantity: null,
        situation: null,
        feedContext: null,
        feedBody: null,
        referenceTitle: "",
        referencingLogSid: null
      },
      readLog: [],
      userPicUrl: undefined
    };
  },
  created() {
    activityClient
      .listActivityLog()
      .then(response => {
        this.readLog = response.data;
      })
      .catch(error => {
        alert(error);
      });
  },
  methods: {
    clearForm: function() {
      this.readLogEntry.referenceTitle = "";
      this.readLogEntry.logTimestamp = "";
      this.readLogEntry.quantity = null;
    },
    submitEntry: function() {
      // alert("Congratulations! for reading " + this.readLogEntry.title + " for " + this.readLogEntry.minsRead + " mins." )
      if (this.readLogEntry.referenceTitle === "") {
        return;
      }
      if (this.readLogEntry.logTimestamp === "") {
        return;
      }
      if (this.readLogEntry.quantity < 1) {
        alert("" + this.readLogEntry.quantity + " is not resonable.");
        return;
      }
      let now = new Date();
      let theDate = new Date(this.readLogEntry.logTimestamp);
      if (theDate.getTime() > now.getTime()) {
        alert("Date cannot be into the future.");
        return;
      }

      activityClient.addActivityLog(this.readLogEntry);
      this.readLog.push(Object.assign({}, this.readLogEntry));
      this.clearForm();
      $("#noteModal").modal();
    },
    addNote: function() {
      alert("Hey!!");
    },
    deleteEntry: function(sid) {
      activityClient.deleteActivityLog(sid);
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
