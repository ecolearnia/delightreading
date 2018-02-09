<template>
  <div id="app">
    <TopNav />
    <router-view/>
  </div>
</template>

<script>
import TopNav from "./components/TopNav.vue"

export default {
  name: 'App',
  components: {
    TopNav
  }

}
</script>

<style>

body  {
  padding: 100px
}

.nav-icon {
  height: 30px;
}

</style>
