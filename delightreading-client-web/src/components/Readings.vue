<template>
  <div >
    <h1>{{ pageTitle }}</h1>
    <form>
      <div class="form-row">
        <div class="form-group col-md-6">
          <BookTitleTypeaheadWidget v-model="referencingLogEntry.referenceTitle" @selected="titleSelected" class="form-control" placeholder="The book you read" >
          </BookTitleTypeaheadWidget>
        </div>
        <div class="form-group col-md-3">
          <v-date-picker  v-model="referencingLogEntry.logTimestamp" >
            <input id="logTimestamp" type="text" class="form-control "  slot-scope='props' :value='props.inputValue'  @change.native='props.updateValue($event.target.value)'>
          </v-date-picker>
        </div>
        <div class="form-group col-md-1">
          <button type="button" class="btn btn-primary" v-on:click="submitEntry" >OK</button>
          <!-- <button type="button" class="btn btn-primary" data-toggle="modal" data-target="#noteModal">Done</button> -->
        </div>
      </div>
    </form>
  </div>
</template>

<script>
import "bootstrap";
import BookTitleTypeaheadWidget from "./widget/BookTitleTypeaheadWidget.vue";

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
      referencingLog: []
    };
  },
  methods: {
    clearForm: function() {
    },
    titleSelected: function(selection) {
      console.log('Selection: ' + JSON.stringify(selection, undefined, 2));
      this.readLogEntry.referenceSourceUri = selection.link;
    },
    submitEntry: function() {
      // alert("Congratulations! for reading " + this.referencingLogEntry.title + " for " + this.referencingLogEntry.minsRead + " mins." )
    }

  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
