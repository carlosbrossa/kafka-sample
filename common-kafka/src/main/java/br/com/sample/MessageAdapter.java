package br.com.sample;

import com.google.gson.*;

import java.lang.reflect.Type;

public class MessageAdapter implements JsonSerializer<Message>, JsonDeserializer<Message> {

    @Override
    public JsonElement serialize(Message message, Type type, JsonSerializationContext context) {

        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("type", message.getPayload().getClass().getName());
        jsonObject.add("payload", context.serialize(message.getPayload()));
        jsonObject.add("correlationId", context.serialize(message.getId()));

        return jsonObject;
    }


    @Override
    public Message deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        var obj = jsonElement.getAsJsonObject();
        var payloadType = obj.get("type").getAsString();
        var correlationId = (CorrelationId)jsonDeserializationContext.deserialize(obj.get("correlationId"), CorrelationId.class);
        try {
            var payload = jsonDeserializationContext.deserialize(obj.get("payload"), Class.forName(payloadType));
            return new Message(correlationId, payload);
        } catch (ClassNotFoundException e) {
            throw new JsonParseException(e);
        }
    }
}
