<template>
  <div class="hello">
    <h1>{{ pageTitle }}</h1>
    <div class="container">
      <form>
        <div class="form-row">
          <div class="form-group col-md-6">
            <label for="bookTitle">Book Title</label>
            <input v-model="readLogEntry.title" type="text" class="form-control" id="bookTitle" placeholder="The book you read">
          </div>
          <div class="form-group col-md-3">
            <label for="date">Date</label>
            <input v-model="readLogEntry.date" type="text" class="form-control" id="date" placeholder="The date you read">
          </div>
          <div class="form-group col-md-2">
            <label for="minutesRead">Mins. Read</label>
            <input v-model="readLogEntry.minsRead" type="number" class="form-control" id="minutesRead" placeholder="Mins. you read">
          </div>
          <div class="form-group col-md-1">
            <button type="button" class="btn btn-outline-primary" v-on:click="submitEntry" >OK</button>
          </div>
        </div>
      </form>

      <table class="table">
        <thead>
          <tr>
            <th>Title</th>
            <th>Date</th>
            <th>Mins</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="readLogItem in readLog" v-bind:key="readLogItem.id">
            <td>{{ readLogItem.title }}</td>
            <td>{{ readLogItem.date }}</td>
            <td>{{ readLogItem.minsRead }}</td>
          </tr>
        </tbody>
      </table>
    </div>
  </div>
</template>

<script>
export default {
  name: 'Home',
  data () {
    return {
      pageTitle: 'Log your readinng',
      readLogEntry: {
        title: null,
        date: null,
        minsRead: null
      },
      readLog: []
    }
  },
  methods: {
    submitEntry: function () {
      // alert("Congratulations! for reading " + this.readLogEntry.title + " for " + this.readLogEntry.minsRead + " mins." )

      this.readLog.push(Object.assign({}, this.readLogEntry))
      this.readLogEntry.title = ''
      this.readLogEntry.date = ''
      this.readLogEntry.minsRead = ''
    }
  }
}
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>

</style>
