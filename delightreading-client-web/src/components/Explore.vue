<template>
  <div >
    <h1>{{ pageTitle }}</h1>
    <form>
      <div class="form-row">
        <div class="form-group col-md-6">
          <BookTitleTypeaheadWidget v-model="searchCriteria.referenceTitle" @selected="titleSelected" @blur.native="onTitleBlur" class="form-control" placeholder="Search" >
          </BookTitleTypeaheadWidget>
        </div>
        <div class="form-group col-md-1">
          <button type="button" class="btn btn-primary" v-on:click="submitEntry" >Search</button>
        </div>
      </div>
    </form>

    <div class="card-deck">
      <div class="card" v-for="popItem in popular"  v-bind:key="popItem.sid" >
        <img class="card-img-top" :src="popItem.imageUrl" alt="Card image cap">
        <div class="card-body">
          <p class="card-text" v-html="popItem.description"></p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import BookTitleTypeaheadWidget from "./widget/BookTitleTypeaheadWidget.vue";
import * as referenceClient from "../utils/reference-client";

export default {
  name: "Explore",
  components: {
    BookTitleTypeaheadWidget
  },
  data() {
    return {
      pageTitle: "Explore",
      popular: [],
      searchCriteria: {
        referenceTitle: undefined
      }
    };
  },
  created() {
    this.loadBooks();
  },
  methods: {
    loadBooks: function() {
      referenceClient
        .listReferences()
        .then(response => {
          this.popular = response.data;
        })
        .catch(error => {
          alert(error);
        });
    },
    titleSelected: function(selection) {
      console.log('Selection: ' + JSON.stringify(selection, undefined, 2));
      this.searchCriteria.referenceTitle = selection.title;
      this.searchCriteria.referenceSourceUri = selection.link;
    },
    submitEntry: function() {
    }
  }
};
</script>

<!-- Add "scoped" attribute to limit CSS to this component only -->
<style scoped>
/* To have the cards fixed width */
.card-deck .card {
  min-width: 180px;
  max-width: 180px;
}
</style>
