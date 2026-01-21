package asia.castis.evoucher.api.common;

import com.google.gson.*;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;

@Slf4j
public class DateTimeAdapter {

    private DateTimeAdapter() {}

    public static class DateAdapter implements JsonSerializer<Date>, JsonDeserializer<Date> {
        private final SimpleDateFormat dateFormat;

        public DateAdapter() {
            this.dateFormat = new SimpleDateFormat(Constant.FormatDate.DATE_TIME_DEFAULT);
        }

        @Override
        public JsonElement serialize(Date src, Type typeOfSrc, JsonSerializationContext context) {
            return new JsonPrimitive(dateFormat.format(src));
        }

        @Override
        public Date deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
            try {
                return dateFormat.parse(json.getAsString());
            } catch (ParseException e) {
                log.error(e.getMessage(), e);
                return null;
            }
        }
    }
}
