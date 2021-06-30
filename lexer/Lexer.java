package lexer;

import java.util.ArrayList;
import java.util.Stack;

public class Lexer {

    private final int e = 23;

    /*
     * S0 S1 S2 S3 S4 S5 S6 S7 S8 S9 S10 S11 S12 S13 S14 S15 S16 S17 S18 S19 S20 S21 S22 Se
     */
    private final boolean[] is_final = {false, true, false, true, true, true, false, true, true, true, true, true, false, false, true, false, true, false, false, false, true, true, true, false};

    private final int[][] transitions = {
            /* S0  S1  S2  S3  S4  S5  S6  S7  S8  S9 S10 S11 S12 S13 S14 S15 S16 S17 S18 S19 S20 S21 S22 */
            /* DIGIT          */ {1, 1, 3, 3, e, e, e, e, e, e, 10, e, 12, 13, e, 13, e, 17, 17, 17, e, e, e},
            /* PERIOD         */ {2, 3, e, e, e, e, e, e, e, e, e, e, 12, 13, e, 13, e, 17, 17, 17, e, e, e},
            /* ADDITIVE_OP    */ {4, e, e, e, e, e, e, e, e, e, e, e, 12, 13, e, 13, e, 17, 17, 17, e, e, e},
            /* ASTERISK       */ {5, e, e, e, e, e, e, e, e, e, e, 13, 12, 15, e, 15, e, 17, 17, 17, e, e, e},
            /* EXCL_MARK      */ {6, e, e, e, e, e, e, e, e, e, e, e, 12, 13, e, 13, e, 17, 17, 17, e, e, e},
            /* ORDER_REL      */ {7, e, e, e, e, e, e, e, e, e, e, e, 12, 13, e, 13, e, 17, 17, 17, e, e, e},
            /* EQUALS         */ {8, e, e, e, e, e, 9, 9, 9, e, e, e, 12, 13, e, 13, e, 17, 17, 17, e, e, e},
            /* UNDERSCORE     */ {10, e, e, e, e, e, e, e, e, e, 10, e, 12, 13, e, 13, e, 17, 17, 17, e, e, e},
            /* FORWARDSLASH   */ {11, e, e, e, e, e, e, e, e, e, e, 12, 12, 13, e, 16, e, 17, 17, 17, e, e, e},
            /* BACKSLASH      */ {e, e, e, e, e, e, e, e, e, e, e, e, 12, 13, e, 13, e, 18, 18, 18, e, e, e},
            /* QUOTATION_MARK */ {17, e, e, e, e, e, e, e, e, e, e, e, 12, 13, e, 13, e, 20, 19, 20, e, e, e},
            /* PUNCTUATION    */ {21, e, e, e, e, e, e, e, e, e, e, e, 12, 13, e, 13, e, 17, 17, 17, e, e, e},
            /* NEWLINE        */ {e, e, e, e, e, e, e, e, e, e, e, e, 14, 13, e, 13, e, e, e, e, e, e, e},
            /* ENDOFFILE      */ {22, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e},
            /* LETTER         */ {10, e, e, e, e, e, e, e, e, e, 10, e, 12, 13, e, 13, e, 17, 17, 17, e, e, e},
            /* PRINTABLE      */ {e, e, e, e, e, e, e, e, e, e, e, e, 12, 13, e, 13, e, 17, 17, 17, e, e, e},
            /* OTHER          */ {e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e, e}};

    private int current_token = 0;
    private ArrayList<Token> tokens = new ArrayList<Token>();

    int current_index = 0;

    public Lexer(String program) {

        // Tokenise the program, ignoring comments
        Token t = new Token();

        while (current_index <= program.length()) {
            
            //tangible.RefObject<Integer> tempRef_current_index = new tangible.RefObject<Integer>(current_index);
            t = next_token(program);
            
            System.out.println("--------------------------");
            System.out.println(t.value + " " + t.type);

            if (t.type != TOKENS.TOK_COMMENT) {
                tokens.add(t);
            }
        }
    }

    /**
     * Returns the next token. This function returns the object in the global vector
     * #tokens at the position specified by the global unsigned integer
     * #current_token, which is then incremented.
     */
    public final Token next_token() {
        if (current_token < tokens.size()) {
            return tokens.get(current_token++);
        } else {
            String error = "Final token surpassed.";
            return new Token(TOKENS.TOK_ERROR.getValue(), error);
        }
    }

