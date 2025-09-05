package pt.amane.infrastructure.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Objects;
import pt.amane.application.castmember.create.CreateCastMemberCommand;
import pt.amane.application.castmember.create.CreateCastMemberUseCase;
import pt.amane.application.castmember.delete.DeleteCastMemberUseCase;
import pt.amane.application.castmember.retrieve.get.GetCastMemberByIdUseCase;
import pt.amane.application.castmember.retrieve.list.ListCastMembersUseCase;
import pt.amane.application.castmember.update.UpdateCastMemberCommand;
import pt.amane.application.castmember.update.UpdateCastMemberUseCase;
import pt.amane.domain.pagination.Pagination;
import pt.amane.domain.pagination.SearchQuery;
import pt.amane.infrastructure.api.CastMemberAPI;
import pt.amane.infrastructure.castmember.model.CastMemberListResponse;
import pt.amane.infrastructure.castmember.model.CastMemberResponse;
import pt.amane.infrastructure.castmember.model.CreateCastMemberRequest;
import pt.amane.infrastructure.castmember.model.UpdateCastMemberRequest;
import pt.amane.infrastructure.castmember.presenter.CastMemberPresenter;

@RestController
public class CastMemberController implements CastMemberAPI {

    private final CreateCastMemberUseCase createCastMemberUseCase;
    private final GetCastMemberByIdUseCase getCastMemberByIdUseCase;
    private final UpdateCastMemberUseCase updateCastMemberUseCase;
    private final DeleteCastMemberUseCase deleteCastMemberUseCase;
    private final ListCastMembersUseCase listCastMembersUseCase;

    public CastMemberController(
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

    @Override
    public ResponseEntity<?> create(final CreateCastMemberRequest input) {
        final var aCommand =
                CreateCastMemberCommand.with(input.name(), input.type());

        final var output = this.createCastMemberUseCase.execute(aCommand);

        return ResponseEntity.created(URI.create("/cast_members/" + output.id())).body(output);
    }

    @Override
    public Pagination<CastMemberListResponse> list(
            final String search,
            final int page,
            final int perPage,
            final String sort,
            final String direction
    ) {
        return this.listCastMembersUseCase.execute(new SearchQuery(page, perPage, search, sort, direction))
                .map(CastMemberPresenter::present);
    }

    @Override
    public CastMemberResponse getById(final String id) {
        return CastMemberPresenter.present(this.getCastMemberByIdUseCase.execute(id));
    }

    @Override
    public ResponseEntity<?> updateById(final String id, final UpdateCastMemberRequest aBody) {
        final var aCommand =
                UpdateCastMemberCommand.with(id, aBody.name(), aBody.type());

        final var output = this.updateCastMemberUseCase.execute(aCommand);

        return ResponseEntity.ok(output);
    }

    @Override
    public void deleteById(final String id) {
        this.deleteCastMemberUseCase.execute(id);
    }
}
