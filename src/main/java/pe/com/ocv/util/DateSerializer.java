package pe.com.ocv.util;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class DateSerializer implements JsonSerializer<Date>, JsonDeserializer<Date>{

	private String formatoFecha;
    
    public DateSerializer(String formatoFecha){
        this.formatoFecha = formatoFecha;
    }
    
    public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
        SimpleDateFormat sdfFormato = new SimpleDateFormat(getFormatoFecha());
        sdfFormato = new SimpleDateFormat(getFormatoFecha());
        return new JsonPrimitive(sdfFormato.format(src));
    }
    
    public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException{
        SimpleDateFormat sdfFormato = new SimpleDateFormat(getFormatoFecha());
        Date fecha = null;
        try{
            fecha = sdfFormato.parse(json.getAsJsonPrimitive().getAsString());
        }catch(ParseException parseException){
        }
        return fecha;
    }

    public String getFormatoFecha() {
        return formatoFecha;
    }

    public void setFormatoFecha(String formatoFecha) {
        this.formatoFecha = formatoFecha;
    }
    
}

