<template>
  <input ref="referenceTitle"
    v-bind:value="value"
    @input="value => { referenceTitle = value }"
    type="text" :class="cssClass" :placeholder="placeholder">
</template>

<script>
// Typeahead:
import Handlebars from "handlebars";
import Bloodhound from "typeahead.js";
import "../../assets/typeahead.css";
import * as googlebooksUtils from "../../utils/googlebooks-utils";

export default {
  name: "BookTitleTypeaheadWidget",
  props: {
    value: String,
    class: String,
    placeholder: {
      type: String,
      default: "Book title"
    }
  },
  data() {
    return {
      referenceTitle: ""
    };
  },
  computed: {
    cssClass: function() {
      var classArray = this.class ? this.class.split(' ') : [];

      if (this.value) {
        classArray.push('active');
      }

      return classArray;
    }
  },
  created() {
  },
  mounted() {
    // @see: https://forum.vuejs.org/t/typeahead-js-with-vue/22231/2
    let refTitle = $(this.$refs.referenceTitle);

    var queryUrl = "https://www.googleapis.com/books/v1/volumes?q=%QUERY";

    var referenceTitlesSource = new Bloodhound({
      datumTokenizer: Bloodhound.tokenizers.obj.whitespace("value"),
      queryTokenizer: Bloodhound.tokenizers.whitespace,
      remote: {
        url: queryUrl,
        wildcard: "%QUERY",
        transform: googlebooksUtils.transform
      }
    });

    const self = this;
    /**
     * suggestion = {
      title: string
      author: string
      isbn: string
      link: string
      imageLink: string
      serchInfo: string
      }
     */
    refTitle.bind('typeahead:select', function(ev, suggestion) {
      console.log('Selection: ' + JSON.stringify(suggestion, undefined, 2));
      self.$emit('BookTitleTypeaheadWidget::selected', suggestion);
    });

    refTitle.typeahead(
      { hint: true, highlight: true, minLength: 1 },
      {
        name: "referenceTitles",
        display: "title",
        source: referenceTitlesSource,
        templates: {
          empty: [
            '<div class="empty-message">',
            "unable to find any Best Picture winners that match the current query",
            "</div>"
          ].join("\n"),
          suggestion: Handlebars.compile(
            "<div><img src='{{imageLink}}' height='40px'> {{title}}<br>by {{author}}</div>"
          )
        }
      }
    );
  },
  methods: {
  }
};
</script>

<style scoped>

</style>
