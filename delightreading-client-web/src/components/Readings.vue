<template>
  <div >
    <h1>{{ pageTitle }}</h1>
    <form>
      <div class="form-row">
        <div class="form-group col-md-6">
          <BookTitleTypeaheadWidget v-model="completionLogEntry.literatureTitle" @selected="titleSelected" @blur.native="onTitleBlur" class="form-control" placeholder="The book you read" >
          </BookTitleTypeaheadWidget>
        </div>
        <div class="form-group col-md-3">
          <v-date-picker  v-model="completionLogEntry.endDate" >
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
        <tr v-for="completionLogItem in completionLog.content" v-bind:key="completionLogItem.sid">
          <td><img :src="completionLogItem.literature.thumbnailImageUrl" height="40"></td>
          <td>{{ completionLogItem.literature.title }}
            <span class v-if="completionLogItem.review">
              <a data-toggle="collapse" :href="'#reviewPane-' + completionLogItem.sid" role="button" aria-expanded="false" aria-controls="review">
                <i class="fas fa-comment-alt"></i></a>
              <div class="collapse" :id="'reviewPane-' + completionLogItem.sid">
                <div class="card card-body">
                  {{ completionLogItem.review }}
                </div>
              </div>
            </span>
          </td>
          <td>{{ completionLogItem.startDate | formatDate}}</td>
          <td>{{ completionLogItem.endDate | formatDate}}</td>
          <td>{{ displayStat(completionLogItem.activityStat) }}  {{ displayPercentage(completionLogItem.percentageComplete) }}</td>
          <td>
            <div v-if="completionLogItem.percentageComplete>=100">
              <VueStars :name="'myRating-'+completionLogItem.uid" v-model="completionLogItem.myRating" @input="(rating) => onRatingInput(completionLogItem.uid, rating)" />
            </div>
            <div v-else class="dr-smalll-info">
              Finish reading to rate
            </div>
          </td>
          <td>
            <button v-if="isDeletable(completionLogItem)" type="button" class="btn btn-danger" v-on:click="deleteEntry(completionLogItem.uid)" >X</button>
          </td>
        </tr>
      </tbody>
    </table>

    <!-- Modal {{ -->
    <div class="modal fade" id="feedbackModal" tabindex="-1" role="dialog" aria-labelledby="reviewModalLabel" aria-hidden="true">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="reviewModalLabel">{{ selectedEntry.literature.title }}</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">
            <form>
              <div class="form-group">
                <label for="feed">What did you like from this book?</label>
                <textarea v-model="selectedEntry.review" class="form-control" id="review" rows="3"></textarea>
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
import * as completionLogClient from "../utils/referencinglog-client";
import VueStars from "./widget/VueStars.vue"

const LOG_ENTRY_NEW = {
  sid: null,
  uid: null,
  accountSid: null,
  literature: {title: null},
  literatureSid: null,
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
      completionLogEntry: Object.assign({}, LOG_ENTRY_NEW),
      selectedEntry: Object.assign({}, LOG_ENTRY_NEW),
      completionLog: { content: [] }
    };
  },
  created() {
    this.loadLog();
  },
  methods: {
    clearForm: function() {
      this.completionLogEntry = Object.assign({}, LOG_ENTRY_NEW);
    },
    loadLog: function() {
      completionLogClient
        .listReferencingLog()
        .then(response => {
          // this.completionLog = Object.assign({}, response.data);
          this.completionLog = response.data;
          // console.log(JSON.stringify(this.completionLog, null, 2));
          if (this.completionLog && this.completionLog.content.length > 0) {
            for (let i = 0; i < this.completionLog.content.length; i++) {
              if (this.completionLog.content[i].myRating == null) {
                this.completionLog.content[i].myRating = 0;
              }
            }
            /*
            response.data.content.forEach((element) => {
              if (element.myRating == null) {
                element.myRating = 0;
              }
              this.completionLog.push(element);
            });
            */
          }
        })
        .catch(error => {
          alert(error);
        });
    },
    titleSelected: function(selection) {
      console.log('Selection: ' + JSON.stringify(selection, undefined, 2));
      this.completionLogEntry.literatureTitle = selection.title;
      this.completionLogEntry.literatureSourceUri = selection.link;
    },
    onDateBlur: function(event) {
      console.log(this.completionLogEntry.literatureTitle);
    },
    onTitleBlur: function(event) {
      console.log("title: " + event.target.value);
      if (event.target.value !== this.completionLogEntry.literatureTitle) {
        alert("Please select a book");
      }
      console.log(this.completionLogEntry.literatureTitle);
    },
    onRatingInput: function(completionLogUid, rating) {
      // alert("Rating of [" + completionLogUid + "] =" + rating);
      this.selectedEntry = this.completionLog.content.find((el) => { return el.uid === completionLogUid; });
      completionLogClient
        .updateReferencingLog(completionLogUid, {myRating: rating})
        .then(response => {
          console.log("OK updating " + completionLogUid);
          if (rating > 3) {
            $("#feedbackModal").modal("show");
          }
        })
        .catch(error => {
          alert(error);
        });
    },
    submitEntry: function() {
      completionLogClient
        .addReferencingLog(this.completionLogEntry)
        .then(response => {
          this.loadLog();
          // this.clearForm();
        })
        .catch(error => {
          alert("Error: " + error);
        });
    },
    updateLogWithFeedback: function() {
      if (!this.selectedEntry.uid) {
        return;
      }
      completionLogClient
        .updateReferencingLog(this.selectedEntry.uid, {review: this.selectedEntry.review})
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
      completionLogClient.deleteReferencingLog(sid).then(() => {
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
.dr-smalll-info {
  font-size: 80%;
  color: darkolivegreen;
}
</style>
