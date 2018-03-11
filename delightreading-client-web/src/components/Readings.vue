<template>
  <div >
    <h1>{{ pageTitle }}</h1>
    <form>
      <div class="form-row">
        <div class="form-group col-md-6">
          <BookTitleTypeaheadWidget v-model="referencingLogEntry.referenceTitle" @selected="titleSelected" @blur.native="onTitleBlur" class="form-control" placeholder="The book you read" >
          </BookTitleTypeaheadWidget>
        </div>
        <div class="form-group col-md-3">
          <v-date-picker  v-model="referencingLogEntry.endDate" >
            <input id="endDate" type="text" class="form-control"  slot-scope='props' :value='props.inputValue' @blur="onDateBlur" @change.native='props.updateValue($event.target.value)'>
          </v-date-picker>
        </div>
        <div class="form-group col-md-1">
          <button type="button" class="btn btn-primary" v-on:click="submitEntry" >Add</button>
          <!-- <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#noteModal">Done</button> -->
        </div>
      </div>
    </form>

    <table class="table">
      <thead>
        <tr>
          <th></th>
          <th>Title</th>
          <th>startDate</th>
          <th>endDate</th>
          <th>myRating</th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="referencingLog in referencingLogs" v-bind:key="referencingLog.sid">
          <td><img :src="referencingLog.reference.thumbnailImageUrl" height="40"></td>
          <td>{{ referencingLog.reference.title }}</td>
          <td>{{ referencingLog.startDate | formatDate}}</td>
          <td>{{ referencingLog.endDate | formatDate}}</td>
          <td>{{ referencingLog.myRating }}</td>
          <td>
            <button type="button" class="btn btn-danger" v-on:click="deleteEntry(readLogItem.sid)" >X</button>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import "bootstrap";
import BookTitleTypeaheadWidget from "./widget/BookTitleTypeaheadWidget.vue";
import * as referencingLogClient from "../utils/referencinglog-client";

const LOG_ENTRY_NEW = {
  sid: null,
  accountSid: null,
  referenceSid: null,
  startDate: null,
  endDate: null,
  percentageComplete: 100,
  postEmotion: null,
  myRating: null,
  review: null,
  likeReason: null,
  synopsys: null
}

export default {
  name: "Readings",
  components: {
    BookTitleTypeaheadWidget
  },
  data() {
    return {
      pageTitle: "Readings",
      referencingLogEntry: Object.assign({}, LOG_ENTRY_NEW),
      referencingLogs: []
    };
  },
  created() {
    this.loadLog();
  },
  methods: {
    clearForm: function() {
      this.referencingLogEntry = Object.assign({}, LOG_ENTRY_NEW);
    },
    loadLog: function() {
      referencingLogClient
        .listReferencingLog()
        .then(response => {
          this.referencingLogs = response.data;
        })
        .catch(error => {
          alert(error);
        });
    },
    titleSelected: function(selection) {
      console.log('Selection: ' + JSON.stringify(selection, undefined, 2));
      this.referencingLogEntry.referenceTitle = selection.title;
      this.referencingLogEntry.referenceSourceUri = selection.link;
    },
    onDateBlur: function(event) {
      console.log(this.referencingLogEntry.referenceTitle);
    },
    onTitleBlur: function(event) {
      console.log("title: " + event.target.value);
      if (event.target.value !== this.referencingLogEntry.referenceTitle) {
        alert("Please select a book");
      }
      console.log(this.referencingLogEntry.referenceTitle);
    },
    submitEntry: function() {
      referencingLogClient
        .addReferencingLog(this.referencingLogEntry)
        .then(response => {
          this.loadLog();
          // this.clearForm();
        })
        .catch(error => {
          alert("Error: " + error);
        });
    }

  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
