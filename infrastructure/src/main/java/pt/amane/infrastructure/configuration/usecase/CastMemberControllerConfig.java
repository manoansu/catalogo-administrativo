package pt.amane.infrastructure.configuration.usecase;

import java.util.Objects;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pt.amane.application.castmember.create.CreateCastMemberUseCase;
import pt.amane.application.castmember.delete.DeleteCastMemberUseCase;
import pt.amane.application.castmember.retrieve.get.GetCastMemberByIdUseCase;
import pt.amane.application.castmember.retrieve.list.ListCastMembersUseCase;
import pt.amane.application.castmember.update.UpdateCastMemberUseCase;
import pt.amane.infrastructure.api.controllers.CastMemberController;

@Configuration
public class CastMemberControllerConfig {

  private final CreateCastMemberUseCase createCastMemberUseCase;
  private final GetCastMemberByIdUseCase getCastMemberByIdUseCase;
  private final UpdateCastMemberUseCase updateCastMemberUseCase;
  private final DeleteCastMemberUseCase deleteCastMemberUseCase;
  private final ListCastMembersUseCase listCastMembersUseCase;

  public CastMemberControllerConfig(
      final CreateCastMemberUseCase createCastMemberUseCase,
      final GetCastMemberByIdUseCase getCastMemberByIdUseCase,
      final UpdateCastMemberUseCase updateCastMemberUseCase,
      final DeleteCastMemberUseCase deleteCastMemberUseCase,
      final ListCastMembersUseCase listCastMembersUseCase
  ) {
    this.createCastMemberUseCase = Objects.requireNonNull(createCastMemberUseCase);
    this.getCastMemberByIdUseCase = Objects.requireNonNull(getCastMemberByIdUseCase);
    this.updateCastMemberUseCase = Objects.requireNonNull(updateCastMemberUseCase);
    this.deleteCastMemberUseCase = Objects.requireNonNull(deleteCastMemberUseCase);
    this.listCastMembersUseCase = Objects.requireNonNull(listCastMembersUseCase);
  }


  @Bean
  public CastMemberController castMemberController() {
    return new CastMemberController(
        createCastMemberUseCase,
        getCastMemberByIdUseCase,
        updateCastMemberUseCase,
        deleteCastMemberUseCase,
        listCastMembersUseCase
    );
  }
}