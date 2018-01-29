<template>
  <div id="app">
    <nav class="navbar fixed-top navbar-light bg-light navbar-expand-md">
      <a class="navbar-brand" href="#"><img src="./assets/dr-logo1.png"></a>
      <button class="navbar-toggler" type="button" data-toggle="collapse" data-target="#navbarCollapse" aria-controls="navbarCollapse" aria-expanded="false" aria-label="Toggle navigation">
        <span class="navbar-toggler-icon"></span>
      </button>
      <div class="collapse navbar-collapse" id="navbarCollapse">
        <ul class="navbar-nav mr-auto">
          <li class="nav-item active">
            <a class="nav-link" href="#/home">Home <span class="sr-only">(current)</span></a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#/goal">Goal</a>
          </li>
        </ul>
        <ul class="navbar-nav mt-2 mt-md-0">
          <li class="nav-item active">
            <a class="nav-link" href="#"><img class="nav-icon" src="./assets/coin-icon.png"></a>
          </li>
          <li class="nav-item">
            <a class="nav-link" href="#/user">User</a>
          </li>
        </ul>

      </div>
    </nav>
    <router-view/>
  </div>
</template>

<script>
export default {
  name: 'App'
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
