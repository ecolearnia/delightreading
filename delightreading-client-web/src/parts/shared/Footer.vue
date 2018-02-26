<template>
    <footer class="site-footer">
        <h3>DelightReading by Seojin & Young-Suk</h3>
        <p>
        <a href="/about">About</a> |
        <a href="/team">The Team</a>
        </p>
        <p class="mt-5 mb-3 text-muted">&copy; {{siteName}} 2017-2018</p>
    </footer>
</template>

<script>
/**
 * This commponent is shared with server-side rendered in delightreadingb-backend/web-ui/components
 */
export default {
  data() {
    return {
      siteName: "DelightReading"
    };
  }
};
</script>
<style>
/*
 * Footer
 */
.site-footer {
  margin-top: 5rem;
  padding: 2.5rem 0;
  color: #999;
  text-align: center;
  background-color: #f9f9f9;
  border-top: 0.05rem solid #e5e5e5;
}
.site-footer p:last-child {
  margin-bottom: 0;
}
</style>
