<template>
  <div>
    <article>
      <div class="title"><a href="#" @click.prevent="onShowPost(post)">{{ post.title }}</a></div>
      <div class="information">By {{ post.user.login }}</div>
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
          <a href="#">{{ post.comments.length }}</a>
        </div>
      </div>
    </article>

    <div v-if="showAllComments" class="comments datatable">
      <div class="form" style="margin-bottom: 2rem">
        <div class="header">Write Comment</div>
        <div class="body">
          <form @submit.prevent="onWriteComment">
            <div class="field">
              <div class="name">
                <label for="text">Text</label>
              </div>
              <div class="value">
                <textarea id="text" name="text" v-model="text"></textarea>
              </div>
              <div class="field error">{{ error }}</div>
            </div>
            <div class="button-field">
              <input type="submit" value="Write">
            </div>
          </form>
        </div>
      </div>

      <div class="caption">Comments</div>
      <table>
        <thead>
        <tr>
          <th>Id</th>
          <th>User Login</th>
          <th>Text</th>
        </tr>
        </thead>
        <Comment v-for="comment in post.comments"
                 :comment="comment" :user="comment.user" :key="comment.id"/>
      </table>
    </div>
  </div>
</template>

<script>
import Comment from "./Comment";

export default {
  name: "Post",
  components: {Comment},
  data: function () {
    return {
      text: "",
      error: ""
    }
  },
  methods: {
    onShowPost: function (post) {
      this.$root.$emit("onShowPost", post);
    },
    onWriteComment: function () {
      this.$root.$emit("onWriteComment", this.text);
      this.text = "";
    }
  },
  beforeCreate() {
    this.$root.$on("onWriteCommentError", (error) => this.error = error);
  },
  props: ["post", "showAllComments"]
}
</script>

<style scoped>

</style>