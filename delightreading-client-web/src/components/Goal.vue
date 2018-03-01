<template>
  <div >
    <h1>{{ pageTitle }}</h1>
    <GoalWidget />
  </div>
</template>

<script>
import "bootstrap";
import GoalWidget from "./widget/GoalWidget.vue";

export default {
  name: "Goal",
  components: {
    GoalWidget
  },
  data() {
    return {
      pageTitle: "Goal"
    };
  },
  methods: {
    clearForm: function() {
    },
    submitEntry: function() {
      // alert("Congratulations! for reading " + this.readLogEntry.title + " for " + this.readLogEntry.minsRead + " mins." )
    }

  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
