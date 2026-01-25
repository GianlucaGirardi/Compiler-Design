package org.example.lexicalAnalyzer.dispatcher;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.example.lexicalAnalyzer.ICharStream;
import org.example.lexicalAnalyzer.config.DfaRegistry;
import org.example.lexicalAnalyzer.config.TransitionTable;
import org.example.lexicalAnalyzer.token.Token;

import static org.example.lexicalAnalyzer.config.Constants.*;

@Data
@RequiredArgsConstructor
public class CommentDispatcher implements Dispatcher{

    private final String dfaName;

    private TransitionTable table;

    private int lastRecordedLine;

    private ICharStream stream;

    @Override
    public Token processToken(ICharStream stream) {
        TransitionTable table = initTable();
        this.setLastRecordedLine(stream.getLine());
        this.setStream(stream);

        ScanResult result = consumeCommentLexeme(stream, table);
        String lexeme = result.lexeme();

        if (lexeme.isEmpty()) return null;

        if (result.forcedInvalid()) {
            return new Token(getInvalidTokenType(null), lexeme, lastRecordedLine, false);
        }

        String type = table.getStateToType().get(result.endState());
        if (type == null) type = COMMENT_NAME;

        return new Token(type, lexeme, lastRecordedLine, true);
    }

    private ScanResult consumeCommentLexeme(ICharStream stream, TransitionTable table) {
        StringBuilder sb = new StringBuilder();
        boolean forcedInvalid = false;

        boolean isInline = false;
        boolean isMultiline = false;

        while (!stream.isAtEnd()) {
            char c = stream.peek();

            /* Handle inline comment */
            if (isInline && (c == '\n' || c == '\r')) {
                break;
            }

            String charClass = classify(c);

            /* transition only on SLASH or STAR if it is in Transition map */
            if (table.hasTransition(table.getCurrentState(), charClass)) {
                table.nextState(table.getCurrentState(), charClass);
            }

            /* consume && ignore non-driving inputs */
            sb.append(c);
            stream.advance();

            /* validate comment type */
            String stateType = table.getStateToType().get(table.getCurrentState());
            if (INLINE_COMMENT_NAME.equals(stateType)) {
                isInline = true;
            } else if (MULTI_LINE_COMMENT_NAME.equals(stateType)) {
                isMultiline = true;
            }

            /* handle terminating multiline */
            if (isMultiline && table.isAcceptState(table.getCurrentState())) {
                break;
            }
        }

        /* handle non-terminating multiline */
        if (isMultiline && !table.isAcceptState(table.getCurrentState())) {
            forcedInvalid = true;
        }

        return new ScanResult(sb.toString(), table.getCurrentState(), forcedInvalid);
    }

    public TransitionTable initTable() {
        TransitionTable table = DfaRegistry.getTable(dfaName);
        table.reset();
        this.setTable(table);
        return table;
    }

    public String classify(char c) {
        if (c == SLASH) return SLASH_NAME;
        else if (c == STAR) return STAR_NAME;
        return DEFAULT_NAME;
    }

    @Override
    public boolean isDelimiter(char c) {
        if(INLINE_COMMENT_NAME.equals(table.getStateToType().get(table.getCurrentState()))) {
            return this.getStream().getLine() != this.getLastRecordedLine();
        }
        return false;
    }

    @Override
    public String getInvalidTokenType(String lexeme) {
        return INVALID_COMMENT_MESSAGE;
    }
}
