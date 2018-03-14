<template>
  <div >
    <h2>{{ pageTitle }}</h2>
    <div class="row">
      <div class="col-9">
        <form>
          <div class="form-row">
            <div class="form-group col-md-6">
              <input ref="referenceTitle" v-model="readLogEntry.referenceTitle" type="text" class="form-control" id="referenceTitle" placeholder="The book you read">
            </div>
            <div class="form-group col-md-3">
              <v-date-picker  v-model="readLogEntry.logTimestamp" >
                <input id="logTimestamp" type="text" class="form-control" slot-scope='props' :value='props.inputValue'  @change.native='props.updateValue($event.target.value)'>
              </v-date-picker>
            </div>
            <div class="form-group col-md-2">
              <select v-model="readLogEntry.duration" class="custom-select" id="duration" placeholder="Time read">
                <option value="5">5 mins</option>
                <option value="10">10 mins</option>
                <option value="20">20 mins</option>
                <option value="30">30 mins</option>
                <option value="40">40 mins</option>
                <option value="60">60 mins</option>
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
              <th>&nbsp;</th>
              <th>Date</th>
              <th>Mins</th>
              <th></th>
            </tr>
          </thead>
          <tbody>
            <tr v-for="readLogItem in readLog" v-bind:key="readLogItem.sid">
              <td><img :src="readLogItem.reference.thumbnailImageUrl" height="40"></td>
              <td>{{ readLogItem.reference.title }}</td>
              <td :title="readLogItem.postEmotion"><img :src="emotionToImageSrc(readLogItem.postEmotion)" class="entry-icon"></td>
              <td>{{ readLogItem.logTimestamp | formatDate}}</td>
              <td class="numeric">{{ readLogItem.duration }}</td>
              <td>
                <button type="button" class="btn btn-danger" v-on:click="deleteEntry(readLogItem.sid)" >X</button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
      <div class="col-3">
        <div class="card">
          <div class="card-header">How am I doing?</div>
          <ul class="list-group ">
            <li class="list-group-item">This month: {{ stats.month.activityDuration }} mins</li>
            <li class="list-group-item">This week: {{ stats.week.activityDuration }} mins</li>
            <li class="list-group-item">Today: {{ stats.day.activityDuration }} mins</li>
          </ul>
        </div>
      </div>
    </div>

    <!-- Modal -->
    <div class="modal fade" id="noteModal" tabindex="-1" role="dialog" aria-labelledby="exampleModalLabel" aria-hidden="true">
      <div class="modal-dialog" role="document">
        <div class="modal-content">
          <div class="modal-header">
            <h5 class="modal-title" id="exampleModalLabel">Reading Log of {{ readLogEntry.referenceTitle }}</h5>
            <button type="button" class="close" data-dismiss="modal" aria-label="Close">
              <span aria-hidden="true">&times;</span>
            </button>
          </div>
          <div class="modal-body">
            <form>
              <div class="form-group">
                <label for="feed">One question I had was</label>
                <textarea v-model="readLogEntry.feedBody" class="form-control" id="feedBody" rows="3"></textarea>
              </div>
              <div class="form-group">
                <label for="feed">Emotion</label>
                <multiselect v-model="readLogEntry.postEmotion" label="title" track-by="title" :options="emotionOptions" select-label="" style="width: 12em;" >
                  <template slot="option" slot-scope="props">
                    <img class="option__image" :src="props.option.img" height="30"  style="float: left; padding-right: 3px" >
                    <div class="option__desc"><span class="option__title">{{ props.option.title }}</span>
                    </div>
                  </template>
                </multiselect>
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

  </div>
</template>

<script>
import "bootstrap";
import "bootstrap/js/dist/util";
import "bootstrap/js/dist/modal";
import * as activityClient from "../utils/activity-client";

// Typeahead:
import Handlebars from "handlebars";
import Bloodhound from "typeahead.js";
import "../assets/typeahead.css";
import * as googlebooksUtils from "../utils/googlebooks-utils";

import Multiselect from "vue-multiselect";
import "vue-multiselect/dist/vue-multiselect.min.css";

const LOG_ENTRY_NEW = {
  sid: null,
  goalSid: null,
  referenceSourceUri: null,
  referencingLogSid: null,
  activity: "read",
  logTimestamp: new Date(),
  duration: null,
  postEmotion: null,
  situation: null,
  feedContext: null,
  feedBody: null,
  retrospective: null
}

const STATS_ENTRY_NEW = {
  activityDuration: 0
}

