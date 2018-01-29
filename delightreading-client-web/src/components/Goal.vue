<template>
  <div >
    <h1>{{ pageTitle }}</h1>
    <div class="container">
      <form class="form-inline">
        <div class="form-group">
          <label for="minutes">My Goal is to read at least </label>
          <input type="number" id="minutes" class="form-control mx-sm-3" aria-describedby="passwordHelpInline">
          minutes
        </div>
        <div class="form-group">
          <label for="fromDate">From</label>
          <div class="input-group date  mx-sm-3" data-provide="datepicker">
            <input id="fromDate" type="text" class="form-control">
            <div class="input-group-addon">
                <span class="glyphicon glyphicon-th"></span>
            </div>
          </div>          
          <label for="toDate">To</label>
          
          <div class="input-group date  mx-sm-3" data-provide="datepicker">
            <input id="endDate" type="text" class="form-control">
            <div class="input-group-addon">
                <span class="glyphicon glyphicon-th"></span>
            </div>
          </div>
        </div>
      </form>
    </div>
  </div>
</template>

<script>
import 'bootstrap'
import 'bootstrap-datepicker'

export default {
  name: "Goal",
  data() {
    return {
      pageTitle: "Reading Goal",
      readGoal: {
      }
    };
  },
  methods: {
    clearForm: function() {
      this.readGoal = '';
    },
    submitEntry: function() {
      // alert("Congratulations! for reading " + this.readLogEntry.title + " for " + this.readLogEntry.minsRead + " mins." )
    },

    addNote: function() {
      alert("Hey!!");
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
