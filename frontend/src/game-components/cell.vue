<template>
  <div
    :class="[
      $style.cell,
      $style[model.color],
      highlighted ? $style.highlighted : '',
    ]"
    :file="model.file"
    :rank="model.rank"
    @click="$emit('click', this)"
  >
    <piece
      v-for="p in model.pieces"
      :selected="isPieceSelected(p)"
      :key="p.id"
      :model="p"
      @piece-clicked="onPieceClicked(p)"
      ref="pieces"
      :class="$style.piece"
    />
  </div>
</template>

<script>
import Piece from "./pieces/piece-svg.vue";

export default {
  components: { Piece },

  props: {
    model: {
      // cell model
      required: true,
      type: Object,
    },
    highlighted: {
      // cell is highlighted
      default: false,
      type: Boolean,
    },
    selectedPiece: {
      // selected piece
      default: null,
      type: Object,
    },
  },

  methods: {
    isPieceSelected(piece) {
      if (this.selectedPiece !== null && piece === this.selectedPiece) {
        this.highlighted = true;
        return true;
      }
      return false;
    },

    onPieceClicked(piece) {
      this.$emit("piece-clicked", piece);
    },
  },
};
</script>

<style module>
:root {
  --white-color: rgb(232, 235, 239);
  --black-color: rgb(125, 135, 150);
}

.black {
  background-color: var(--black-color);
}

.white {
  background-color: var(--white-color);
}

.highlighted {
  /* background-image: radial-gradient(
    circle at center,
    rgb(0, 0, 0, 0),
    rgb(100, 255, 0, 0.2) 0%
  ); */
  background-color: red;
}

.piece {
  position: absolute;
  z-index: 1;
}
</style>
