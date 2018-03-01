<template>
  <div class="card">
    <div class="card-body">
      <form class="form-inline">
        <div class="form-group">
          <label for="quantity">My Goal is to read at least </label>
          <input type="number" v-model="goal.quantity" id="quantity" class="form-control  mx-sm-2" aria-describedby="quantity" style="width:5em">
          <select id="quantityUnit" v-model="goal.quantityUnit" class="form-control">
            <option value="books">books</option>
            <option value="minutes">minutes</option>
          </select>,
        </div>
        <div class="form-group  mx-sm-2">
          <label for="startDate">from</label>
          <v-date-picker  v-model="goal.startDate" >
            <input id="startDate" type="text" class="form-control mx-sm-2 datepicker"  slot-scope='props' :value='props.inputValue'  @change.native='props.updateValue($event.target.value)'>
          </v-date-picker>
          <label for="toDate">to</label>
          <v-date-picker  v-model="goal.endDate" >
            <input id="endDate" type="text" class="form-control mx-sm-2 datepicker"  slot-scope='props' :value='props.inputValue'  @change.native='props.updateValue($event.target.value)'>
          </v-date-picker>
        </div>
        <button type="submit" class="btn btn-primary" v-on:click="submitEntry">Save</button>
      </form>
    </div>
  </div>
</template>

<script>
import "bootstrap";
import "../../assets/delightreading.css"

import * as goalClient from "../../utils/goal-client";

export default {
  name: "GoalWidget",
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
      this.goal = {
        quantityUnit: "books"
      };
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
