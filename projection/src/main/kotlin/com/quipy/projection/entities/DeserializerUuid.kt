package com.quipy.projection.entities

import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.deser.std.StdDeserializer
import java.io.IOException
import java.util.UUID

class DeserializerUuid @JvmOverloads constructor(vc: Class<*>? = null) :
    StdDeserializer<UUID?>(vc) {
    @Throws(IOException::class)
    override fun deserialize(jsonparser: JsonParser, context: DeserializationContext): UUID {
        val uuidString = jsonparser.valueAsString
        return UUID.fromString(uuidString)
    }
}
