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
          <th>Start Date</th>
          <th>End Date</th>
          <th>Read</th>
          <th>Rating</th>
          <th></th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="referencingLog in referencingLogs" v-bind:key="referencingLog.sid">
          <td><img :src="referencingLog.reference.thumbnailImageUrl" height="40"></td>
          <td>{{ referencingLog.reference.title }}</td>
          <td>{{ referencingLog.startDate | formatDate}}</td>
          <td>{{ referencingLog.endDate | formatDate}}</td>
          <td>{{ displayStat(referencingLog.activityStat) }}  {{ displayPercentage(referencingLog.percentageComplete) }}</td>
          <td><VueStars :name="'myRating-'+referencingLog.sid" v-model="referencingLog.myRating" @input="(rating) => onRatingInput(referencingLog.sid, rating)" />
          </td>
          <td>
            <button v-if="isDeletable(referencingLog)" type="button" class="btn btn-danger" v-on:click="deleteEntry(referencingLog.sid)" >X</button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Modal {{ -->
    <div class="modal fade" id="feedbackModal" tabindex="-1" role="dialog" aria-labelledby="reviewModalLabel" aria-hidden="true">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="reviewModalLabel">{{ selectedEntry.reference.title }}</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">
            <form>
              <div class="form-group">
                <label for="feed">What did you like from this book?</label>
                <textarea v-model="selectedEntry.likeReason" class="form-control" id="likeReason" rows="3"></textarea>
              </div>
            </form>
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
import "bootstrap";
import BookTitleTypeaheadWidget from "./widget/BookTitleTypeaheadWidget.vue";
import * as referencingLogClient from "../utils/referencinglog-client";
import VueStars from "./widget/VueStars.vue"

const LOG_ENTRY_NEW = {
  sid: null,
  accountSid: null,
  reference: {title: null},
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
    BookTitleTypeaheadWidget,
    VueStars
  },
  data() {
    return {
      pageTitle: "Readings",
      referencingLogEntry: Object.assign({}, LOG_ENTRY_NEW),
      selectedEntry: Object.assign({}, LOG_ENTRY_NEW),
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
          // this.referencingLogs = Object.assign({}, response.data);
          if (response.data && response.data.length > 0) {
            response.data.forEach((element) => {
              if (element.myRating == null) {
                element.myRating = 0;
              }
              this.referencingLogs.push(element);
            });
          }
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
    onRatingInput: function(referencingLogSid, rating) {
      // alert("Rating of [" + referencingLogSid + "] =" + rating);
      this.selectedEntry = this.referencingLogs.find((el) => { return el.sid === referencingLogSid; });
      referencingLogClient
        .updateReferencingLog(referencingLogSid, {myRating: rating})
        .then(response => {
          console.log("OK updating " + referencingLogSid);
          if (rating > 3) {
            $("#feedbackModal").modal("show");
          }
        })
        .catch(error => {
          alert(error);
        });
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
    },
    updateLogWithFeedback: function() {
      if (!this.selectedEntry.sid) {
        return;
      }
      referencingLogClient
        .updateReferencingLog(this.selectedEntry.sid, {likeReason: this.selectedEntry.likeReason})
        .then(response => {
          this.clearForm();
          $("#feedbackModal").modal("hide");
        })
        .catch(error => {
          alert("Error: " + error);
        });
    },
    isDeletable: function(refencingLog) {
      if (refencingLog.activityStat && refencingLog.activityStat.totalCount) {
        return (refencingLog.activityStat.totalCount === 0)
      }
      return true;
    },
    deleteEntry: function(sid) {
      referencingLogClient.deleteReferencingLog(sid).then(() => {
        this.loadLog();
      });
    },
    displayStat(stat) {
      if (stat && stat.totalCount > 0 && stat.totalDuration) {
        return stat.totalDuration + " mins";
      }
      return ""
    },
    displayPercentage(val) {
      if ((val && (val > 0)) || val === 0) {
        return "(" + val + " %)";
      }
      return ""
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
