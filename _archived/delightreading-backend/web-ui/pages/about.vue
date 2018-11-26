<template>
  <div >
    <h1>{{ pageTitle }}</h1>

    <h3>Our Story</h3>
    <p>
      We started as a small team of dad and daughter.
    </p>

    <h3>How you can use DelightReading</h3>

    <h3>Message from the team</h3>

    <h3>Get Involved</h3>
    <a href="/app/#/tickets">Qustions, Comments, Suggestions</a>
    <a href="">Development</a>

  </div>
</template>

<script>
export default {
  name: "About",
  data() {
    return {
      pageTitle: "About Us"
    };
  },
  methods: {
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
</style>
