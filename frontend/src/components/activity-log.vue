<template>
  <!-- list of activities -->
  <div>
    <h4>{{ title }}</h4>
    <ul class="list-group">
      <li class="list-group-item" v-for="log in logs" :key="log.id" :log="log">
        <p>{{ log }}</p>
      </li>
    </ul>
  </div>
</template>

<script>
export default {
  data() {
    return {};
  },
  props: ["title", "logs"],
};
</script>
