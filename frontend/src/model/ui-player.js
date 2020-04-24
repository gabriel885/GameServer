import Player from "./chess/player";

export default class extends Player {
  constructor(uiVm, id, color, name) {
    super(id, color, name);
    this.selectedPiece = null; // piece selected to move
    this.uiVm = uiVm; // ui ViewModel
  }
}
