package com.tzj.garvel.core.parser.json;

import com.tzj.garvel.core.parser.api.ast.json.*;
import com.tzj.garvel.core.parser.exception.JsonParserException;
import com.tzj.garvel.core.parser.api.JsonParser;
import com.tzj.garvel.core.parser.exception.JsonScannerException;

import static com.tzj.garvel.core.parser.json.JsonTokenType.STRING;

public class JsonParserImpl implements JsonParser {
    private String filename;
    private JsonScanner scanner;
    private JsonToken currentToken;

    public JsonParserImpl(final String filename) {
        this.filename = filename;
        this.scanner = new JsonScanner(filename);
    }

    // test
    public static void main(String[] args) {
        final String filename = System.getProperty("user.dir") + "/src/com/tzj/garvel/playground/misc/Garvel.gl.sample";

        JsonParser parser = new JsonParserImpl(filename);
        JsonObject root = parser.parse();
        System.out.println(root);
    }

    /**
     * accept the current token unconditionally.
     */
    private void acceptIt() {
        currentToken = scanner.scan();
    }

    /**
     * accept the current token only if it is the expected kind.
     *
     * @param expectedKind
     */
    private void accept(final JsonTokenType expectedKind) {
        if (currentToken.kind() != expectedKind) {
            throw new JsonParserException(String.format("Json Parser error at line %d, col %d while parsing %s. Expected to accept token of kind %s, found token of kind %s",
                    currentToken.line(), currentToken.column(), filename, expectedKind, currentToken.kind()));
        }

        if (currentToken.kind() == JsonTokenType.EOT) {
            return;
        }

        currentToken = scanner.scan();
    }

    /**
     * Dummy rule for the start rule
     *
     * @return
     */
    JsonObject parseJsonRoot() {
        final JsonObject root = parseJsonObject();

        return root;
    }

    /**
     * JsonObject ::= '{' (epislon | Members)* '}'
     *
     * @return
     */
    private JsonObject parseJsonObject() {
        accept(JsonTokenType.LEFT_BRACE);
        final JsonObject root = new JsonObject(parseJsonMembers());
        accept(JsonTokenType.RIGHT_BRACE);

        return root;
    }

    /**
     * JsonMembers ::= JsonPair (, JsonMembers)*
     *
     * @return
     */
    private JsonMember parseJsonMembers() {
        JsonMember p1 = parseJsonPair();

        while (currentToken.kind() == JsonTokenType.COMMA) {
            acceptIt();
            final JsonMember p2 = parseJsonPair();
            p1 = new JsonSequentialPair(p1, p2);
        }

        return p1;
    }

    /**
     * Pair ::= JsonString : JsonValue
     *
     * @return
     */
    private JsonPair parseJsonPair() {
        final JsonString jsonString = parseJsonString();
        accept(JsonTokenType.COLON);
        final JsonValue jsonValue = parseJsonValue();

        return new JsonPair(jsonString, jsonValue);
    }

    /**
     * JsonValue ::= JsonString | JsonObject | JsonArray
     *
     * @return
     */
    private JsonValue parseJsonValue() {
        JsonValue jsonValue = null;

        switch (currentToken.kind()) {
            case STRING: {
                final JsonString jsonString = parseJsonString();
                jsonValue = jsonString;
            }
            break;

            case LEFT_BRACE: {
                final JsonObject jsonObject = parseJsonObject();
                jsonValue = jsonObject;
            }
            break;

            case LEFT_SQUARE_BRACKETS: {
                acceptIt();
                final JsonArrayElement jsonArray = parseJsonArray();
                accept(JsonTokenType.RIGHT_SQUARE_BRACKETS);
                jsonValue = jsonArray;
            }
            break;

            default:
                throw new JsonParserException(String.format("Json Parser Exception at line %d, col %d while parsing %s. %s cannot start a valid JsonValue",
                        currentToken.line(), currentToken.column(), filename, currentToken.kind()));
        }

        return jsonValue;
    }

    /**
     * JsonArray ::= '[' (epsilon | Elements)* ']'
     *
     * @return
     */
    private JsonArrayElement parseJsonArray() {
        JsonArrayElement jsonArray1 = parseJsonArrayElement();

        while (currentToken.kind() == JsonTokenType.COMMA) {
            acceptIt();
            final JsonArrayElement jsonArray2 = parseJsonArrayElement();
            jsonArray1 = new JsonSequentialArray(jsonArray1, jsonArray2);
        }

        return jsonArray1;
    }

    /**
     * JsonArrayElement ::=
     *
     * @return
     */
    private JsonArrayElement parseJsonArrayElement() {
        final JsonValue jsonValue = parseJsonValue();
        final JsonArray jsonArray = new JsonArray(jsonValue);

        return jsonArray;
    }

    /**
     * JSonString ::= String
     *
     * @return
     */
    private JsonString parseJsonString() {
        if (currentToken.kind() != STRING) {
            throw new JsonParserException(String.format("Json Parser Error at line %d, col %d while parsing %s. Expected token of kind %s, found tokmn of kind %s",
                    currentToken.line(), currentToken.column(), filename, STRING, currentToken.kind()));
        }

        final JsonString jsonString = new JsonString(currentToken.spelling());
        currentToken = scanner.scan();

        return jsonString;
    }

    /**
     * Parse the given JSON file and return the contents in an AST representation
     * so that the client can choose how to process it further.
     *
     * @return
     * @throws JsonParserException
     */
    @Override
    public JsonObject parse() throws JsonParserException {
        try {
            currentToken = scanner.scan();
        } catch (JsonScannerException e) {
            throw new JsonParserException(String.format("Error while parsting Json file %s. Message: %s", filename, e.getLocalizedMessage()));
        }

        JsonObject root = parseJsonRoot();
        accept(JsonTokenType.EOT);

        return root;
    }
}
