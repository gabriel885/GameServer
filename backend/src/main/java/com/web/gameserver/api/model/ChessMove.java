package com.web.gameserver.api.model;

import com.web.gameserver.api.dto.ChessMoveDTO;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.Date;

@Entity
@NoArgsConstructor
@Setter
@Getter
@AllArgsConstructor
@Table(name = "chessmove")
public class ChessMove {

    @Column(name = "created")
    private Date created;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "move_id")
    private long id;
    @NotNull
    @JoinColumn(name = "player")
    @ManyToOne
    private Player player; // player who made the move
    @NotNull
    @JoinColumn(name = "game_id")
    @ManyToOne
    private Game game;

    // from
    @NotNull
    @Column(name = "from_board_row")
    private int fromBoardRow;

    @NotNull
    @Column(name = "from_board_column")
    private int fromBoardColumn;

    // to
    @NotNull
    @Column(name = "to_board_row")
    private int toBoardRow;
    @NotNull
    @Column(name = "to_board_column")
    private int toBoardColumn;

    @NonNull
    @Column(name = "piece_type")
    @Enumerated(EnumType.STRING)
    private ChessPiece chessPiece;

    @NotNull
    @Column(name = "piece_color")
    @Enumerated(EnumType.STRING)
    private PieceColor color;

    @NotNull
    @Column(name = "move_count")
    private int moveCount;

    @NotNull
    @Column(name = "piece_id")
    private int pieceId;

    public ChessMove(ChessMoveDTO chessMoveDTO) {

        this();
        if (chessMoveDTO == null) {
            return;
        }
        setId(chessMoveDTO.getId());
        setPlayer(chessMoveDTO.getPlayer());
        setGame(chessMoveDTO.getGame());
        setFromBoardRow(chessMoveDTO.getFromBoardRow());
        setFromBoardColumn(chessMoveDTO.getFromBoardColumn());
        setToBoardRow(chessMoveDTO.getToBoardRow());
        setToBoardColumn(chessMoveDTO.getToBoardColumn());
        setChessPiece(chessMoveDTO.getChessPiece());
        setColor(chessMoveDTO.getColor());
        setCreated(chessMoveDTO.getCreated());
        setMoveCount(chessMoveDTO.getMoveCount());
        setPieceId(chessMoveDTO.getPieceId());
    }


}
