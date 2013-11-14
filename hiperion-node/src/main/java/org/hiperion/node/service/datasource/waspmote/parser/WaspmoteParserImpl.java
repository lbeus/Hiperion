package org.hiperion.node.service.datasource.waspmote.parser;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * User: iobestar
 * Date: 22.06.13.
 * Time: 17:36
 */
public class WaspmoteParserImpl implements WaspmoteParser {

    private final static String END_TAG = "end";

    private enum StateDefinition {
        BEGIN_STATE() {
            @Override
            public StateDefinition nextState(char inputChar) {
                if ('#' == inputChar) {
                    return MOTE_ID_VALUE;
                }
                return BEGIN_STATE;
            }
        }, MOTE_ID_VALUE() {
            @Override
            public StateDefinition nextState(char inputChar) {
                if ('#' == inputChar) {
                    return NEW_FIELD;
                }
                return MOTE_ID_VALUE;
            }
        }, NEW_FIELD() {
            @Override
            public StateDefinition nextState(char inputChar) {
                if ('!' == inputChar) {
                    return FIELD_NAME_BEGIN;
                }
                return NEW_FIELD;
            }
        }, FIELD_NAME_BEGIN {
            @Override
            public StateDefinition nextState(char inputChar) {
                if ('!' == inputChar) {
                    return FIELD_VALUE;
                }
                return FIELD_NAME_BEGIN;
            }
        },
        FIELD_NAME_END {
            @Override
            public StateDefinition nextState(char inputChar) {
                if ('!' == inputChar) {
                    return NEW_FIELD;
                }
                return FIELD_NAME_END;
            }
        },
        FIELD_VALUE {
            @Override
            public StateDefinition nextState(char inputChar) {
                if ('!' == inputChar) {
                    return FIELD_NAME_END;
                }
                return FIELD_VALUE;
            }
        };

        public abstract StateDefinition nextState(char inputChar);
    }

    @Override
    public WaspmoteOutput parseInputPacket(String inputPacket) {
        char[] chars = inputPacket.replaceAll("\u0000", "").toCharArray();
        String waspmoteId = null;

        Map<String, String> dataStructure = new HashMap<String, String>();
        Stack<String> stack = new Stack<String>();
        StateDefinition currentState = StateDefinition.BEGIN_STATE;
        StringBuffer stringBuffer = new StringBuffer();
        int i = 0;
        while (true) {
            char currentChar = chars[i++];
            StateDefinition newState = currentState.nextState(currentChar);
            if (newState != currentState) {
                if (StateDefinition.BEGIN_STATE == currentState) {
                    stringBuffer = new StringBuffer();
                } else if (currentState == StateDefinition.MOTE_ID_VALUE) {
                    waspmoteId = stringBuffer.toString().trim();
                    stringBuffer = new StringBuffer();
                } else if (StateDefinition.NEW_FIELD == currentState) {
                    stringBuffer = new StringBuffer();
                } else if (StateDefinition.FIELD_NAME_BEGIN == currentState) {
                    String fieldName = stringBuffer.toString().trim();
                    stack.push(fieldName);
                    stringBuffer = new StringBuffer();
                    if (fieldName.equals(END_TAG)) {
                        break;
                    }
                } else if (StateDefinition.FIELD_VALUE == currentState) {
                    String value = stringBuffer.toString().trim();
                    dataStructure.put(stack.pop(), value);
                    stringBuffer = new StringBuffer();
                } else if (StateDefinition.FIELD_NAME_END == currentState) {
                    stringBuffer = new StringBuffer();
                }
            } else {
                stringBuffer.append(currentChar);
            }
            currentState = newState;
            if (i == chars.length) break;
        }
        WaspmoteOutput waspmoteOutput = new WaspmoteOutput(waspmoteId,dataStructure);
        return waspmoteOutput;
    }
}
