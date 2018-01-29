<template>
  <div>
    <h1>{{ pageTitle }}</h1>
    <div class="container">
      <form class="form-inline">
        <div class="form-group">
          <label for="name">name</label>
          <input type="word" id="name" class="form-control mx-sm-3" aria-describedby="passwordHelpInline">
        </div>
        <div class="form-group">
          <label for="fromDate">school</label>
          <input id="fromDate" type="text" class="form-control">
          <div class="input-group-addon">
              <span class="glyphicon glyphicon-th"></span>
          </div>
        </div>
        <div class="form-group">
          <label for="toDate">grade</label>
          <input id="endDate" type="text" class="form-control">
          <div class="input-group-addon">
              <span class="glyphicon glyphicon-th"></span>
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
