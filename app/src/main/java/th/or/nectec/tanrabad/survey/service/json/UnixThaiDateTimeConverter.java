package th.or.nectec.tanrabad.survey.service.json;

import com.bluelinelabs.logansquare.typeconverters.LongBasedTypeConverter;
import org.joda.time.DateTime;
import th.or.nectec.tanrabad.survey.utils.time.ThaiDateTimeConverter;

public class UnixThaiDateTimeConverter extends LongBasedTypeConverter<DateTime> {

    @Override
    public DateTime getFromLong(long l) {
        return ThaiDateTimeConverter.convert(new DateTime(l * 1000).toString());
    }

    @Override
    public long convertToLong(DateTime object) {
        return object.getMillis() / 1000;
    }
}
