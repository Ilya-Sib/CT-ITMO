<template>
  <div id="app">
    <Header :user="user"/>
    <Middle :posts="posts" :users="users" :post="post"/>
    <Footer/>
  </div>
</template>

<script>
import Header from "./components/Header";
import Middle from "./components/Middle";
import Footer from "./components/Footer";
import axios from "axios"

export default {
  name: 'App',
  components: {
    Footer,
    Middle,
    Header
  },
  data: function () {
    return {
      user: null,
      post: null,
      posts: [],
      users: []
    }
  },
  beforeMount() {
    if (localStorage.getItem("jwt") && !this.user) {
      this.$root.$emit("onJwt", localStorage.getItem("jwt"));
    }

    axios.get("/api/1/posts").then(response => {
      this.posts = response.data;
    });

    axios.get("/api/1/users").then(response => {
      this.users = response.data;
    });
  },
  beforeCreate() {
    this.$root.$on("onEnter", (login, password) => {
      if (password === "") {
        this.$root.$emit("onEnterValidationError", "Password is required");
        return;
      }

      axios.post("/api/1/jwt", {
        login, password
      }).then(response => {
        localStorage.setItem("jwt", response.data);
        this.$root.$emit("onJwt", response.data);
      }).catch(error => {
        this.$root.$emit("onEnterValidationError", error.response.data);
      });
    });

    this.$root.$on("onJwt", (jwt) => {
      localStorage.setItem("jwt", jwt);

      axios.get("/api/1/users/auth", {
        params: {
          jwt
        }
      }).then(response => {
        this.user = response.data;
        this.$root.$emit("onChangePage", "Index");
      }).catch(() => this.$root.$emit("onLogout"))
    });

    this.$root.$on("onLogout", () => {
      localStorage.removeItem("jwt");
      this.user = null;
    });

    this.$root.$on("onShowPost", post => {
      this.post = post
      this.$root.$emit("onChangePage", "Post");
    });

    this.$root.$on("onWriteComment", text => {
      if (this.user) {
        axios.post("/api/1/comments", {
          text, post: this.post, user: this.user
        }).then(() => {
          axios.get("/api/1/posts/" + this.post.id).then(newResponse => {
            this.$root.$emit("onShowPost", newResponse.data);
          });
          axios.get("/api/1/posts").then(newNewResponse => {
            this.posts = newNewResponse.data;
          });
        }).catch(error => {
          this.$root.$emit("onWriteCommentError", error.response.data);
        });
      } else {
        this.$root.$emit("onWriteCommentError", "No access");
      }
    });

    this.$root.$on("onRegister", (login, name, password) => {
      axios.post("/api/1/users", {
        login, name, password
      }).then(response => {
        axios.get("/api/1/users").then(newResponse => {
          this.users = newResponse.data;
        });
        this.$root.$emit("onEnter", response.data.login, response.data.password);
      }).catch(error => {
        this.$root.$emit("onRegisterValidationError", error.response.data);
      });
    });

    this.$root.$on("onWritePost", (title, text) => {
      if (this.user) {
        axios.post("/api/1/posts", {
          title, text, user: this.user
        }).then(() => {
          axios.get("/api/1/posts").then(response => {
            this.posts = response.data;
          });
          this.$root.$emit("onChangePage", "Index")
        }).catch(error => {
          this.$root.$emit("onWritePostValidationError", error.response.data);
        });
      } else {
        this.$root.$emit("onWritePostValidationError", "No access");
      }
    });
  }
}
</script>

<style>
#app {

}
</style>
