<template>
  <!-- <component
    :is="model.type"
    viewBox="-8 -8 140 140"
    @click="onPieceClick"
    :class="[$style.piece, $style[model.color]]"
  /> -->
  <component
    :is="model.type"
    viewBox="-8 -8 140 140"
    @click="onPieceClick()"
    :class="[$style.piece, $style[model.color]]"
  />
</template>

<script>
import Rook from "./rook.vue";
import Knight from "./knight.vue";
import Bishop from "./bishop.vue";
import Pawn from "./pawn.vue";
import Queen from "./queen.vue";
import King from "./king.vue";

export default {
  components: { Rook, Knight, Bishop, Pawn, Queen, King },
  props: {
    model: {
      required: true,
      type: Object,
    },
    selected: {
      type: Boolean,
      default: false,
    },
  },

  computed: {
    colorInv() {
      return this.model.color == "white" ? "black" : "white";
    },

    viewBox() {
      return this.selected ? "3 3 94 94" : "-5 -5 110 110";
    },
  },

  methods: {
    onPieceClick() {
      this.$emit("piece-clicked", this.model);
    },
  },
};
</script>

<style module>
.container {
  width: 100%;
  will-change: transform;
}

.white {
  stroke: #333;
  fill: white;
}

.black {
  /*stroke: white;*/
  fill: #222;
}

.piece {
  stroke-width: 5;
  stroke-linecap: round;
  stroke-linejoin: round;
}
</style>
