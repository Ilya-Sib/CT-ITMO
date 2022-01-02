<template>
  <div class="middle">
    <Sidebar :posts="viewPosts"/>
    <main>
      <Index :posts="posts" :users="users" :commentsMap="commentsMap" v-if="page === 'Index'"/>
      <Enter v-if="page === 'Enter'"/>
      <Register v-if="page === 'Register'"/>
      <WritePost v-if="page === 'WritePost'"/>
      <EditPost v-if="page === 'EditPost'"/>
      <Users :users="users" v-if="page === 'Users'"/>
      <Post :post="posts[postId]" :users="users"
            :comments="commentsMap[postId] || []"
            :showAllComments="true" v-if="page === 'Post'"/>
    </main>
  </div>
</template>

<script>
import Sidebar from "./sidebar/Sidebar";
import Index from "./page/Index";
import Enter from "./page/Enter";
import WritePost from "./page/WritePost";
import EditPost from "./page/EditPost";
import Register from "./page/Register";
import Users from "./page/Users";
import Post from "./page/Post";

export default {
  name: "Middle",
  data: function () {
    return {
      page: "Index"
    }
  },
  components: {
    Post,
    Users,
    Register,
    WritePost,
    Enter,
    Index,
    Sidebar,
    EditPost
  },
  props: ["posts", "users", "commentsMap", "postId"],
  computed: {
    viewPosts: function () {
      return Object.values(this.posts).sort((a, b) => b.id - a.id).slice(0, 2);
    }
  }, beforeCreate() {
    this.$root.$on("onChangePage", (page) => {
      this.page = page
    })
  }
}
</script>

<style scoped>

</style>
