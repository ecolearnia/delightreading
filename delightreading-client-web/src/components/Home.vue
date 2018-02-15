<template>
  <div class="hello">
    <h2>{{ pageTitle }}</h2>
    <div class="container">
      <form>
        <div class="form-row">
          <div class="form-group col-md-6">
            <input ref="referenceTitle" v-model="readLogEntry.referenceTitle" type="text" class="form-control" id="referenceTitle" placeholder="The book you read">
          </div>
          <div class="form-group col-md-3">
            <div class="input-group date " data-provide="datepicker">
              <input v-model="readLogEntry.logTimestamp" id="logTimestamp" type="text" class="form-control" placeholder="Date">
              <div class="input-group-addon">
            </div>
            </div>
          </div>
          <div class="form-group col-md-2">
            <select v-model="readLogEntry.quantity" class="custom-select" id="readLogEntry.quantity">
              <option >Mins.</option>
              <option value="5">5 mins</option>
              <option value="10">10</option>
              <option value="20">20</option>
              <option value="30">30</option>
              <option value="40">40</option>
              <option value="60">60</option>
            </select>
          </div>
          <div class="form-group col-md-1">
            <button type="button" class="btn btn-primary" v-on:click="submitEntry" >OK</button>
            <!-- <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#noteModal">Done</button> -->
          </div>
        </div>
      </form>
      <table class="table">
        <thead>
          <tr>
            <th></th>
            <th>Title</th>
            <th>Date</th>
            <th>Mins</th>
            <th></th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="readLogItem in readLog" v-bind:key="readLogItem.sid">
            <td><img :src="readLogItem.reference.thumbnailImageUrl" height="30"></td>
            <td>{{ readLogItem.reference.title }}</td>
            <td>{{ readLogItem.logTimestamp }}</td>
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

import Handlebars from "handlebars";
import Bloodhound from "typeahead.js";
import "../assets/typeahead.css";
import * as googlebooksUtils from "../utils/googlebooks-utils";

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
        referenceSourceUri: null,
        referencingLogSid: null
      },
      readLog: [],
      userPicUrl: undefined
    };
  },
  created() {
    this.loadLog();
  },
  mounted() {
    // @see: https://forum.vuejs.org/t/typeahead-js-with-vue/22231/2
    let refTitle = $(this.$refs.referenceTitle);

    var queryUrl = "https://www.googleapis.com/books/v1/volumes?q=%QUERY";

    var referenceTitlesSource = new Bloodhound({
      datumTokenizer: Bloodhound.tokenizers.obj.whitespace("value"),
      queryTokenizer: Bloodhound.tokenizers.whitespace,
      remote: {
        url: queryUrl,
        wildcard: "%QUERY",
        transform: googlebooksUtils.transform
      }
    });

    const self = this;
    refTitle.bind('typeahead:select', function(ev, suggestion) {
      console.log('Selection: ' + JSON.stringify(suggestion, undefined, 2));
      self.updateReferenceSoruceUri(suggestion.link);
    });

    refTitle.typeahead(
      { hint: true, highlight: true, minLength: 1 },
      {
        name: "referenceTitles",
        display: "title",
        source: referenceTitlesSource,
        templates: {
          empty: [
            '<div class="empty-message">',
            "unable to find any Best Picture winners that match the current query",
            "</div>"
          ].join("\n"),
          suggestion: Handlebars.compile(
            "<div><img src='{{imageLink}}' height='40px'> {{title}}<br>by {{author}}</div>"
          )
        }
      }
    );
    console.log(refTitle);
  },
  methods: {
    updateReferenceSoruceUri: function(link) {
      this.readLogEntry.referenceSourceUri = link;
    },
    loadLog: function() {
      activityClient
        .listActivityLog()
        .then(response => {
          this.readLog = response.data;
        })
        .catch(error => {
          alert(error);
        });
    },
    clearForm: function() {
      this.readLogEntry.referenceTitle = "";
      this.readLogEntry.logTimestamp = "";
      this.readLogEntry.quantity = null;
    },
    submitEntry: function() {
      console.log("Entering submitEntry");
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

      activityClient
        .addActivityLog(this.readLogEntry)
        .then(response => {
          // this.readLog.push(Object.assign({}, this.readLogEntry));
          this.loadLog();
          this.clearForm();
          $("#noteModal").modal();
        })
        .catch(error => {
          alert("Error: " + error);
        });
    },
    addNote: function() {
      alert("Hey!!");
    },
    deleteEntry: function(sid) {
      activityClient.deleteActivityLog(sid).then(() => {
        this.loadLog();
      });
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
