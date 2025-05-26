package pt.amane.infrastructure.category.persistence;

import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import pt.amane.MySQLGatewayTest;
import pt.amane.domain.category.Category;

@MySQLGatewayTest
public class CategoryRepositoryTest {

  @Autowired
  private CategoryRepository categoryRepository;

  @Test
  void givenAnInvalidName_whenCallsSave_shouldReturnError() {
    final var expectedPropertyNme = "name";
    final var expectedMessage = "not-null property references a null or transient value: pt.amane.infrastructure.category.persistence.CategoryJpaEntity.name";

    final var aCategory = Category.newCategory("Finmes", "A categoria mais assistida", true);

    final var anEntity = CategoryJpaEntity.from(aCategory);
    anEntity.setName(null);

    final var actualException = Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(anEntity));

    // essa validação é feita pelo hibernate usando a propriedade PropertyValueException.class
    final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class, actualException.getCause());

    Assertions.assertEquals(expectedPropertyNme, actualCause.getPropertyName());
    Assertions.assertEquals(expectedMessage, actualCause.getMessage());

  }

  @Test
  void givenAnInvalidCreatedAt_whenCallsSave_shouldReturnError() {
    final var expectedPropertyNme = "createdAt";
    final var expectedMessage = "not-null property references a null or transient value: pt.amane.infrastructure.category.persistence.CategoryJpaEntity.createdAt";

    final var aCategory = Category.newCategory("Finmes", "A categoria mais assistida", true);

    final var anEntity = CategoryJpaEntity.from(aCategory);
    anEntity.setCreatedAt(null);

    final var atualException = Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(anEntity));

    // essa validação é feita pelo hibernate usando a propriedade PropertyValueException.class
    final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class, atualException.getCause());

    Assertions.assertEquals(expectedPropertyNme, actualCause.getPropertyName());
    Assertions.assertEquals(expectedMessage, actualCause.getMessage());

  }

  @Test
  void givenAnInvalidUpdatedAt_whenCallsSave_shouldReturnError() {
    final var expectedPropertyNme = "updatedAt";
    final var expectedMessage = "not-null property references a null or transient value: pt.amane.infrastructure.category.persistence.CategoryJpaEntity.updatedAt";

    final var aCategory = Category.newCategory("Finmes", "A categoria mais assistida", true);

    final var anEntity = CategoryJpaEntity.from(aCategory);
    anEntity.setUpdatedAt(null);

    final var atualException = Assertions.assertThrows(DataIntegrityViolationException.class, () -> categoryRepository.save(anEntity));

    // essa validação é feita pelo hibernate usando a propriedade PropertyValueException.class
    final var actualCause = Assertions.assertInstanceOf(PropertyValueException.class, atualException.getCause());

    Assertions.assertEquals(expectedPropertyNme, actualCause.getPropertyName());
    Assertions.assertEquals(expectedMessage, actualCause.getMessage());

  }

}