package pt.amane.infrastructure.configuration.usecase;

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
import pt.amane.domain.validation.ObjectsValidator;

@Configuration
public class CastMemberUseCaseConfig {

  private final CastMemberGateway castMemberGateway;

  public CastMemberUseCaseConfig(final CastMemberGateway castMemberGateway) {
    this.castMemberGateway = (CastMemberGateway) ObjectsValidator.objectValidation(castMemberGateway);
  }
  @Bean
  public CreateCastMemberUseCase createCastMemberUseCase() {
    return new CreateCastMemberUseCaseImpl(castMemberGateway);
  }

  @Bean
  public UpdateCastMemberUseCase updateCastMemberUseCase() {
    return new UpdateCastMemberUseCaseImpl(castMemberGateway);
  }

  @Bean
  public GetCastMemberByIdUseCase getCastMemberByIdUseCase() {
    return new GetCastMemberByIdUseCaseImpl(castMemberGateway);
  }

  @Bean
  public ListCastMembersUseCase listCastMembersUseCase() {
    return new ListCastMembersUseCaseImpl(castMemberGateway);
  }

  @Bean
  public DeleteCastMemberUseCase deleteCastMemberUseCase() {
    return new DeleteCastMemberUseCaseImpl(castMemberGateway);
  }


}
