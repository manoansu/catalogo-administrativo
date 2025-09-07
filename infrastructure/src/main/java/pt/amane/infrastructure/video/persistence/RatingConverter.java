package pt.amane.infrastructure.video.persistence;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import pt.amane.domain.video.Rating;

@Converter(autoApply = true)
public class RatingConverter implements AttributeConverter<Rating, String> {

  /**
   * Received enumerate and return to data base column.
   * convert enumerate To Database Column.
   * @param attribute
   * @return
   */
  @Override
  public String convertToDatabaseColumn(Rating attribute) {
    if (attribute == null) return null;
    return attribute.getName();
  }

  /**
   * Convert the database value to enumerate.
   * @param dbData
   * @return
   */
  @Override
  public Rating convertToEntityAttribute(String dbData) {
    if (dbData == null) return null;
    return Rating.of(dbData).orElse(null);
  }
}
