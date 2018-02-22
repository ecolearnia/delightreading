<template>
  <div class="card">
    <div class="card-body">
      <form class="form-inline">
        <div class="form-group">
          <label for="quantity">My Goal is to read at least </label>
          <input type="number" v-model="goal.quantity" id="quantity" class="form-control mx-sm-3" aria-describedby="passwordHelpInline">
          minutes
        </div>
        <div class="form-group">
          <label for="startDate">From</label>
          <div class="input-group date  mx-sm-3" data-provide="datepicker">
            <input v-model="goal.startDate" id="startDate" type="text" class="form-control">
            <div class="input-group-addon">
                <span class="glyphicon glyphicon-th"></span>
            </div>
          </div>
          <label for="toDate">To</label>
          <div class="input-group date  mx-sm-3" data-provide="datepicker">
            <input v-model="goal.endDate" id="endDate" type="text" class="form-control">
            <div class="input-group-addon">
                <span class="glyphicon glyphicon-th"></span>
            </div>
          </div>
        </div>
        <button type="submit" class="btn btn-primary" v-on:click="submitEntry">Save</button>
      </form>
    </div>
  </div>
</template>

<script>
import "bootstrap";
import "bootstrap-datepicker";

import * as goalClient from "../utils/goal-client";

export default {
  name: "Goal",
  data() {
    return {
      pageTitle: "Reading Goal",
      goal: {
        quantity: undefined,
        startDate: undefined,
        endDate: undefined
      }
    };
  },
  methods: {
    clearForm: function() {
      this.goal = {};
    },
    submitEntry: function() {
      this.goal.title = "title";
      this.goal.activity = "reading";
      this.goal.quantityUnit = "minutes";
      console.log(JSON.stringify(this.goal, undefined, 2));
      goalClient
        .addGoal(this.goal)
        .then(response => {
          console.log(JSON.stringify(response.data, undefined, 2));
        })
        .catch(error => {
          alert("Error:" + error);
        });
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
