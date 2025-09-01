package pt.amane.application;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import pt.amane.Identifier;

@ExtendWith(MockitoExtension.class)
@Tag("unitTest")
public abstract class UseCaseTest implements BeforeEachCallback {

  @Override
  public void beforeEach(ExtensionContext extensionContext) throws Exception {
    Mockito.reset(getMocks().toArray());
  }

  protected abstract List<Object> getMocks();

  protected Set<String> asString(final Set<? extends Identifier> ids) {
    return ids.stream()
        .map(Identifier::getValue)
        .collect(Collectors.toSet());
  }

  // The Method clean each test, and avoid or
  // ensure that each test does not interfere with another
  protected List<String> asString(final List<? extends Identifier> ids) {
    return ids.stream()
        .map(Identifier::getValue)
        .toList();
  }



}
