// private long id;
//     private Player player; // player who made the move
//     private Game game; // the game the move belongs to
//     private int boardRow;
//     private int boardColumn;
//     private ChessPiece chessPiece;
//     private Date created;
export default class {
  // move class

  constructor(
    fromBoardRow,
    fromBoardColumn,
    toBoardRow,
    toBoardColumn,
    color,
    moveCount,
    pieceId
  ) {
    this.fromBoardRow = fromBoardRow;
    this.fromBoardColumn = fromBoardColumn;
    this.toBoardRow = toBoardRow;
    this.toBoardColumn = toBoardColumn;
    this.color = color;
    this.moveCount = moveCount;
    this.pieceId = pieceId;
  }
}
