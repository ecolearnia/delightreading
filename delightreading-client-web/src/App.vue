<template>
  <div id="app">
    <TopNav />
    <router-view/>
    <footer class="site-footer">
      <p>DelightReading by Seojin & Young-Suk</p>
      <p>
        <a href="#">Back to top</a>
      </p>
    </footer>
  </div>
</template>

<script>
// import { USER_REQUEST } from "actions/user"
import TopNav from "./components/TopNav.vue"

export default {
  name: "App",
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

/*
 * Footer
 */
.site-footer {
  margin-top: 5rem;
  padding: 2.5rem 0;
  color: #999;
  text-align: center;
  background-color: #f9f9f9;
  border-top: .05rem solid #e5e5e5;
}
.site-footer p:last-child {
  margin-bottom: 0;
}
</style>