export default {
  name: "Home",
  components: {
    Multiselect
  },
  data() {
    return {
      pageTitle: "Log your reading",
      readLogEntry: Object.assign({}, LOG_ENTRY_NEW),
      readLog: [],
      stats: {
        month: STATS_ENTRY_NEW,
        week: STATS_ENTRY_NEW,
        day: STATS_ENTRY_NEW
      },
      feedContexts: [
        "One question I had was",
        "One word I learned",
        "How did I feel after reading?",
        "How would you change the story?"
      ],
      emotionOptions: [
        { title: "warm/touched", img: "https://emojipedia-us.s3.amazonaws.com/thumbs/144/twitter/131/smiling-face-with-smiling-eyes_1f60a.png" },
        { title: "glad", img: "https://emojipedia-us.s3.amazonaws.com/thumbs/72/twitter/131/grinning-face_1f600.png" },
        { title: "hopeful/positive", img: "https://emojipedia-us.s3.amazonaws.com/thumbs/72/twitter/131/smiling-face-with-open-mouth_1f603.png" },
        { title: "tears of joy", img: "https://emojipedia-us.s3.amazonaws.com/thumbs/72/twitter/131/face-with-tears-of-joy_1f602.png" },
        { title: "delighted", img: "https://emojipedia-us.s3.amazonaws.com/thumbs/72/twitter/131/hugging-face_1f917.png" },
        { title: "curious", img: "https://emojipedia-us.s3.amazonaws.com/thumbs/72/twitter/131/thinking-face_1f914.png" },
        { title: "amazed/impressed", img: "https://emojipedia-us.s3.amazonaws.com/thumbs/72/twitter/131/grinning-face-with-star-eyes_1f929.png" },
        { title: "sad", img: "https://emojipedia-us.s3.amazonaws.com/thumbs/72/twitter/131/crying-face_1f622.png" },
        { title: "angry", img: "https://emojipedia-us.s3.amazonaws.com/thumbs/72/twitter/131/angry-face_1f620.png" },
        { title: "mistrustful", img: "https://emojipedia-us.s3.amazonaws.com/thumbs/72/twitter/131/expressionless-face_1f611.png" },
        { title: "astonished/scared", img: "https://emojipedia-us.s3.amazonaws.com/thumbs/72/twitter/131/astonished-face_1f632.png" },
        { title: "bored", img: "https://emojipedia-us.s3.amazonaws.com/thumbs/72/twitter/131/sleepy-face_1f62a.png" }
      ]
    };
  },
  created() {
    this.loadLog();
    this.loadStats();
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
      self.assignReferenceSoruceUri(suggestion.link);
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
    emotionToImageSrc: function(emotion) {
      const item = this.emotionOptions.find(function(element) {
        return element.title === emotion;
      });
      return (item) && item.img;
    },
    assignReferenceSoruceUri: function(link) {
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
    loadStats: function() {
      activityClient
        .getStats()
        .then(response => {
          this.stats = response.data;
        })
        .catch(error => {
          alert(error);
        });
    },
    clearForm: function() {
      this.readLogEntry = Object.assign({}, LOG_ENTRY_NEW);
    },
    submitEntry: function() {
      console.log("Entering submitEntry");
      if (!this.readLogEntry.referenceTitle) {
        alert("Title not provided.");
        return;
      }
      if (!this.readLogEntry.logTimestamp) {
        alert("Date not provided.");
        return;
      }
      if (this.readLogEntry.quantity < 1) {
        alert("" + this.readLogEntry.quantity + " is not resonable.");
        return;
      }
      if (this.readLogEntry.duration < 1) {
        alert("" + this.readLogEntry.duration + " is not resonable.");
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
          this.readLogEntry.sid = response.data.sid;
          this.loadLog();
          $("#noteModal").modal();
          // this.clearForm();
        })
        .catch(error => {
          alert("Error: " + error);
        });
    },
    updateLogWithFeedback: function() {
      const readLogEntry = Object.assign({}, this.readLogEntry);
      readLogEntry.postEmotion = this.readLogEntry.postEmotion && this.readLogEntry.postEmotion.title;
      activityClient
        .updateActivityLog(readLogEntry.sid, readLogEntry)
        .then(response => {
          this.loadLog();
          this.clearForm();
          $("#noteModal").modal("hide");
        })
        .catch(error => {
          alert("Error: " + error);
        });
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
<style src="vue-multiselect/dist/vue-multiselect.min.css"></style>
<style scoped>

</style>