    /**
     * Returns the state number to go to from state S<SUB>s</SUB> in the DFA when
     * encountering the character \f$\sigma\f$.
     */
    private int transition_delta(int s, char sigma) {

        /*
         * Check which transition type we have, and then refer to the transition table.
         */
        switch (sigma) {
            case '0':
            case '1':
            case '2':
            case '3':
            case '4':
            case '5':
            case '6':
            case '7':
            case '8':
            case '9':
                return transitions[(int) TRANSITION_TYPE.DIGIT.getValue()][s];

            case '.':
                return transitions[(int) TRANSITION_TYPE.PERIOD.getValue()][s];

            case '+':
            case '-':
                return transitions[(int) TRANSITION_TYPE.ADDITIVE_OP.getValue()][s];

            case '*':
                return transitions[(int) TRANSITION_TYPE.ASTERISK.getValue()][s];

            case '!':
                return transitions[(int) TRANSITION_TYPE.EXCL_MARK.getValue()][s];
            case '>':
            case '<':
                return transitions[(int) TRANSITION_TYPE.ORDER_REL.getValue()][s];

            case '=':
                return transitions[(int) TRANSITION_TYPE.EQUALS.getValue()][s];

            case '_':
                return transitions[(int) TRANSITION_TYPE.UNDERSCORE.getValue()][s];

            case '/':
                return transitions[(int) TRANSITION_TYPE.FORWARDSLASH.getValue()][s];

            case '\\':
                return transitions[(int) TRANSITION_TYPE.BACKSLASH.getValue()][s];

            case '\"':
                return transitions[(int) TRANSITION_TYPE.QUOTATION_MARK.getValue()][s];

            case ':':
            case ';':
            case ',':
            case '(':
            case ')':
            case '{':
            case '}':
                return transitions[(int) TRANSITION_TYPE.PUNCTUATION.getValue()][s];

            case '\n':
                return transitions[(int) TRANSITION_TYPE.NEWLINE.getValue()][s];

            case '¬':
                return transitions[(int) TRANSITION_TYPE.ENDOFFILE.getValue()][s];

            default:
                //auto ascii = (int) sigma;
                int ascii = (int) sigma;

                // If alpha is in [A-Z] or [a-z]
                if (((0x41 <= ascii) && (ascii <= 0x5A)) || ((0x61 <= ascii) && (ascii <= 0x7A)))
                    return transitions[TRANSITION_TYPE.LETTER.getValue()][s];

                // Else if Printable
                if ((0x20 <= ascii) && (ascii <= 0x7E))
                    return transitions[TRANSITION_TYPE.PRINTABLE.getValue()][s];

                // If other
                return transitions[TRANSITION_TYPE.OTHER.getValue()][s];
        }

    }

    public Token next_token(String program) {

        // Setup stack and lexeme
        int current_state = 0;
        Stack<Integer> state_stack = new Stack<Integer>();
        char current_symbol;
        //String lexeme;
        String lexeme = "";

        // Push 'BAD' state on the stack
        state_stack.push(-1);

        // Ignore whitespaces or newlines in front of lexeme
        while (current_index < program.length() && (program.charAt(current_index) == ' ' || program.charAt(current_index) == '\n')) {
            current_index++;
        }

        // Check if EOF
        System.out.println(current_index);
        System.out.println(program.length());
        if (current_index == program.length()) {
            //lexeme = (char) EOF;
            lexeme = "¬";   //EOF token
            current_index++;
            //return Token(22, lexeme, get_line_number(program, current_index.argValue));
            return new Token(22, String.valueOf(lexeme), get_line_number(program, current_index));
        }

        // While current state is not error state
        while (current_state != e) {
            
            if (current_index == program.length()) {
                //lexeme = (char) EOF;
                lexeme = "¬";   //EOF token
                current_index++;
                //return Token(22, lexeme, get_line_number(program, current_index.argValue));
                return new Token(22, String.valueOf(lexeme), get_line_number(program, current_index));
            }

            current_symbol = program.charAt(current_index);
            lexeme += current_symbol;

            // If current state is final, remove previously recorded final states
            if (is_final[current_state]) {
                while (!state_stack.empty()) {
                    state_stack.pop();
                }
            }

            // and push current one on the stack
            state_stack.push(current_state);

            // Go to next state using delta function in DFA
            current_state = transition_delta(current_state, current_symbol);

            // Update current index for next iteration
            current_index++;
        }

        // Rollback loop
        while (current_state != -1 && !is_final[current_state]) {
            current_state = state_stack.peek();
            state_stack.pop();
            lexeme = pop_back(lexeme);
            current_index--;
        }

        if (current_state == -1) {
            throw new RuntimeException("Lexical error.");
        }


        if (is_final[current_state]) {
            return new Token(current_state, String.valueOf(lexeme), get_line_number(program, current_index));
        } else {
            throw new RuntimeException("Lexical error on line " + String.valueOf(get_line_number(program, current_index)) + ".");
        }
    }

    public int get_line_number(String program, int index) {
        int line = 1;
        for (int i = 0; i < index-1; i++) {
            if (program.charAt(i) == '\n') {
                line++;
            }
        }
        return line;
    }

    public static String pop_back(String str) {
        return removeLastChars(str, 1);
    }
    
    public static String removeLastChars(String str, int chars) {
        if(str.length() > 0){
            return str.substring(0, str.length() - chars);
        }
        return str;
    }
}