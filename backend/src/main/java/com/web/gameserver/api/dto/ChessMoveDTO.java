package com.web.gameserver.api.dto;

import com.web.gameserver.api.model.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.json.JSONObject;

import java.util.Date;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ChessMoveDTO {
    private long id;
    private Player player; // player who made the move
    private Game game; // the game the move belongs to
    // from
    private int fromBoardRow;
    private int fromBoardColumn;
    // to
    private int toBoardRow;
    private int toBoardColumn;

    // piece move count
    private int moveCount = 0;
    // piece ID
    private int pieceId;

    private PieceColor color;
    private ChessPiece chessPiece;
    private Date created;

    public ChessMoveDTO(ChessMove chessMove) {
        setId(chessMove.getId());
        setPlayer(chessMove.getPlayer());
        setGame(chessMove.getGame());
        setFromBoardRow(chessMove.getFromBoardRow());
        setFromBoardColumn(chessMove.getFromBoardColumn());
        setToBoardRow(chessMove.getToBoardRow());
        setToBoardColumn(chessMove.getToBoardColumn());
        setChessPiece(chessMove.getChessPiece());
        setColor(chessMove.getColor());
        setCreated(chessMove.getCreated());
        setMoveCount(chessMove.getMoveCount());
        setPieceId(chessMove.getPieceId());
    }

    public JSONObject toJson() {
        return new JSONObject()
                .put("id", this.id)
                .put("player", this.player.getUsername())
                .put("game", this.game.getId())
                .put("fromBoardRow", this.fromBoardRow)
                .put("fromBoardColumn", this.fromBoardColumn)
                .put("toBoardRow", this.toBoardRow)
                .put("toBoardColumn", this.toBoardColumn)
                .put("piece", this.chessPiece.toString().toLowerCase())
                .put("color", this.color.toString().toLowerCase())
                .put("moveCount", this.moveCount)
                .put("pieceId", this.pieceId)
                .put("created", this.created.toString());
    }


}
