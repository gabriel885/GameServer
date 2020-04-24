// chess move
import Move from "../move";

//      private long id;
//      private Player player; // player who made the move
//      private Game game; // the game the move belongs to
//      private int boardRow;
//      private int boardColumn;
//      private ChessPiece chessPiece;

export default class ChessMove extends Move {
  constructor(
    fromBoardRow,
    fromBoardColumn,
    toBoardRow,
    toBoardColumn,
    piece,
    color,
    moveCount,
    pieceId
  ) {
    super(
      fromBoardRow,
      fromBoardColumn,
      toBoardRow,
      toBoardColumn,
      color,
      moveCount,
      pieceId
    );
    this.piece = piece;
  }
  toJSON() {
    return {
      fromBoardRow: super.boardRow,
      fromBoardColumn: super.boardColumn,
      toBoardRow: super.toBoardRow,
      toBoardColumn: super.toBoardColumn,
      chessPiece: this.piece.toUpperCase(),
      color: super.color,
      moveCount: super.moveCount,
      pieceId: super.pieceId,
    };
  }
}
