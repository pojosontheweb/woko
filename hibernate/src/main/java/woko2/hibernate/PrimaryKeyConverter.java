package woko2.hibernate;

import net.sourceforge.stripes.util.Log;
import org.hibernate.SessionFactory;
import org.hibernate.metadata.ClassMetadata;
import woko2.util.Util;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Date;

/**
 * Class that allows to convert stringified PKs to their
 * real type (e.g. String, Long, etc.). This implementation supports
 * <code>Number</code>, <code>Date</code> and <code>String</code>
 * primary keys.
 */
public class PrimaryKeyConverter {

    private static final Log logger = Log.getInstance(HibernateInterceptor.class);

    public Serializable convert(String value, Class<?> targetConversionType) {
        logger.debug("converting String value='", value, "' to type '", targetConversionType.getName(), "'...");
        Serializable result = null;
        if (Number.class.isAssignableFrom(targetConversionType)) {
            try {
                result = NumberFormat.getInstance().parse(value);
            } catch (ParseException e) {
                logger.error("Unable to parse Number from str '" + value + "'", e);
                return null;
            }
        } else if (Date.class.isAssignableFrom(targetConversionType)) {
            try {
                result = DateFormat.getInstance().parse(value);
            } catch (ParseException e) {
                logger.error("Unable to parse Date from str '" + value + "'", e);
                return null;
            }
        } else if (String.class.equals(targetConversionType)) {
            result = value;
        } else {
            String msg = "Supplied primary key type " + targetConversionType.getName() + " is not supported.\n" +
                    "You can extend PrimaryKeyConverter and wire your implementation in the Spring context " +
                    "in order to handle this type.";
            logger.error(msg);
            throw new UnsupportedOperationException(msg);
        }
        if (result!=null) {
            logger.debug("converted  String value='", value, "' to '", result, "', class=", result.getClass());            
        }
        return result;
    }

    public Serializable getPrimaryKeyValue(SessionFactory sessionFactory, Object entity)	{
        Class<?> entityClass = entity.getClass();
        ClassMetadata cm = sessionFactory.getClassMetadata(entityClass);
        if (cm==null) {
            return null;
        }
        String idPropName = cm.getIdentifierPropertyName();
        if (idPropName==null) {
            return null;
        }
        return (Serializable) Util.getPropertyValue(entity, idPropName);
    }


}
