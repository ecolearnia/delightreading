<template>
  <div class="card">
    <div class="card-body">
      <div v-if="hasActiveGoal && !isEditing">
        <div class="subtitle">
          My Goal is to read at least <span>{{ goal.quantity }}</span> <span>{{goal.quantityUnit }}</span>
          every <span>{{ goal.timePeriod }}</span>
          from <span>{{ goal.startDate }}</span> to <span>{{ goal.endDate }}</span>
        </div>
      </div>
      <div v-else>
        <form class="form-inline">
          <div class="form-group">
            <label for="quantity">My Goal is to read at least </label>
            <input type="number" min="1" v-model="goal.quantity" id="quantity" class="form-control mx-sm-2" aria-describedby="quantity" style="width:5em">
            <select id="quantityUnit" v-model="goal.quantityUnit" class="form-control">
              <option value="minutes">minutes</option>
              <option value="hours">hours</option>
              <option value="books">books</option>
            </select>
          </div>

          <div class="form-group mx-sm-2">
            <label class="mx-sm-2" for="quantity"> every </label>
            <select id="quantityUnit" v-model="goal.timePeriod" class="form-control">
              <option value="day">day</option>
              <option value="week">week</option>
              <option value="month">month</option>
            </select>
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
  </div>
</template>

<script>
import "bootstrap";
import "../../assets/delightreading.css"

import * as goalClient from "../../utils/goal-client";

const GOAL_ENTRY_NEW = {
  sid: null,
  title: "My Goal",
  activity: "reading",
  quantity: undefined,
  startDate: new Date(),
  endDate: undefined,
  timePeriod: undefined
}
export default {
  name: "GoalWidget",
  data() {
    return {
      isEditing: false,
      pageTitle: "Reading Goal",
      goals: [],
      goal: Object.assign({}, GOAL_ENTRY_NEW)
    };
  },
  created() {
    this.loadGoal();
  },
  computed: {
    hasActiveGoal: function() {
      const hasActive = (this.goal.uid !== undefined && this.goal.uid !== null);
      console.log("hasActive=" + hasActive + "; this.goal.uid=" + this.goal.uid);
      return hasActive;
    }
  },
  methods: {
    clearForm: function() {
      this.goal = Object.assign({}, GOAL_ENTRY_NEW);
    },
    loadGoal: function() {
      goalClient
        .getActiveGoal()
        .then(response => {
          this.goals = response.data;
          // For the moment only first Goal is used
          if (this.goals && this.goals.length > 0) {
            this.goal = this.goals[0];
          } else {
            this.goal = Object.assign({}, GOAL_ENTRY_NEW);
          }
        })
        .catch(error => {
          alert(error);
        });
    },
    submitEntry: function() {
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
