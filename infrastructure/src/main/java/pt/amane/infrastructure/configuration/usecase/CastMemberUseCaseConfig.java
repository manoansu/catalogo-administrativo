package pt.amane.infrastructure.configuration.usecase;

import java.util.Objects;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.amane.application.castmember.create.CreateCastMemberUseCase;
import pt.amane.application.castmember.create.CreateCastMemberUseCaseImpl;
import pt.amane.application.castmember.delete.DeleteCastMemberUseCase;
import pt.amane.application.castmember.delete.DeleteCastMemberUseCaseImpl;
import pt.amane.application.castmember.retrieve.get.GetCastMemberByIdUseCase;
import pt.amane.application.castmember.retrieve.get.GetCastMemberByIdUseCaseImpl;
import pt.amane.application.castmember.retrieve.list.ListCastMembersUseCase;
import pt.amane.application.castmember.retrieve.list.ListCastMembersUseCaseImpl;
import pt.amane.application.castmember.update.UpdateCastMemberUseCase;
import pt.amane.application.castmember.update.UpdateCastMemberUseCaseImpl;
import pt.amane.domain.castmember.CastMemberGateway;
import pt.amane.domain.category.CategoryGateway;

@Configuration
public class CastMemberUseCaseConfig {

  private final CategoryGateway categoryGateway;
  private final CastMemberGateway CastMemberGateway;

  public CastMemberUseCaseConfig(
      final CategoryGateway categoryGateway,
      final CastMemberGateway CastMemberGateway
  ) {
    this.categoryGateway = Objects.requireNonNull(categoryGateway);
    this.CastMemberGateway = Objects.requireNonNull(CastMemberGateway);
  }

  @Bean
  public CreateCastMemberUseCase createCastMemberUseCase() {
    return new CreateCastMemberUseCaseImpl(CastMemberGateway);
  }

  @Bean
  public DeleteCastMemberUseCase deleteCastMemberUseCase() {
    return new DeleteCastMemberUseCaseImpl(CastMemberGateway);
  }

  @Bean
  public GetCastMemberByIdUseCase getCastMemberByIdUseCase() {
    return new GetCastMemberByIdUseCaseImpl(CastMemberGateway);
  }

  @Bean
  public ListCastMembersUseCase listCastMemberUseCase() {
    return new ListCastMembersUseCaseImpl(CastMemberGateway);
  }

  @Bean
  public UpdateCastMemberUseCase updateCastMemberUseCase() {
    return new UpdateCastMemberUseCaseImpl(CastMemberGateway);
  }
}
