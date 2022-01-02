<template>
  <div>
    <Post v-for="post in Object.values(posts).sort((a, b) => b.id - a.id)"
          :post="post" :users="users"
          :comments="commentsMap[post.id] || []"
          :showAllComments="false" :key="post.id"/>
  </div>
</template>

<script>
import Post from "./Post";
export default {
  name: "Index",
  components: {Post},
  props: ["posts", "users", "commentsMap"]
}
</script>

<style scoped>

</style>
