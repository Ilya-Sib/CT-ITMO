<template>
  <footer>
    <a href="/">Codeforces</a> &copy; 2021 by Mike Mirzayanov
    <div> Already registered {{ usersSize }} users and written {{ postsSize }} posts </div>
  </footer>
</template>

<script>
export default {
  name: "Footer",
  props: ["usersSize", "postsSize"]
}
</script>

<style scoped>

</style>