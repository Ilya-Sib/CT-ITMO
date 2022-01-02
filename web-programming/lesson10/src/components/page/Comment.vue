<template>
  <tbody>
  <tr>
    <td class="id">{{ comment.id }}</td>
    <td class="userLogin">{{ user.login }}</td>
    <td class="text">{{ comment.text }}</td>
  </tr>
  </tbody>
</template>

<script>
export default {
  name: "Comment",
  props: ["comment", "user"]
}
</script>

<style scoped>

</style>