package com.thejoeflow.graphql

import graphql.language.StringValue
import graphql.schema.*
import org.springframework.stereotype.Component
import java.text.SimpleDateFormat
import java.util.*

@Component
class DateScalar() : GraphQLScalarType("Date", "Maps dates from 'DD-MM-YYYY' to java.util.Date", object: Coercing<Date, String> {

    private val df = SimpleDateFormat("dd-MM-yyyy")

    override fun serialize(input: Any): String {
        try {
            if (input is Date){
                return df.format(input)
            }
        }
        catch (e: Exception){}
        throw CoercingSerializeException("Unable to serialize $input to a Date value")
    }

    override fun parseValue(input: Any): Date {
        try {
            if (input is String){
                return df.parse(input)
            }
        }
        catch (e: Exception){}
        throw CoercingParseValueException("Unable to parse input value. Input is not a string")
    }

    override fun parseLiteral(input: Any): Date {
        try {
            if (input is StringValue){
                return df.parse(input.value)
            }
        }
        catch (e: Exception){}
        throw CoercingParseLiteralException("Unable to parse input AST value. Input is not an AST String Type")
    }
}) {
}