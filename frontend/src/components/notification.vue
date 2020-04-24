<template>
  <b-alert :variant="variant" v-if="!isEmpty && !hide" @blur="onBlur" show>{{
    msg
  }}</b-alert>
</template>

<script>
import Vue from "vue";

export default {
  props: {
    message: {
      type: [String, Object],
      default: null,
    },
    variant: {
      type: String,
      validator: (val) =>
        ["success", "danger", "primary", "warning", "info"].includes(val),
    },
    hideOnBlur: {
      type: Boolean,
      default: false,
    },
  },

  data() {
    return {
      hide: false,
    };
  },

  computed: {
    isEmpty() {
      return this.msg == null || this.msg == "";
    },

    msg() {
      return this.message.split('"').join("");
    },
  },

  watch: {
    message() {
      // If message was updated then show notification if it was hidden.
      this.hide = false;
      Vue.nextTick(() => {
        if (this.hideOnBlur && this.$refs.btn) this.$refs.btn.focus();
      });
    },
  },

  methods: {
    onBlur() {
      this.hide = true;
    },
  },
};
</script>
