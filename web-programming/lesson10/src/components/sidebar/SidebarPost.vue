<template>
  <section>
    <div class="header">
      {{ post.title }}
    </div>
    <div class="body">
      {{ post.text }}
    </div>
    <div class="footer">
      <a href="#" @click.prevent="onShowPost(post.id)">View all</a>
    </div>
  </section>
</template>

<script>
export default {
  name: "SidebarPost",
  methods: {
    onShowPost: function (postId) {
      this.$root.$emit("onShowPost", postId);
    }
  },
  props: ["post"]
}
</script>

<style scoped>
section {
  margin-bottom: 1rem;
}
</style>