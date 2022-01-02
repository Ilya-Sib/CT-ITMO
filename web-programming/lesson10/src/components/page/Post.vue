<template>
  <div>
    <article>
      <div class="title"><a href="#" @click.prevent="onShowPost(post.id)">{{ post.title }}</a></div>
      <div class="information">By {{ users[post.userId].login }}</div>
      <div class="body">{{ post.text }}</div>

      <div class="footer">
        <div class="left">
          <img src="../../assets/img/voteup.png" title="Vote Up" alt="Vote Up"/>
          <span class="positive-score">+173</span>
          <img src="../../assets/img/votedown.png" title="Vote Down" alt="Vote Down"/>
        </div>
        <div class="right">
          <img src="../../assets/img/date_16x16.png" title="Publish Time" alt="Publish Time"/>
          <img src="../../assets/img/comments_16x16.png" title="Comments" alt="Comments"/>
          <a href="#">{{ comments.length }}</a>
        </div>
      </div>
    </article>
    <div v-if="showAllComments" class="comments datatable">
      <div class="caption">Comments</div>
      <table>
        <thead>
        <tr>
          <th>Id</th>
          <th>User Login</th>
          <th>Text</th>
        </tr>
        </thead>
        <Comment v-for="comment in comments"
                 :comment="comment" :user="users[comment.userId]" :key="comment.id"/>
      </table>
    </div>
  </div>
</template>

<script>
import Comment from "./Comment";
export default {
  name: "Post",
  components: {Comment},
  methods: {
    onShowPost: function (postId) {
      this.$root.$emit("onShowPost", postId);
    }
  },
  props: ["post", "users", "comments", "showAllComments"]
}
</script>

<style scoped>

</style>