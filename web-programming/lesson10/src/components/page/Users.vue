<template>
  <div class="users datatable">
    <div class="caption">Users</div>
    <table>
      <thead>
      <tr>
        <th>Id</th>
        <th>Login</th>
        <th>Name</th>
      </tr>
      </thead>
      <UsersBody v-for="user in users" :user="user" :key="user.id"/>
    </table>
  </div>
</template>

<script>
import UsersBody from "./UsersBody";
export default {
  name: "Users",
  components: {UsersBody},
  props: ["users"]
}
</script>

<style scoped>

</style>
