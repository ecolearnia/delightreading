<template>
  <div >
    <h1>{{ pageTitle }}</h1>
    <Goal />
  </div>
</template>

<script>
import "bootstrap";
import "bootstrap-datepicker";
import Goal from "./Goal.vue";

export default {
  name: "Readings",
  components: {
    Goal
  },
  data() {
    return {
      pageTitle: "Readings"
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
