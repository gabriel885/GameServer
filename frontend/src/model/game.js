//import Event from "../utils/event";

export default class {
  constructor(plrs, options = null) {
    this.plrs = plrs;
    this.options = options || {};
    this.plrsQueue = [...plrs];
    this.stopPending = false; // waiting to opponent
    this.log = []; // game valid moves
    this.gameResult = null; // game result
  }

  handleInterruption() {}

  validateMove() {
    // If game is ended then no valid moves
    return !this.isEnded;
  }

  commitMove(plr, move) {
    this.log.push({ plr, move });
  }

  undoLastMove() {
    return this.log.pop();
  }

  getGameResult() {
    return null;
  }

  stop() {
    this.stopPending = true;
  }

  get isEnded() {
    return this.gameResult != null;
  }

  endGame(gameResult) {
    // Check if game was not ended before.
    if (this.gameResult != null) return;

    this.gameResult = gameResult;
    this.plrsQueue = [];
    this.gameEndedEvent.emit(this.gameResult);
  }
}
